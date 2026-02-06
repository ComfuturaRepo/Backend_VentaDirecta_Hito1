import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription, debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import Swal from 'sweetalert2';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { PageResponse } from '../../model/page.interface';
import { Site, SiteRequest, SiteDescripcionRequest, SiteFilter } from '../../model/site.interface';
import { SiteService } from '../../service/site.service';
import { DropdownItem, DropdownService } from '../../service/dropdown.service';
import { NgselectDropdownComponent } from '../../component/ngselect-dropdown-component/ngselect-dropdown-component';
import { PermisoDirective } from '../../directive/permiso.directive';

@Component({
  selector: 'app-site',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent,PermisoDirective],
  templateUrl: './site-component.html',
  styleUrls: ['./site-component.css']
})
export class SiteComponent implements OnInit, OnDestroy {
  // Datos principales
  sites: Site[] = [];

  // Configuración de paginación
  paginationConfig = {
    showInfo: true,
    showSizeSelector: true,
    showNavigation: true,
    showJumpToPage: true,
    showPageNumbers: true,
    pageSizes: [10, 25, 50, 100],
    maxPageNumbers: 5,
    align: 'center' as const,
    size: 'md' as const
  };

  // Cambiado a array de objetos
  descripcionesTemporal: { value: string }[] = [{ value: '' }];

  // Filtros avanzados
  filters: SiteFilter = {
    search: '',
    activo: null,
    codigoSitio: '',
    descripcion: '',
    direccion: ''
  };

  // Filtro con debounce para búsqueda general
  searchTerm: string = '';
  private searchSubject = new Subject<string>();
  private searchSubscription?: Subscription;

  // Estado del filtro activo (para compatibilidad)
  filterActivos?: boolean;

  // Formulario modal
  showModal = false;
  isEditMode = false;
  currentSite: Site = { codigoSitio: '', descripciones: [] };
  formSubmitted = false;

  // Dropdown para sitios compuestos
  siteOptions: DropdownItem[] = [];
  selectedSiteId: number | null = null;

  // Loading y mensajes
  isLoading = false;
  isTableLoading = false;
  errorMessage: string = '';

  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Control de filtros avanzados
  showAdvancedFilters = false;
  appliedFiltersCount = 0;

  constructor(
    private siteService: SiteService,
    private dropdownService: DropdownService
  ) { }

  ngOnInit(): void {
    this.setupSearchDebounce();
    this.loadSites();
    this.loadSiteOptions();
  }

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }

  private setupSearchDebounce(): void {
    this.searchSubscription = this.searchSubject
      .pipe(
        debounceTime(300),
        distinctUntilChanged()
      )
      .subscribe((searchTerm) => {
        this.searchTerm = searchTerm;
        this.filters.search = searchTerm;
        this.updateAppliedFiltersCount();
        this.loadSitesWithCurrentFilters();
      });
  }

  loadSitesWithCurrentFilters(page: number = 0): void {
    this.isTableLoading = true;
    this.errorMessage = '';

    // Si no hay filtros activos, cargar todos
    if (!this.hasActiveFilters()) {
      this.loadSites(page);
      return;
    }

    this.siteService.filtrar(
      this.filters,
      page,
      this.pageSize,
      'codigoSitio',
      'asc'
    ).subscribe({
      next: (response: PageResponse<Site>) => {
        this.handleSuccessResponse(response, page);
      },
      error: (err) => {
        this.handleError(err, 'Error al filtrar los sitios');
      }
    });
  }

  // Método para verificar si hay filtros activos
  hasActiveFilters(): boolean {
    return this.appliedFiltersCount > 0;
  }

  // Método de compatibilidad (mantenido)
  loadSites(page: number = this.currentPage): void {
    this.isTableLoading = true;
    this.errorMessage = '';

    this.siteService.listar(
      page,
      this.pageSize,
      'codigoSitio',
      'asc',
      this.filterActivos
    ).subscribe({
      next: (response: PageResponse<Site>) => {
        this.handleSuccessResponse(response, page);
      },
      error: (err) => {
        this.handleError(err, 'Error al cargar los sitios');
      }
    });
  }

  // Cargar opciones para dropdown de sitios compuestos
  loadSiteOptions(): void {
    this.dropdownService.getSiteCompuesto().subscribe({
      next: (options) => {
        this.siteOptions = options;
      },
      error: (err) => {
        console.error('Error cargando opciones de sitios:', err);
      }
    });
  }

  // Métodos para filtros
  onSearchInput(): void {
    this.searchSubject.next(this.searchTerm);
  }

  applyFilters(): void {
    this.currentPage = 0; // Siempre ir a la primera página
    this.loadSitesWithCurrentFilters();
  }

  clearFilters(): void {
    this.filters = {
      search: '',
      activo: null,
      codigoSitio: '',
      descripcion: '',
      direccion: ''
    };
    this.searchTerm = '';
    this.filterActivos = undefined;
    this.appliedFiltersCount = 0;
    this.currentPage = 0;
    this.loadSites();
  }

  updateAppliedFiltersCount(): void {
    let count = 0;

    // Solo contar filtros que tengan valor (no vacío o null)
    if (this.filters.search && this.filters.search.trim() !== '') count++;
    if (this.filters.activo !== null && this.filters.activo !== undefined) count++;
    if (this.filters.codigoSitio && this.filters.codigoSitio.trim() !== '') count++;
    if (this.filters.descripcion && this.filters.descripcion.trim() !== '') count++;
    if (this.filters.direccion && this.filters.direccion.trim() !== '') count++;

    this.appliedFiltersCount = count;
  }

  toggleAdvancedFilters(): void {
    this.showAdvancedFilters = !this.showAdvancedFilters;
  }

  // Método de compatibilidad para filtro de activos
  changeFilterActivos(filter?: boolean): void {
    this.filterActivos = filter;
    this.filters.activo = filter;
    this.updateAppliedFiltersCount();
    this.currentPage = 0;
    this.loadSitesWithCurrentFilters();
  }

  // Métodos auxiliares para la vista
  getFilterButtonClass(filterValue?: boolean): string {
    if (filterValue === undefined) {
      return this.filters.activo === undefined ? 'btn-primary' : 'btn-outline-primary';
    } else {
      return this.filters.activo === filterValue
        ? (filterValue ? 'btn-success' : 'btn-secondary')
        : (filterValue ? 'btn-outline-success' : 'btn-outline-secondary');
    }
  }

  getFilterButtonText(filterValue?: boolean): string {
    if (filterValue === undefined) return 'Todos';
    return filterValue ? 'Activos' : 'Inactivos';
  }

  // Modal methods
  openCreateModal(): void {
    this.isEditMode = false;
    this.formSubmitted = false;
    this.currentSite = {
      codigoSitio: '',
      descripciones: []
    };
    this.descripcionesTemporal = [{ value: '' }];
    this.selectedSiteId = null;
    this.showModal = true;
    this.errorMessage = '';
  }

  openEditModal(site: Site): void {
    this.isEditMode = true;
    this.formSubmitted = false;

    this.currentSite = {
      idSite: site.idSite,
      codigoSitio: site.codigoSitio || '',
      activo: site.activo ?? true,
      descripciones: [...site.descripciones]
    };

    // Convertir descripciones a array temporal para edición (usando objetos)
    this.descripcionesTemporal = site.descripciones.map(d => ({ value: d.descripcion }));
    if (this.descripcionesTemporal.length === 0) {
      this.descripcionesTemporal = [{ value: '' }];
    }

    this.showModal = true;
    this.errorMessage = '';
  }

  openDuplicateModal(): void {
    if (this.selectedSiteId) {
      this.siteService.obtenerPorId(this.selectedSiteId).subscribe({
        next: (site) => {
          this.isEditMode = false;
          this.formSubmitted = false;
          this.currentSite = {
            codigoSitio: `${site.codigoSitio}_COPIA`,
            activo: site.activo ?? true,
            descripciones: [...site.descripciones]
          };
          // Convertir a array de objetos
          this.descripcionesTemporal = site.descripciones.map(d => ({ value: d.descripcion }));
          this.showModal = true;
          this.selectedSiteId = null;
        },
        error: (err) => {
          this.handleError(err, 'Error al cargar sitio para duplicar');
        }
      });
    }
  }

  // Métodos para manejar descripciones
  hasValidDescriptions(): boolean {
    return this.descripcionesTemporal &&
           this.descripcionesTemporal.length > 0 &&
           this.descripcionesTemporal.some(d => d.value && d.value.trim() !== '');
  }

  showDescriptionsError(): boolean {
    return this.formSubmitted &&
           this.descripcionesTemporal &&
           this.descripcionesTemporal.every(d => !d.value || d.value.trim() === '');
  }

  addDescripcion(): void {
    this.descripcionesTemporal.push({ value: '' });
  }

  removeDescripcion(index: number): void {
    this.descripcionesTemporal.splice(index, 1);
    if (this.descripcionesTemporal.length === 0) {
      this.descripcionesTemporal.push({ value: '' });
    }
  }

  trackByIndex(index: number): number {
    return index;
  }

 // Por este:
isFormValid(): boolean {
  if (!this.formSubmitted) {
    return true;
  }

  // Verificar que haya al menos una descripción válida
  const hasValidDescription = this.descripcionesTemporal.some(
    desc => desc.value && desc.value.trim() !== ''
  );

  return hasValidDescription;
}

  saveSite(): void {
    this.formSubmitted = true;

    // Validaciones locales primero
    const codigoSitio = (this.currentSite.codigoSitio ?? '').trim();

    if (!codigoSitio) {
      this.errorMessage = 'El código de sitio es requerido';
      return;
    }

    // Extraer valores de los objetos
    const descripcionesValidas = this.descripcionesTemporal
      .filter(item => item.value.trim() !== '')
      .map(item => item.value.trim());

    if (descripcionesValidas.length === 0) {
      this.errorMessage = 'Debe ingresar al menos una descripción válida';
      return;
    }

    // Solo validar código duplicado si es creación (no edición)
    if (!this.isEditMode) {
      this.verificarYGuardarSite(codigoSitio, descripcionesValidas);
    } else {
      // En edición, guardar directamente (la validación se hace en el backend)
      this.guardarSiteDirectamente(codigoSitio, descripcionesValidas);
    }
  }

  private verificarYGuardarSite(codigoSitio: string, descripcionesValidas: string[]): void {
    this.isLoading = true;
    this.errorMessage = '';

    // Primero verificar si el código ya existe
    this.siteService.verificarCodigo(codigoSitio).subscribe({
      next: (existe: boolean) => {
        if (existe) {
          this.errorMessage = `El código de sitio "${codigoSitio}" ya existe. Use otro código.`;
          this.isLoading = false;
        } else {
          // Código no existe, proceder a guardar
          this.guardarSiteDirectamente(codigoSitio, descripcionesValidas);
        }
      },
      error: (err) => {
        this.errorMessage = 'Error al verificar el código. Intente nuevamente.';
        this.isLoading = false;
      }
    });
  }

  private guardarSiteDirectamente(codigoSitio: string, descripcionesValidas: string[]): void {
    const siteRequest: SiteRequest = {
      codigoSitio: codigoSitio,
      activo: this.currentSite.activo ?? true,
      descripciones: descripcionesValidas.map(descripcion => ({
        descripcion,
        activo: true
      }))
    };

    const id = this.isEditMode ? this.currentSite.idSite : undefined;

    this.siteService.guardar(siteRequest, id).subscribe({
      next: (savedSite) => {
        this.handleSaveSuccess(savedSite);
      },
      error: (err) => {
        this.handleSaveError(err);
      }
    });
  }

  private verificarCodigoEnOtroRegistro(): void {
    this.errorMessage = 'El código de sitio ya existe en otro registro. Use otro código.';
    this.isLoading = false;
  }

  private procederConGuardado(descripcionesValidas: string[]): void {
    if (!this.currentSite.codigoSitio) {
      this.errorMessage = 'El código de sitio es requerido';
      this.isLoading = false;
      return;
    }

    const siteRequest: SiteRequest = {
      codigoSitio: this.currentSite.codigoSitio.trim(),
      activo: this.currentSite.activo ?? true,
      descripciones: descripcionesValidas.map(descripcion => ({
        descripcion,
        activo: true
      }))
    };

    const id = this.isEditMode ? this.currentSite.idSite : undefined;

    this.siteService.guardar(siteRequest, id).subscribe({
      next: (savedSite) => {
        this.handleSaveSuccess(savedSite);
      },
      error: (err) => {
        this.handleSaveError(err);
      }
    });
  }

  closeModal(): void {
    this.showModal = false;
    this.formSubmitted = false;
    this.errorMessage = '';
    this.isLoading = false;
    this.descripcionesTemporal = [{ value: '' }];
    this.selectedSiteId = null;
  }

  toggleActivo(site: Site): void {
    if (!site.idSite) {
      this.showError('El sitio no tiene un ID válido');
      return;
    }

    const accion = site.activo ? 'desactivar' : 'activar';
    const titulo = site.activo ? 'Desactivar Sitio' : 'Activar Sitio';
    const texto = site.activo
      ? '¿Está seguro de que desea desactivar este sitio? No estará disponible para nuevas asignaciones.'
      : '¿Está seguro de que desea activar este sitio? Estará disponible para nuevas asignaciones.';

    Swal.fire({
      title: titulo,
      text: texto,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: site.activo ? '#ef4444' : '#10b981',
      cancelButtonColor: '#6b7280',
      confirmButtonText: site.activo ? 'Sí, desactivar' : 'Sí, activar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;

        this.siteService.toggle(site.idSite!).subscribe({
          next: () => {
            const index = this.sites.findIndex(s => s.idSite === site.idSite);
            if (index !== -1) {
              this.sites[index].activo = !this.sites[index].activo;
            }

            this.showSuccess(`El sitio ha sido ${accion}do correctamente.`);
            this.isLoading = false;
          },
          error: (err) => {
            this.handleError(err, `No se pudo ${accion} el sitio`);
          }
        });
      }
    });
  }

  // Métodos para paginación
  onPageChange(page: number): void {
    this.currentPage = page;
    if (this.hasActiveFilters()) {
      this.loadSitesWithCurrentFilters(page);
    } else {
      this.loadSites(page);
    }
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    if (this.hasActiveFilters()) {
      this.loadSitesWithCurrentFilters(0);
    } else {
      this.loadSites(0);
    }
  }

  onRefresh(): void {
    if (this.hasActiveFilters()) {
      this.loadSitesWithCurrentFilters(this.currentPage);
    } else {
      this.loadSites(this.currentPage);
    }
  }

  // Métodos auxiliares
  private handleSuccessResponse(response: PageResponse<Site>, page: number): void {
    this.sites = response.content;
    this.currentPage = response.currentPage;
    this.totalElements = response.totalItems;
    this.totalPages = response.totalPages;
    this.pageSize = response.pageSize;
    this.isTableLoading = false;
  }

  private handleError(err: any, defaultMessage: string): void {
    const errorMessage = err?.error?.message || err?.message || defaultMessage;
    this.errorMessage = errorMessage;
    console.error('Error:', err);
    this.isTableLoading = false;
    this.isLoading = false;

    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: errorMessage,
      confirmButtonColor: '#ef4444'
    });
  }

  private handleSaveSuccess(savedSite: Site): void {
    this.showModal = false;
    this.descripcionesTemporal = [{ value: '' }];

    Swal.fire({
      icon: 'success',
      title: '¡Guardado exitoso!',
      text: this.isEditMode
        ? 'El sitio ha sido actualizado correctamente.'
        : 'El nuevo sitio ha sido creado correctamente.',
      timer: 2000,
      showConfirmButton: false,
      timerProgressBar: true
    });

    if (this.hasActiveFilters()) {
      this.loadSitesWithCurrentFilters(this.currentPage);
    } else {
      this.loadSites(this.currentPage);
    }

    this.isLoading = false;
  }

  private handleSaveError(err: any): void {
    let errorMessage = 'Error al guardar el sitio.';

    // Extraer mensaje específico si existe
    if (err?.error?.message) {
      errorMessage = err.error.message;
    } else if (err?.message) {
      errorMessage = err.message;
    }

    // Solo mostrar SweetAlert para errores graves, no para validaciones
    if (!errorMessage.includes('código de sitio') && !errorMessage.includes('ya existe')) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
        confirmButtonColor: '#ef4444'
      });
    }

    this.errorMessage = errorMessage;
    this.isLoading = false;
  }

  private showSuccess(message: string): void {
    Swal.fire({
      icon: 'success',
      title: '¡Éxito!',
      text: message,
      timer: 1500,
      showConfirmButton: false
    });
  }

  private showError(message: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: message,
      confirmButtonColor: '#ef4444'
    });
  }

  // Métodos para la vista
  getStatusBadgeClass(activo?: boolean): string {
    return activo ? 'bg-success' : 'bg-secondary';
  }

  getStatusText(activo?: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  getStatusIcon(activo?: boolean): string {
    return activo ? 'bi-check-circle' : 'bi-x-circle';
  }

  getActiveSitesCount(): number {
    return this.sites.filter(s => s.activo === true).length;
  }

  getInactiveSitesCount(): number {
    return this.sites.filter(s => s.activo === false).length;
  }

  getTotalSitesCount(): number {
    return this.sites.length;
  }

  getCurrentRange(): string {
    const start = this.currentPage * this.pageSize + 1;
    const end = Math.min((this.currentPage + 1) * this.pageSize, this.totalElements);
    return `${start}-${end}`;
  }

  // Helper para mostrar descripciones en tarjetas
  getDescripcionesDisplay(descripciones: SiteDescripcionRequest[]): string {
    if (!descripciones || descripciones.length === 0) return 'Sin descripciones';
    return descripciones.map(d => d.descripcion).join(', ');
  }

  // Helper para mostrar primera descripción
  getFirstDescripcion(descripciones: SiteDescripcionRequest[]): string {
    if (!descripciones || descripciones.length === 0) return 'Sin descripción';
    return descripciones[0].descripcion;
  }

  // Métodos específicos para filtros avanzados
  getActiveFilterCount(): string {
    return this.appliedFiltersCount > 0 ? `(${this.appliedFiltersCount})` : '';
  }

  getFilterSummary(): string {
    const parts = [];
    if (this.filters.search) parts.push(`Buscar: "${this.filters.search}"`);
    if (this.filters.activo !== null) parts.push(this.filters.activo ? 'Activos' : 'Inactivos');
    if (this.filters.codigoSitio) parts.push(`Código: "${this.filters.codigoSitio}"`);
    if (this.filters.descripcion) parts.push(`Descripción: "${this.filters.descripcion}"`);
    if (this.filters.direccion) parts.push(`Dirección: "${this.filters.direccion}"`);

    return parts.length > 0 ? parts.join(' • ') : 'Sin filtros aplicados';
  }
}
