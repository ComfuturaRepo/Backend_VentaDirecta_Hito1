import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

import { DropdownItem, DropdownService } from '../../service/dropdown.service';
import {
  OtCreateRequest,
  CrearOtCompletaRequest,
  OtResponse,
  OtService
} from '../../service/ot.service';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'app-create-ot',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './ot-component.html',
  styleUrl: './ot-component.css'
})
export class CreateOtComponent implements OnInit {
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

  // Nuevos dropdowns para responsables
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
// Carga con tipado explícito
  this.dropdownService.getJefaturasClienteSolicitante().subscribe((d: DropdownItem[]) => {
    this.jefaturasCliente = d || [];
  });

  this.dropdownService.getAnalistasClienteSolicitante().subscribe((d: DropdownItem[]) => {
    this.analistasCliente = d || [];
  });

  this.dropdownService.getCoordinadoresTiCw().subscribe((d: DropdownItem[]) => {
    this.coordinadoresTiCw = d || [];
  });

  this.dropdownService.getJefaturasResponsable().subscribe((d: DropdownItem[]) => {
    this.jefaturasResponsable = d || [];
  });

  this.dropdownService.getLiquidador().subscribe((d: DropdownItem[]) => {
    this.liquidadores = d || [];
  });

  this.dropdownService.getEjecutantes().subscribe((d: DropdownItem[]) => {
    this.ejecutantes = d || [];
  });

  this.dropdownService.getAnalistasContable().subscribe((d: DropdownItem[]) => {
    this.analistasContable = d || [];
  });
    this.form = this.fb.group({
      idCliente: ['', Validators.required],
      idArea: ['', Validators.required],
      idProyecto: ['', Validators.required],
      idFase: ['', Validators.required],
      idSite: ['', Validators.required],
      idRegion: ['', Validators.required],
      descripcion: [{ value: '', disabled: true }, Validators.required],

      fechaApertura: ['', Validators.required],

      // Dropdowns obligatorios u opcionales
      idJefaturaClienteSolicitante: [null],
      idAnalistaClienteSolicitante: [null],
      coordinadoresTiCwPextEnergia: ['', Validators.maxLength(500)], // texto libre para varios
      idJefaturaResponsable: [null],
      idLiquidador: [null],
      idEjecutante: [null],
      idAnalistaContable: [null]
    });

    // Suscripciones
    this.form.valueChanges.subscribe(() => this.actualizarDescripcion());

    this.form.get('idCliente')?.valueChanges.subscribe(clienteId => {
      if (clienteId) {
        this.cargarAreasPorCliente(Number(clienteId));
        this.form.get('idArea')?.reset();
      } else {
        this.areas = [];
        this.form.get('idArea')?.reset();
      }
    });

    this.cargarTodosLosDropdowns();
    this.actualizarDescripcion();
  }

  get f() { return this.form.controls; }

  private cargarTodosLosDropdowns(): void {
    // Dropdowns principales
    this.dropdownService.getClientes().subscribe(d => this.clientes = d || []);
    this.dropdownService.getProyectos().subscribe(d => this.proyectos = d || []);
    this.dropdownService.getFases().subscribe(d => this.fases = d || []);
    this.dropdownService.getSites().subscribe(d => this.sites = d || []);
    this.dropdownService.getRegiones().subscribe(d => this.regiones = d || []);

    // Nuevos dropdowns de responsables
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
        Swal.fire('Error', 'No se pudieron cargar las áreas', 'error');
      }
    });
  }

  private actualizarDescripcion(): void {
    const values = this.form.value;

    const proyecto = this.proyectos.find(p => p.id === Number(values.idProyecto))?.label || '';
    const area     = this.areas.find(a => a.id === Number(values.idArea))?.label || '';
    const siteId   = values.idSite ? String(values.idSite) : '';
    const site     = this.sites.find(s => s.id === Number(values.idSite))?.label || '';

    let desc = [proyecto, area, siteId, site]
      .filter(Boolean)
      .join(' - ');

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
        text: 'Por favor completa los campos obligatorios',
        confirmButtonColor: '#3085d6'
      });
      return;
    }

    this.loading = true;

    const values = this.form.value;

    const otPayload: OtCreateRequest = {
      idCliente: Number(values.idCliente),
      idArea: Number(values.idArea),
      idProyecto: Number(values.idProyecto),
      idFase: Number(values.idFase),
      idSite: Number(values.idSite),
      idRegion: Number(values.idRegion),
      descripcion: values.descripcion?.trim() || '',
      diasAsignados: 0,
      idOtsAnterior: null,

      // IDs de las tablas maestras (FKs)
      idJefaturaClienteSolicitante: values.idJefaturaClienteSolicitante || null,
      idAnalistaClienteSolicitante: values.idAnalistaClienteSolicitante || null,
      idCoordinadorTiCw: null, // ← si solo permites uno, usa el dropdown; si varios → ver nota abajo
      idJefaturaResponsable: values.idJefaturaResponsable || null,
      idLiquidador: values.idLiquidador || null,
      idEjecutante: values.idEjecutante || null,
      idAnalistaContable: values.idAnalistaContable || null,

      // Campo de texto libre para múltiples coordinadores (como antes)
      coordinadoresTiCwPextEnergia: values.coordinadoresTiCwPextEnergia?.trim() || null
    };

    // Nota: si quieres que coordinadores sea solo dropdown (uno), elimina el campo de texto
    // y usa idCoordinadorTiCw: values.idCoordinadorTiCw || null

    const payload: CrearOtCompletaRequest = {
      ot: otPayload,
      trabajadores: [],          // ← ya no se usa
      detalles: []
    };

    this.otService.crearOtCompleta(payload).subscribe({
      next: (res: OtResponse) => {
        Swal.fire({
          icon: 'success',
          title: '¡Orden creada!',
          html: `OT <strong>#${res.ot}</strong> registrada exitosamente`,
          timer: 3500,
          showConfirmButton: false
        });
        this.router.navigate(['/ots/list']);
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error al crear OT',
          text: err.error?.message || 'Ocurrió un problema inesperado'
        });
      },
      complete: () => this.loading = false
    });
  }

  resetForm(): void {
    this.form.reset();
    this.submitted = false;
    this.areas = [];
    this.actualizarDescripcion();
  }
}
