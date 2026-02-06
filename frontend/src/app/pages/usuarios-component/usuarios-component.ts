import { DropdownItem, DropdownService } from './../../service/dropdown.service';
// usuarios.component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { Subscription, debounceTime, distinctUntilChanged } from 'rxjs';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';
import {  UsuarioSimple, UsuarioRequest, UsuarioUpdate } from '../../model/usuario';
import { UsuarioService } from '../../service/usuario.service';
import { Trabajador } from '../../service/trabajador.service';
import { PermisoDirective } from '../../directive/permiso.directive';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    PaginationComponent,PermisoDirective
  ],
  templateUrl: './usuarios-component.html',
  styleUrls: ['./usuarios-component.css']
})
export class UsuariosComponent implements OnInit, OnDestroy {
  // Datos
  usuarios: UsuarioSimple[] = [];
  pageResponse: any = null;

  // Filtros
  searchTerm: string = '';
  filtroActivo: boolean | null = null;
  filtroNivelId: number | null = null;

  // Configuración de paginación
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalItems: number = 0;

  // Ordenamiento
  sortBy: string = 'idUsuario';
  sortDirection: string = 'desc';

  // Estados
  isLoading: boolean = false;
  showFilters: boolean = true;
  showModal: boolean = false;
  showPassword: boolean = false;
  showConfirmPassword: boolean = false;

  // Formulario
  usuarioForm: FormGroup;
  modalMode: 'create' | 'edit' = 'create';
  modalTitle: string = '';
  usuarioSeleccionado: UsuarioSimple | null = null;

  // Datos para selects
  niveles: DropdownItem[] = [];
  trabajadores: DropdownItem[] = [];

  // Suscripciones
  private searchSubscription?: Subscription;

  constructor(
    public usuarioService: UsuarioService,
    public dropdownService: DropdownService,
    private fb: FormBuilder
  ) {
    this.usuarioForm = this.createUsuarioForm();
  }

 ngOnInit(): void {
  this.initSearchDebounce();
  this.loadUsuarios();
  this.loadNiveles();
  // QUITAR: this.loadTrabajadores(); // Ya no se carga aquí
}

  ngOnDestroy(): void {
    this.searchSubscription?.unsubscribe();
  }

  // ========== PAGINACIÓN Y FILTROS ==========

  loadUsuarios(): void {
    this.isLoading = true;

    const params = {
      page: this.currentPage,
      size: this.pageSize,
      sortBy: this.sortBy,
      direction: this.sortDirection,
      search: this.searchTerm || undefined,
      activos: this.filtroActivo !== null ? this.filtroActivo : undefined,
      nivelId: this.filtroNivelId || undefined
    };

    this.usuarioService.getUsuarios(params).subscribe({
      next: (response) => {
        this.pageResponse = response;
        this.usuarios = response.content || [];
        this.currentPage = response.currentPage || 0;
        this.totalPages = response.totalPages || 0;
        this.totalItems = response.totalItems || 0;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
        this.isLoading = false;
        this.usuarioService.showError('Error', 'No se pudieron cargar los usuarios');
      }
    });
  }
// Método para creación: solo trabajadores SIN usuario activo
private loadTrabajadoresSinUsuario(): void {
  this.dropdownService.getTrabajadoresSinUSuarioActivo().subscribe({
    next: (trabajadores) => {
      this.trabajadores = trabajadores;
    },
    error: (error) => {
      console.error('Error al cargar trabajadores:', error);
      this.trabajadores = [];
    }
  });
}

// Método para edición: todos los trabajadores + el asignado
private loadTrabajadoresParaEdicion(idTrabajadorActual: number): void {
  // Primero cargar todos los trabajadores
  this.dropdownService.getTrabajadores().subscribe({
    next: (todosTrabajadores) => {
      // Si el trabajador actual no está en la lista, lo agregamos
      const trabajadorActual = todosTrabajadores.find(t => t.id === idTrabajadorActual);

      if (!trabajadorActual) {
        // Opcional: podrías hacer otra llamada para obtener el trabajador específico
        this.trabajadores = todosTrabajadores;
      } else {
        this.trabajadores = todosTrabajadores;
      }
    },
    error: (error) => {
      console.error('Error al cargar trabajadores:', error);
      this.trabajadores = [];
    }
  });
}
  onPageChange(page: number): void {
    this.currentPage = page;
    this.loadUsuarios();
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadUsuarios();
  }

  onRefresh(): void {
    this.currentPage = 0;
    this.loadUsuarios();
  }

  sort(column: string): void {
    if (this.sortBy === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = column;
      this.sortDirection = 'asc';
    }
    this.loadUsuarios();
  }

  aplicarFiltros(): void {
    this.currentPage = 0;
    this.loadUsuarios();
  }

  limpiarFiltros(): void {
    this.searchTerm = '';
    this.filtroActivo = null;
    this.filtroNivelId = null;
    this.currentPage = 0;
    this.loadUsuarios();
  }

  // ========== CRUD ==========

  openCreateModal(): void {
  this.modalMode = 'create';
  this.modalTitle = 'Crear Nuevo Usuario';
  this.usuarioSeleccionado = null;

  // Cargar trabajadores SIN usuario activo
  this.loadTrabajadoresSinUsuario();

  this.usuarioForm.reset({
    username: '',
    password: '',
    confirmPassword: '',
    trabajadorId: null,
    nivelId: null,
    activo: true
  });

  this.showPassword = false;
  this.showConfirmPassword = false;
  this.showModal = true;
}

openEditModal(usuario: UsuarioSimple): void {
  this.modalMode = 'edit';
  this.modalTitle = 'Editar Usuario';
  this.usuarioSeleccionado = usuario;

  // Cargar TODOS los trabajadores para edición
  this.loadTrabajadoresParaEdicion(usuario.idTrabajador);

  this.usuarioForm.patchValue({
    username: usuario.username,
    password: '',
    confirmPassword: '',
    trabajadorId: usuario.idTrabajador,
    nivelId: usuario.idNivel,
    activo: usuario.activo
  });

  this.showPassword = false;
  this.showConfirmPassword = false;
  this.showModal = true;
}

  closeModal(): void {
    this.showModal = false;
    this.usuarioForm.reset();
    this.usuarioSeleccionado = null;
  }

  saveUsuario(): void {
    if (this.usuarioForm.invalid) {
      this.marcarCamposInvalidos();
      return;
    }

    const formValue = this.usuarioForm.value;

    if (this.modalMode === 'create') {
      const usuarioRequest: UsuarioRequest = {
        username: formValue.username,
        password: formValue.password,
        trabajadorId: formValue.trabajadorId,
        nivelId: formValue.nivelId,
        activo: formValue.activo
      };

      this.usuarioService.createUsuario(usuarioRequest).subscribe({
        next: () => {
          this.usuarioService.showSuccess('Éxito', 'Usuario creado exitosamente');
          this.closeModal();
          this.loadUsuarios();
        },
        error: (error) => {
          this.usuarioService.showError('Error', error.message || 'No se pudo crear el usuario');
        }
      });
    } else if (this.modalMode === 'edit' && this.usuarioSeleccionado) {
      const usuarioUpdate: UsuarioUpdate = {
        username: formValue.username,
        trabajadorId: formValue.trabajadorId,
        nivelId: formValue.nivelId
      };

      this.usuarioService.updateUsuario(this.usuarioSeleccionado.idUsuario, usuarioUpdate).subscribe({
        next: () => {
          this.usuarioService.showSuccess('Éxito', 'Usuario actualizado exitosamente');
          this.closeModal();
          this.loadUsuarios();
        },
        error: (error) => {
          this.usuarioService.showError('Error', error.message || 'No se pudo actualizar el usuario');
        }
      });
    }
  }

  toggleActivo(usuario: UsuarioSimple): void {
    const actionText = usuario.activo ? 'Desactivar' : 'Activar';

    this.usuarioService.showConfirm(
      `¿${actionText} Usuario?`,
      `¿Estás seguro de ${actionText.toLowerCase()} el usuario <strong>${usuario.username}</strong>?`
    ).then((result) => {
      if (result.isConfirmed) {
        this.usuarioService.toggleActivo(usuario.idUsuario).subscribe({
          next: () => {
            const message = usuario.activo ? 'Usuario desactivado' : 'Usuario activado';
            this.usuarioService.showSuccess('Éxito', message);
            this.loadUsuarios();
          },
          error: (error) => {
            this.usuarioService.showError('Error', 'No se pudo cambiar el estado del usuario');
          }
        });
      }
    });
  }

  // ========== DATOS PARA FORMULARIOS ==========

  loadNiveles(): void {
    this.dropdownService.getNivel().subscribe({
      next: (niveles) => {
        this.niveles = niveles;
      },
      error: (error) => {
        console.error('Error al cargar niveles:', error);
        this.niveles = [];
      }
    });
  }



  // ========== HELPERS ==========

  private initSearchDebounce(): void {
    // Si necesitas implementar búsqueda en tiempo real
  }

  private createUsuarioForm(): FormGroup {
    return this.fb.group({
      username: ['', [Validators.required, Validators.maxLength(100)]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]],
      confirmPassword: ['', [Validators.required]],
      trabajadorId: [null, [Validators.required]],
      nivelId: [null, [Validators.required]],
      activo: [true]
    }, { validators: this.passwordMatchValidator });
  }

  private passwordMatchValidator(g: FormGroup) {
    const password = g.get('password')?.value;
    const confirmPassword = g.get('confirmPassword')?.value;

    if (password !== confirmPassword) {
      g.get('confirmPassword')?.setErrors({ mismatch: true });
      return { mismatch: true };
    }

    g.get('confirmPassword')?.setErrors(null);
    return null;
  }

  private marcarCamposInvalidos(): void {
    Object.keys(this.usuarioForm.controls).forEach(key => {
      const control = this.usuarioForm.get(key);
      if (control?.invalid) {
        control.markAsTouched();
      }
    });
  }

  getEstadoClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  getEstadoText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  isFieldInvalid(fieldName: string): boolean {
    const field = this.usuarioForm.get(fieldName);
    return field ? field.invalid && field.touched : false;
  }

  getErrorMessage(fieldName: string): string {
    const field = this.usuarioForm.get(fieldName);
    if (!field || !field.errors) return '';

    if (field.errors['required']) return 'Este campo es obligatorio';
    if (field.errors['minlength']) return `Mínimo ${field.errors['minlength'].requiredLength} caracteres`;
    if (field.errors['maxlength']) return `Máximo ${field.errors['maxlength'].requiredLength} caracteres`;
    if (field.errors['mismatch']) return 'Las contraseñas no coinciden';

    return '';
  }
}
