import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ProyectoResponse } from '../../model/proyecto.model';
import { ProyectoService } from '../../service/proyecto.service';
import { CommonModule } from '@angular/common';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import { PermisoDirective } from '../../directive/permiso.directive';

@Component({
  selector: 'app-proyectos',
  templateUrl: './proyectos.component.html',
  styleUrls: ['./proyectos.component.css'],
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    PaginationComponent,PermisoDirective
  ]
})
export class ProyectosComponent implements OnInit {
  // Datos
  proyectos: ProyectoResponse[] = [];
  proyectoSeleccionado: ProyectoResponse | null = null;

  // Paginación
  paginaActual: number = 0;
  itemsPorPagina: number = 10;
  totalElementos: number = 0;
  totalPaginas: number = 0;

  // Filtros
  filtroNombre: string = '';
  filtroEstado: string = '';
  mostrarTodos: boolean = false;
  campoOrden: string = 'nombre';
  direccionOrden: string = 'asc';

  // Estados
  cargando: boolean = false;
  modalCrearVisible: boolean = false;
  modalDetalleVisible: boolean = false;
  modalEditarVisible: boolean = false;

  // Formulario
  proyectoForm: FormGroup;

  constructor(
    private proyectoService: ProyectoService,
    private fb: FormBuilder
  ) {
    this.proyectoForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.minLength(3)]],
      activo: [true]
    });
  }

  ngOnInit(): void {
    this.cargarProyectos();
  }

  // ============ CARGAR DATOS ============
  cargarProyectos(): void {
    this.cargando = true;

    this.proyectoService.listarProyectos(
      this.paginaActual,
      this.itemsPorPagina,
      this.campoOrden,
      this.direccionOrden,
      this.filtroNombre || undefined,
      this.filtroEstado ? JSON.parse(this.filtroEstado) : undefined,
      this.mostrarTodos
    ).subscribe({
      next: (response) => {
        this.proyectos = response.content;
        this.totalElementos = response.totalItems;
        this.totalPaginas = response.totalPages;
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error cargando proyectos:', error);
        this.mostrarError('Error al cargar los proyectos');
        this.cargando = false;
      }
    });
  }

  // ============ PAGINACIÓN ============
  cambiarPagina(pagina: number): void {
    this.paginaActual = pagina;
    this.cargarProyectos();
  }

  cambiarItemsPorPagina(tamano: number): void {
    this.itemsPorPagina = tamano;
    this.paginaActual = 0;
    this.cargarProyectos();
  }

  // ============ FILTROS ============
  aplicarFiltros(): void {
    this.paginaActual = 0;
    this.cargarProyectos();
  }

  limpiarFiltros(): void {
    this.filtroNombre = '';
    this.filtroEstado = '';
    this.mostrarTodos = false;
    this.paginaActual = 0;
    this.cargarProyectos();
  }

  cambiarOrden(): void {
    this.cargarProyectos();
  }

  toggleDireccionOrden(): void {
    this.direccionOrden = this.direccionOrden === 'asc' ? 'desc' : 'asc';
    this.cargarProyectos();
  }

  // ============ MODALES ============
  abrirModalCrear(): void {
    this.proyectoForm.reset({
      nombre: '',
      activo: true
    });
    this.modalCrearVisible = true;
  }

  cerrarModalCrear(): void {
    this.modalCrearVisible = false;
  }

  abrirModalDetalle(proyecto: ProyectoResponse): void {
    this.proyectoSeleccionado = proyecto;
    this.modalDetalleVisible = true;
  }

  cerrarModalDetalle(): void {
    this.proyectoSeleccionado = null;
    this.modalDetalleVisible = false;
  }

  abrirModalEditar(proyecto: ProyectoResponse): void {
    this.proyectoSeleccionado = proyecto;
    this.proyectoForm.patchValue({
      nombre: proyecto.nombre,
      activo: proyecto.activo
    });
    this.modalEditarVisible = true;
  }

  cerrarModalEditar(): void {
    this.proyectoSeleccionado = null;
    this.modalEditarVisible = false;
  }

  // ============ CRUD ============
  crearProyecto(): void {
    if (this.proyectoForm.invalid) {
      this.marcarCamposComoTocados();
      return;
    }

    const datos = this.proyectoForm.value;

    this.proyectoService.crearProyecto(datos).subscribe({
      next: (response) => {
        this.cerrarModalCrear();

        Swal.fire({
          icon: 'success',
          title: '¡Proyecto creado!',
          text: `El proyecto "${response.nombre}" ha sido creado exitosamente`,
          timer: 2500,
          showConfirmButton: false
        });

        this.cargarProyectos();
      },
      error: (error) => {
        console.error('Error creando proyecto:', error);

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: error.error?.message || 'No se pudo crear el proyecto. Intente nuevamente.'
        });
      }
    });
  }

  editarProyecto(): void {
    if (this.proyectoForm.invalid || !this.proyectoSeleccionado) {
      this.marcarCamposComoTocados();
      return;
    }

    const datos = this.proyectoForm.value;

    this.proyectoService.editarProyecto(this.proyectoSeleccionado.idProyecto, datos).subscribe({
      next: (response) => {
        this.cerrarModalEditar();

        Swal.fire({
          icon: 'success',
          title: '¡Proyecto actualizado!',
          text: `El proyecto "${response.nombre}" ha sido actualizado exitosamente`,
          timer: 2500,
          showConfirmButton: false
        });

        this.cargarProyectos();
      },
      error: (error) => {
        console.error('Error editando proyecto:', error);

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: error.error?.message || 'No se pudo actualizar el proyecto. Intente nuevamente.'
        });
      }
    });
  }

  toggleEstado(proyecto: ProyectoResponse): void {
    const accion = proyecto.activo ? 'desactivar' : 'activar';
    const titulo = proyecto.activo ? 'Desactivar Proyecto' : 'Activar Proyecto';
    const texto = proyecto.activo
      ? '¿Está seguro que desea desactivar este proyecto?'
      : '¿Está seguro que desea activar este proyecto?';

    Swal.fire({
      title: titulo,
      text: texto,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: `Sí, ${accion}`,
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.proyectoService.toggleProyecto(proyecto.idProyecto).subscribe({
          next: (response) => {
            proyecto.activo = response.activo;

            Swal.fire({
              icon: 'success',
              title: '¡Estado cambiado!',
              text: `El proyecto ha sido ${response.activo ? 'activado' : 'desactivado'}`,
              timer: 1500,
              showConfirmButton: false
            });
          },
          error: (error) => {
            this.mostrarError('No se pudo cambiar el estado del proyecto');
          }
        });
      }
    });
  }

  // ============ UTILIDADES ============
  marcarCamposComoTocados(): void {
    Object.keys(this.proyectoForm.controls).forEach(key => {
      const control = this.proyectoForm.get(key);
      control?.markAsTouched();
    });
  }

  mostrarError(mensaje: string): void {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: mensaje
    });
  }

  // ============ GETTERS ============
  get nombreInvalido(): boolean {
    const control = this.proyectoForm.get('nombre');
    return control ? control.invalid && control.touched : false;
  }
}
