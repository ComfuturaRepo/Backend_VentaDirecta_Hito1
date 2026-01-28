import { Component, OnInit, ViewChild, TemplateRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { PageResponse, Usuario, UsuarioService } from '../../service/usuario.service';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';

@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    PaginationComponent
  ],
  templateUrl: './usuarios-component.html',
  styleUrls: ['./usuarios-component.css']
})
export class UsuariosComponent implements OnInit {
  // Datos
  usuarios: Usuario[] = [];
  pageResponse!: PageResponse<Usuario>;

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
  showFilters: boolean = false;
  showModal: boolean = false;

  // Formulario
  usuarioForm: FormGroup;
  modalMode: 'create' | 'edit' = 'create';
  modalTitle: string = '';
  usuarioSeleccionado: Usuario | null = null;

  // Listas para selects
  niveles: any[] = [];
  trabajadores: any[] = [];

  constructor(
    private usuarioService: UsuarioService,
    private fb: FormBuilder
  ) {
    this.usuarioForm = this.createUsuarioForm();
  }

  ngOnInit(): void {
    this.loadUsuarios();
    this.loadNiveles();
    this.loadTrabajadores();
  }

  // Crear formulario reactivo
  private createUsuarioForm(): FormGroup {
    return this.fb.group({
      username: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required]],
      trabajadorId: [null, [Validators.required]],
      nivelId: [null, [Validators.required]],
      activo: [true]
    }, { validators: this.passwordMatchValidator });
  }

  // Validador de coincidencia de contraseñas
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

  // Cargar datos
  loadUsuarios(): void {
    this.isLoading = true;

    const params = {
      page: this.currentPage,
      size: this.pageSize,
      sortBy: this.sortBy,
      direction: this.sortDirection,
      search: this.searchTerm,
      activos: this.filtroActivo !== null ? this.filtroActivo : undefined,
      nivelId: this.filtroNivelId || undefined
    };

    this.usuarioService.getUsuarios(params).subscribe({
      next: (response) => {
        this.pageResponse = response;
        this.usuarios = response.content;
        this.totalPages = response.totalPages;
        this.totalItems = response.totalItems;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar usuarios:', error);
        this.isLoading = false;
      }
    });
  }

  // Cargar niveles
  loadNiveles(): void {
    // Implementa este método según tu API
    // Por ahora, datos de ejemplo
    this.niveles = [
      { idNivel: 1, nombre: 'Administrador', codigo: 'ADMIN' },
      { idNivel: 2, nombre: 'Supervisor', codigo: 'SUPER' },
      { idNivel: 3, nombre: 'Operador', codigo: 'OPER' }
    ];
  }

  // Cargar trabajadores
  loadTrabajadores(): void {
    // Implementa este método según tu API
    // Por ahora, datos de ejemplo
    this.trabajadores = [
      { idTrabajador: 1, nombres: 'Juan', apellidos: 'Pérez', nombreCompleto: 'Juan Pérez' },
      { idTrabajador: 2, nombres: 'María', apellidos: 'García', nombreCompleto: 'María García' },
      { idTrabajador: 3, nombres: 'Carlos', apellidos: 'López', nombreCompleto: 'Carlos López' }
    ];
  }

  // Métodos de paginación
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
    this.loadUsuarios();
  }

  // Ordenamiento
  sort(column: string): void {
    if (this.sortBy === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = column;
      this.sortDirection = 'asc';
    }
    this.loadUsuarios();
  }

  // Filtros
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

  // Modal
  openCreateModal(): void {
    this.modalMode = 'create';
    this.modalTitle = 'Nuevo Usuario';
    this.usuarioSeleccionado = null;
    this.usuarioForm.reset({
      username: '',
      password: '',
      confirmPassword: '',
      trabajadorId: null,
      nivelId: null,
      activo: true
    });
    this.showModal = true;
  }

  openEditModal(usuario: Usuario): void {
    this.modalMode = 'edit';
    this.modalTitle = 'Editar Usuario';
    this.usuarioSeleccionado = usuario;

    this.usuarioForm.patchValue({
      username: usuario.username,
      password: '',
      confirmPassword: '',
      trabajadorId: usuario.trabajadorId,
      nivelId: usuario.nivelId,
      activo: usuario.activo
    });

    // Eliminar validación de contraseña en edición
    this.usuarioForm.get('password')?.clearValidators();
    this.usuarioForm.get('confirmPassword')?.clearValidators();
    this.usuarioForm.get('password')?.updateValueAndValidity();
    this.usuarioForm.get('confirmPassword')?.updateValueAndValidity();

    this.showModal = true;
  }

  closeModal(): void {
    this.showModal = false;
    this.usuarioSeleccionado = null;
    this.usuarioForm.reset();
  }

  // Guardar usuario
  saveUsuario(): void {
    if (this.usuarioForm.invalid) {
      this.marcarCamposInvalidos();
      return;
    }

    const formValue = this.usuarioForm.value;

    if (this.modalMode === 'create') {
      const usuarioRequest = {
        username: formValue.username,
        password: formValue.password,
        trabajadorId: formValue.trabajadorId,
        nivelId: formValue.nivelId
      };

      this.usuarioService.createUsuario(usuarioRequest).subscribe({
        next: (response) => {
          this.usuarioService.showSuccess('Usuario creado exitosamente');
          this.closeModal();
          this.loadUsuarios();
        },
        error: (error) => {
          console.error('Error al crear usuario:', error);
        }
      });
    } else if (this.usuarioSeleccionado) {
      const usuarioUpdate = {
        username: formValue.username,
        trabajadorId: formValue.trabajadorId,
        nivelId: formValue.nivelId
      };

      this.usuarioService.updateUsuario(this.usuarioSeleccionado.idUsuario, usuarioUpdate).subscribe({
        next: (response) => {
          this.usuarioService.showSuccess('Usuario actualizado exitosamente');
          this.closeModal();
          this.loadUsuarios();
        },
        error: (error) => {
          console.error('Error al actualizar usuario:', error);
        }
      });
    }
  }

  // Marcar campos inválidos
  private marcarCamposInvalidos(): void {
    Object.keys(this.usuarioForm.controls).forEach(key => {
      const control = this.usuarioForm.get(key);
      if (control?.invalid) {
        control.markAsTouched();
      }
    });
  }

  // Cambiar estado activo/inactivo
  toggleActivo(usuario: Usuario): void {
    const action = usuario.activo ? 'desactivar' : 'activar';

    this.usuarioService.showConfirm(`¿Estás seguro de ${action} este usuario?`)
      .then((result) => {
        if (result.isConfirmed) {
          this.usuarioService.toggleActivo(usuario.idUsuario).subscribe({
            next: (response) => {
              const message = usuario.activo ? 'Usuario desactivado' : 'Usuario activado';
              this.usuarioService.showSuccess(message);
              this.loadUsuarios();
            },
            error: (error) => {
              console.error('Error al cambiar estado:', error);
            }
          });
        }
      });
  }

  // Eliminar usuario
  deleteUsuario(usuario: Usuario): void {
    this.usuarioService.showConfirm('¿Estás seguro de eliminar este usuario? Esta acción no se puede deshacer.')
      .then((result) => {
        if (result.isConfirmed) {
          this.usuarioService.deleteUsuario(usuario.idUsuario).subscribe({
            next: (response) => {
              this.usuarioService.showSuccess('Usuario eliminado exitosamente');
              this.loadUsuarios();
            },
            error: (error) => {
              console.error('Error al eliminar usuario:', error);
            }
          });
        }
      });
  }

  // Cambiar contraseña
  cambiarPassword(usuario: Usuario): void {
    Swal.fire({
      title: 'Cambiar Contraseña',
      html: `
        <input type="password" id="currentPassword" class="swal2-input" placeholder="Contraseña actual">
        <input type="password" id="newPassword" class="swal2-input" placeholder="Nueva contraseña">
        <input type="password" id="confirmPassword" class="swal2-input" placeholder="Confirmar nueva contraseña">
      `,
      focusConfirm: false,
      showCancelButton: true,
      confirmButtonText: 'Cambiar',
      cancelButtonText: 'Cancelar',
      preConfirm: () => {
        const currentPassword = (document.getElementById('currentPassword') as HTMLInputElement).value;
        const newPassword = (document.getElementById('newPassword') as HTMLInputElement).value;
        const confirmPassword = (document.getElementById('confirmPassword') as HTMLInputElement).value;

        if (!currentPassword || !newPassword || !confirmPassword) {
          Swal.showValidationMessage('Todos los campos son obligatorios');
          return false;
        }

        if (newPassword !== confirmPassword) {
          Swal.showValidationMessage('Las contraseñas no coinciden');
          return false;
        }

        if (newPassword.length < 6) {
          Swal.showValidationMessage('La contraseña debe tener al menos 6 caracteres');
          return false;
        }

        return { currentPassword, newPassword };
      }
    }).then((result) => {
      if (result.isConfirmed) {
        this.usuarioService.changePassword(usuario.idUsuario, result.value).subscribe({
          next: (response) => {
            this.usuarioService.showSuccess('Contraseña cambiada exitosamente');
          },
          error: (error) => {
            console.error('Error al cambiar contraseña:', error);
          }
        });
      }
    });
  }

  // Helper methods
  getEstadoClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  getEstadoText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  getTrabajadorNombre(usuario: Usuario): string {
    return usuario.nombreTrabajador ||
           (usuario.trabajadorNombre && usuario.trabajadorApellidos
            ? `${usuario.trabajadorNombre} ${usuario.trabajadorApellidos}`
            : 'N/A');
  }

  getNivelNombre(usuario: Usuario): string {
    return usuario.nivelNombre || usuario.nivelCodigo || 'N/A';
  }

  // Verificar si un campo es inválido
  isFieldInvalid(fieldName: string): boolean {
    const field = this.usuarioForm.get(fieldName);
    return field ? field.invalid && field.touched : false;
  }

  // Obtener mensaje de error
  getErrorMessage(fieldName: string): string {
    const field = this.usuarioForm.get(fieldName);

    if (!field || !field.errors) return '';

    if (field.errors['required']) {
      return 'Este campo es obligatorio';
    }

    if (field.errors['email']) {
      return 'Ingrese un email válido';
    }

    if (field.errors['minlength']) {
      return `Mínimo ${field.errors['minlength'].requiredLength} caracteres`;
    }

    if (field.errors['mismatch']) {
      return 'Las contraseñas no coinciden';
    }

    return '';
  }
}
