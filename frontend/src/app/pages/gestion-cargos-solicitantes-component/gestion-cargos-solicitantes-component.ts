// src/app/pages/gestion-cargos-solicitantes/gestion-cargos-solicitantes-component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription, debounceTime, distinctUntilChanged, Subject } from 'rxjs';
import Swal from 'sweetalert2';

import { CargoSolicitante } from '../../model/cargo-solicitante.interface';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { CargoSolicitanteService } from '../../service/cargo-solicitante.service';
import { PageResponse } from '../../service/usuario.service';


type ModoVista = 'ANALISTA' | 'JEFATURA';

@Component({
  selector: 'app-gestion-cargos-solicitantes',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './gestion-cargos-solicitantes-component.html',
  styleUrls: ['./gestion-cargos-solicitantes-component.css']
})
export class GestionCargosSolicitantesComponent implements OnInit, OnDestroy {
  modo: ModoVista = 'ANALISTA';

  // Datos principales
  cargos: CargoSolicitante[] = [];
  paginationConfig = {
    showInfo: true,
    showSizeSelector: true,
    showNavigation: true,
    showJumpToPage: true,
    showPageNumbers: true,
    pageSizes: [5, 10, 25, 50],
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
  showForm = false;
  isEditMode = false;
  currentCargo: CargoSolicitante = {
    descripcion: '',
    activo: true,
    tipo: 'ANALISTA'
  };
  formSubmitted = false;

  // Loading y mensajes
  isLoading = false;
  isTableLoading = false;
  errorMessage: string | null = null;

  // Paginación
  currentPage = 0;
  pageSize = 10;
  totalElements = 0;
  totalPages = 0;

  // Estadísticas
  estadisticas = {
    totalAnalistas: 0,
    totalJefaturas: 0,
    analistasActivos: 0,
    jefaturasActivas: 0,
    total: 0,
    totalActivos: 0
  };

  constructor(private cargoService: CargoSolicitanteService) {}

  ngOnInit(): void {
    this.setupSearchDebounce();
    this.loadCargos();
    this.loadEstadisticas();
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

  // ========== MÉTODOS DE CARGA DE DATOS ==========

  loadCargos(page: number = this.currentPage): void {
    this.isTableLoading = true;
    this.errorMessage = null;

    this.cargoService.listarPaginated(
      this.modo,
      page,
      this.pageSize,
      'descripcion',
      'asc',
      this.filterActivos
    ).subscribe({
      next: (response: PageResponse<CargoSolicitante>) => {
        this.cargos = response.content;
        this.currentPage = response.currentPage;
        this.totalElements = response.totalItems;
        this.totalPages = response.totalPages;
        this.pageSize = response.pageSize;
        this.isTableLoading = false;
      },
      error: (err) => {
        this.handleError(err, 'Error al cargar los cargos');
      }
    });
  }

  loadEstadisticas(): void {
    this.cargoService.obtenerEstadisticas().subscribe({
      next: (estadisticas) => {
        this.estadisticas = estadisticas;
      },
      error: (err) => {
        console.error('Error cargando estadísticas:', err);
      }
    });
  }

  // ========== BÚSQUEDA ==========

  performSearch(): void {
    if (!this.searchTerm.trim()) {
      this.loadCargos(0);
      return;
    }

    this.isTableLoading = true;
    this.cargoService.buscar(
      this.modo,
      this.searchTerm.trim(),
      this.currentPage,
      this.pageSize
    ).subscribe({
      next: (response: PageResponse<CargoSolicitante>) => {
        this.cargos = response.content;
        this.currentPage = response.currentPage;
        this.totalElements = response.totalItems;
        this.totalPages = response.totalPages;
        this.pageSize = response.pageSize;
        this.isTableLoading = false;
      },
      error: (err) => {
        this.handleError(err, 'Error en la búsqueda');
      }
    });
  }

  clearSearch(): void {
    this.searchTerm = '';
    this.currentPage = 0;
    this.loadCargos(0);
  }

  // ========== CAMBIO DE MODO ==========

  cambiarModo(nuevoModo: ModoVista): void {
    if (this.modo === nuevoModo) return;

    this.modo = nuevoModo;
    this.currentPage = 0;
    this.searchTerm = '';
    this.filterActivos = undefined;
    this.loadCargos(0);
    this.cancelForm();

    // Scroll suave al inicio
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  changeFilterActivos(filter?: boolean): void {
    this.filterActivos = filter;
    this.currentPage = 0;
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadCargos(0);
    }
  }

  // ========== FORMULARIO ==========

  openCreateForm(): void {
    this.isEditMode = false;
    this.formSubmitted = false;
    this.currentCargo = {
      descripcion: '',
      activo: true,
      tipo: this.modo
    };
    this.showForm = true;
    this.errorMessage = null;
  }

  openEditModal(cargo: CargoSolicitante): void {
    this.isEditMode = true;
    this.formSubmitted = false;
    this.currentCargo = { ...cargo };
    this.showForm = true;
    this.errorMessage = null;
  }

  saveCargo(): void {
    this.formSubmitted = true;

    // Validaciones
    if (!this.currentCargo.descripcion?.trim()) {
      this.errorMessage = 'La descripción es obligatoria';
      return;
    }

    if (this.currentCargo.descripcion.length > 150) {
      this.errorMessage = 'La descripción no puede exceder los 150 caracteres';
      return;
    }

    this.isLoading = true;
    this.errorMessage = null;

    this.cargoService.guardar(this.currentCargo).subscribe({
      next: (response) => {
        this.handleSaveSuccess(response);
      },
      error: (err) => {
        this.handleSaveError(err);
      }
    });
  }

  cancelForm(): void {
    this.showForm = false;
    this.formSubmitted = false;
    this.errorMessage = null;
  }

  // ========== TOGGLE ACTIVO/INACTIVO ==========

  toggleActivo(cargo: CargoSolicitante): void {
    if (!cargo.id) return;

    const accion = cargo.activo ? 'desactivar' : 'activar';
    const titulo = cargo.activo ? 'Desactivar Cargo' : 'Activar Cargo';
    const texto = cargo.activo
      ? '¿Está seguro de que desea desactivar este cargo? No estará disponible para nuevas asignaciones.'
      : '¿Está seguro de que desea activar este cargo? Estará disponible para nuevas asignaciones.';

    Swal.fire({
      title: titulo,
      text: texto,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: cargo.activo ? '#ef4444' : '#10b981',
      cancelButtonColor: '#6b7280',
      confirmButtonText: cargo.activo ? 'Sí, desactivar' : 'Sí, activar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.isLoading = true;

        this.cargoService.toggle(cargo.id!, cargo.tipo).subscribe({
          next: () => {
            this.handleToggleSuccess(cargo, accion);
          },
          error: (err) => {
            this.handleToggleError(err, accion);
          }
        });
      }
    });
  }

  // ========== PAGINACIÓN ==========

  onPageChange(page: number): void {
    this.currentPage = page;
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadCargos(page);
    }
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadCargos(0);
    }
  }

  onRefresh(): void {
    if (this.searchTerm.trim()) {
      this.performSearch();
    } else {
      this.loadCargos(this.currentPage);
    }
  }

  // ========== MANEJO DE ÉXITOS ==========

  private handleSaveSuccess(savedCargo: CargoSolicitante): void {
    this.showForm = false;

    Swal.fire({
      icon: 'success',
      title: '¡Guardado exitoso!',
      text: this.isEditMode
        ? 'El cargo ha sido actualizado correctamente.'
        : 'El nuevo cargo ha sido creado correctamente.',
      timer: 2000,
      showConfirmButton: false,
      timerProgressBar: true
    });

    // Recargar datos
    this.loadCargos(this.currentPage);
    this.loadEstadisticas();
    this.isLoading = false;
  }

  private handleToggleSuccess(cargo: CargoSolicitante, accion: string): void {
    // Actualizar estado local
    const index = this.cargos.findIndex(c => c.id === cargo.id);
    if (index !== -1) {
      this.cargos[index].activo = !this.cargos[index].activo;
    }

    Swal.fire({
      icon: 'success',
      title: '¡Estado actualizado!',
      text: `El cargo ha sido ${accion}do correctamente.`,
      timer: 1500,
      showConfirmButton: false
    });

    this.loadEstadisticas();
    this.isLoading = false;
  }

  // ========== MANEJO DE ERRORES ==========

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

  private handleSaveError(err: any): void {
    const errorMessage = err?.error?.message || 'Error al guardar el cargo.';
    this.errorMessage = errorMessage;
    console.error('Error guardando cargo:', err);
    this.isLoading = false;

    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: errorMessage,
      confirmButtonColor: '#ef4444'
    });
  }

  private handleToggleError(err: any, accion: string): void {
    const errorMessage = `No se pudo ${accion} el cargo`;
    this.errorMessage = errorMessage;
    console.error(`Error ${accion}do cargo:`, err);
    this.isLoading = false;

    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: err?.error?.message || errorMessage,
      confirmButtonColor: '#ef4444'
    });
  }

  // ========== MÉTODOS AUXILIARES PARA LA VISTA ==========

  getContador(tipo: ModoVista): number {
    return tipo === 'ANALISTA' ? this.estadisticas.totalAnalistas : this.estadisticas.totalJefaturas;
  }

  getCargoIcon(cargo: CargoSolicitante): string {
    return cargo.tipo === 'ANALISTA' ? 'bi-person-badge' : 'bi-person-vcard';
  }

  getCargoIconClass(cargo: CargoSolicitante): string {
    return cargo.tipo === 'ANALISTA' ? 'analista' : 'jefatura';
  }

  getTipoBadgeClass(cargo: CargoSolicitante): string {
    return cargo.tipo === 'ANALISTA' ? 'analista' : 'jefatura';
  }

  getFilterButtonClass(filterValue?: boolean): string {
    if (filterValue === undefined) {
      // Botón "Todos"
      return this.filterActivos === undefined ? 'btn-primary' : 'btn-outline-primary';
    } else {
      // Botón "Activos" o "Inactivos"
      return this.filterActivos === filterValue
        ? (filterValue ? 'btn-success' : 'btn-secondary')
        : (filterValue ? 'btn-outline-success' : 'btn-outline-secondary');
    }
  }

  getFilterButtonText(filterValue?: boolean): string {
    if (filterValue === undefined) return 'Todos';
    return filterValue ? 'Activos' : 'Inactivos';
  }

  getTitulo(): string {
    return this.modo === 'ANALISTA'
      ? 'Gestión de Analistas Cliente Solicitante'
      : 'Gestión de Jefaturas Cliente Solicitante';
  }

  getCurrentRange(): string {
    const start = this.currentPage * this.pageSize + 1;
    const end = Math.min((this.currentPage + 1) * this.pageSize, this.totalElements);
    return `${start}-${end}`;
  }

  getActiveCount(): number {
    return this.cargos.filter(c => c.activo === true).length;
  }

  getInactiveCount(): number {
    return this.cargos.filter(c => c.activo === false).length;
  }

  // Para compatibilidad con el template existente
  openEditForm(cargo: CargoSolicitante): void {
    this.openEditModal(cargo);
  }

  // Alias para el template
  onSearchChange(): void {
    this.onSearchInput();
  }
}
