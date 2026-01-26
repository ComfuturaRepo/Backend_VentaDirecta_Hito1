import { DropdownItem, DropdownService } from './../../service/dropdown.service';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { TrabajadorService, Trabajador, PageResponse, Area, Cargo, Empresa } from '../../service/trabajador.service';

@Component({
  selector: 'app-trabajador-component',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './trabajador-component.html',
  styleUrls: ['./trabajador-component.css']
})
export class TrabajadorComponent implements OnInit {
  // Datos
  trabajadores: Trabajador[] = [];
  pageResponse: PageResponse<Trabajador> = {
    content: [],
    currentPage: 0,
    totalItems: 0,
    totalPages: 0,
    first: true,
    last: true,
    pageSize: 10
  };

  // Dropdowns
areas: DropdownItem[] = [];
cargos: DropdownItem[] = [];
empresas: DropdownItem[] = [];


  // Filtros
  searchTerm: string = '';
  filtroActivo: boolean | null = null;
  filtroAreaId: number | null = null;
  filtroCargoId: number | null = null;
  filtroEmpresaId: number | null = null;

  // Paginación
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalItems: number = 0;
  pageSizes: number[] = [5, 10, 20, 50];

  // Ordenamiento
  sortBy: string = 'idTrabajador';
  sortDirection: string = 'asc';

  // Estados
  isLoading: boolean = false;
  isLoadingDropdowns: boolean = false;
  showFilters: boolean = false;

  // Modal
  showModal: boolean = false;
  modalTitle: string = '';
  modalMode: 'create' | 'edit' = 'create';

  // Formulario
  trabajadorForm: any = {
    idTrabajador: null,
    nombres: '',
    apellidos: '',
    dni: '',
    celular: '',
    correoCorporativo: '',
    areaId: null,
    cargoId: null,
    empresaId: null,
    activo: true
  };

  // Validación
  formErrors: any = {};
  isSubmitting: boolean = false;

  constructor(private trabajadorService: TrabajadorService ,private dropdownService:DropdownService) {}

  ngOnInit(): void {
    this.loadDropdowns();
    this.loadTrabajadores();
  }

loadDropdowns(): void {
  this.isLoadingDropdowns = true;

  this.dropdownService.getAreas().subscribe({
    next: (areas) => {
      this.areas = areas;
    }
  });

  this.dropdownService.getCargos().subscribe({
    next: (cargos) => {
      this.cargos = cargos;
    }
  });

  this.dropdownService.getEmpresas().subscribe({
    next: (empresas) => {
      this.empresas = empresas;
    },
    complete: () => {
      this.isLoadingDropdowns = false;
    }
  });
}


  // Cargar trabajadores
  loadTrabajadores(): void {
    this.isLoading = true;

    const params = {
      page: this.currentPage,
      size: this.pageSize,
      sortBy: this.sortBy,
      direction: this.sortDirection,
      search: this.searchTerm,
      activos: this.filtroActivo !== null ? this.filtroActivo : undefined,
      areaId: this.filtroAreaId || undefined,
      cargoId: this.filtroCargoId || undefined,
      empresaId: this.filtroEmpresaId || undefined
    };

    this.trabajadorService.getTrabajadores(params).subscribe({
      next: (response) => {
        this.pageResponse = response;
        this.trabajadores = response.content;
        this.totalPages = response.totalPages;
        this.totalItems = response.totalItems;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar trabajadores:', error);
        this.isLoading = false;
      }
    });
  }

  // Cambiar página
  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadTrabajadores();
    }
  }

  // Cambiar tamaño de página
  changePageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadTrabajadores();
  }

  // Ordenar por columna
  sort(column: string): void {
    if (this.sortBy === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = column;
      this.sortDirection = 'asc';
    }
    this.loadTrabajadores();
  }

  // Aplicar filtros
  aplicarFiltros(): void {
    this.currentPage = 0;
    this.loadTrabajadores();
  }

  // Limpiar filtros
  limpiarFiltros(): void {
    this.searchTerm = '';
    this.filtroActivo = null;
    this.filtroAreaId = null;
    this.filtroCargoId = null;
    this.filtroEmpresaId = null;
    this.currentPage = 0;
    this.loadTrabajadores();
  }

  // Abrir modal para crear
  openCreateModal(): void {
    this.modalMode = 'create';
    this.modalTitle = 'Nuevo Trabajador';
    this.resetForm();
    this.showModal = true;
  }

  // Abrir modal para editar
  openEditModal(trabajador: Trabajador): void {
    this.modalMode = 'edit';
    this.modalTitle = 'Editar Trabajador';

    this.trabajadorForm = {
      idTrabajador: trabajador.idTrabajador,
      nombres: trabajador.nombres,
      apellidos: trabajador.apellidos,
      dni: trabajador.dni,
      celular: trabajador.celular,
      correoCorporativo: trabajador.correoCorporativo,
      areaId: trabajador.areaId,
      cargoId: trabajador.cargoId,
      empresaId: trabajador.empresaId,
      activo: trabajador.activo
    };

    this.showModal = true;
  }

  // Resetear formulario
  resetForm(): void {
    this.trabajadorForm = {
      idTrabajador: null,
      nombres: '',
      apellidos: '',
      dni: '',
      celular: '',
      correoCorporativo: '',
      areaId: null,
      cargoId: null,
      empresaId: null,
      activo: true
    };
    this.formErrors = {};
  }

  // Validar formulario
  validateForm(): boolean {
    this.formErrors = {};

    if (!this.trabajadorForm.nombres?.trim()) {
      this.formErrors.nombres = 'Los nombres son obligatorios';
    }

    if (!this.trabajadorForm.apellidos?.trim()) {
      this.formErrors.apellidos = 'Los apellidos son obligatorios';
    }

    if (!this.trabajadorForm.dni?.trim()) {
      this.formErrors.dni = 'El DNI es obligatorio';
    } else if (!/^\d{8}$/.test(this.trabajadorForm.dni)) {
      this.formErrors.dni = 'El DNI debe tener 8 dígitos';
    }

    if (!this.trabajadorForm.celular?.trim()) {
      this.formErrors.celular = 'El celular es obligatorio';
    } else if (!/^\d{9}$/.test(this.trabajadorForm.celular)) {
      this.formErrors.celular = 'El celular debe tener 9 dígitos';
    }

    if (!this.trabajadorForm.correoCorporativo?.trim()) {
      this.formErrors.correoCorporativo = 'El correo es obligatorio';
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.trabajadorForm.correoCorporativo)) {
      this.formErrors.correoCorporativo = 'Correo electrónico inválido';
    }

    if (!this.trabajadorForm.areaId) {
      this.formErrors.areaId = 'El área es obligatoria';
    }

    if (!this.trabajadorForm.cargoId) {
      this.formErrors.cargoId = 'El cargo es obligatorio';
    }

    return Object.keys(this.formErrors).length === 0;
  }

  // Guardar trabajador (crear/editar)
  saveTrabajador(): void {
    if (!this.validateForm()) {
      return;
    }

    this.isSubmitting = true;

    if (this.modalMode === 'create') {
      const trabajadorRequest = {
        nombres: this.trabajadorForm.nombres.trim(),
        apellidos: this.trabajadorForm.apellidos.trim(),
        dni: this.trabajadorForm.dni.trim(),
        celular: this.trabajadorForm.celular.trim(),
        correoCorporativo: this.trabajadorForm.correoCorporativo.trim(),
        areaId: this.trabajadorForm.areaId,
        cargoId: this.trabajadorForm.cargoId,
        empresaId: this.trabajadorForm.empresaId || undefined,
        activo: this.trabajadorForm.activo
      };

      this.trabajadorService.createTrabajador(trabajadorRequest).subscribe({
        next: (response) => {
          this.trabajadorService.showSuccess('Trabajador creado exitosamente');
          this.closeModal();
          this.loadTrabajadores();
        },
        error: (error) => {
          console.error('Error al crear trabajador:', error);
          this.isSubmitting = false;
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });

    } else {
      const trabajadorUpdate = {
        nombres: this.trabajadorForm.nombres.trim(),
        apellidos: this.trabajadorForm.apellidos.trim(),
        dni: this.trabajadorForm.dni.trim(),
        celular: this.trabajadorForm.celular.trim(),
        correoCorporativo: this.trabajadorForm.correoCorporativo.trim(),
        areaId: this.trabajadorForm.areaId,
        cargoId: this.trabajadorForm.cargoId,
        empresaId: this.trabajadorForm.empresaId || undefined
      };

      this.trabajadorService.updateTrabajador(this.trabajadorForm.idTrabajador, trabajadorUpdate).subscribe({
        next: (response) => {
          this.trabajadorService.showSuccess('Trabajador actualizado exitosamente');
          this.closeModal();
          this.loadTrabajadores();
        },
        error: (error) => {
          console.error('Error al actualizar trabajador:', error);
          this.isSubmitting = false;
        },
        complete: () => {
          this.isSubmitting = false;
        }
      });
    }
  }

  // Cambiar estado activo/inactivo
  toggleActivo(trabajador: Trabajador): void {
    const action = trabajador.activo ? 'desactivar' : 'activar';

    this.trabajadorService.showConfirm(`¿Estás seguro de ${action} este trabajador?`)
      .then((result) => {
        if (result.isConfirmed) {
          this.trabajadorService.toggleActivo(trabajador.idTrabajador).subscribe({
            next: (response) => {
              const message = trabajador.activo ? 'Trabajador desactivado' : 'Trabajador activado';
              this.trabajadorService.showSuccess(message);
              this.loadTrabajadores();
            },
            error: (error) => {
              console.error('Error al cambiar estado:', error);
            }
          });
        }
      });
  }

  // Buscar por DNI
  buscarPorDni(): void {
    if (!this.searchTerm.trim()) return;

    const dni = this.searchTerm.trim();
    if (!/^\d{8}$/.test(dni)) {
      Swal.fire('Error', 'Ingrese un DNI válido de 8 dígitos', 'warning');
      return;
    }

    this.trabajadorService.searchByDni(dni).subscribe({
      next: (response) => {
        this.trabajadores = [response];
        this.totalItems = 1;
        this.totalPages = 1;
        this.currentPage = 0;
      },
      error: (error) => {
        this.trabajadores = [];
        this.totalItems = 0;
        this.totalPages = 0;
      }
    });
  }

  // Ver detalles del trabajador
  verDetalles(trabajador: Trabajador): void {
    Swal.fire({
      title: 'Detalles del Trabajador',
      html: `
        <div class="text-start">
          <p><strong>ID:</strong> ${trabajador.idTrabajador}</p>
          <p><strong>Nombre:</strong> ${trabajador.nombres} ${trabajador.apellidos}</p>
          <p><strong>DNI:</strong> ${trabajador.dni || 'No especificado'}</p>
          <p><strong>Celular:</strong> ${trabajador.celular || 'No especificado'}</p>
          <p><strong>Correo:</strong> ${trabajador.correoCorporativo || 'No especificado'}</p>
          <p><strong>Área:</strong> ${trabajador.areaNombre || 'No especificado'}</p>
          <p><strong>Cargo:</strong> ${trabajador.cargoNombre || 'No especificado'}</p>
          <p><strong>Empresa:</strong> ${trabajador.empresaNombre || 'No asignada'}</p>
          <p><strong>Estado:</strong> <span class="badge ${trabajador.activo ? 'bg-success' : 'bg-danger'}">${trabajador.activo ? 'Activo' : 'Inactivo'}</span></p>
          <p><strong>Fecha Creación:</strong> ${this.formatFecha(trabajador.fechaCreacion)}</p>
        </div>
      `,
      icon: 'info',
      confirmButtonText: 'Cerrar',
      width: '600px'
    });
  }

  // Formatear fecha
  formatFecha(fecha: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // Cerrar modal
  closeModal(): void {
    this.showModal = false;
    this.resetForm();
    this.isSubmitting = false;
  }

  // Obtener clase de estado
  getEstadoClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  // Obtener texto de estado
  getEstadoText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  // Obtener nombre del área
  getAreaNombre(trabajador: Trabajador): string {
    return trabajador.areaNombre || 'N/A';
  }

  // Obtener nombre del cargo
  getCargoNombre(trabajador: Trabajador): string {
    return trabajador.cargoNombre || 'N/A';
  }

  // Obtener nombre de la empresa
  getEmpresaNombre(trabajador: Trabajador): string {
    return trabajador.empresaNombre || 'N/A';
  }

  // Obtener iniciales para avatar
  getIniciales(nombres: string, apellidos: string): string {
    const primeraLetraNombre = nombres?.charAt(0) || '';
    const primeraLetraApellido = apellidos?.charAt(0) || '';
    return (primeraLetraNombre + primeraLetraApellido).toUpperCase();
  }
}
