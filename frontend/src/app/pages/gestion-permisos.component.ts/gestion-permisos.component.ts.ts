import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import Swal from 'sweetalert2';

// Interfaces
import { PermisoResponseDTO, PermisoTablaDTO } from '../../model/permiso.interface';
import { DropdownItem } from '../../service/dropdown.service';
import { PermisoService } from '../../service/permiso.service';
import { DropdownService } from '../../service/dropdown.service';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { PageResponseDTO } from '../../service/cliente.service';

// Componentes

@Component({
  selector: 'app-gestion-permisos',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PaginationComponent
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

  // Filtros
  filtroCodigo: string = '';
  filtroNombre: string = '';
  filtroNivel: number = 0;
  filtroArea: number = 0;
  filtroEstado: string = 'all';

  // Paginación (ahora gestionada por PaginationComponent)
  currentPage: number = 0;
  totalItems: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;

  // Formulario
  permisoForm!: FormGroup;
  permisoSeleccionado: PermisoResponseDTO | null = null;
  mostrarModal: boolean = false;
  guardando: boolean = false;
  cargando: boolean = false;
  cargandoPermisos: boolean = false;

  // Selecciones temporales
  nivelesSeleccionados: number[] = [];
  areasSeleccionadas: number[] = [];
  cargosSeleccionados: number[] = [];

  // Estados
  modoEdicion: boolean = false;

  // Subscripciones
  private subscriptions: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private permisoService: PermisoService,
    private dropdownService: DropdownService
  ) {}

  ngOnInit(): void {
    this.inicializarFormulario();
    this.cargarDatos();
    this.cargarDropdowns();
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  // ========== INICIALIZACIÓN ==========

  private inicializarFormulario(): void {
    this.permisoForm = this.fb.group({
      codigo: ['', [
        Validators.required,
        Validators.pattern(/^[A-Z][A-Z0-9_]*$/),
        Validators.maxLength(50)
      ]],
      nombre: ['', [
        Validators.required,
        Validators.maxLength(100)
      ]],
      descripcion: ['', Validators.maxLength(200)],
      activo: [true]
    });
  }

 // gestion-permisos.component.ts
private cargarDatos(): void {
  this.cargandoPermisos = true;

  this.subscriptions.add(
    this.permisoService.listarPermisosPaginados(
      this.currentPage,
      this.pageSize,
      'codigo',
      'asc'
    ).subscribe({
      next: (response: PageResponseDTO<PermisoResponseDTO>) => {
        this.permisos = response.content;
        this.permisosFiltrados = [...this.permisos];
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

  private cargarDropdowns(): void {
    this.subscriptions.add(
      this.dropdownService.getNivel().subscribe({
        next: (niveles) => this.niveles = niveles,
        error: (error) => console.error('Error cargando niveles:', error)
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

  // ========== EVENTOS DE PAGINACIÓN ==========

  onPageChange(page: number): void {
    this.currentPage = page;
    this.cargarDatos();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0; // Reset a primera página
    this.cargarDatos();
  }

  onRefresh(): void {
    this.cargarDatos();
  }

  // ========== FILTROS Y BÚSQUEDA ==========

  aplicarFiltros(): void {
    this.currentPage = 0; // Reset a primera página

    // Si hay filtros activos, filtrar localmente
    if (this.filtroCodigo || this.filtroNombre || this.filtroNivel || this.filtroArea || this.filtroEstado !== 'all') {
      this.filtrarLocalmente();
    } else {
      // Si no hay filtros, recargar desde el servidor
      this.cargarDatos();
    }
  }

  private filtrarLocalmente(): void {
    this.cargandoPermisos = true;

    this.subscriptions.add(
      this.permisoService.listarTodosPermisos().subscribe({
        next: (todosPermisos: PermisoResponseDTO[]) => {
          this.permisosFiltrados = todosPermisos.filter(permiso => {
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

          this.totalItems = this.permisosFiltrados.length;
          this.totalPages = Math.ceil(this.totalItems / this.pageSize);
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
    this.cargarDatos();
  }

  // ========== FORMULARIO ==========

  mostrarFormulario(): void {
    this.modoEdicion = false;
    this.permisoSeleccionado = null;
    this.nivelesSeleccionados = [];
    this.areasSeleccionadas = [];
    this.cargosSeleccionados = [];

    this.permisoForm.reset({
      codigo: '',
      nombre: '',
      descripcion: '',
      activo: true
    });

    this.mostrarModal = true;
  }

  editarPermiso(permiso: PermisoResponseDTO): void {
    this.modoEdicion = true;
    this.permisoSeleccionado = permiso;

    // Cargar datos del permiso
    this.permisoForm.patchValue({
      codigo: permiso.codigo,
      nombre: permiso.nombre,
      descripcion: permiso.descripcion,
      activo: permiso.activo
    });

    // Cargar selecciones
    this.nivelesSeleccionados = permiso.niveles.map(n => n.idNivel);
    this.areasSeleccionadas = permiso.areas.map(a => a.idArea);
    this.cargosSeleccionados = permiso.cargos.map(c => c.idCargo);

    this.mostrarModal = true;
  }

  cerrarModal(): void {
    this.mostrarModal = false;
    this.permisoSeleccionado = null;
    this.modoEdicion = false;
    this.permisoForm.reset();
  }

  // ========== MANEJO DE CHECKBOXES ==========

  toggleNivel(id: number): void {
    const index = this.nivelesSeleccionados.indexOf(id);
    if (index === -1) {
      this.nivelesSeleccionados.push(id);
    } else {
      this.nivelesSeleccionados.splice(index, 1);
    }
  }

  toggleArea(id: number): void {
    const index = this.areasSeleccionadas.indexOf(id);
    if (index === -1) {
      this.areasSeleccionadas.push(id);
    } else {
      this.areasSeleccionadas.splice(index, 1);
    }
  }

  toggleCargo(id: number): void {
    const index = this.cargosSeleccionados.indexOf(id);
    if (index === -1) {
      this.cargosSeleccionados.push(id);
    } else {
      this.cargosSeleccionados.splice(index, 1);
    }
  }

  // ========== CRUD OPERATIONS ==========

  guardarPermiso(): void {
    if (this.permisoForm.invalid) {
      this.marcarCamposComoSucios();
      Swal.fire({
        icon: 'warning',
        title: 'Formulario incompleto',
        text: 'Por favor complete todos los campos requeridos correctamente.',
        confirmButtonColor: '#0d6efd'
      });
      return;
    }

    this.guardando = true;

    const permisoData = {
      ...this.permisoForm.value,
      nivelesIds: this.nivelesSeleccionados,
      areasIds: this.areasSeleccionadas,
      cargosIds: this.cargosSeleccionados
    };

    const observable = this.modoEdicion && this.permisoSeleccionado
      ? this.permisoService.actualizarPermiso(this.permisoSeleccionado.idPermiso, permisoData)
      : this.permisoService.crearPermiso(permisoData);

    this.subscriptions.add(
      observable.pipe(
        finalize(() => this.guardando = false)
      ).subscribe({
        next: (permisoGuardado) => {
          // Recargar datos después de guardar
          this.cargarDatos();

          this.cerrarModal();

          Swal.fire({
            icon: 'success',
            title: '¡Éxito!',
            text: `Permiso ${this.modoEdicion ? 'actualizado' : 'creado'} correctamente.`,
            confirmButtonColor: '#0d6efd',
            timer: 2000,
            showConfirmButton: false
          });
        },
        error: (error) => {
          console.error('Error guardando permiso:', error);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: `Error al ${this.modoEdicion ? 'actualizar' : 'crear'} el permiso: ${error.error?.message || error.message || 'Error desconocido'}`,
            confirmButtonColor: '#0d6efd'
          });
        }
      })
    );
  }

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
              // Recargar datos después de eliminar
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

  // ========== VALIDACIÓN ==========

  get codigoInvalido(): boolean {
    const control = this.permisoForm.get('codigo');
    return control ? (control.invalid && (control.dirty || control.touched)) : false;
  }

  get nombreInvalido(): boolean {
    const control = this.permisoForm.get('nombre');
    return control ? (control.invalid && (control.dirty || control.touched)) : false;
  }

  private marcarCamposComoSucios(): void {
    Object.keys(this.permisoForm.controls).forEach(key => {
      const control = this.permisoForm.get(key);
      if (control) {
        control.markAsDirty();
        control.markAsTouched();
      }
    });
  }

  // ========== GETTERS PARA TEMPLATE ==========

  get mostrarResultados(): boolean {
    return this.totalItems > 0 && !this.cargandoPermisos;
  }

  get mostrarTablaVacia(): boolean {
    return !this.cargandoPermisos && this.totalItems === 0;
  }
}
