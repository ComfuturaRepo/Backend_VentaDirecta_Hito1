import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import Swal from 'sweetalert2';

// Interfaces
import { PermisoResponseDTO, PermisoTablaDTO } from '../../model/permiso.interface';
import { DropdownItem } from '../../service/dropdown.service';
import { PermisoService } from '../../service/permiso.service';
import { DropdownService } from '../../service/dropdown.service';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { PageResponseDTO } from '../../service/cliente.service';
import { PermisoFormComponent } from './permiso-form.component/permiso-form.component';

// Componente de formulario

@Component({
  selector: 'app-gestion-permisos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    PaginationComponent,
    PermisoFormComponent
  ],
  templateUrl: './gestion-permisos.component.ts.html',
  styleUrl: './gestion-permisos.component.ts.css',
})
export class GestionPermisosComponent implements OnInit, OnDestroy {
  // Referencia al componente de paginación
  @ViewChild(PaginationComponent) paginationComponent!: PaginationComponent;

  // Listas de datos
  permisos: PermisoResponseDTO[] = [];
  permisosFiltrados: PermisoResponseDTO[] = [];
  niveles: DropdownItem[] = [];
  areas: DropdownItem[] = [];
  cargos: DropdownItem[] = [];
  trabajadores: DropdownItem[] = [];

  // Filtros
  filtroCodigo: string = '';
  filtroNombre: string = '';
  filtroNivel: number = 0;
  filtroArea: number = 0;
  filtroEstado: string = 'all';

  // Paginación
  currentPage: number = 0;
  totalItems: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;

  // Estados del modal
  mostrarModal: boolean = false;
  permisoSeleccionado: PermisoResponseDTO | null = null;
  modoEdicion: boolean = false;

  // Estados de carga
  cargandoPermisos: boolean = false;

  // Subscripciones
  private subscriptions: Subscription = new Subscription();

  constructor(
    private permisoService: PermisoService,
    private dropdownService: DropdownService
  ) {}

  ngOnInit(): void {
    this.cargarDatos();
    this.cargarDropdowns();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  // ========== CARGA DE DATOS ==========

 // En el método cargarDatos(), cambia esta parte:
cargarDatos(): void {
  this.cargandoPermisos = true;

  this.subscriptions.add(
    this.permisoService.listarPermisosPaginados(
      this.currentPage,
      this.pageSize,
      'codigo',
      'asc'
    ).subscribe({
      next: (response: PageResponseDTO<PermisoTablaDTO>) => {
        this.permisos = response.content.map((tablaDTO: PermisoTablaDTO) => {
          return {
            idPermiso: tablaDTO.idPermiso,
            codigo: tablaDTO.codigo,
            nombre: tablaDTO.nombre,
            descripcion: tablaDTO.descripcion,
            activo: tablaDTO.activo,
            niveles: tablaDTO.niveles.map(n => ({
              idNivel: n.idNivel,
              codigo: n.codigo,
              nombre: n.nombre,
              descripcion: ''
            })),
            areas: tablaDTO.areas.map(a => ({
              idArea: a.idArea,
              nombre: a.nombre,
              activo: a.activo
            })),
            cargos: tablaDTO.cargos.map(c => ({
              idCargo: c.idCargo,
              nombre: c.nombre,
              idNivel: 0,
              activo: c.activo,
              nombreNivel: ''
            })),
            trabajadores: tablaDTO.trabajadores ? tablaDTO.trabajadores.map(t => ({
              idTrabajador: t.idTrabajador,
              nombres: t.nombres,
              apellidos: t.apellidos,
              dni: t.dni,
              activo: t.activo
            })) : []
          };
        });

        // CAMBIA ESTO:
        // this.permisosFiltrados = [...this.permisos];
        this.permisosFiltrados = this.permisos; // Sin spread operator

        this.totalItems = response.totalItems;
        this.totalPages = response.totalPages;
        this.currentPage = response.currentPage;
        this.cargandoPermisos = false;
      },
      error: (error) => {
        console.error('Error cargando permisos:', error);
        this.cargandoPermisos = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Error al cargar los permisos. Por favor, intente nuevamente.',
          confirmButtonColor: '#0d6efd'
        });
      }
    })
  );
}

// ========== FILTROS Y BÚSQUEDA ==========

aplicarFiltros(): void {
  this.currentPage = 0;

  if (this.filtroCodigo || this.filtroNombre || this.filtroNivel || this.filtroArea || this.filtroEstado !== 'all') {
    this.filtrarLocalmente();
  } else {
    this.cargarDatos();
  }
}

private filtrarLocalmente(): void {
  this.cargandoPermisos = true;

  this.subscriptions.add(
    this.permisoService.listarTodosPermisos().subscribe({
      next: (todosPermisos: PermisoResponseDTO[]) => {
        // Primero aplicar los filtros
        let permisosFiltrados = todosPermisos.filter(permiso => {
          // Filtro por código
          if (this.filtroCodigo && !permiso.codigo.toLowerCase().includes(this.filtroCodigo.toLowerCase())) {
            return false;
          }

          // Filtro por nombre
          if (this.filtroNombre && !permiso.nombre.toLowerCase().includes(this.filtroNombre.toLowerCase())) {
            return false;
          }

          // Filtro por nivel
          if (this.filtroNivel > 0) {
            const tieneNivel = permiso.niveles.some(n => n.idNivel === this.filtroNivel);
            if (!tieneNivel) return false;
          }

          // Filtro por área
          if (this.filtroArea > 0) {
            const tieneArea = permiso.areas.some(a => a.idArea === this.filtroArea);
            if (!tieneArea) return false;
          }

          // Filtro por estado
          if (this.filtroEstado !== 'all') {
            const activo = this.filtroEstado === 'true';
            if (permiso.activo !== activo) return false;
          }

          return true;
        });

        // Actualizar totales
        this.totalItems = permisosFiltrados.length;
        this.totalPages = Math.ceil(this.totalItems / this.pageSize);

        // Validar que la página actual sea válida
        if (this.currentPage >= this.totalPages && this.totalPages > 0) {
          this.currentPage = this.totalPages - 1;
        } else if (this.totalPages === 0) {
          this.currentPage = 0;
        }

        // Aplicar paginación manualmente
        const startIndex = this.currentPage * this.pageSize;
        const endIndex = startIndex + this.pageSize;
        this.permisosFiltrados = permisosFiltrados.slice(startIndex, endIndex);

        this.cargandoPermisos = false;
      },
      error: (error) => {
        console.error('Error filtrando permisos:', error);
        this.cargandoPermisos = false;
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Error al aplicar filtros. Por favor, intente nuevamente.',
          confirmButtonColor: '#0d6efd'
        });
      }
    })
  );
}

limpiarFiltros(): void {
  this.filtroCodigo = '';
  this.filtroNombre = '';
  this.filtroNivel = 0;
  this.filtroArea = 0;
  this.filtroEstado = 'all';
  this.currentPage = 0;
  this.cargarDatos(); // Esto cargará desde el backend con paginación
}

  private cargarDropdowns(): void {
    this.subscriptions.add(
      this.dropdownService.getNivel().subscribe({
        next: (niveles) => this.niveles = niveles,
        error: (error) => console.error('Error cargando niveles:', error)
      })
    );

    this.subscriptions.add(
      this.dropdownService.getTrabajadores().subscribe({
        next: (trabajadores) => this.trabajadores = trabajadores,
        error: (error) => console.error('Error cargando trabajadores:', error)
      })
    );

    this.subscriptions.add(
      this.dropdownService.getAreas().subscribe({
        next: (areas) => this.areas = areas,
        error: (error) => console.error('Error cargando áreas:', error)
      })
    );

    this.subscriptions.add(
      this.dropdownService.getCargos().subscribe({
        next: (cargos) => this.cargos = cargos,
        error: (error) => console.error('Error cargando cargos:', error)
      })
    );
  }

  // ========== MANEJO DEL FORMULARIO/MODAL ==========

  mostrarFormularioNuevo(): void {
    this.modoEdicion = false;
    this.permisoSeleccionado = null;
    this.mostrarModal = true;
  }

  editarPermiso(permiso: PermisoResponseDTO): void {
    this.modoEdicion = true;
    this.permisoSeleccionado = permiso;
    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.permisoSeleccionado = null;
    this.modoEdicion = false;
  }

  onPermisoGuardado(): void {
    this.cerrarModal();
    this.cargarDatos();
  }

  // ========== EVENTOS DE PAGINACIÓN ==========

  onPageChange(page: number): void {
    this.currentPage = page;
    this.cargarDatos();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.cargarDatos();
  }

  onRefresh(): void {
    this.cargarDatos();
  }





  // ========== ELIMINACIÓN ==========

  eliminarPermiso(id: number): void {
    Swal.fire({
      title: '¿Está seguro?',
      text: 'Esta acción eliminará el permiso permanentemente y no se podrá deshacer.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#dc3545',
      cancelButtonColor: '#6c757d',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar',
      reverseButtons: true
    }).then((result) => {
      if (result.isConfirmed) {
        this.subscriptions.add(
          this.permisoService.eliminarPermiso(id).subscribe({
            next: () => {
              this.cargarDatos();
              Swal.fire({
                icon: 'success',
                title: '¡Eliminado!',
                text: 'El permiso ha sido eliminado correctamente.',
                confirmButtonColor: '#0d6efd',
                timer: 2000,
                showConfirmButton: false
              });
            },
            error: (error) => {
              console.error('Error eliminando permiso:', error);
              Swal.fire({
                icon: 'error',
                title: 'Error',
                text: `Error al eliminar el permiso: ${error.error?.message || error.message || 'Error desconocido'}`,
                confirmButtonColor: '#0d6efd'
              });
            }
          })
        );
      }
    });
  }

  // ========== UTILIDADES PARA TEMPLATE ==========
// Métodos para contar permisos activos/inactivos
getPermisosActivos(): number {
  return this.permisosFiltrados.filter(p => p.activo).length;
}

getPermisosInactivos(): number {
  return this.permisosFiltrados.filter(p => !p.activo).length;
}
  getNombreTrabajador(trabajador: any): string {
    if (!trabajador) return '';

    const nombre = trabajador.nombres ? trabajador.nombres.split(' ')[0] : '';
    const apellido = trabajador.apellidos ? trabajador.apellidos.split(' ')[0] : '';

    return `${nombre} ${apellido}`.trim();
  }

  get mostrarResultados(): boolean {
    return this.totalItems > 0 && !this.cargandoPermisos;
  }

  get mostrarTablaVacia(): boolean {
    return !this.cargandoPermisos && this.totalItems === 0;
  }
}
