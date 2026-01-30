import { Component, Input, OnInit, Output, EventEmitter, inject, ViewChild, ElementRef, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, tap, Subscription, catchError, of } from 'rxjs';
import Swal from 'sweetalert2';

import { DropdownItem, DropdownService } from '../../../service/dropdown.service';
import { AuthService } from '../../../service/auth.service';
import { OtService } from '../../../service/ot.service';
import { OtCreateRequest, OtDetailResponse } from '../../../model/ots';
import { DropdownConfig } from '../../../model/dropdown-filter.model';
import { NgselectDropdownComponent } from '../../../component/ngselect-dropdown-component/ngselect-dropdown-component';

@Component({
  selector: 'app-form-ots',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule,NgselectDropdownComponent],
  templateUrl: './form-ots-component.html',
  styleUrls: ['./form-ots-component.css']
})
export class FormOtsComponent implements OnInit, OnDestroy {
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

  // Responsables
  jefaturasCliente: DropdownItem[] = [];
  analistasCliente: DropdownItem[] = [];
  coordinadoresTiCw: DropdownItem[] = [];
  jefaturasResponsable: DropdownItem[] = [];
  liquidadores: DropdownItem[] = [];
  ejecutantes: DropdownItem[] = [];
  analistasContable: DropdownItem[] = [];
  estadoOT: DropdownItem[] = [];

  // Subscripciones para limpiar memoria
  private subscriptions: Subscription[] = [];

  // Configuración de dropdowns con filtro
 configDropdown: DropdownConfig = {
  placeholder: 'Seleccione una opción',
  searchPlaceholder: 'Buscar...',
  noResultsText: 'No se encontraron resultados',
  loadingText: 'Cargando...',
  showClearButton: true,
  showSearch: true,
  multiple: false,
  maxHeight: '250px',
  width: '100%'
};

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
      this.form.disable();
    }
  }

  ngOnDestroy(): void {
    // Limpiar todas las subscripciones
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private crearFormularioBase(): void {
    const hoy = new Date().toISOString().split('T')[0];

    this.form = this.fb.group({
      // Paso 1: Información Principal
      idOts: [null],
      idCliente: [null, Validators.required],
      idArea: [{ value: null, disabled: true }, Validators.required],
      idProyecto: [null, Validators.required],
      idFase: [null, Validators.required],
      idSite: [null, Validators.required],
      idRegion: [null, Validators.required],
      descripcion: [{ value: '', disabled: !this.isEditMode }, Validators.required],
      fechaApertura: [hoy, Validators.required],
      idOtsAnterior: [null], // Opcional

      // Paso 2: Responsables
      idJefaturaClienteSolicitante: [null, Validators.required],
      idAnalistaClienteSolicitante: [null, Validators.required],
      idCoordinadorTiCw: [null, Validators.required],
      idJefaturaResponsable: [null, Validators.required],
      idLiquidador: [null, Validators.required],
      idEjecutante: [null, Validators.required],
      idAnalistaContable: [null, Validators.required],
      idEstadoOt: [null] // Solo obligatorio en edición
    });

    // Si es modo edición, el estado es obligatorio
    if (this.isEditMode) {
      this.form.get('idEstadoOt')?.setValidators(Validators.required);
      this.form.get('idEstadoOt')?.updateValueAndValidity();
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
    const site = this.sites.find(s => s.id === this.form.value.idSite);
    return site ? `${site.label} ${site.adicional || ''}`.trim() : '—';
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

  // Helper para acceder a los controles del formulario
  get f() {
    return this.form.controls as { [K in keyof typeof this.form.value]: AbstractControl };
  }

 private suscribirCambiosFormulario(): void {
  // Cuando cambia el cliente, habilitar/deshabilitar el área
  const clienteSub = this.form.get('idCliente')?.valueChanges.subscribe(clienteId => {
    if (clienteId) {
      this.cargarAreasPorCliente(Number(clienteId));
      this.form.get('idArea')?.enable(); // ✅ CORRECTO: habilitar programáticamente
    } else {
      this.areas = [];
      this.form.get('idArea')?.disable(); // ✅ CORRECTO: deshabilitar programáticamente
      this.form.get('idArea')?.setValue(null);
    }
  });

  if (clienteSub) this.subscriptions.push(clienteSub);
}
  private cargarDropdownsParaCreacion(): void {
    this.loading = true;
    const catalogSub = this.cargarTodosLosCatalogos().subscribe({
      next: () => {
        this.loading = false;
        // Forzar detección de cambios después de cargar
        setTimeout(() => {
          this.actualizarDescripcion();
        }, 100);
      },
      error: (error) => {
        console.error('Error al cargar catálogos:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los catálogos necesarios',
          confirmButtonColor: '#dc3545'
        });
        this.loading = false;
      }
    });

    this.subscriptions.push(catalogSub);
  }

  private cargarDatosParaEdicion(id: number): void {
    this.loading = true;
    const editSub = forkJoin({
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
          this.form.disable();
        }
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar datos para edición:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudo cargar la información de la OT',
          confirmButtonColor: '#dc3545'
        });
        this.loading = false;
        this.onCancel();
      }
    });

    this.subscriptions.push(editSub);
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
    analistasCont: this.dropdownService.getAnalistasContable(),
    estadoOt: this.dropdownService.getEstadoOt()
  }).pipe(
    tap(data => {
      console.log('✅ Datos recibidos del backend:', data);

      // Asignar directamente SIN adaptar si ya vienen en formato correcto
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
      this.estadoOT = data.estadoOt || [];

      console.log('📊 Total de clientes:', this.clientes.length);
      console.log('📊 Primer cliente:', this.clientes[0]);
    }),
    catchError(error => {
      console.error('❌ Error al cargar catálogos:', error);
      return of({
        clientes: [], proyectos: [], fases: [], sites: [], regiones: [],
        jefaturasCliente: [], analistasCliente: [], coordinadoresTiCw: [],
        jefaturasResp: [], liquidadores: [], ejecutantes: [],
        analistasCont: [], estadoOt: []
      });
    })
  );
}
  private cargarAreasPorCliente(idCliente: number): void {
    const areasSub = this.dropdownService.getAreasByCliente(idCliente).subscribe({
      next: areas => {
        this.areas = areas || [];
        const areaActual = this.form.get('idArea')?.value;
        if (areaActual && !this.areas.some(a => a.id === areaActual)) {
          this.form.get('idArea')?.setValue(null);
        }
      },
      error: () => {
        this.areas = [];
        Swal.fire({
          icon: 'warning',
          title: 'Atención',
          text: 'No se pudieron cargar las áreas del cliente',
          confirmButtonColor: '#ffc107'
        });
      }
    });

    this.subscriptions.push(areasSub);
  }

  private actualizarDescripcion(): void {
    if (this.isEditMode) return;

    const v = this.form.getRawValue();

    const proyecto = this.proyectos.find(p => p.id === Number(v.idProyecto))?.label || '';
    const area = this.areas.find(a => a.id === Number(v.idArea))?.label || '';
    const site = this.sites.find(s => s.id === Number(v.idSite));
    const siteNombre = site?.label || '';
    const siteDescripcion = site?.adicional || '';

    const normalize = (str: string) =>
      str
        .trim()
        .replace(/[^\w\s]/g, '')
        .replace(/\s+/g, ' ');

    const partes = [
      normalize(proyecto),
      normalize(area),
      normalize(siteNombre),
      normalize(siteDescripcion)
    ].filter(Boolean);

    const desc = partes.join('_') || 'OT SIN DESCRIPCION AUTOMATICA';

    this.form.get('descripcion')?.setValue(desc);
  }

  // Validar paso 1 antes de continuar
  validarPaso1(): boolean {
    this.submitted = true;
    const controlesPaso1 = [
      'idCliente',
      'idArea',
      'idProyecto',
      'idFase',
      'idSite',
      'idRegion',
      'fechaApertura'
    ];

    // Marcar todos los controles como tocados
    controlesPaso1.forEach(control => {
      this.f[control]?.markAsTouched();
    });

    // Verificar si hay controles inválidos
    const invalidos = controlesPaso1.filter(control => this.f[control]?.invalid);

    if (invalidos.length > 0) {
      // Encontrar el primer elemento inválido y hacer scroll
      const primerInvalido = invalidos[0];
      const elemento = document.querySelector(`[formControlName="${primerInvalido}"]`);

      if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // Agregar clase de error temporalmente
        elemento.classList.add('is-invalid');
        setTimeout(() => elemento.classList.remove('is-invalid'), 3000);
      }

      Swal.fire({
        icon: 'warning',
        title: 'Campos incompletos',
        html: `Faltan <strong>${invalidos.length}</strong> campo(s) requerido(s) en el Paso 1`,
        confirmButtonColor: '#ffc107'
      });
      return false;
    }

    // Validar también la descripción (que se genera automáticamente)
    const descripcion = this.form.get('descripcion')?.value;
    if (!descripcion || descripcion.trim() === '') {
      Swal.fire({
        icon: 'warning',
        title: 'Descripción incompleta',
        text: 'La descripción automática no se pudo generar. Verifique los campos seleccionados.',
        confirmButtonColor: '#ffc107'
      });
      return false;
    }

    return true;
  }

  // Validar paso 2 antes de continuar
  validarPaso2(): boolean {
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

    // Si es modo edición, también validar el estado
    if (this.isEditMode) {
      controlesPaso2.push('idEstadoOt');
    }

    // Marcar todos los controles como tocados
    controlesPaso2.forEach(control => {
      this.f[control]?.markAsTouched();
    });

    const invalidos = controlesPaso2.filter(control => this.f[control]?.invalid);

    if (invalidos.length > 0) {
      const primerInvalido = invalidos[0];
      const elemento = document.querySelector(`[formControlName="${primerInvalido}"]`);

      if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });

        // Agregar clase de error temporalmente
        elemento.classList.add('is-invalid');
        setTimeout(() => elemento.classList.remove('is-invalid'), 3000);
      }

      Swal.fire({
        icon: 'warning',
        title: 'Responsables incompletos',
        html: `Faltan <strong>${invalidos.length}</strong> responsable(s) requerido(s)`,
        confirmButtonColor: '#ffc107'
      });
      return false;
    }

    return true;
  }

  // Cambiar entre pasos con validación
  cambiarPaso(paso: number): void {
    // Validación al avanzar del paso 1 al 2
    if (paso === 2 && this.currentStep === 1) {
      if (!this.validarPaso1()) {
        return;
      }
    }

    // Validación al avanzar del paso 2 al 3
    if (paso === 3 && this.currentStep === 2) {
      if (!this.validarPaso2()) {
        return;
      }
    }

    // Si es retroceder, siempre permitir
    if (paso < this.currentStep) {
      this.currentStep = paso;
    } else {
      this.currentStep = paso;
    }

    // Scroll al inicio del paso
    setTimeout(() => {
      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = 0;
      }
    }, 100);
  }

  // Método para enviar el formulario
  onSubmit(): void {
    this.submitted = true;
    this.form.markAllAsTouched();

    // Validar todos los pasos antes de enviar
    if (!this.validarPaso1() || !this.validarPaso2()) {
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor completa todos los campos obligatorios en todos los pasos.',
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
      console.log('📋 Valores del formulario:', values);

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
        // Responsables (todos obligatorios)
        idJefaturaClienteSolicitante: Number(values.idJefaturaClienteSolicitante),
        idAnalistaClienteSolicitante: Number(values.idAnalistaClienteSolicitante),
        idCoordinadorTiCw: Number(values.idCoordinadorTiCw),
        idJefaturaResponsable: Number(values.idJefaturaResponsable),
        idLiquidador: Number(values.idLiquidador),
        idEjecutante: Number(values.idEjecutante),
        idAnalistaContable: Number(values.idAnalistaContable),
        idEstadoOt: this.isEditMode ? Number(values.idEstadoOt) : null
      };

      const saveSub = this.otService.saveOt(payload).subscribe({
        next: (res: OtDetailResponse) => {
          this.loading = false;

          Swal.fire({
            icon: 'success',
            title: this.isEditMode ? '¡OT actualizada!' : '¡OT creada con éxito!',
            html: `Número OT: <strong>#${res.ot}</strong>`,
            timer: 2400,
            timerProgressBar: true,
            showConfirmButton: false
          });

          // Emitir evento guardado
          this.saved.emit();

          // Cerrar automáticamente después de mostrar el mensaje
          setTimeout(() => {
            this.onClose();
          }, 2500);
        },
        error: (err) => {
          this.loading = false;
          Swal.fire({
            icon: 'error',
            title: 'Error al guardar',
            text: err?.error?.message || 'Ocurrió un problema inesperado',
            confirmButtonColor: '#dc3545'
          });
        }
      });

      this.subscriptions.push(saveSub);
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
          const resetSub = this.otService.getOtParaEdicion(this.otId).subscribe({
            next: (ot) => {
              this.form.reset();
              this.form.patchValue(ot);
              this.loading = false;
            },
            error: () => {
              Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'No se pudieron restaurar los datos',
                confirmButtonColor: '#dc3545'
              });
              this.loading = false;
            }
          });

          this.subscriptions.push(resetSub);
        }
      } else {
        const hoy = new Date().toISOString().split('T')[0];
        this.form.reset({
          fechaApertura: hoy
        });
        this.submitted = false;
        this.areas = [];
        this.form.get('idArea')?.disable();
        this.actualizarDescripcion();

        // Volver al paso 1
        this.currentStep = 1;

        // Scroll al inicio
        if (this.scrollContainer) {
          this.scrollContainer.nativeElement.scrollTop = 0;
        }
      }
    });
  }

  // Handlers para búsquedas en dropdowns
  onSearchJefaturaCliente(term: string): void {
    console.log('Buscando jefatura cliente:', term);
  }

  onSearchAnalistaCliente(term: string): void {
    console.log('Buscando analista cliente:', term);
  }

  onSearchCoordinadorTiCw(term: string): void {
    console.log('Buscando coordinador TI/CW:', term);
  }

  onSearchJefaturaResponsable(term: string): void {
    console.log('Buscando jefatura responsable:', term);
  }

  onSearchLiquidador(term: string): void {
    console.log('Buscando liquidador:', term);
  }

  onSearchEjecutante(term: string): void {
    console.log('Buscando ejecutante:', term);
  }

  onSearchAnalistaContable(term: string): void {
    console.log('Buscando analista contable:', term);
  }

  onSearchEstadoOT(term: string): void {
    console.log('Buscando estado OT:', term);
  }
}
