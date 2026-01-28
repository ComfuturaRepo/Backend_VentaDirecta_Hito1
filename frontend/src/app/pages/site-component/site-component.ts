import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription, debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import Swal from 'sweetalert2';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { DEFAULT_PAGINATION_CONFIG } from '../../model/page.interface';
import { Site } from '../../model/site.interface';
import { SiteService } from '../../service/site.service';
import { PageResponse } from '../../service/usuario.service';


@Component({
  selector: 'app-site',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './site-component.html',
  styleUrls: ['./site-component.css']
})
export class SiteComponent implements OnInit, OnDestroy {
  // Datos principales
  sites: Site[] = [];

  // Configuración de paginación con valores por defecto EXPLÍCITOS
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

  // Filtro con debounce
  searchTerm: string = '';
  private searchSubject = new Subject<string>();
  private searchSubscription?: Subscription;

  // Estado del filtro activo
  filterActivos?: boolean;

  // Formulario modal
  showModal = false;
  isEditMode = false;
  currentSite: Site = { codigoSitio: '', descripcion: '', activo: true };
  formSubmitted = false;

  // Loading y mensajes
  isLoading = false;
  isTableLoading = false;
  errorMessage: string = '';

  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  constructor(private siteService: SiteService) {
    // Configuración específica para este componente
    this.paginationConfig.pageSizes = [10, 25, 50, 100];
    this.paginationConfig.showInfo = true;
    this.paginationConfig.showSizeSelector = true;
    this.paginationConfig.showNavigation = true;
    this.paginationConfig.showJumpToPage = true;
    this.paginationConfig.showPageNumbers = true;
    this.paginationConfig.align = 'center';
    this.paginationConfig.size = 'md';
  }

  ngOnInit(): void {
    this.setupSearchDebounce();
    this.loadSites();
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
        if (searchTerm.trim() === '') {
          this.clearSearch();
        } else {
          this.performSearch();
        }
      });
  }

  onSearchInput(): void {
    this.searchSubject.next(this.searchTerm);
  }

  // Método principal para cargar sites
  loadSites(page: number = this.currentPage, useFilter: boolean = true): void {
    this.isTableLoading = true;
    this.errorMessage = '';

    this.siteService.listar(
      page,
      this.pageSize,
      'codigoSitio',
      'asc',
      useFilter ? this.filterActivos : undefined // Ahora sí es compatible
    ).subscribe({
      next: (response: PageResponse<Site>) => {
        this.handleSuccessResponse(response, page);
      },
      error: (err) => {
        this.handleError(err, 'Error al cargar los sitios');
      }
    });
  }

  // Realizar búsqueda por texto
  performSearch(): void {
    if (!this.searchTerm.trim()) {
      this.loadSites(0, false);
      return;
    }

    this.isTableLoading = true;
    this.siteService.buscar(this.searchTerm, this.currentPage, this.pageSize)
      .subscribe({
        next: (response: PageResponse<Site>) => {
          this.handleSuccessResponse(response, this.currentPage);
        },
        error: (err) => {
          this.handleError(err, 'Error en la búsqueda');
        }
      });
  }

  // Limpiar búsqueda
  clearSearch(): void {
    this.searchTerm = '';
    this.currentPage = 0;
    this.loadSites(0, true);
  }

  // Cambiar filtro de activos
  changeFilterActivos(filter?: boolean): void { // Cambiado de boolean | null a boolean | undefined
    this.filterActivos = filter;
    this.currentPage = 0;
    this.loadSites(0, true);
  }

  // Métodos auxiliares para la vista (usados en el HTML)
  getFilterButtonClass(filterValue?: boolean, currentFilter?: boolean): string {
    if (filterValue === undefined) {
      // Botón "Todos"
      return currentFilter === undefined ? 'btn-primary' : 'btn-outline-primary';
    } else {
      // Botón "Activos" o "Inactivos"
      return currentFilter === filterValue
        ? (filterValue ? 'btn-success' : 'btn-secondary')
        : (filterValue ? 'btn-outline-success' : 'btn-outline-secondary');
    }
  }

  getFilterButtonText(filterValue?: boolean): string {
    if (filterValue === undefined) return 'Todos';
    return filterValue ? 'Activos' : 'Inactivos';
  }

  // Resto de los métodos permanecen igual...
  openCreateModal(): void {
    this.isEditMode = false;
    this.formSubmitted = false;
    this.currentSite = {
      codigoSitio: '',
      descripcion: '',
      activo: true
    };
    this.showModal = true;
    this.errorMessage = '';
  }

  openEditModal(site: Site): void {
    this.isEditMode = true;
    this.formSubmitted = false;

    this.currentSite = {
      idSite: site.idSite,
      codigoSitio: site.codigoSitio || '',
      descripcion: site.descripcion || '',
      activo: site.activo ?? true
    };

    this.showModal = true;
    this.errorMessage = '';
  }

  saveSite(): void {
    this.formSubmitted = true;

    if (!this.currentSite.descripcion?.trim()) {
      this.errorMessage = 'La descripción es obligatoria';
      return;
    }

    if (this.currentSite.descripcion.length > 255) {
      this.errorMessage = 'La descripción no puede exceder los 255 caracteres';
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    this.siteService.guardar(this.currentSite).subscribe({
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

  deleteSite(site: Site): void {
    if (!site.idSite) {
      this.showError('El sitio no tiene un ID válido');
      return;
    }

    Swal.fire({
      title: '¿Eliminar sitio?',
      text: 'Esta acción no se puede deshacer. El sitio será marcado como inactivo.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#ef4444',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;

        this.siteService.eliminar(site.idSite).subscribe({
          next: () => {
            this.loadSites(this.currentPage);
            this.showSuccess('Sitio eliminado correctamente');
          },
          error: (err) => {
            this.handleError(err, 'Error al eliminar el sitio');
          }
        });
      }
    });
  }

  // Métodos para paginación
  onPageChange(page: number): void {
    this.currentPage = page;
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadSites(page, this.filterActivos !== undefined);
    }
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadSites(0, this.filterActivos !== undefined);
    }
  }

  onRefresh(): void {
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadSites(this.currentPage, this.filterActivos !== undefined);
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

    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: errorMessage,
      confirmButtonColor: '#ef4444'
    });
  }

  private handleSaveSuccess(savedSite: Site): void {
    this.showModal = false;

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

    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadSites(this.currentPage, this.filterActivos !== undefined);
    }

    this.isLoading = false;
  }

  private handleSaveError(err: any): void {
    const errorMessage = err?.error?.message || 'Error al guardar el sitio.';
    this.errorMessage = errorMessage;
    console.error('Error guardando sitio:', err);
    this.isLoading = false;

    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: errorMessage,
      confirmButtonColor: '#ef4444'
    });
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
}
