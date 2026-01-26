import { Component, Input, OnInit, Output, EventEmitter, inject, ViewChild, ElementRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, tap } from 'rxjs';
import Swal from 'sweetalert2';

import { DropdownItem, DropdownService } from '../../../service/dropdown.service';
import { AuthService } from '../../../service/auth.service';
import { OtService } from '../../../service/ot.service';
import { OtCreateRequest, OtDetailResponse } from '../../../model/ots';

@Component({
  selector: 'app-form-ots',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './form-ots-component.html',
  styleUrls: ['./form-ots-component.css']
})
export class FormOtsComponent implements OnInit {
  @ViewChild('scrollContainer') scrollContainer!: ElementRef;

  private fb = inject(FormBuilder);
  private otService = inject(OtService);
  private dropdownService = inject(DropdownService);
  private authService = inject(AuthService);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  @Input() otId: number | null = null;
  @Input() isViewMode: boolean = false;
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() otData: any = null;
  @Input() onClose: () => void = () => {};

  @Output() saved = new EventEmitter<void>();
  @Output() canceled = new EventEmitter<void>();

  currentStep: number = 1;
  form!: FormGroup;
  submitted = false;
  loading = true;
  isEditMode = false;

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

  constructor() {
    this.crearFormularioBase();
  }

  ngOnInit(): void {
    this.isEditMode = this.mode === 'edit';

    const user = this.authService.currentUser;
    this.usernameLogueado = user?.username || '—';
    this.trabajadorIdLogueado = user?.idTrabajador ?? null;

    this.suscribirCambiosFormulario();

    if (this.isEditMode && this.otId) {
      this.cargarDatosParaEdicion(this.otId);
    } else if (this.mode === 'edit' && this.otData) {
      this.loading = false;
      this.form.patchValue(this.otData);
      const clienteId = this.form.get('idCliente')?.value;
      if (clienteId) {
        this.cargarAreasPorCliente(clienteId);
      }
    } else {
      this.cargarDropdownsParaCreacion();
    }

    if (this.isViewMode) {
      this.form.disable({ emitEvent: false });
    }
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
      descripcion: ['', Validators.required],
      fechaApertura: [hoy, Validators.required],

      // TODOS LOS CAMPOS AHORA SON OBLIGATORIOS
      idJefaturaClienteSolicitante: [null, Validators.required],
      idAnalistaClienteSolicitante: [null, Validators.required],
      idCoordinadorTiCw: [null, Validators.required],
      idJefaturaResponsable: [null, Validators.required],
      idLiquidador: [null, Validators.required],
      idEjecutante: [null, Validators.required],
      idAnalistaContable: [null, Validators.required],

      // ÚNICO CAMPO OPCIONAL
      idOtsAnterior: [null]
    });

    if (!this.isEditMode) {
      this.form.get('descripcion')?.disable({ emitEvent: false });
    }
  }

  // Getters para resumen visual
  get clienteNombre(): string {
    return this.clientes.find(c => c.id === this.form.value.idCliente)?.label || '—';
  }

  get areaNombre(): string {
    return this.areas.find(a => a.id === this.form.value.idArea)?.label || '—';
  }

  get proyectoNombre(): string {
    return this.proyectos.find(p => p.id === this.form.value.idProyecto)?.label || '—';
  }

  get faseNombre(): string {
    return this.fases.find(f => f.id === this.form.value.idFase)?.label || '—';
  }

  get siteNombre(): string {
    return this.sites.find(s => s.id === this.form.value.idSite)?.label || '—';
  }

  get regionNombre(): string {
    return this.regiones.find(r => r.id === this.form.value.idRegion)?.label || '—';
  }

  get fechaAperturaFormatted(): string {
    const fecha = this.form.value.fechaApertura;
    return fecha ? new Date(fecha).toLocaleDateString('es-PE', { day: '2-digit', month: '2-digit', year: 'numeric' }) : '—';
  }

  getResponsableNombre(tipo: string): string {
    let dropdown: DropdownItem[] = [];
    let value: number | null = null;

    switch (tipo) {
      case 'jefaturaCliente':
        dropdown = this.jefaturasCliente;
        value = this.form.value.idJefaturaClienteSolicitante;
        break;
      case 'analistaCliente':
        dropdown = this.analistasCliente;
        value = this.form.value.idAnalistaClienteSolicitante;
        break;
      case 'coordinadorTiCw':
        dropdown = this.coordinadoresTiCw;
        value = this.form.value.idCoordinadorTiCw;
        break;
      case 'jefaturaResponsable':
        dropdown = this.jefaturasResponsable;
        value = this.form.value.idJefaturaResponsable;
        break;
      case 'liquidador':
        dropdown = this.liquidadores;
        value = this.form.value.idLiquidador;
        break;
      case 'ejecutante':
        dropdown = this.ejecutantes;
        value = this.form.value.idEjecutante;
        break;
      case 'analistaContable':
        dropdown = this.analistasContable;
        value = this.form.value.idAnalistaContable;
        break;
    }

    return dropdown.find(item => item.id === value)?.label || '—';
  }

  // Tipado seguro para f
  get f() {
    return this.form.controls as { [K in keyof typeof this.form.value]: AbstractControl };
  }

  private suscribirCambiosFormulario(): void {
    this.form.get('idCliente')?.valueChanges.subscribe(clienteId => {
      const areaCtrl = this.form.get('idArea');
      if (clienteId) {
        this.cargarAreasPorCliente(Number(clienteId));
        areaCtrl?.enable({ emitEvent: false });
      } else {
        this.areas = [];
        areaCtrl?.disable({ emitEvent: false });
        areaCtrl?.setValue(null, { emitEvent: false });
      }
    });

    if (!this.isEditMode) {
      ['idProyecto', 'idArea', 'idSite'].forEach(field => {
        this.form.get(field)?.valueChanges.subscribe(() => this.actualizarDescripcion());
      });
    }
  }

  private cargarDropdownsParaCreacion(): void {
    this.cargarTodosLosCatalogos().subscribe({
      next: () => this.loading = false,
      error: () => {
        Swal.fire('Error', 'No se pudieron cargar los catálogos necesarios', 'error');
        this.loading = false;
      }
    });
  }

  private cargarDatosParaEdicion(id: number): void {
    this.loading = true;
    forkJoin({
      ot: this.otService.getOtParaEdicion(id),
      catalogs: this.cargarTodosLosCatalogos()
    }).subscribe({
      next: ({ ot }) => {
        this.form.patchValue(ot);
        const clienteId = this.form.get('idCliente')?.value;
        if (clienteId) {
          this.cargarAreasPorCliente(clienteId);
        }
        if (this.isViewMode) {
          this.form.disable({ emitEvent: false });
        }
        this.loading = false;
      },
      error: () => {
        Swal.fire('Error', 'No se pudo cargar la información de la OT', 'error');
        this.onCancel();
      }
    });
  }

  private cargarTodosLosCatalogos() {
    return forkJoin({
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
    }).pipe(
      tap(data => {
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
      })
    );
  }

  private cargarAreasPorCliente(idCliente: number): void {
    this.dropdownService.getAreasByCliente(idCliente).subscribe({
      next: areas => {
        this.areas = areas || [];
        const areaActual = this.form.get('idArea')?.value;
        if (areaActual && !this.areas.some(a => a.id === areaActual)) {
          this.form.get('idArea')?.setValue(null, { emitEvent: false });
        }
      },
      error: () => {
        this.areas = [];
        Swal.fire('Atención', 'No se pudieron cargar las áreas del cliente', 'warning');
      }
    });
  }

 private actualizarDescripcion(): void {
  if (this.isEditMode) return;

  const v = this.form.getRawValue();

  const proyecto = this.proyectos.find(p => p.id === Number(v.idProyecto))?.label || '';
  const area     = this.areas.find(a => a.id === Number(v.idArea))?.label || '';
  const site     = this.sites.find(s => s.id === Number(v.idSite))?.label || '';

  const normalize = (str: string) =>
    str
      .trim()
      .replace(/[^\w\s]/g, '')   // quita símbolos raros, mantiene espacios
      .replace(/\s+/g, ' ');     // normaliza múltiples espacios

  const partes = [
    normalize(proyecto),
    normalize(area),
    normalize(site)
  ].filter(Boolean);

  const desc = partes.join('_') || 'OT SIN DESCRIPCION AUTOMATICA';

  this.form.get('descripcion')?.setValue(desc, { emitEvent: false });
}


  onSubmit(): void {
    this.submitted = true;
    this.form.markAllAsTouched();

    if (this.form.invalid) {
      // Desplazar al primer campo inválido
      const invalidElements = document.querySelectorAll('.is-invalid');
      if (invalidElements.length > 0) {
        invalidElements[0].scrollIntoView({ behavior: 'smooth', block: 'center' });
      }

      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor completa todos los campos obligatorios.',
        confirmButtonColor: '#ffc107'
      });
      return;
    }

    const title = this.isEditMode ? '¿Guardar cambios?' : '¿Crear nueva OT?';
    const text = this.isEditMode
      ? 'Se actualizará la información de esta OT.'
      : 'Se creará una nueva Orden de Trabajo con los datos proporcionados.';

    Swal.fire({
      title,
      text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#198754',
      cancelButtonColor: '#6c757d',
      confirmButtonText: this.isEditMode ? 'Guardar' : 'Crear',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;

      const values = this.form.getRawValue();

      // AHORA TODOS LOS CAMPOS SON OBLIGATORIOS, ASÍ QUE NUNCA SERÁN null
      const payload: OtCreateRequest = {
        idOts: this.isEditMode ? Number(values.idOts) : undefined,
        idCliente: Number(values.idCliente),
        idArea: Number(values.idArea),
        idProyecto: Number(values.idProyecto),
        idFase: Number(values.idFase),
        idSite: Number(values.idSite),
        idRegion: Number(values.idRegion),
        descripcion: values.descripcion.trim(),
        fechaApertura: values.fechaApertura,
        idOtsAnterior: values.idOtsAnterior ? Number(values.idOtsAnterior) : null,
        // TODOS OBLIGATORIOS AHORA
        idJefaturaClienteSolicitante: Number(values.idJefaturaClienteSolicitante),
        idAnalistaClienteSolicitante: Number(values.idAnalistaClienteSolicitante),
        idCoordinadorTiCw: Number(values.idCoordinadorTiCw),
        idJefaturaResponsable: Number(values.idJefaturaResponsable),
        idLiquidador: Number(values.idLiquidador),
        idEjecutante: Number(values.idEjecutante),
        idAnalistaContable: Number(values.idAnalistaContable),
      };

      this.otService.saveOt(payload).subscribe({
        next: (res: OtDetailResponse) => {
          Swal.fire({
            icon: 'success',
            title: this.isEditMode ? '¡OT actualizada!' : '¡OT creada con éxito!',
            html: `Número OT: <strong>#${res.ot}</strong>`,
            timer: 2400,
            timerProgressBar: true,
            showConfirmButton: false
          });

          // Emitir evento guardado y cerrar modal
          this.saved.emit();

          // Cerrar automáticamente después de mostrar el mensaje
          setTimeout(() => {
            this.onClose();
          }, 2500);
        },
        error: (err) => {
          Swal.fire({
            icon: 'error',
            title: 'Error al guardar',
            text: err?.error?.message || 'Ocurrió un problema inesperado',
            confirmButtonColor: '#dc3545'
          });
          this.loading = false;
        }
      });
    });
  }

  onCancel(): void {
    if (this.form.dirty && !this.submitted) {
      Swal.fire({
        title: '¿Descartar cambios?',
        text: 'Tiene cambios sin guardar. ¿Está seguro de que desea salir?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#dc3545',
        cancelButtonColor: '#6c757d',
        confirmButtonText: 'Sí, descartar',
        cancelButtonText: 'Cancelar'
      }).then(result => {
        if (result.isConfirmed) {
          this.canceled.emit();
          this.onClose();
        }
      });
    } else {
      this.canceled.emit();
      this.onClose();
    }
  }

  resetForm(): void {
    const title = this.isEditMode ? '¿Descartar cambios?' : '¿Limpiar formulario?';
    const text = this.isEditMode
      ? 'Perderá todos los cambios realizados.'
      : 'Se eliminarán todos los datos ingresados.';

    Swal.fire({
      title,
      text,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonText: 'Cancelar',
      confirmButtonText: this.isEditMode ? 'Descartar' : 'Limpiar'
    }).then(result => {
      if (!result.isConfirmed) return;

      if (this.isEditMode) {
        // Recargar datos originales
        if (this.otId) {
          this.loading = true;
          this.otService.getOtParaEdicion(this.otId).subscribe({
            next: (ot) => {
              this.form.reset();
              this.form.patchValue(ot);
              this.loading = false;
            },
            error: () => {
              Swal.fire('Error', 'No se pudieron restaurar los datos', 'error');
              this.loading = false;
            }
          });
        }
      } else {
        const hoy = new Date().toISOString().split('T')[0];
        this.form.reset({
          fechaApertura: hoy
        });
        this.submitted = false;
        this.areas = [];
        this.form.get('idArea')?.disable({ emitEvent: false });
        this.actualizarDescripcion();

        // Scroll al inicio
        if (this.scrollContainer) {
          this.scrollContainer.nativeElement.scrollTop = 0;
        }
      }
    });
  }

  // Método para cambiar de paso con validación
  cambiarPaso(siguientePaso: number): void {
    if (siguientePaso === 2 && this.currentStep === 1) {
      // Validar paso 1 antes de continuar
      this.submitted = true;
      const controlesPaso1 = ['idCliente', 'idArea', 'idProyecto', 'idFase', 'idSite', 'idRegion', 'fechaApertura', 'descripcion'];
      const invalidos = controlesPaso1.filter(control => this.f[control].invalid);

      if (invalidos.length > 0) {
        const elemento = document.querySelector(`[formControlName="${invalidos[0]}"]`);
        if (elemento) {
          elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        return;
      }
    } else if (siguientePaso === 3 && this.currentStep === 2) {
      // Validar paso 2 antes de continuar
      this.submitted = true;
      const controlesPaso2 = [
        'idJefaturaClienteSolicitante',
        'idAnalistaClienteSolicitante',
        'idCoordinadorTiCw',
        'idJefaturaResponsable',
        'idLiquidador',
        'idEjecutante',
        'idAnalistaContable'
      ];
      const invalidos = controlesPaso2.filter(control => this.f[control].invalid);

      if (invalidos.length > 0) {
        const elemento = document.querySelector(`[formControlName="${invalidos[0]}"]`);
        if (elemento) {
          elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });
        }
        return;
      }
    }

    this.currentStep = siguientePaso;

    // Scroll al inicio del paso
    setTimeout(() => {
      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = 0;
      }
    }, 100);
  }
}
