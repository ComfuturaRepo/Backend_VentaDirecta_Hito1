import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import Swal from 'sweetalert2';
import { forkJoin } from 'rxjs';

import { DropdownItem, DropdownService } from '../../../service/dropdown.service';
import { AuthService } from '../../../service/auth.service';
import { OtService } from '../../../service/ot.service';
import { CrearOtCompletaRequest, OtCreateRequest, OtResponse, OtFullResponse } from '../../../model/ots';

@Component({
  selector: 'app-form-ots-component',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-ots-component.html',
  styleUrl: './form-ots-component.css'
})
export class FormOtsComponent implements OnInit {
  form!: FormGroup;
  submitted = false;
  loading = true;           // inicia en true para mostrar spinner
  isEditMode = false;
  otId: number | null = null;
  otNumber: string | null = null;

  usernameLogueado: string = '—';
  trabajadorIdLogueado: number | null = null;

  // Dropdowns
  clientes: DropdownItem[] = [];
  areas: DropdownItem[] = [];
  proyectos: DropdownItem[] = [];
  fases: DropdownItem[] = [];
  sites: DropdownItem[] = [];
  regiones: DropdownItem[] = [];

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
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Creamos el formulario INMEDIATAMENTE (antes de ngOnInit)
    this.crearFormularioBase();
  }

  ngOnInit(): void {
    // Usuario logueado
    const user = this.authService.currentUser;
    this.usernameLogueado = user?.username || '—';
    this.trabajadorIdLogueado = user?.idTrabajador ?? null;

    // Detectar modo edición
    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      if (idParam && !isNaN(Number(idParam))) {
        this.otId = Number(idParam);
        this.isEditMode = true;
        this.cargarDatosEdicion(this.otId);
      } else {
        this.isEditMode = false;
        this.loading = false; // ya podemos mostrar el form en creación
      }
    });

    // Cargar dropdowns comunes (siempre)
    this.cargarDropdownsComunes();

    // Suscripciones reactivas (solo si ya existe el form)
    this.suscribirCambiosFormulario();
  }

  private crearFormularioBase(): void {
    const hoy = new Date().toISOString().split('T')[0];

    this.form = this.fb.group({
      idOts: [null],
      idCliente: [null, Validators.required],
      idArea: [{ value: null, disabled: true }, Validators.required],
      idProyecto: [null, Validators.required],
      idFase: [null, Validators.required],
      idSite: [null, Validators.required],
      idRegion: [null, Validators.required],
      descripcion: [{ value: '', disabled: !this.isEditMode }, Validators.required],
      fechaApertura: [hoy, Validators.required],

      idJefaturaClienteSolicitante: [null],
      idAnalistaClienteSolicitante: [null],
      idCoordinadorTiCw: [null],
      idJefaturaResponsable: [null],
      idLiquidador: [null],
      idEjecutante: [null],
      idAnalistaContable: [null],

      idOtsAnterior: [null, [Validators.min(1), Validators.pattern('^[0-9]+$')]]
    });
  }

  private suscribirCambiosFormulario(): void {
    // Cascada: áreas según cliente
    this.form.get('idCliente')?.valueChanges.subscribe(clienteId => {
      const areaCtrl = this.form.get('idArea');
      if (clienteId) {
        this.cargarAreasPorCliente(Number(clienteId));
        areaCtrl?.enable();
      } else {
        this.areas = [];
        areaCtrl?.disable();
        areaCtrl?.setValue(null);
      }
    });

    // Descripción automática SOLO en creación
    if (!this.isEditMode) {
      ['idProyecto', 'idArea', 'idSite'].forEach(field => {
        this.form.get(field)?.valueChanges.subscribe(() => this.actualizarDescripcion());
      });
    }
  }

  private cargarDropdownsComunes(): void {
    forkJoin({
      clientes: this.dropdownService.getClientes(),
      proyectos: this.dropdownService.getProyectos(),
      fases: this.dropdownService.getFases(),
      sites: this.dropdownService.getSites(),
      regiones: this.dropdownService.getRegiones(),
      jefaturasCliente: this.dropdownService.getJefaturasClienteSolicitante(),
      analistasCliente: this.dropdownService.getAnalistasClienteSolicitante(),
      coordinadoresTiCw: this.dropdownService.getCoordinadoresTiCw(),
      jefaturasResp: this.dropdownService.getJefaturasResponsable(),
      liquidadores: this.dropdownService.getLiquidador(),
      ejecutantes: this.dropdownService.getEjecutantes(),
      analistasCont: this.dropdownService.getAnalistasContable()
    }).subscribe({
      next: (data) => {
        this.clientes = data.clientes || [];
        this.proyectos = data.proyectos || [];
        this.fases = data.fases || [];
        this.sites = data.sites || [];
        this.regiones = data.regiones || [];
        this.jefaturasCliente = data.jefaturasCliente || [];
        this.analistasCliente = data.analistasCliente || [];
        this.coordinadoresTiCw = data.coordinadoresTiCw || [];
        this.jefaturasResponsable = data.jefaturasResp || [];
        this.liquidadores = data.liquidadores || [];
        this.ejecutantes = data.ejecutantes || [];
        this.analistasContable = data.analistasCont || [];
      },
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los catálogos', 'error');
      }
    });
  }

  private cargarDatosEdicion(id: number): void {
    this.otService.obtenerOtParaEdicion(id).subscribe({
      next: (ot: OtFullResponse) => {
        this.otNumber = ot.ot?.toString() ?? null;

        // Parcheamos valores
        this.form.patchValue({
          idOts: ot.idOts,
          idCliente: ot.idCliente,
          idArea: ot.idArea,
          idProyecto: ot.idProyecto,
          idFase: ot.idFase,
          idSite: ot.idSite,
          idRegion: ot.idRegion,
          descripcion: ot.descripcion,
          fechaApertura: ot.fechaApertura?.split('T')[0] ?? '',
          idJefaturaClienteSolicitante: ot.idJefaturaClienteSolicitante,
          idAnalistaClienteSolicitante: ot.idAnalistaClienteSolicitante,
          idCoordinadorTiCw: ot.idCoordinadorTiCw,
          idJefaturaResponsable: ot.idJefaturaResponsable,
          idLiquidador: ot.idLiquidador,
          idEjecutante: ot.idEjecutante,
          idAnalistaContable: ot.idAnalistaContable,
          idOtsAnterior: ot.idOtsAnterior
        });

        // Forzamos carga de áreas si hay cliente
        if (ot.idCliente) {
          this.cargarAreasPorCliente(ot.idCliente);
        }

        this.loading = false;
      },
      error: () => {
        Swal.fire('Error', 'No se pudo cargar la OT para edición', 'error');
        this.router.navigate(['/ot']);
      }
    });
  }

  private cargarAreasPorCliente(idCliente: number): void {
    this.dropdownService.getAreasByCliente(idCliente).subscribe({
      next: areas => this.areas = areas || [],
      error: () => {
        this.areas = [];
        Swal.fire('Error', 'No se pudieron cargar las áreas', 'error');
      }
    });
  }

  private actualizarDescripcion(): void {
    if (this.isEditMode) return;

    const v = this.form.getRawValue();

    const proyecto = this.proyectos.find(p => p.id === Number(v.idProyecto))?.label || '';
    const area     = this.areas.find(a => a.id === Number(v.idArea))?.label || '';
    const site     = this.sites.find(s => s.id === Number(v.idSite))?.label || '';

    const toSlug = (str: string) => str
      .toLowerCase()
      .trim()
      .replace(/\s+/g, '_')
      .replace(/[^a-z0-9_]/g, '')
      .replace(/_+/g, '_')
      .replace(/^_+|_+$/g, '');

    const partes = [
      toSlug(proyecto),
      toSlug(area),
      v.idSite ? String(v.idSite) : '',
      toSlug(site)
    ].filter(Boolean);

    const desc = partes.join('_') || '';
    this.form.get('descripcion')?.setValue(desc, { emitEvent: false });
  }

  get f() { return this.form.controls; }

  onSubmit(): void {
    this.submitted = true;
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Revisa los campos marcados.',
        confirmButtonColor: '#ffc107'
      });
      return;
    }

    const title = this.isEditMode ? '¿Guardar cambios?' : '¿Crear OT?';
    Swal.fire({
      title,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#6c757d',
      confirmButtonText: this.isEditMode ? 'Sí, guardar' : 'Sí, crear',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;

      const values = this.form.getRawValue();

      const otPayload: OtCreateRequest = {
        idOts: this.isEditMode ? Number(values.idOts) : undefined,
        idCliente: Number(values.idCliente),
        idArea: Number(values.idArea),
        idProyecto: Number(values.idProyecto),
        idFase: Number(values.idFase),
        idSite: Number(values.idSite),
        idRegion: Number(values.idRegion),
        descripcion: (values.descripcion || '').trim(),
        fechaApertura: values.fechaApertura,
        idOtsAnterior: values.idOtsAnterior ? Number(values.idOtsAnterior) : null,
        idJefaturaClienteSolicitante: values.idJefaturaClienteSolicitante || null,
        idAnalistaClienteSolicitante: values.idAnalistaClienteSolicitante || null,
        idCoordinadorTiCw: values.idCoordinadorTiCw || null,
        idJefaturaResponsable: values.idJefaturaResponsable || null,
        idLiquidador: values.idLiquidador || null,
        idEjecutante: values.idEjecutante || null,
        idAnalistaContable: values.idAnalistaContable || null,
      };

      const payload: CrearOtCompletaRequest = { ot: otPayload, trabajadores: [] };

      this.otService.saveOtCompleta(payload).subscribe({
        next: (res: OtResponse) => {
          Swal.fire({
            icon: 'success',
            title: this.isEditMode ? '¡OT actualizada!' : '¡OT creada!',
            html: `Número: <strong>#${res.ot}</strong>`,
            timer: 2800,
            timerProgressBar: true,
            showConfirmButton: false
          });
          setTimeout(() => this.router.navigate(['/ot']), 2800);
        },
        error: err => {
          Swal.fire('Error', err.error?.message || 'No se pudo guardar', 'error');
          this.loading = false;
        }
      });
    });
  }

  resetForm(): void {
    Swal.fire({
      title: this.isEditMode ? '¿Descartar cambios?' : '¿Limpiar todo?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonText: 'Cancelar',
      confirmButtonText: this.isEditMode ? 'Sí, descartar' : 'Sí, limpiar'
    }).then(result => {
      if (!result.isConfirmed) return;

      if (this.isEditMode) {
        this.router.navigate(['/ot']);
      } else {
        this.form.reset();
        this.submitted = false;
        this.areas = [];
        this.form.get('idArea')?.disable();
        this.form.patchValue({ fechaApertura: new Date().toISOString().split('T')[0] });
        this.actualizarDescripcion();
      }
    });
  }
}
