import { Component, OnInit, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { finalize } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { PermisoResponseDTO } from '../../../model/permiso.interface';
import { DropdownItem } from '../../../service/dropdown.service';
import { PermisoService } from '../../../service/permiso.service';

// Interfaces

@Component({
  selector: 'app-permiso-form',
  standalone: true, // Asegúrate de que sea standalone
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './permiso-form.component.html',
  styleUrl: './permiso-form.component.css',
})
export class PermisoFormComponent  implements OnInit, OnChanges {
  @Input() mostrar: boolean = false;
  @Input() modoEdicion: boolean = false;
  @Input() permisoSeleccionado: PermisoResponseDTO | null = null;
  @Input() niveles: DropdownItem[] = [];
  @Input() areas: DropdownItem[] = [];
  @Input() cargos: DropdownItem[] = [];
  @Input() trabajadores: DropdownItem[] = [];
tabActiva: string = 'niveles';

  @Output() cerrar = new EventEmitter<void>();
  @Output() guardado = new EventEmitter<void>();

  // Formulario
  permisoForm!: FormGroup;

  // Selecciones temporales
  nivelesSeleccionados: number[] = [];
  areasSeleccionadas: number[] = [];
  cargosSeleccionados: number[] = [];
  trabajadoresSeleccionados: number[] = [];
nivelesFiltrados: DropdownItem[] = [];
areasFiltradas: DropdownItem[] = [];
cargosFiltrados: DropdownItem[] = [];
trabajadoresFiltrados: DropdownItem[] = [];
  // Estados
  guardando: boolean = false;

  // Subscripciones
  private subscriptions: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private permisoService: PermisoService
  ) {}
ngOnInit(): void {
  this.inicializarFormulario();
  this.inicializarFiltros(); // Asegúrate de que este método existe
}
cambiarTab(tab: string): void {
  this.tabActiva = tab;
}
ngOnChanges(changes: SimpleChanges): void {
  if (changes['niveles'] && this.niveles) {
    this.nivelesFiltrados = [...this.niveles];
  }
  if (changes['areas'] && this.areas) {
    this.areasFiltradas = [...this.areas];
  }
  if (changes['cargos'] && this.cargos) {
    this.cargosFiltrados = [...this.cargos];
  }
  if (changes['trabajadores'] && this.trabajadores) {
    this.trabajadoresFiltrados = [...this.trabajadores];
  }

  if (changes['permisoSeleccionado'] && this.permisoSeleccionado) {
    this.cargarDatosPermiso();
  }

  if (changes['mostrar'] && changes['mostrar'].currentValue === false) {
    this.limpiarFormulario();
  }
}

// Métodos de filtrado
filtrarNiveles(termino: string): void {
  if (!termino.trim()) {
    this.nivelesFiltrados = [...this.niveles];
    return;
  }
  const busqueda = termino.toLowerCase();
  this.nivelesFiltrados = this.niveles.filter(nivel =>
    nivel.label.toLowerCase().includes(busqueda) ||
    (nivel.adicional && nivel.adicional.toLowerCase().includes(busqueda))
  );
}

filtrarAreas(termino: string): void {
  if (!termino.trim()) {
    this.areasFiltradas = [...this.areas];
    return;
  }
  const busqueda = termino.toLowerCase();
  this.areasFiltradas = this.areas.filter(area =>
    area.label.toLowerCase().includes(busqueda)
  );
}

filtrarCargos(termino: string): void {
  if (!termino.trim()) {
    this.cargosFiltrados = [...this.cargos];
    return;
  }
  const busqueda = termino.toLowerCase();
  this.cargosFiltrados = this.cargos.filter(cargo =>
    cargo.label.toLowerCase().includes(busqueda)
  );
}

filtrarTrabajadores(termino: string): void {
  if (!termino.trim()) {
    this.trabajadoresFiltrados = [...this.trabajadores];
    return;
  }
  const busqueda = termino.toLowerCase();
  this.trabajadoresFiltrados = this.trabajadores.filter(trabajador =>
    trabajador.label.toLowerCase().includes(busqueda) ||
    (trabajador.adicional && trabajador.adicional.toLowerCase().includes(busqueda))
  );
}

// Métodos para seleccionar todos/deseleccionar todos
seleccionarTodos(tipo: string): void {
  switch (tipo) {
    case 'niveles':
      this.nivelesSeleccionados = this.niveles
        .filter(n => !this.nivelesSeleccionados.includes(n.id))
        .map(n => n.id)
        .concat(this.nivelesSeleccionados);
      break;
    case 'areas':
      this.areasSeleccionadas = this.areas
        .filter(a => !this.areasSeleccionadas.includes(a.id) && a.estado !== false)
        .map(a => a.id)
        .concat(this.areasSeleccionadas);
      break;
    case 'cargos':
      this.cargosSeleccionados = this.cargos
        .filter(c => !this.cargosSeleccionados.includes(c.id) && c.estado !== false)
        .map(c => c.id)
        .concat(this.cargosSeleccionados);
      break;
    case 'trabajadores':
      this.trabajadoresSeleccionados = this.trabajadores
        .filter(t => !this.trabajadoresSeleccionados.includes(t.id) && t.estado !== false)
        .map(t => t.id)
        .concat(this.trabajadoresSeleccionados);
      break;
  }
}

limpiarSelecciones(): void {
  this.nivelesSeleccionados = [];
  this.areasSeleccionadas = [];
  this.cargosSeleccionados = [];
  this.trabajadoresSeleccionados = [];

  // Resetear filtros de búsqueda
  this.nivelesFiltrados = [...this.niveles];
  this.areasFiltradas = [...this.areas];
  this.cargosFiltrados = [...this.cargos];
  this.trabajadoresFiltrados = [...this.trabajadores];
}
deseleccionarTodos(tipo: string): void {
  switch (tipo) {
    case 'niveles':
      this.nivelesSeleccionados = [];
      break;
    case 'areas':
      this.areasSeleccionadas = [];
      break;
    case 'cargos':
      this.cargosSeleccionados = [];
      break;
    case 'trabajadores':
      this.trabajadoresSeleccionados = [];
      break;
  }
}
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

private inicializarFiltros(): void {
  this.nivelesFiltrados = [...this.niveles];
  this.areasFiltradas = [...this.areas];
  this.cargosFiltrados = [...this.cargos];
  this.trabajadoresFiltrados = [...this.trabajadores];
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

  private cargarDatosPermiso(): void {
    if (!this.permisoSeleccionado) return;

    // Cargar trabajadores seleccionados
    if (this.permisoSeleccionado.trabajadores && this.permisoSeleccionado.trabajadores.length > 0) {
      this.trabajadoresSeleccionados = this.permisoSeleccionado.trabajadores.map(t => t.idTrabajador);
    } else {
      this.trabajadoresSeleccionados = [];
    }

    // Cargar datos del permiso
    this.permisoForm.patchValue({
      codigo: this.permisoSeleccionado.codigo,
      nombre: this.permisoSeleccionado.nombre,
      descripcion: this.permisoSeleccionado.descripcion,
      activo: this.permisoSeleccionado.activo
    });

    // Cargar selecciones
    this.nivelesSeleccionados = this.permisoSeleccionado.niveles.map(n => n.idNivel);
    this.areasSeleccionadas = this.permisoSeleccionado.areas.map(a => a.idArea);
    this.cargosSeleccionados = this.permisoSeleccionado.cargos.map(c => c.idCargo);
  }

  private limpiarFormulario(): void {
    this.permisoForm.reset({
      codigo: '',
      nombre: '',
      descripcion: '',
      activo: true
    });

    this.nivelesSeleccionados = [];
    this.areasSeleccionadas = [];
    this.cargosSeleccionados = [];
    this.trabajadoresSeleccionados = [];
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

  toggleTrabajador(id: number): void {
    const index = this.trabajadoresSeleccionados.indexOf(id);
    if (index === -1) {
      this.trabajadoresSeleccionados.push(id);
    } else {
      this.trabajadoresSeleccionados.splice(index, 1);
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

  // ========== OPERACIONES CRUD ==========

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
      cargosIds: this.cargosSeleccionados,
      trabajadoresIds: this.trabajadoresSeleccionados
    };

    const observable = this.modoEdicion && this.permisoSeleccionado
      ? this.permisoService.actualizarPermiso(this.permisoSeleccionado.idPermiso, permisoData)
      : this.permisoService.crearPermiso(permisoData);

    this.subscriptions.add(
      observable.pipe(
        finalize(() => this.guardando = false)
      ).subscribe({
        next: () => {
          this.guardado.emit();

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

  // ========== EVENTOS ==========

  onCerrar(): void {
    this.cerrar.emit();
  }
}
