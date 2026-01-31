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

  // Loading states
  loadingClientes: boolean = false;
  loadingAreas: boolean = false;
  loadingProyectos: boolean = false;
  loadingFases: boolean = false;
  loadingSites: boolean = false;
  loadingRegiones: boolean = false;
  loadingJefaturasCliente: boolean = false;
  loadingAnalistasCliente: boolean = false;
  loadingCoordinadoresTiCw: boolean = false;
  loadingJefaturasResponsable: boolean = false;
  loadingLiquidadores: boolean = false;
  loadingEjecutantes: boolean = false;
  loadingAnalistasContable: boolean = false;
  loadingEstadoOT: boolean = false;

  currentStep: number = 1;
  form!: FormGroup;
  submitted = false;
  loading = false;
  isEditMode = false;

  usernameLogueado: string = '—';
  trabajadorIdLogueado: number | null = null;

  // IDs seleccionados
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

  private subscriptions: Subscription[] = [];

  constructor() {
    // No crear formulario en constructor
  }

  ngOnInit(): void {
    console.log('FormOtsComponent inicializando...');
    
    this.isEditMode = this.mode === 'edit';
    this.crearFormularioBase();
    
    const user = this.authService.currentUser;
    this.usernameLogueado = user?.username || '—';
    this.trabajadorIdLogueado = user?.idTrabajador ?? null;

    if (this.isEditMode && this.otId) {
      this.cargarDatosParaEdicion(this.otId);
    } else if (this.mode === 'edit' && this.otData) {
      this.patchFormValues(this.otData);
      const clienteId = this.form.get('idCliente')?.value;
      if (clienteId) {
        this.cargarAreasPorCliente(clienteId);
      }
      this.cargarTodosLosCatalogos();
    } else {
      this.cargarDropdownsParaCreacion();
    }

    if (this.isViewMode) {
      setTimeout(() => {
        this.form.disable();
      }, 100);
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  private crearFormularioBase(): void {
    const hoy = new Date().toISOString().split('T')[0];

    this.form = this.fb.group({
      idOts: [null],
      idCliente: [null, Validators.required],
      idArea: [null, Validators.required],
      idProyecto: [null, Validators.required],
      idFase: [null, Validators.required],
      idSite: [null, Validators.required],
      idRegion: [null, Validators.required],
      descripcion: ['', [Validators.required, Validators.minLength(10)]],
      fechaApertura: [hoy, Validators.required],
      idOtsAnterior: [null],

      idJefaturaClienteSolicitante: [null, Validators.required],
      idAnalistaClienteSolicitante: [null, Validators.required],
      idCoordinadorTiCw: [null, Validators.required],
      idJefaturaResponsable: [null, Validators.required],
      idLiquidador: [null, Validators.required],
      idEjecutante: [null, Validators.required],
      idAnalistaContable: [null, Validators.required],
      idEstadoOt: [null]
    });

    this.form.get('idArea')?.disable();
    
    if (!this.isEditMode) {
      this.form.get('descripcion')?.disable();
    }

    if (this.isEditMode) {
      this.form.get('idEstadoOt')?.setValidators(Validators.required);
      this.form.get('idEstadoOt')?.updateValueAndValidity();
    }
  }

  // Métodos para cambios en dropdowns
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

  // Helper para obtener nombres
  getItemNombre(id: number | null, items: DropdownItem[]): string {
    if (!id || !items || items.length === 0) return '';
    const item = items.find(i => i.id === id);
    if (!item) return '';
    
    if (item.label && item.label.trim() !== '') return item.label;
    if (item.adicional && item.adicional.trim() !== '') return item.adicional;
    return '—';
  }

  // Getters para resumen
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
    return fecha ? new Date(fecha).toLocaleDateString('es-PE', { day: '2-digit', month: '2-digit', year: 'numeric' }) : '—';
  }

  get f() {
    return this.form.controls as { [K in keyof typeof this.form.value]: AbstractControl };
  }

  // Métodos de carga
  private cargarDropdownsParaCreacion(): void {
    this.loading = true;
    
    const catalogSub = forkJoin({
      clientes: this.dropdownService.getClientes().pipe(catchError(() => of([]))),
      proyectos: this.dropdownService.getProyectos().pipe(catchError(() => of([]))),
      fases: this.dropdownService.getFases().pipe(catchError(() => of([]))),
      sites: this.dropdownService.getSites().pipe(catchError(() => of([]))),
      regiones: this.dropdownService.getRegiones().pipe(catchError(() => of([])))
    }).subscribe({
      next: (data) => {
        this.clientes = data.clientes || [];
        this.proyectos = data.proyectos || [];
        this.fases = data.fases || [];
        this.sites = data.sites || [];
        this.regiones = data.regiones || [];
        
        this.cargarDropdownsResponsables();
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (error) => {
        console.error('Error al cargar catálogos:', error);
        this.loading = false;
        this.cdr.detectChanges();
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar los catálogos',
          confirmButtonColor: '#dc3545'
        });
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

  private cargarTodosLosCatalogos(): void {
    const catalogSub = forkJoin({
      clientes: this.dropdownService.getClientes().pipe(catchError(() => of([]))),
      proyectos: this.dropdownService.getProyectos().pipe(catchError(() => of([]))),
      fases: this.dropdownService.getFases().pipe(catchError(() => of([]))),
      sites: this.dropdownService.getSites().pipe(catchError(() => of([]))),
      regiones: this.dropdownService.getRegiones().pipe(catchError(() => of([]))),
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
    this.loadingAreas = true;
    
    const areasSub = this.dropdownService.getAreasByCliente(idCliente).pipe(
      catchError(() => of([]))
    ).subscribe(areas => {
      this.areas = areas || [];
      if (this.areas.length > 0) {
        this.form.get('idArea')?.enable();
      }
      
      this.loadingAreas = false;
      this.cdr.detectChanges();
    });

    this.subscriptions.push(areasSub);
  }

  private patchFormValues(data: any): void {
    if (!data) return;
    
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
    this.selectedJefaturaResponsableId = data.idJefaturaResponsable || null;
    this.selectedLiquidadorId = data.idLiquidador || null;
    this.selectedEjecutanteId = data.idEjecutante || null;
    this.selectedAnalistaContableId = data.idAnalistaContable || null;
    this.selectedEstadoOTId = data.idEstadoOt || null;
  }

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

  // Validaciones
  validarPaso1(): boolean {
    this.submitted = true;
    
    const controlesPaso1 = [
      'idCliente',
      'idArea',
      'idProyecto',
      'idFase',
      'idSite',
      'idRegion',
      'fechaApertura',
      'descripcion'
    ];

    controlesPaso1.forEach(control => {
      if (this.f[control]) {
        this.f[control].markAsTouched();
      }
    });

    const invalidos = controlesPaso1.filter(control => 
      this.f[control] && this.f[control].invalid
    );

    if (invalidos.length > 0) {
      const primerInvalido = invalidos[0];
      const elemento = document.querySelector(`[formControlName="${primerInvalido}"]`);

      if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });
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

    const descripcion = this.form.get('descripcion')?.value;
    if (!descripcion || descripcion.trim() === '' || descripcion.trim().length < 10) {
      Swal.fire({
        icon: 'warning',
        title: 'Descripción incompleta',
        text: 'La descripción debe tener al menos 10 caracteres. Verifique los campos seleccionados.',
        confirmButtonColor: '#ffc107'
      });
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
      const primerInvalido = invalidos[0];
      const elemento = document.querySelector(`[formControlName="${primerInvalido}"]`);

      if (elemento) {
        elemento.scrollIntoView({ behavior: 'smooth', block: 'center' });
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

  // Navegación
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

  // Submit
  onSubmit(): void {
    this.submitted = true;
    
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
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then(result => {
      if (!result.isConfirmed) return;

      this.loading = true;

      const values = this.form.getRawValue();
      
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
            showConfirmButton: false,
            background: '#f8f9fa'
          });

          this.saved.emit();

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

  // Cancelar
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
        cancelButtonText: 'Cancelar',
        reverseButtons: true
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

  // Reset
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
      confirmButtonText: this.isEditMode ? 'Descartar' : 'Limpiar',
      reverseButtons: true
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
      } else {
        const hoy = new Date().toISOString().split('T')[0];
        this.form.reset({
          fechaApertura: hoy
        });
        
        this.selectedClienteId = null;
        this.selectedAreaId = null;
        this.selectedProyectoId = null;
        this.selectedFaseId = null;
        this.selectedSiteId = null;
        this.selectedRegionId = null;
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

  // Handlers para búsquedas
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