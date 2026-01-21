import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { DropdownItem, DropdownService } from '../../../service/dropdown.service';
import { CrearOtCompletaRequest, OtCreateRequest, OtResponse, OtService } from '../../../service/ot.service';
import { AuthService } from '../../../service/auth.service';


@Component({
  selector: 'app-form-ots-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-ots-component.html',
  styleUrl: './form-ots-component.css',
})
export class FormOtsComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  loading = false;

  usernameLogueado: string = '—';
  trabajadorIdLogueado: number | null = null;

  // Dropdowns principales
  clientes: DropdownItem[] = [];
  areas: DropdownItem[] = [];
  proyectos: DropdownItem[] = [];
  fases: DropdownItem[] = [];
  sites: DropdownItem[] = [];
  regiones: DropdownItem[] = [];

  // Dropdowns de responsables
  jefaturasCliente: DropdownItem[] = [];
  analistasCliente: DropdownItem[] = [];
  coordinadoresTiCw: DropdownItem[] = [];
  jefaturasResponsable: DropdownItem[] = [];
  liquidadores: DropdownItem[] = [];
  ejecutantes: DropdownItem[] = [];
  analistasContable: DropdownItem[] = [];

  constructor(
    private fb: FormBuilder,
    private otService: OtService,
    private dropdownService: DropdownService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const user = this.authService.currentUser;
    this.usernameLogueado = user?.username || '—';
    this.trabajadorIdLogueado = user?.idTrabajador ?? null;

    // Cargar dropdowns
    this.cargarTodosLosDropdowns();

    // Inicializar formulario
    this.form = this.fb.group({
      idCliente: ['', Validators.required],
      idArea: [{ value: '', disabled: true }, Validators.required],  // inicia deshabilitado
      idProyecto: ['', Validators.required],
      idFase: ['', Validators.required],
      idSite: ['', Validators.required],
      idRegion: ['', Validators.required],
      descripcion: [{ value: '', disabled: true }, Validators.required],
      fechaApertura: ['', Validators.required],

      idJefaturaClienteSolicitante: [null],
      idAnalistaClienteSolicitante: [null],
      idCoordinadorTiCw: [null],
      idJefaturaResponsable: [null],
      idLiquidador: [null],
      idEjecutante: [null],
      idAnalistaContable: [null],

      idOtsAnterior: [null, [Validators.min(1), Validators.pattern('^[0-9]+$')]]
    });

    // Suscripciones
    this.form.valueChanges.subscribe(() => this.actualizarDescripcion());

    this.form.get('idCliente')?.valueChanges.subscribe(clienteId => {
      const areaControl = this.form.get('idArea');

      if (clienteId) {
        this.cargarAreasPorCliente(Number(clienteId));
        areaControl?.enable();
        areaControl?.reset();
      } else {
        this.areas = [];
        areaControl?.disable();
        areaControl?.reset();
      }
    });

    this.actualizarDescripcion();
  }

  get f() { return this.form.controls; }

  private cargarTodosLosDropdowns(): void {
    this.dropdownService.getClientes().subscribe(d => this.clientes = d || []);
    this.dropdownService.getProyectos().subscribe(d => this.proyectos = d || []);
    this.dropdownService.getFases().subscribe(d => this.fases = d || []);
    this.dropdownService.getSites().subscribe(d => this.sites = d || []);
    this.dropdownService.getRegiones().subscribe(d => this.regiones = d || []);

    this.dropdownService.getJefaturasClienteSolicitante().subscribe(d => this.jefaturasCliente = d || []);
    this.dropdownService.getAnalistasClienteSolicitante().subscribe(d => this.analistasCliente = d || []);
    this.dropdownService.getCoordinadoresTiCw().subscribe(d => this.coordinadoresTiCw = d || []);
    this.dropdownService.getJefaturasResponsable().subscribe(d => this.jefaturasResponsable = d || []);
    this.dropdownService.getLiquidador().subscribe(d => this.liquidadores = d || []);
    this.dropdownService.getEjecutantes().subscribe(d => this.ejecutantes = d || []);
    this.dropdownService.getAnalistasContable().subscribe(d => this.analistasContable = d || []);
  }

  private cargarAreasPorCliente(idCliente: number): void {
    this.dropdownService.getAreasByCliente(idCliente).subscribe({
      next: (areas) => this.areas = areas || [],
      error: () => {
        this.areas = [];
        Swal.fire({
          icon: 'error',
          title: 'Error al cargar áreas',
          text: 'No se pudieron obtener las áreas asociadas al cliente seleccionado.',
          confirmButtonColor: '#dc3545'
        });
      }
    });
  }

  private actualizarDescripcion(): void {
    const values = this.form.getRawValue();
    const proyecto = this.proyectos.find(p => p.id === Number(values.idProyecto))?.label || '';
    const area     = this.areas.find(a => a.id === Number(values.idArea))?.label || '';
    const siteId   = values.idSite ? String(values.idSite) : '';
    const site     = this.sites.find(s => s.id === Number(values.idSite))?.label || '';

    let desc = [proyecto, area, siteId, site].filter(Boolean).join(' - ');

    if (!desc.trim()) {
      desc = 'Completar campos principales para generar descripción';
    }

    this.form.get('descripcion')?.setValue(desc, { emitEvent: false });
  }

onSubmit(): void {
  this.submitted = true;
  this.form.markAllAsTouched();

  if (this.form.invalid) {
    Swal.fire({
      icon: 'warning',
      title: 'Formulario incompleto',
      text: 'Por favor completa todos los campos obligatorios correctamente.',
      confirmButtonColor: '#ffc107',
      confirmButtonText: 'Entendido'
    });
    return;
  }

  Swal.fire({
    title: '¿Confirmar creación?',
    text: 'Se registrará una nueva Orden de Trabajo',
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#3085d6',
    cancelButtonColor: '#6c757d',
    confirmButtonText: 'Sí, crear OT',
    cancelButtonText: 'Cancelar'
  }).then((result) => {
    if (!result.isConfirmed) return;

    this.loading = true;

    const values = this.form.getRawValue();

    const otPayload: OtCreateRequest = {
      idCliente: Number(values.idCliente),
      idArea: Number(values.idArea),
      idProyecto: Number(values.idProyecto),
      idFase: Number(values.idFase),
      idSite: Number(values.idSite),
      idRegion: Number(values.idRegion),
      descripcion: (values.descripcion || '').trim(),
      idOtsAnterior: values.idOtsAnterior ? Number(values.idOtsAnterior) : null,

      // ─── NUEVO ───
      fechaApertura: values.fechaApertura,   // "YYYY-MM-DD" directamente del input date

      idJefaturaClienteSolicitante: values.idJefaturaClienteSolicitante || null,
      idAnalistaClienteSolicitante: values.idAnalistaClienteSolicitante || null,
      idCoordinadorTiCw: values.idCoordinadorTiCw || null,
      idJefaturaResponsable: values.idJefaturaResponsable || null,
      idLiquidador: values.idLiquidador || null,
      idEjecutante: values.idEjecutante || null,
      idAnalistaContable: values.idAnalistaContable || null,
    };

    const payload: CrearOtCompletaRequest = {
      ot: otPayload,
      trabajadores: [],
      detalles: []
    };

    // Opcional: log para verificar
    console.log('Payload enviado:', payload);

    this.otService.crearOtCompleta(payload).subscribe({
      next: (res: OtResponse) => {
        Swal.fire({
          icon: 'success',
          title: '¡Orden creada exitosamente!',
          html: `La OT <strong>#${res.ot}</strong> ha sido registrada.<br>Redirigiendo al listado...`,
          timer: 3500,
          timerProgressBar: true,
          showConfirmButton: false,
          allowOutsideClick: false
        });
        setTimeout(() => this.router.navigate(['/ots/list']), 3500);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error al crear la OT',
          text: err.error?.message || 'Ocurrió un problema inesperado. Intenta nuevamente.',
          confirmButtonColor: '#dc3545',
          confirmButtonText: 'Aceptar'
        });
        this.loading = false;
      },
      complete: () => this.loading = false

    });
  });
}

  resetForm(): void {
    Swal.fire({
      title: '¿Limpiar formulario?',
      text: 'Se perderán todos los datos ingresados hasta ahora.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, limpiar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.form.reset();
        this.submitted = false;
        this.areas = [];
        this.form.get('idArea')?.disable();  // mantener deshabilitado al resetear
        this.actualizarDescripcion();

        Swal.fire({
          icon: 'info',
          title: 'Formulario limpiado',
          text: 'Todos los campos han sido restablecidos.',
          timer: 2000,
          showConfirmButton: false,
          position: 'top-end',
          toast: true
        });
      }
    });
  }
}

