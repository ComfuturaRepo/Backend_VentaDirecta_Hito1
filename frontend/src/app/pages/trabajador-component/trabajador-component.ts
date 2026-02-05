import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators, FormControl, AbstractControl, ValidationErrors } from '@angular/forms';
import { RouterModule } from '@angular/router';

// Componentes personalizados

// Utils
import Swal from 'sweetalert2';
import { debounceTime, distinctUntilChanged, Subject, takeUntil, forkJoin, firstValueFrom, Observable, map, catchError, of } from 'rxjs';
import { NgselectDropdownComponent } from '../../component/ngselect-dropdown-component/ngselect-dropdown-component';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { TrabajadorDetail, TrabajadorRequest, TrabajadorSimple, TrabajadorStats, TrabajadorUpdate } from '../../model/trabajador.model';
import { DropdownItem, DropdownService } from '../../service/dropdown.service';
import { ValidationService } from '../../service/validation.service';
import { TrabajadorService } from '../../service/trabajador.service';
import { PageResponse } from '../../model/page.interface';
import { PermisoDirective } from '../../directive/permiso.directive';

@Component({
  selector: 'app-trabajador',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    NgselectDropdownComponent,
    PaginationComponent,     PermisoDirective


  ],
  templateUrl: './trabajador-component.html',
  styleUrls: ['./trabajador-component.css']
})
export class TrabajadorComponent implements OnInit, OnDestroy {
  // Estados del componente
  isCreating = false;
  isEditing = false;
  isLoading = false;
  isSearching = false;
  showModal = false;
  modalTitle = '';

  // Datos
  trabajadores: TrabajadorSimple[] = [];
  selectedTrabajador: TrabajadorDetail | null = null;
  stats: TrabajadorStats | null = null;

  // Filtros y paginación
  currentPage = 0;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;

  // Dropdowns como DropdownItem[]
  areas: DropdownItem[] = [];
  cargos: DropdownItem[] = [];
  empresas: DropdownItem[] = [];

  // Roles para filtros
  rolesOptions = [
    { id: 1, label: 'Sí', estado: true },
    { id: 2, label: 'No', estado: false },
    { id: 3, label: 'Todos' }
  ];

  estadoOptions = [
    { id: 1, label: 'Activo', estado: true },
    { id: 2, label: 'Inactivo', estado: false },
    { id: 3, label: 'Todos' }
  ];

  // Formularios
  trabajadorForm: FormGroup;
  filterForm: FormGroup;

  // Para búsqueda con debounce
  private searchSubject: Subject<string>;
  private destroy$: Subject<void>;

  constructor(
    private fb: FormBuilder,
    private dropdownService: DropdownService,
    private trabajadorService: TrabajadorService,
    private validationService: ValidationService
  ) {
    // Inicializar Subjects
    this.searchSubject = new Subject<string>();
    this.destroy$ = new Subject<void>();

    // Inicializar arrays vacíos
    this.trabajadores = [];
    this.areas = [];
    this.cargos = [];
    this.empresas = [];

    // Formulario principal
    this.trabajadorForm = this.fb.group({
      nombres: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      apellidos: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(100)]],
      dni: ['', [
        Validators.required,
        this.validationService.dniValidator()
      ]],
      celular: ['', [
        Validators.required,
        this.validationService.celularValidator()
      ]],
      correoCorporativo: ['', [
        Validators.required,
        Validators.email,
        Validators.maxLength(150)
      ]],
      areaId: new FormControl<number | null>(null, Validators.required),
      cargoId: new FormControl<number | null>(null, Validators.required),
      empresaId: new FormControl<number | null>(null),
      activo: [true],
      puedeSerLiquidador: [false],
      puedeSerEjecutante: [false],
      puedeSerAnalistaContable: [false],
      puedeSerJefaturaResponsable: [false],
      puedeSerCoordinadorTiCw: [false]
    });

    // Formulario de filtros
    this.filterForm = this.fb.group({
      search: [''],
      activo: new FormControl<number | null>(null),
      areaId: new FormControl<number | null>(null),
      cargoId: new FormControl<number | null>(null),
      empresaId: new FormControl<number | null>(null),
      puedeSerLiquidador: new FormControl<boolean | null>(null),
      puedeSerEjecutante: new FormControl<boolean | null>(null),
      puedeSerAnalistaContable: new FormControl<boolean | null>(null),
      puedeSerJefaturaResponsable: new FormControl<boolean | null>(null),
      puedeSerCoordinadorTiCw: new FormControl<boolean | null>(null)
    });
  }

// Validador async para DNI
private dniAsyncValidator(control: AbstractControl): Observable<ValidationErrors | null> {
  if (!control.value || control.invalid) {
    return of(null);
  }

  return this.trabajadorService.checkDniExists(control.value).pipe(
    map(exists => exists ? { dniExists: true } : null),
    catchError(() => of(null)) // Si hay error, no mostramos validación
  );
}

// Validador async para email
private emailAsyncValidator(control: AbstractControl): Observable<ValidationErrors | null> {
  if (!control.value || control.invalid) {
    return of(null);
  }

  // En modo edición, permitimos el mismo email
  if (this.isEditing && this.selectedTrabajador &&
      this.selectedTrabajador.correoCorporativo === control.value) {
    return of(null);
  }

  return this.trabajadorService.checkEmailExists(control.value).pipe(
    map(exists => exists ? { emailExists: true } : null),
    catchError(() => of(null))
  );
}

  // ==============================================
  // GETTERS PARA FORM CONTROLS
  // ==============================================

  // Getters para el formulario de FILTROS
  get searchControl(): FormControl {
    return this.filterForm.get('search') as FormControl;
  }

  get activoControl(): FormControl {
    return this.filterForm.get('activo') as FormControl;
  }

  get areaIdControl(): FormControl {
    return this.filterForm.get('areaId') as FormControl;
  }

  get cargoIdControl(): FormControl {
    return this.filterForm.get('cargoId') as FormControl;
  }

  get empresaIdControl(): FormControl {
    return this.filterForm.get('empresaId') as FormControl;
  }

  get puedeSerLiquidadorControl(): FormControl {
    return this.filterForm.get('puedeSerLiquidador') as FormControl;
  }

  get puedeSerEjecutanteControl(): FormControl {
    return this.filterForm.get('puedeSerEjecutante') as FormControl;
  }

  get puedeSerAnalistaContableControl(): FormControl {
    return this.filterForm.get('puedeSerAnalistaContable') as FormControl;
  }

  get puedeSerJefaturaResponsableControl(): FormControl {
    return this.filterForm.get('puedeSerJefaturaResponsable') as FormControl;
  }

  get puedeSerCoordinadorTiCwControl(): FormControl {
    return this.filterForm.get('puedeSerCoordinadorTiCw') as FormControl;
  }

  // Getters para el formulario PRINCIPAL (trabajadorForm)
  get nombresControl(): FormControl {
    return this.trabajadorForm.get('nombres') as FormControl;
  }

  get apellidosControl(): FormControl {
    return this.trabajadorForm.get('apellidos') as FormControl;
  }

  get dniControl(): FormControl {
    return this.trabajadorForm.get('dni') as FormControl;
  }

  get celularControl(): FormControl {
    return this.trabajadorForm.get('celular') as FormControl;
  }

  get correoCorporativoControl(): FormControl {
    return this.trabajadorForm.get('correoCorporativo') as FormControl;
  }

  get areaIdTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('areaId') as FormControl;
  }

  get cargoIdTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('cargoId') as FormControl;
  }

  get empresaIdTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('empresaId') as FormControl;
  }

  get activoTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('activo') as FormControl;
  }

  get puedeSerLiquidadorTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('puedeSerLiquidador') as FormControl;
  }

  get puedeSerEjecutanteTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('puedeSerEjecutante') as FormControl;
  }

  get puedeSerAnalistaContableTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('puedeSerAnalistaContable') as FormControl;
  }

  get puedeSerJefaturaResponsableTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('puedeSerJefaturaResponsable') as FormControl;
  }

  get puedeSerCoordinadorTiCwTrabajadorControl(): FormControl {
    return this.trabajadorForm.get('puedeSerCoordinadorTiCw') as FormControl;
  }

  ngOnInit(): void {
    this.loadInitialData();
    this.setupSearchListener();
    this.loadStats();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  // ================ CARGA INICIAL ================
  loadInitialData(): void {
    this.loadDropdowns();
    this.loadTrabajadores();
  }

  loadDropdowns(): void {
    forkJoin({
      areas: this.dropdownService.getAreas(),
      cargos: this.dropdownService.getCargos(),
      empresas: this.dropdownService.getEmpresas()
    }).subscribe({
      next: (results) => {
        this.areas = results.areas;
        this.cargos = results.cargos;
        this.empresas = results.empresas;
      },
      error: (error) => this.showError('Error al cargar datos', error)
    });
  }

  loadStats(): void {
    this.trabajadorService.getStats().subscribe({
      next: (stats) => this.stats = stats,
      error: (error) => console.error('Error loading stats:', error)
    });
  }

  // ================ CRUD OPERATIONS ================
  loadTrabajadores(): void {
    this.isLoading = true;
    const filters = this.getFilters();

    this.trabajadorService.searchAdvanced({
      ...filters,
      page: this.currentPage,
      size: this.pageSize,
      sortBy: 'fechaCreacion',
      sortDirection: 'desc'
    }).subscribe({
      next: (response: PageResponse<TrabajadorSimple>) => {
        this.trabajadores = response.content;
        this.totalItems = response.totalItems;
        this.totalPages = response.totalPages;
        this.isLoading = false;
      },
      error: (error) => {
        this.showError('Error al cargar trabajadores', error);
        this.isLoading = false;
      }
    });
  }

  openCreateModal(): void {
    this.isCreating = true;
    this.isEditing = false;
    this.modalTitle = 'Nuevo Trabajador';
    this.selectedTrabajador = null;
    this.resetForm();
    this.showModal = true;
  }

  openEditModal(trabajador: TrabajadorSimple): void {
    this.isCreating = false;
    this.isEditing = true;
    this.modalTitle = 'Editar Trabajador';

    this.trabajadorService.findById(trabajador.idTrabajador).subscribe({
      next: (detail) => {
        this.selectedTrabajador = detail;
        this.populateForm(detail);
        this.showModal = true;
      },
      error: (error) => this.showError('Error al cargar datos del trabajador', error)
    });
  }

  closeModal(): void {
    this.showModal = false;
    this.isCreating = false;
    this.isEditing = false;
    this.selectedTrabajador = null;
  }

 saveTrabajador(): void {
  if (this.trabajadorForm.invalid) {
    this.markFormGroupTouched(this.trabajadorForm);
    return;
  }

  // Deshabilitar botones durante validación/guardado
  this.isLoading = true;

  const formValue = this.trabajadorForm.value;

  // Validar formulario de manera asíncrona
  this.validateFormAsync().then(isValid => {
    if (isValid) {
      if (this.isCreating) {
        this.createTrabajador(formValue);
      } else {
        this.updateTrabajador(formValue);
      }
    } else {
      this.isLoading = false; // Reactivar botones si la validación falla
    }
  }).catch(() => {
    this.isLoading = false;
  });
}
async validateFormAsync(): Promise<boolean> {
  // Resetear errores previos
  this.dniControl.setErrors(null);
  this.correoCorporativoControl.setErrors(null);

  let isValid = true;

  // Validar DNI único solo para creación
  if (this.isCreating && this.dniControl.valid) {
    try {
      const dniExists = await firstValueFrom(
        this.trabajadorService.checkDniExists(this.dniControl.value)
      );
      if (dniExists) {
        this.dniControl.setErrors({ dniExists: true });
        this.showWarning('El DNI ya está registrado');
        isValid = false;
      }
    } catch (error) {
      console.error('Error validando DNI:', error);
      // No bloquear por error de validación
    }
  }

  // Validar correo único (para creación y edición)
  if (this.correoCorporativoControl.valid) {
    try {
      const emailExists = await firstValueFrom(
        this.trabajadorService.checkEmailExists(this.correoCorporativoControl.value)
      );

      const isDifferentEmail = this.isEditing &&
        this.selectedTrabajador &&
        this.selectedTrabajador.correoCorporativo !== this.correoCorporativoControl.value;

      if (emailExists && (this.isCreating || isDifferentEmail)) {
        this.correoCorporativoControl.setErrors({ emailExists: true });
        this.showWarning('El correo corporativo ya está registrado');
        isValid = false;
      }
    } catch (error) {
      console.error('Error validando correo:', error);
      // No bloquear por error de validación
    }
  }

  return isValid && this.trabajadorForm.valid;
}
createTrabajador(data: TrabajadorRequest): void {
  this.isLoading = true;

  this.trabajadorService.create(data).subscribe({
    next: (response) => {
      this.showSuccess('Trabajador creado exitosamente');
      this.closeModal();
      this.loadTrabajadores();
      this.loadStats();
      this.isLoading = false;
    },
    error: (error) => {
      // Manejar error específico de duplicados
      if (error.status === 400 || error.status === 409) {
        let errorMessage = error.error?.message || 'Error de validación';

        if (errorMessage.toLowerCase().includes('dni')) {
          this.dniControl.setErrors({ dniExists: true });
          this.showWarning('El DNI ya está registrado');
        } else if (errorMessage.toLowerCase().includes('correo') || errorMessage.toLowerCase().includes('email')) {
          this.correoCorporativoControl.setErrors({ emailExists: true });
          this.showWarning('El correo ya está registrado');
        } else {
          this.showWarning(errorMessage);
        }
      } else {
        this.handleError('Error al crear trabajador', error);
      }
      this.isLoading = false;
    }
  });
}

updateTrabajador(data: TrabajadorUpdate): void {
  if (!this.selectedTrabajador) return;

  this.isLoading = true;

  this.trabajadorService.update(this.selectedTrabajador.idTrabajador, data).subscribe({
    next: (response) => {
      this.showSuccess('Trabajador actualizado exitosamente');
      this.closeModal();
      this.loadTrabajadores();
      this.isLoading = false;
    },
    error: (error) => {
      // Manejar error específico de duplicados
      if (error.status === 400 || error.status === 409) {
        let errorMessage = error.error?.message || 'Error de validación';

        if (errorMessage.toLowerCase().includes('correo') || errorMessage.toLowerCase().includes('email')) {
          this.correoCorporativoControl.setErrors({ emailExists: true });
          this.showWarning('El correo ya está registrado por otro trabajador');
        } else if (errorMessage.toLowerCase().includes('dni')) {
          this.dniControl.setErrors({ dniExists: true });
          this.showWarning('El DNI ya está registrado por otro trabajador');
        } else {
          this.showWarning(errorMessage);
        }
      } else {
        this.handleError('Error al actualizar trabajador', error);
      }
      this.isLoading = false;
    }
  });
}

  toggleActivo(trabajador: TrabajadorSimple): void {
    Swal.fire({
      title: `¿${trabajador.activo ? 'Desactivar' : 'Activar'} trabajador?`,
      text: `¿Estás seguro de ${trabajador.activo ? 'desactivar' : 'activar'} a ${trabajador.nombres} ${trabajador.apellidos}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: `Sí, ${trabajador.activo ? 'desactivar' : 'activar'}`,
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.trabajadorService.toggleActivo(trabajador.idTrabajador).subscribe({
          next: () => {
            this.showSuccess(`Trabajador ${trabajador.activo ? 'desactivado' : 'activado'} exitosamente`);
            this.loadTrabajadores();
            this.loadStats();
          },
          error: (error) => this.showError('Error al cambiar estado', error)
        });
      }
    });
  }

  deleteTrabajador(trabajador: TrabajadorSimple): void {
    Swal.fire({
      title: '¿Eliminar trabajador?',
      text: `¿Estás seguro de eliminar a ${trabajador.nombres} ${trabajador.apellidos}? Esta acción no se puede deshacer.`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.trabajadorService.toggleActivo(trabajador.idTrabajador).subscribe({
          next: () => {
            this.showSuccess('Trabajador eliminado exitosamente');
            this.loadTrabajadores();
            this.loadStats();
          },
          error: (error) => this.showError('Error al eliminar trabajador', error)
        });
      }
    });
  }

  // ================ FILTROS Y BÚSQUEDA ================
  setupSearchListener(): void {
    this.searchSubject.pipe(
      debounceTime(500),
      distinctUntilChanged(),
      takeUntil(this.destroy$)
    ).subscribe(() => {
      this.currentPage = 0;
      this.loadTrabajadores();
    });

    this.searchControl.valueChanges
      .pipe(takeUntil(this.destroy$))
      .subscribe(value => {
        this.searchSubject.next(value);
      });
  }

  onFilterChange(): void {
    this.currentPage = 0;
    this.loadTrabajadores();
  }

  clearFilters(): void {
    this.filterForm.reset({
      search: '',
      activo: null,
      areaId: null,
      cargoId: null,
      empresaId: null,
      puedeSerLiquidador: null,
      puedeSerEjecutante: null,
      puedeSerAnalistaContable: null,
      puedeSerJefaturaResponsable: null,
      puedeSerCoordinadorTiCw: null
    });
    this.loadTrabajadores();
  }

 getFilters(): any {
  const formValue = this.filterForm.value;
  const filters: any = {};

  // Para search
  if (formValue.search && formValue.search.trim() !== '') {
    filters.search = formValue.search.trim();
  }

  // Para estado (true/false/null)
  if (formValue.activo !== null && formValue.activo !== undefined) {
    filters.activo = formValue.activo === true;
  }

  // Para IDs (área, cargo, empresa)
  if (formValue.areaId) {
    filters.areaIds = [formValue.areaId];
  }

  if (formValue.cargoId) {
    filters.cargoIds = [formValue.cargoId];
  }

  if (formValue.empresaId) {
    filters.empresaIds = [formValue.empresaId];
  }

  // Para los roles booleanos (liquidador, ejecutante, etc.)
  const roleFields = [
    'puedeSerLiquidador',
    'puedeSerEjecutante',
    'puedeSerAnalistaContable',
    'puedeSerJefaturaResponsable',
    'puedeSerCoordinadorTiCw'
  ];

  roleFields.forEach(field => {
    if (formValue[field] !== null && formValue[field] !== undefined) {
      // El backend espera un array de booleanos [true] o [false]
      filters[field] = [formValue[field] === true];
    }
  });

  return filters;
}

  // ================ PAGINACIÓN ================
  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadTrabajadores();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadTrabajadores();
  }

  onRefresh(): void {
    this.loadTrabajadores();
    this.loadStats();
  }

  // ================ HELPERS ================
  populateForm(trabajador: TrabajadorDetail): void {
    this.trabajadorForm.patchValue({
      nombres: trabajador.nombres,
      apellidos: trabajador.apellidos,
      dni: trabajador.dni,
      celular: trabajador.celular,
      correoCorporativo: trabajador.correoCorporativo,
      areaId: trabajador.areaId,
      cargoId: trabajador.cargoId,
      empresaId: trabajador.empresaId || null,
      activo: trabajador.activo,
      puedeSerLiquidador: trabajador.puedeSerLiquidador,
      puedeSerEjecutante: trabajador.puedeSerEjecutante,
      puedeSerAnalistaContable: trabajador.puedeSerAnalistaContable,
      puedeSerJefaturaResponsable: trabajador.puedeSerJefaturaResponsable,
      puedeSerCoordinadorTiCw: trabajador.puedeSerCoordinadorTiCw
    });
  }

  resetForm(): void {
    this.trabajadorForm.reset({
      nombres: '',
      apellidos: '',
      dni: '',
      celular: '',
      correoCorporativo: '',
      areaId: null,
      cargoId: null,
      empresaId: null,
      activo: true,
      puedeSerLiquidador: false,
      puedeSerEjecutante: false,
      puedeSerAnalistaContable: false,
      puedeSerJefaturaResponsable: false,
      puedeSerCoordinadorTiCw: false
    });

    // Limpiar errores de validación
    Object.keys(this.trabajadorForm.controls).forEach(key => {
      const control = this.trabajadorForm.get(key);
      control?.setErrors(null);
    });
  }

  validateForm(): boolean {
    // Validar DNI único solo para creación
    if (this.isCreating) {
      if (this.dniControl.valid) {
        this.trabajadorService.checkDniExists(this.dniControl.value).subscribe({
          next: (exists) => {
            if (exists) {
              this.dniControl.setErrors({ dniExists: true });
              this.showWarning('El DNI ya está registrado');
            }
          }
        });
      }
    }

    // Validar correo único
    if (this.correoCorporativoControl.valid) {
      this.trabajadorService.checkEmailExists(this.correoCorporativoControl.value).subscribe({
        next: (exists) => {
          if (exists && (!this.selectedTrabajador || this.selectedTrabajador.correoCorporativo !== this.correoCorporativoControl.value)) {
            this.correoCorporativoControl.setErrors({ emailExists: true });
            this.showWarning('El correo corporativo ya está registrado');
          }
        }
      });
    }

    return this.trabajadorForm.valid;
  }

  markFormGroupTouched(formGroup: FormGroup): void {
    Object.values(formGroup.controls).forEach(control => {
      control.markAsTouched();
      if (control instanceof FormGroup) {
        this.markFormGroupTouched(control);
      }
    });
  }

  // ================ UTILIDADES ================
  getNombreCompleto(trabajador: TrabajadorSimple): string {
    return `${trabajador.nombres} ${trabajador.apellidos}`;
  }

  getEstadoBadge(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  getEstadoClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  // ================ ALERTAS ================
  showSuccess(message: string): void {
    Swal.fire({
      icon: 'success',
      title: '¡Éxito!',
      text: message,
      timer: 3000,
      showConfirmButton: false,
      toast: true,
      position: 'top-end'
    });
  }

  showError(title: string, error: any): void {
    console.error(title, error);
    Swal.fire({
      icon: 'error',
      title: title,
      text: error.error?.message || 'Ha ocurrido un error inesperado',
      confirmButtonText: 'Aceptar'
    });
  }

  showWarning(message: string): void {
    Swal.fire({
      icon: 'warning',
      title: 'Advertencia',
      text: message,
      confirmButtonText: 'Aceptar'
    });
  }

  handleError(title: string, error: any): void {
    let errorMessage = 'Ha ocurrido un error inesperado';

    if (error.error) {
      if (typeof error.error === 'string') {
        errorMessage = error.error;
      } else if (error.error.message) {
        errorMessage = error.error.message;
      } else if (error.error.errors) {
        errorMessage = Object.values(error.error.errors).join(', ');
      }
    }

    Swal.fire({
      icon: 'error',
      title: title,
      text: errorMessage,
      confirmButtonText: 'Aceptar'
    });
  }
}
