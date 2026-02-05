import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { ProyectoResponse } from '../../model/proyecto.model';
import { ProyectoService } from '../../service/proyecto.service';
import bootstrap from '../../../main.server';

@Component({
  selector: 'app-proyectos',
  templateUrl: './proyectos.component.html',
  styleUrls: ['./proyectos.component.css']
})
export class ProyectosComponent implements OnInit {
  @ViewChild('proyectoModal') modalElement!: ElementRef;

  // Datos
  proyectos: ProyectoResponse[] = [];
  proyectoEditando: ProyectoResponse | null = null;

  // Paginación
  paginaActual: number = 0;
  itemsPorPagina: number = 10;
  totalElementos: number = 0;
  totalPaginas: number = 0;
  paginasVisibles: number[] = [];

  // Filtros
  filtroNombre: string = '';
  filtroEstado: string = '';
  mostrarTodos: boolean = false;
  campoOrden: string = 'nombre';
  direccionOrden: string = 'asc';

  // Estados
  cargando: boolean = false;
  guardando: boolean = false;

  // Formulario
  proyectoForm: FormGroup;
  modal: any;

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

  ngAfterViewInit(): void {
    this.modal = new bootstrap.NgbModal(this.modalElement.nativeElement);
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
        this.calcularPaginasVisibles();
        this.cargando = false;
      },
      error: (error) => {
        console.error('Error cargando proyectos:', error);
        this.mostrarError('Error al cargar los proyectos');
        this.cargando = false;
      }
    });
  }

  // ============ FILTROS Y BÚSQUEDA ============
  aplicarFiltros(): void {
    this.paginaActual = 0;
    this.cargarProyectos();
  }

  limpiarBusqueda(): void {
    this.filtroNombre = '';
    this.filtroEstado = '';
    this.mostrarTodos = false;
    this.aplicarFiltros();
  }

  ordenarPor(campo: string): void {
    if (this.campoOrden === campo) {
      this.direccionOrden = this.direccionOrden === 'asc' ? 'desc' : 'asc';
    } else {
      this.campoOrden = campo;
      this.direccionOrden = 'asc';
    }
    this.cargarProyectos();
  }

  cambiarItemsPorPagina(): void {
    this.paginaActual = 0;
    this.cargarProyectos();
  }

  // ============ PAGINACIÓN ============
  calcularPaginasVisibles(): void {
    const paginas = [];
    const maxPaginasVisibles = 5;

    let inicio = Math.max(0, this.paginaActual - Math.floor(maxPaginasVisibles / 2));
    let fin = Math.min(this.totalPaginas, inicio + maxPaginasVisibles);

    if (fin - inicio < maxPaginasVisibles) {
      inicio = Math.max(0, fin - maxPaginasVisibles);
    }

    for (let i = inicio; i < fin; i++) {
      paginas.push(i);
    }

    this.paginasVisibles = paginas;
  }

  irPagina(pagina: number): void {
    if (pagina >= 0 && pagina < this.totalPaginas && pagina !== this.paginaActual) {
      this.paginaActual = pagina;
      this.cargarProyectos();
    }
  }

  // ============ CRUD OPERACIONES ============
  abrirModalCrear(): void {
    this.proyectoEditando = null;
    this.proyectoForm.reset({
      nombre: '',
      activo: true
    });
    this.modal.show();
  }

  abrirModalEditar(proyecto: ProyectoResponse): void {
    this.proyectoEditando = proyecto;
    this.proyectoForm.patchValue({
      nombre: proyecto.nombre,
      activo: proyecto.activo
    });
    this.modal.show();
  }

  guardarProyecto(): void {
    if (this.proyectoForm.invalid) {
      this.marcarCamposComoTocados();
      return;
    }

    this.guardando = true;
    const datos = this.proyectoForm.value;

    const operacion = this.proyectoEditando
      ? this.proyectoService.editarProyecto(this.proyectoEditando.idProyecto, datos)
      : this.proyectoService.crearProyecto(datos);

    operacion.subscribe({
      next: (response) => {
        this.guardando = false;
        this.modal.hide();

        Swal.fire({
          icon: 'success',
          title: this.proyectoEditando ? '¡Proyecto actualizado!' : '¡Proyecto creado!',
          text: `El proyecto "${response.nombre}" ha sido ${this.proyectoEditando ? 'actualizado' : 'creado'} exitosamente`,
          timer: 2000,
          showConfirmButton: false
        });

        this.cargarProyectos();
      },
      error: (error) => {
        this.guardando = false;
        console.error('Error guardando proyecto:', error);

        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: error.error?.message || 'No se pudo guardar el proyecto. Intente nuevamente.',
          confirmButtonColor: '#3085d6'
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
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: `Sí, ${accion}`,
      cancelButtonText: 'Cancelar',
      reverseButtons: true
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

  verDetalles(proyecto: ProyectoResponse): void {
    Swal.fire({
      title: proyecto.nombre,
      html: `
        <div class="text-start">
          <p><strong>ID:</strong> ${proyecto.idProyecto}</p>
          <p><strong>Estado:</strong>
            <span class="badge ${proyecto.activo ? 'bg-success' : 'bg-danger'}">
              ${proyecto.activo ? 'Activo' : 'Inactivo'}
            </span>
          </p>
          <p><strong>Fecha de creación:</strong> ${new Date().toLocaleDateString()}</p>
        </div>
      `,
      icon: 'info',
      confirmButtonColor: '#3085d6',
      confirmButtonText: 'Cerrar'
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
      text: mensaje,
      confirmButtonColor: '#3085d6'
    });
  }

  // ============ GETTERS PARA TEMPLATE ============
  get nombreInvalido(): boolean {
    const control = this.proyectoForm.get('nombre');
    return control ? control.invalid && control.touched : false;
  }
}
