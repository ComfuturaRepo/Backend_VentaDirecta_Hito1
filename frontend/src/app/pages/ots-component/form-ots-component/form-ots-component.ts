import { Component, Input, OnInit, Output, EventEmitter, inject, ViewChild, ElementRef, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators, AbstractControl, FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { forkJoin, Subscription, catchError, of } from 'rxjs';
import Swal from 'sweetalert2';

import { DropdownItem, DropdownService } from '../../../service/dropdown.service';
import { AuthService } from '../../../service/auth.service';
import { OtService } from '../../../service/ot.service';
import { OtCreateRequest, OtDetailResponse } from '../../../model/ots';
import { NgselectDropdownComponent } from '../../../component/ngselect-dropdown-component/ngselect-dropdown-component';

@Component({
  selector: 'app-form-ots',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, NgselectDropdownComponent],
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
  private cdr = inject(ChangeDetectorRef);

  @Input() otId: number | null = null;
  @Input() isViewMode: boolean = false;
  @Input() mode: 'create' | 'edit' = 'create';
  @Input() otData: any = null;
  @Input() onClose: () => void = () => {};

  @Output() saved = new EventEmitter<void>();
  @Output() canceled = new EventEmitter<void>();

  // Loading states mejorados
  loading = false;
  loadingSubmit = false;
  submitted = false;
  isEditMode = false;
  currentStep: number = 1;

  // Fecha actual para mostrar
  fechaActualFormatted: string = '';

  // IDs seleccionados
  selectedTipoOtId: number | null = null;
  selectedClienteId: number | null = null;
  selectedAreaId: number | null = null;
  selectedProyectoId: number | null = null;
  selectedFaseId: number | null = null;
  selectedSiteId: number | null = null;
  selectedRegionId: number | null = null;
  selectedJefaturaClienteId: number | null = null;
  selectedAnalistaClienteId: number | null = null;
  selectedCoordinadorTiCwId: number | null = null;
  selectedJefaturaResponsableId: number | null = null;
  selectedLiquidadorId: number | null = null;
  selectedEjecutanteId: number | null = null;
  selectedAnalistaContableId: number | null = null;
  selectedEstadoOTId: number | null = null;

  // Usuario logueado
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
  estadoOT: DropdownItem[] = [];
tiposOt: DropdownItem[] = [];

  // Formulario
  form!: FormGroup;

  private subscriptions: Subscription[] = [];

  constructor() {
    // Inicializar fecha actual
    this.fechaActualFormatted = this.formatDate(new Date());
  }

ngOnInit(): void {
  console.log('FormOtsComponent inicializando...');

  this.isEditMode = this.mode === 'edit';
  this.crearFormularioBase();

  // Obtener usuario actual
  const user = this.authService.currentUser;
  this.usernameLogueado = user?.username || 'Usuario';
  this.trabajadorIdLogueado = user?.idTrabajador ?? null;

  // Cargar datos según modo
  if (this.isEditMode && this.otId) {
    this.cargarDatosParaEdicion(this.otId);
  } else if (this.mode === 'edit' && this.otData) {
    this.patchFormValues(this.otData);
    const clienteId = this.form.get('idCliente')?.value;
    if (clienteId) {
      this.cargarAreasPorCliente(clienteId);
    }
    // Asegúrate de cargar tiposOt aquí también
    this.cargarTodosLosCatalogos();
  } else {
    this.cargarDropdownsParaCreacion();
  }

  // Si es modo vista, deshabilitar formulario
  if (this.isViewMode) {
    setTimeout(() => {
      this.form.disable();
    }, 100);
  }

  // Ejecutar validación inicial después de un breve delay
  setTimeout(() => {
    this.actualizarValidacionOtAnterior();
  }, 100);
}

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  // ============================================
  // MÉTODOS DE INICIALIZACIÓN
  // ============================================
private crearFormularioBase(): void {
  const hoy = new Date().toISOString().split('T')[0];

  this.form = this.fb.group({
    idOts: [null],
    idCliente: [null, Validators.required],
    idArea: [null, Validators.required],
    idProyecto: [null, Validators.required],
    idFase: [null, Validators.required],
    idSite: [null, Validators.required],
    idTipoOt: [null, Validators.required],
    idRegion: [null, Validators.required],
    descripcion: ['', [Validators.required, Validators.minLength(10)]],
    fechaApertura: [hoy, [Validators.required, this.fechaAperturaValidator]],
    idOtsAnterior: [null, [
      // Validadores básicos que siempre aplican
      Validators.min(1),
      Validators.max(2147483647), // LÍMITE MÁXIMO DE int EN JAVA
      Validators.pattern('^[0-9]*$') // Solo números
    ]],

    // Responsables (todos requeridos)
    idJefaturaClienteSolicitante: [null, Validators.required],
    idAnalistaClienteSolicitante: [null, Validators.required],
    idCoordinadorTiCw: [null, Validators.required],
    idJefaturaResponsable: [null, Validators.required],
    idLiquidador: [null, Validators.required],
    idEjecutante: [null, Validators.required],
    idAnalistaContable: [null, Validators.required],
    idEstadoOt: [null]
  });

  // Deshabilitar área hasta que se seleccione cliente
  this.form.get('idArea')?.disable();

  // Si no es edición, deshabilitar descripción (se genera automática)
  if (!this.isEditMode) {
    this.form.get('descripcion')?.disable();
  }

  // En modo edición, el estado OT es requerido
  if (this.isEditMode) {
    this.form.get('idEstadoOt')?.setValidators(Validators.required);
    this.form.get('idEstadoOt')?.updateValueAndValidity();
  }

  // Escuchar cambios en la fecha de apertura para actualizar validaciones
  this.form.get('fechaApertura')?.valueChanges.subscribe(() => {
    this.actualizarValidacionOtAnterior();
  });
}
// ============================================
// MÉTODO PARA VALIDACIÓN CONDICIONAL
// ============================================

private actualizarValidacionOtAnterior(): void {
  const fechaAperturaControl = this.form.get('fechaApertura');
  const otAnteriorControl = this.form.get('idOtsAnterior');

  if (!fechaAperturaControl || !otAnteriorControl) return;

  const fechaApertura = fechaAperturaControl.value;

  if (!fechaApertura) {
    // Si no hay fecha, mantener solo validadores básicos
    otAnteriorControl.setValidators([
      Validators.min(1),
      Validators.max(2147483647),
      Validators.pattern('^[0-9]*$')
    ]);
    otAnteriorControl.updateValueAndValidity();
    return;
  }

  const fechaSeleccionada = new Date(fechaApertura);
  const fechaActual = new Date();
  const anioSeleccionado = fechaSeleccionada.getFullYear();
  const anioActual = fechaActual.getFullYear();

  // Verificar si la fecha es del año anterior
  if (anioSeleccionado < anioActual) {
    // Fecha del año pasado: hacer OT Anterior requerido
    otAnteriorControl.setValidators([
      Validators.required,
      Validators.min(1),
      Validators.max(2147483647),
      Validators.pattern('^[0-9]*$')
    ]);
  } else {
    // Fecha del año actual o futuro: solo validadores básicos
    otAnteriorControl.setValidators([
      Validators.min(1),
      Validators.max(2147483647),
      Validators.pattern('^[0-9]*$')
    ]);
  }

  otAnteriorControl.updateValueAndValidity();
  this.cdr.detectChanges(); // Forzar actualización de la vista
}
// ============================================
// VALIDADORES PERSONALIZADOS
// ============================================

private fechaAperturaValidator = (control: AbstractControl): { [key: string]: boolean } | null => {
  if (!control.value) {
    return null; // Deja que Validators.required maneje esto
  }

  const fechaSeleccionada = new Date(control.value);
  const fechaActual = new Date();

  // Validar que la fecha no sea futura (opcional, si quieres)
  if (fechaSeleccionada > fechaActual) {
    return { fechaFutura: true };
  }

  return null;
};
// ============================================
// GETTER PARA SABER SI OT ANTERIOR ES REQUERIDO
// ============================================
// ============================================
// VALIDACIÓN EN TIEMPO REAL
// ============================================

validarNumeroOtAnterior(event: any): void {
  const input = event.target;
  const value = input.value;

  // Remover cualquier caracter no numérico
  const numericValue = value.replace(/[^0-9]/g, '');

  // Limitar a 10 dígitos máximo (2,147,483,647 tiene 10 dígitos)
  if (numericValue.length > 10) {
    input.value = numericValue.slice(0, 10);
    this.form.get('idOtsAnterior')?.setValue(parseInt(numericValue.slice(0, 10)));
  } else {
    input.value = numericValue;
    this.form.get('idOtsAnterior')?.setValue(numericValue ? parseInt(numericValue) : null);
  }

  // Validar límite máximo
  const numValue = parseInt(numericValue || '0');
  if (numValue > 2147483647) {
    this.form.get('idOtsAnterior')?.setValue(2147483647);
    input.value = '2147483647';
  }

  this.form.get('idOtsAnterior')?.updateValueAndValidity();
}

// También agrega un método para formatear el número
formatearNumeroOtAnterior(): void {
  const control = this.form.get('idOtsAnterior');
  if (!control) return;

  const value = control.value;
  if (value && value > 2147483647) {
    control.setValue(2147483647);
    control.updateValueAndValidity();
  }
}
get esOtAnteriorRequerido(): boolean {
  const fechaApertura = this.form.get('fechaApertura')?.value;

  if (!fechaApertura) return false;

  const fechaSeleccionada = new Date(fechaApertura);
  const fechaActual = new Date();
  const anioSeleccionado = fechaSeleccionada.getFullYear();
  const anioActual = fechaActual.getFullYear();

  return anioSeleccionado < anioActual;
}

// También puedes agregar un getter para el mensaje de error
get otAnteriorMensajeError(): string {
  return this.esOtAnteriorRequerido
    ? 'Campo obligatorio para fechas del año anterior'
    : 'Campo opcional';
}
  // ============================================
  // MÉTODOS DE CARGA DE DATOS
  // ============================================

private cargarDropdownsParaCreacion(): void {
  this.loading = true;

  const catalogSub = forkJoin({
    clientes: this.dropdownService.getClientes().pipe(catchError(() => of([]))),
    proyectos: this.dropdownService.getProyectos().pipe(catchError(() => of([]))),
    fases: this.dropdownService.getFases().pipe(catchError(() => of([]))),
    sites: this.dropdownService.getSiteCompuesto().pipe(catchError(() => of([]))),
    regiones: this.dropdownService.getRegiones().pipe(catchError(() => of([]))),
    tiposOt: this.dropdownService.getTipoOt().pipe(catchError(() => of([]))) // ✅ AGREGAR ESTA LÍNEA
  }).subscribe({
    next: (data) => {
      this.clientes = data.clientes || [];
      this.proyectos = data.proyectos || [];
      this.fases = data.fases || [];
      this.sites = data.sites || [];
      this.regiones = data.regiones || [];
      this.tiposOt = data.tiposOt || []; // ✅ AGREGAR ESTA LÍNEA

      // Cargar responsables
      this.cargarDropdownsResponsables();
      this.loading = false;
      this.cdr.detectChanges();
    },
    error: (error) => {
      console.error('Error al cargar catálogos:', error);
      this.loading = false;
      this.cdr.detectChanges();
      this.mostrarError('No se pudieron cargar los catálogos');
    }
  });

  this.subscriptions.push(catalogSub);
}
  private cargarDropdownsResponsables(): void {
    const responsablesSub = forkJoin({
      jefaturasCliente: this.dropdownService.getJefaturasClienteSolicitante().pipe(catchError(() => of([]))),
      analistasCliente: this.dropdownService.getAnalistasClienteSolicitante().pipe(catchError(() => of([]))),
      coordinadoresTiCw: this.dropdownService.getCoordinadoresTiCw().pipe(catchError(() => of([]))),
      jefaturasResp: this.dropdownService.getJefaturasResponsable().pipe(catchError(() => of([]))),
      liquidadores: this.dropdownService.getLiquidador().pipe(catchError(() => of([]))),
      ejecutantes: this.dropdownService.getEjecutantes().pipe(catchError(() => of([]))),
      analistasCont: this.dropdownService.getAnalistasContable().pipe(catchError(() => of([]))),
      estadoOt: this.dropdownService.getEstadoOt().pipe(catchError(() => of([])))
    }).subscribe(data => {
      this.jefaturasCliente = data.jefaturasCliente || [];
      this.analistasCliente = data.analistasCliente || [];
      this.coordinadoresTiCw = data.coordinadoresTiCw || [];
      this.jefaturasResponsable = data.jefaturasResp || [];
      this.liquidadores = data.liquidadores || [];
      this.ejecutantes = data.ejecutantes || [];
      this.analistasContable = data.analistasCont || [];
      this.estadoOT = data.estadoOt || [];

      this.cdr.detectChanges();
    });

    this.subscriptions.push(responsablesSub);
  }

  private cargarDatosParaEdicion(id: number): void {
    this.loading = true;

    const otSub = this.otService.getOtParaEdicion(id).pipe(
      catchError(() => of(null))
    ).subscribe(ot => {
      if (ot) {
        this.patchFormValues(ot);
        const clienteId = this.form.get('idCliente')?.value;
        if (clienteId) {
          this.cargarAreasPorCliente(clienteId);
        }
      }

      this.cargarTodosLosCatalogos();

      if (this.isViewMode) {
        this.form.disable();
      }
      this.loading = false;
      this.cdr.detectChanges();
    });

    this.subscriptions.push(otSub);
  }
onTipoOtChange(event: any): void {
  console.log('Tipo OT change event:', event); // DEBUG

  // Verificar diferentes formatos del evento
  let idTipoOt = null;

  if (event) {
    // Dependiendo de cómo envía el componente dropdown el evento
    if (typeof event === 'object') {
      idTipoOt = event.id || event.value || event.valor || null;
    } else if (typeof event === 'number') {
      idTipoOt = event;
    }
  }

  this.selectedTipoOtId = idTipoOt;
  this.form.get('idTipoOt')?.setValue(idTipoOt);
  this.form.get('idTipoOt')?.markAsTouched();

  console.log('Selected Tipo OT ID:', idTipoOt); // DEBUG
  console.log('Form value idTipoOt:', this.form.get('idTipoOt')?.value); // DEBUG

  this.cdr.detectChanges();
}
private cargarTodosLosCatalogos(): void {
  const catalogSub = forkJoin({
    clientes: this.dropdownService.getClientes().pipe(catchError(() => of([]))),
    proyectos: this.dropdownService.getProyectos().pipe(catchError(() => of([]))),
    fases: this.dropdownService.getFases().pipe(catchError(() => of([]))),
    sites: this.dropdownService.getSiteCompuesto().pipe(catchError(() => of([]))),
    regiones: this.dropdownService.getRegiones().pipe(catchError(() => of([]))),
    tiposOt: this.dropdownService.getTipoOt().pipe(catchError(() => of([]))), // ✅ AGREGAR
    jefaturasCliente: this.dropdownService.getJefaturasClienteSolicitante().pipe(catchError(() => of([]))),
    analistasCliente: this.dropdownService.getAnalistasClienteSolicitante().pipe(catchError(() => of([]))),
    coordinadoresTiCw: this.dropdownService.getCoordinadoresTiCw().pipe(catchError(() => of([]))),
    jefaturasResp: this.dropdownService.getJefaturasResponsable().pipe(catchError(() => of([]))),
    liquidadores: this.dropdownService.getLiquidador().pipe(catchError(() => of([]))),
    ejecutantes: this.dropdownService.getEjecutantes().pipe(catchError(() => of([]))),
    analistasCont: this.dropdownService.getAnalistasContable().pipe(catchError(() => of([]))),
    estadoOt: this.dropdownService.getEstadoOt().pipe(catchError(() => of([])))
  }).subscribe(data => {
    this.clientes = data.clientes || [];
    this.proyectos = data.proyectos || [];
    this.fases = data.fases || [];
    this.sites = data.sites || [];
    this.regiones = data.regiones || [];
    this.tiposOt = data.tiposOt || []; // ✅ AGREGAR
    this.jefaturasCliente = data.jefaturasCliente || [];
    this.analistasCliente = data.analistasCliente || [];
    this.coordinadoresTiCw = data.coordinadoresTiCw || [];
    this.jefaturasResponsable = data.jefaturasResp || [];
    this.liquidadores = data.liquidadores || [];
    this.ejecutantes = data.ejecutantes || [];
    this.analistasContable = data.analistasCont || [];
    this.estadoOT = data.estadoOt || [];

    this.cdr.detectChanges();
  });

  this.subscriptions.push(catalogSub);
}
  private cargarAreasPorCliente(idCliente: number): void {
    const areasSub = this.dropdownService.getAreasByCliente(idCliente).pipe(
      catchError(() => of([]))
    ).subscribe(areas => {
      this.areas = areas || [];
      if (this.areas.length > 0) {
        this.form.get('idArea')?.enable();
      }

      this.cdr.detectChanges();
    });

    this.subscriptions.push(areasSub);
  }

  // ============================================
  // MÉTODOS DE CAMBIOS EN DROPDOWNS
  // ============================================

  onClienteChange(event: any): void {
    if (event) {
      this.selectedClienteId = event.id;
      this.form.get('idCliente')?.setValue(event.id);
      this.form.get('idCliente')?.markAsTouched();
      this.cargarAreasPorCliente(event.id);
    } else {
      this.selectedClienteId = null;
      this.form.get('idCliente')?.setValue(null);
      this.areas = [];
      this.selectedAreaId = null;
      this.form.get('idArea')?.setValue(null);
      this.form.get('idArea')?.disable();
    }
    this.actualizarDescripcion();
  }

  onAreaChange(event: any): void {
    this.selectedAreaId = event?.id || null;
    this.form.get('idArea')?.setValue(event?.id || null);
    if (event) this.form.get('idArea')?.markAsTouched();
    this.actualizarDescripcion();
  }

  onProyectoChange(event: any): void {
    this.selectedProyectoId = event?.id || null;
    this.form.get('idProyecto')?.setValue(event?.id || null);
    if (event) this.form.get('idProyecto')?.markAsTouched();
    this.actualizarDescripcion();
  }

  onFaseChange(event: any): void {
    this.selectedFaseId = event?.id || null;
    this.form.get('idFase')?.setValue(event?.id || null);
    if (event) this.form.get('idFase')?.markAsTouched();
  }

  onSiteChange(event: any): void {
    this.selectedSiteId = event?.id || null;
    this.form.get('idSite')?.setValue(event?.id || null);
    if (event) this.form.get('idSite')?.markAsTouched();
    this.actualizarDescripcion();
  }

  onRegionChange(event: any): void {
    this.selectedRegionId = event?.id || null;
    this.form.get('idRegion')?.setValue(event?.id || null);
    if (event) this.form.get('idRegion')?.markAsTouched();
  }
get tipoOtNombre(): string {
  console.log('Getting tipoOtNombre:', {
    selectedId: this.selectedTipoOtId,
    items: this.tiposOt,
    formValue: this.form.get('idTipoOt')?.value
  }); // DEBUG

  // Usar el valor del formulario si selectedTipoOtId es null
  const id = this.selectedTipoOtId || this.form.get('idTipoOt')?.value;

  if (!id || !this.tiposOt || this.tiposOt.length === 0) {
    return '';
  }

  const item = this.tiposOt.find(i => i.id === id);
  if (!item) {
    console.log('Item no encontrado para ID:', id);
    return '';
  }

  if (item.label && item.label.trim() !== '') return item.label;
  if (item.adicional && item.adicional.trim() !== '') return item.adicional;
  return '—';
}
  onJefaturaClienteChange(event: any): void {
    this.selectedJefaturaClienteId = event?.id || null;
    this.form.get('idJefaturaClienteSolicitante')?.setValue(event?.id || null);
    if (event) this.form.get('idJefaturaClienteSolicitante')?.markAsTouched();
  }

  onAnalistaClienteChange(event: any): void {
    this.selectedAnalistaClienteId = event?.id || null;
    this.form.get('idAnalistaClienteSolicitante')?.setValue(event?.id || null);
    if (event) this.form.get('idAnalistaClienteSolicitante')?.markAsTouched();
  }

  onCoordinadorTiCwChange(event: any): void {
    this.selectedCoordinadorTiCwId = event?.id || null;
    this.form.get('idCoordinadorTiCw')?.setValue(event?.id || null);
    if (event) this.form.get('idCoordinadorTiCw')?.markAsTouched();
  }

  onJefaturaResponsableChange(event: any): void {
    this.selectedJefaturaResponsableId = event?.id || null;
    this.form.get('idJefaturaResponsable')?.setValue(event?.id || null);
    if (event) this.form.get('idJefaturaResponsable')?.markAsTouched();
  }

  onLiquidadorChange(event: any): void {
    this.selectedLiquidadorId = event?.id || null;
    this.form.get('idLiquidador')?.setValue(event?.id || null);
    if (event) this.form.get('idLiquidador')?.markAsTouched();
  }

  onEjecutanteChange(event: any): void {
    this.selectedEjecutanteId = event?.id || null;
    this.form.get('idEjecutante')?.setValue(event?.id || null);
    if (event) this.form.get('idEjecutante')?.markAsTouched();
  }

  onAnalistaContableChange(event: any): void {
    this.selectedAnalistaContableId = event?.id || null;
    this.form.get('idAnalistaContable')?.setValue(event?.id || null);
    if (event) this.form.get('idAnalistaContable')?.markAsTouched();
  }

  onEstadoOTChange(event: any): void {
    this.selectedEstadoOTId = event?.id || null;
    this.form.get('idEstadoOt')?.setValue(event?.id || null);
    if (event) this.form.get('idEstadoOt')?.markAsTouched();
  }

  // ============================================
  // MÉTODOS UTILITARIOS
  // ============================================

  getItemNombre(id: number | null, items: DropdownItem[]): string {
    if (!id || !items || items.length === 0) return '';
    const item = items.find(i => i.id === id);
    if (!item) return '';

    if (item.label && item.label.trim() !== '') return item.label;
    if (item.adicional && item.adicional.trim() !== '') return item.adicional;
    return '—';
  }

  // Getters para resumen mejorados
  get clienteNombre(): string {
    return this.getItemNombre(this.selectedClienteId, this.clientes);
  }

  get areaNombre(): string {
    return this.getItemNombre(this.selectedAreaId, this.areas);
  }

  get proyectoNombre(): string {
    return this.getItemNombre(this.selectedProyectoId, this.proyectos);
  }

  get faseNombre(): string {
    return this.getItemNombre(this.selectedFaseId, this.fases);
  }

  get siteNombre(): string {
    const item = this.sites.find(s => s.id === this.selectedSiteId);
    if (!item) return '';

    if (item.label && item.label.trim() !== '') {
      const adicional = item.adicional?.trim();
      return adicional ? `${item.label} ${adicional}` : item.label;
    }
    if (item.adicional && item.adicional.trim() !== '') return item.adicional;
    return '—';
  }

  get regionNombre(): string {
    return this.getItemNombre(this.selectedRegionId, this.regiones);
  }

  get fechaAperturaFormatted(): string {
    const fecha = this.form.value.fechaApertura;
    return fecha ? this.formatDate(new Date(fecha)) : '—';
  }

  private formatDate(date: Date): string {
  return date.toLocaleDateString('es-PE', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
    weekday: 'long'
  });
}
  get f() {
    return this.form.controls as { [K in keyof typeof this.form.value]: AbstractControl };
  }

  // ============================================
  // MÉTODOS DE VALIDACIÓN
  // ============================================

  private actualizarDescripcion(): void {
    if (this.isEditMode || this.isViewMode) return;

    const proyecto = this.proyectoNombre;
    const area = this.areaNombre;
    const site = this.siteNombre;

    const normalize = (str: string) =>
      str
        .trim()
        .replace(/[^\w\s]/g, '')
        .replace(/\s+/g, ' ');

    const partes = [
      normalize(proyecto),
      normalize(area),
      normalize(site)
    ].filter(part => part && part.length > 0);

    const desc = partes.join('_').toUpperCase() || 'OT SIN DESCRIPCION AUTOMATICA';

    this.form.get('descripcion')?.setValue(desc);
  }

  validarPaso1(): boolean {
  this.submitted = true;

  const controlesPaso1 = [
    'idCliente',
    'idArea',
    'idProyecto',
    'idFase',
    'idSite',
    'idRegion',
    'idTipoOt',
    'fechaApertura',
    'descripcion'
  ];

  // Agregar idOtsAnterior si es requerido
  if (this.esOtAnteriorRequerido) {
    controlesPaso1.push('idOtsAnterior');
  }

  controlesPaso1.forEach(control => {
    if (this.f[control]) {
      this.f[control].markAsTouched();
    }
  });

  const invalidos = controlesPaso1.filter(control =>
    this.f[control] && this.f[control].invalid
  );

  if (invalidos.length > 0) {
    this.mostrarErrorValidacion('Información Principal', invalidos.length);
    this.scrollToFirstInvalid(controlesPaso1);
    return false;
  }

  const descripcion = this.form.get('descripcion')?.value;
  if (!descripcion || descripcion.trim() === '' || descripcion.trim().length < 10) {
    this.mostrarError('La descripción debe tener al menos 10 caracteres.');
    return false;
  }

  return true;
}

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

    if (this.isEditMode) {
      controlesPaso2.push('idEstadoOt');
    }

    controlesPaso2.forEach(control => {
      if (this.f[control]) {
        this.f[control].markAsTouched();
      }
    });

    const invalidos = controlesPaso2.filter(control =>
      this.f[control] && this.f[control].invalid
    );

    if (invalidos.length > 0) {
      this.mostrarErrorValidacion('Responsables', invalidos.length);
      this.scrollToFirstInvalid(controlesPaso2);
      return false;
    }

    return true;
  }

  private scrollToFirstInvalid(controles: string[]): void {
    const primerInvalido = controles.find(control =>
      this.f[control] && this.f[control].invalid
    );

    if (primerInvalido) {
      const elemento = document.querySelector(`[formControlName="${primerInvalido}"]`);
      if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });
        elemento.classList.add('is-invalid-highlight');
        setTimeout(() => elemento.classList.remove('is-invalid-highlight'), 3000);
      }
    }
  }
// ============================================
// MÉTODOS UTILITARIOS PARA FECHAS
// ============================================

private getAnioActual(): number {
  return new Date().getFullYear();
}

private getAnioDeFecha(fechaString: string): number | null {
  if (!fechaString) return null;

  try {
    const fecha = new Date(fechaString);
    return fecha.getFullYear();
  } catch (error) {
    console.error('Error al parsear fecha:', error);
    return null;
  }
}

// También puedes actualizar el método formatDate para mostrar años

  // ============================================
  // MÉTODOS DE NAVEGACIÓN
  // ============================================

  cambiarPaso(paso: number): void {
    if (paso === 2 && this.currentStep === 1) {
      if (!this.validarPaso1()) {
        return;
      }
    }

    if (paso === 3 && this.currentStep === 2) {
      if (!this.validarPaso2()) {
        return;
      }
    }

    this.currentStep = paso;

    setTimeout(() => {
      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = 0;
      }
    }, 100);
  }

  // ============================================
  // MÉTODOS DE FORMULARIO
  // ============================================

private patchFormValues(data: any): void {
  if (!data) return;

  console.log('Patching form values:', data); // DEBUG

  this.form.patchValue(data);

  this.selectedClienteId = data.idCliente || null;
  this.selectedAreaId = data.idArea || null;
  this.selectedProyectoId = data.idProyecto || null;
  this.selectedFaseId = data.idFase || null;
  this.selectedSiteId = data.idSite || null;
  this.selectedRegionId = data.idRegion || null;
  this.selectedJefaturaClienteId = data.idJefaturaClienteSolicitante || null;
  this.selectedAnalistaClienteId = data.idAnalistaClienteSolicitante || null;
  this.selectedCoordinadorTiCwId = data.idCoordinadorTiCw || null;
  this.selectedTipoOtId = data.idTipoOt || null; // ✅ Esto debería estar
  this.selectedJefaturaResponsableId = data.idJefaturaResponsable || null;
  this.selectedLiquidadorId = data.idLiquidador || null;
  this.selectedEjecutanteId = data.idEjecutante || null;
  this.selectedAnalistaContableId = data.idAnalistaContable || null;
  this.selectedEstadoOTId = data.idEstadoOt || null;

  console.log('Selected Tipo OT ID after patch:', this.selectedTipoOtId); // DEBUG
  console.log('Form idTipoOt value:', this.form.get('idTipoOt')?.value); // DEBUG

  // Ejecutar validación condicional después de cargar datos
  setTimeout(() => {
    this.actualizarValidacionOtAnterior();
  }, 100);
}
  // ============================================
  // MÉTODOS DE GUARDADO
  // ============================================

 onSubmit(): void {
  this.submitted = true;

  // Validar límites numéricos antes de validar
  this.formatearNumeroOtAnterior();

  // Validar todos los pasos
  if (!this.validarPaso1() || !this.validarPaso2()) {
    this.mostrarError('Por favor complete todos los campos obligatorios en todos los pasos.');
    return;
  }

  // Confirmar acción
  this.confirmarAccion();
}

  private confirmarAccion(): void {
    const title = this.isEditMode ? '¿Guardar cambios?' : '¿Crear nueva OT?';
    const html = `
      <div class="text-center">
        <i class="bi bi-question-circle text-primary fs-1 mb-3 d-block"></i>
        <p class="mb-0">${this.isEditMode ? 'Se actualizará la información de esta orden de trabajo.' : '¿Desea crear la nueva orden de trabajo?'}</p>
        ${!this.isEditMode ? `
          <div class="mt-3 p-3 bg-light rounded">
            <small class="text-muted d-block">Resumen:</small>
            <strong class="d-block">${this.proyectoNombre}</strong>
            <small class="text-muted">${this.clienteNombre} • ${this.areaNombre}</small>
          </div>
        ` : ''}
      </div>
    `;

    Swal.fire({
      title,
      html,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#10b981',
      cancelButtonColor: '#6b7280',
      confirmButtonText: this.isEditMode ? 'Guardar cambios' : 'Crear OT',
      cancelButtonText: 'Cancelar',
      reverseButtons: true,
      background: '#f8fafc',
      showClass: {
        popup: 'animate__animated animate__fadeInDown'
      },
      hideClass: {
        popup: 'animate__animated animate__fadeOutUp'
      }
    }).then(result => {
      if (!result.isConfirmed) return;

      this.ejecutarGuardado();
    });
  }
// ============================================
// MÉTODOS DE AYUDA
// ============================================

get otAnteriorLimite(): number {
  return 2147483647; // Límite máximo de int en Java
}

get otAnteriorLimiteFormateado(): string {
  return '2,147,483,647';
}

// Método para verificar si está dentro del límite
esOtAnteriorValido(): boolean {
  const valor = this.form.get('idOtsAnterior')?.value;
  if (!valor) return true; // Si está vacío, es válido (a menos que sea requerido)
  return valor >= 1 && valor <= 2147483647;
}
private ejecutarGuardado(): void {
  this.loadingSubmit = true;

  const values = this.form.getRawValue();

  // Validar límite de OT Anterior
  if (values.idOtsAnterior && values.idOtsAnterior > 2147483647) {
    this.loadingSubmit = false;
    this.mostrarError('El número de OT anterior excede el límite permitido (2,147,483,647)');
    return;
  }

  // DEBUG: Verifica los valores del formulario
  console.log('Valores del formulario:', values);
  console.log('idOtsAnterior value:', values.idOtsAnterior, 'Tipo:', typeof values.idOtsAnterior);

  // ⚠️ ERROR AQUÍ: Estás validando idOtsAnterior en lugar de idTipoOt
  // Validar que idTipoOt tenga un valor
  if (!values.idTipoOt) { // Cambia esto de idOtsAnterior a idTipoOt
    this.loadingSubmit = false;
    this.mostrarError('El campo Tipo OT es requerido');
    return;
  }

  const payload: OtCreateRequest = {
    idOts: this.isEditMode ? Number(values.idOts) : undefined,
    idCliente: Number(values.idCliente),
    idArea: Number(values.idArea),
    idProyecto: Number(values.idProyecto),
    idFase: Number(values.idFase),
    idSite: Number(values.idSite),
    idRegion: Number(values.idRegion),
    idTipoOt: Number(values.idTipoOt), // Esto debe tener un valor
    descripcion: values.descripcion.trim(),
    fechaApertura: values.fechaApertura,
    idOtsAnterior: values.idOtsAnterior ? Number(values.idOtsAnterior) : null,
    idJefaturaClienteSolicitante: Number(values.idJefaturaClienteSolicitante),
    idAnalistaClienteSolicitante: Number(values.idAnalistaClienteSolicitante),
    idCoordinadorTiCw: Number(values.idCoordinadorTiCw),
    idJefaturaResponsable: Number(values.idJefaturaResponsable),
    idLiquidador: Number(values.idLiquidador),
    idEjecutante: Number(values.idEjecutante),
    idAnalistaContable: Number(values.idAnalistaContable),
    idEstadoOt: this.isEditMode ? Number(values.idEstadoOt) : null
  };

  console.log('Payload enviado:', payload); // DEBUG

  const saveSub = this.otService.saveOt(payload).subscribe({
    next: (res: OtDetailResponse) => {
      console.log('Respuesta del backend:', res); // DEBUG
      this.loadingSubmit = false;

      // Mostrar animación de éxito
      this.mostrarExito(res);

      this.saved.emit();

      setTimeout(() => {
        this.onClose();
      }, 2500);
    },
    error: (err) => {
      console.error('Error al guardar:', err); // DEBUG
      this.loadingSubmit = false;
      this.mostrarError(err?.error?.message || 'Ocurrió un problema inesperado al guardar');
    }
  });

  this.subscriptions.push(saveSub);
}
  // ============================================
  // MÉTODOS DE RESET Y CANCELAR
  // ============================================

  onCancel(): void {
    if (this.form.dirty && !this.submitted) {
      Swal.fire({
        title: '¿Descartar cambios?',
        text: 'Tiene cambios sin guardar. ¿Está seguro de que desea salir?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#ef4444',
        cancelButtonColor: '#6b7280',
        confirmButtonText: 'Sí, descartar',
        cancelButtonText: 'Cancelar',
        reverseButtons: true,
        background: '#f8fafc'
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
    confirmButtonColor: '#ef4444',
    cancelButtonText: 'Cancelar',
    confirmButtonText: this.isEditMode ? 'Descartar' : 'Limpiar',
    reverseButtons: true,
    background: '#f8fafc'
  }).then(result => {
    if (!result.isConfirmed) return;

    if (this.isEditMode && this.otId) {
      this.loading = true;
      const resetSub = this.otService.getOtParaEdicion(this.otId).subscribe({
        next: (ot) => {
          this.form.reset();
          this.patchFormValues(ot);
          this.loading = false;
        },
        error: () => {
          this.mostrarError('No se pudieron restaurar los datos');
          this.loading = false;
        }
      });
      this.subscriptions.push(resetSub);
    } else {
      const hoy = new Date().toISOString().split('T')[0];
      this.form.reset({
        fechaApertura: hoy
      });

      // Resetear IDs seleccionados
      this.selectedClienteId = null;
      this.selectedAreaId = null;
      this.selectedProyectoId = null;
      this.selectedFaseId = null;
      this.selectedSiteId = null;
      this.selectedRegionId = null;
      this.selectedTipoOtId = null; // ✅ AGREGAR ESTA LÍNEA
      this.selectedJefaturaClienteId = null;
      this.selectedAnalistaClienteId = null;
      this.selectedCoordinadorTiCwId = null;
      this.selectedJefaturaResponsableId = null;
      this.selectedLiquidadorId = null;
      this.selectedEjecutanteId = null;
      this.selectedAnalistaContableId = null;
      this.selectedEstadoOTId = null;

      this.submitted = false;
      this.areas = [];
      this.form.get('idArea')?.disable();
      this.actualizarDescripcion();
      this.currentStep = 1;

      if (this.scrollContainer) {
        this.scrollContainer.nativeElement.scrollTop = 0;
      }
    }
  });
}

  // ============================================
  // MÉTODOS DE BÚSQUEDA (para completar)
  // ============================================

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

  // ============================================
  // MÉTODOS DE NOTIFICACIÓN
  // ============================================

  private mostrarError(mensaje: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: mensaje,
      confirmButtonColor: '#ef4444',
      background: '#f8fafc',
      iconColor: '#ef4444',
      showClass: {
        popup: 'animate__animated animate__headShake'
      }
    });
  }

  private mostrarErrorValidacion(seccion: string, cantidad: number): void {
    Swal.fire({
      icon: 'warning',
      title: `${seccion} incompleta`,
      html: `Faltan <strong>${cantidad}</strong> campo(s) requerido(s)`,
      confirmButtonColor: '#f59e0b',
      background: '#f8fafc',
      iconColor: '#f59e0b'
    });
  }

  private mostrarExito(res: OtDetailResponse): void {
    Swal.fire({
      icon: 'success',
      title: this.isEditMode ? '¡Actualizado!' : '¡Creado con éxito!',
      html: `
        <div class="text-center">
          <i class="bi bi-check-circle-fill text-success display-4 mb-3"></i>
          <h5 class="fw-bold mb-2">${this.isEditMode ? 'OT Actualizada' : 'Nueva OT Creada'}</h5>
          <div class="ot-success-number mt-3">
            <span class="badge bg-success bg-opacity-10 text-success fs-5 p-3">
              #${res.ot}
            </span>
          </div>
          <p class="text-muted mt-3 mb-0">La orden de trabajo ha sido procesada correctamente.</p>
        </div>
      `,
      timer: 2000,
      timerProgressBar: true,
      showConfirmButton: false,
      background: '#f8fafc',
      showClass: {
        popup: 'animate__animated animate__zoomIn'
      },
      hideClass: {
        popup: 'animate__animated animate__zoomOut'
      }
    });
  }
}
