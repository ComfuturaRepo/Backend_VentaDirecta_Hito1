import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { PageResponse, Usuario, UsuarioService } from '../../service/usuario.service';

// Definir interfaz local si no la importas del servicio
interface PaginationParams {
  page: number;
  size: number;
  sortBy: string;
  direction: string;
  search?: string;
  activos?: boolean;
  nivelId?: number;
}

@Component({
  selector: 'app-usuarios-component',
  imports: [CommonModule, FormsModule],
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

  // Paginación
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  totalItems: number = 0;
  pageSizes: number[] = [5, 10, 20, 50];

  // Ordenamiento
  sortBy: string = 'idUsuario';
  sortDirection: string = 'asc';

  // Estados
  isLoading: boolean = false;
  showFilters: boolean = false;

  // Modal
  showModal: boolean = false;
  modalTitle: string = '';
  modalMode: 'create' | 'edit' = 'create';

  // Formulario - CORREGIDO: usar nombre diferente al del template reference
  usuarioFormData: any = {
    idUsuario: null,
    username: '',
    password: '',
    trabajadorId: null,
    nivelId: null,
    confirmPassword: ''
  };

  // Trabajadores y Niveles
  trabajadores: any[] = [
    { idTrabajador: 1, nombreCompleto: 'Juan Pérez' },
    { idTrabajador: 2, nombreCompleto: 'María García' },
    { idTrabajador: 3, nombreCompleto: 'Carlos López' }
  ];

  niveles: any[] = [
    { idNivel: 1, nombre: 'Administrador', codigo: 'ADMIN' },
    { idNivel: 2, nombre: 'Supervisor', codigo: 'SUPER' },
    { idNivel: 3, nombre: 'Operador', codigo: 'OPER' }
  ];

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.loadUsuarios();
  }

  // Cargar usuarios
  loadUsuarios(): void {
    this.isLoading = true;

    const params: PaginationParams = {
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
  // Cambiar página
  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadUsuarios();
    }
  }

  // Cambiar tamaño de página
  changePageSize(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.loadUsuarios();
  }

  // Ordenar por columna
  sort(column: string): void {
    if (this.sortBy === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortBy = column;
      this.sortDirection = 'asc';
    }
    this.loadUsuarios();
  }

  // Aplicar filtros
  aplicarFiltros(): void {
    this.currentPage = 0;
    this.loadUsuarios();
  }

  // Limpiar filtros
  limpiarFiltros(): void {
    this.searchTerm = '';
    this.filtroActivo = null;
    this.filtroNivelId = null;
    this.currentPage = 0;
    this.loadUsuarios();
  }

  // Abrir modal para crear
  openCreateModal(): void {
    this.modalMode = 'create';
    this.modalTitle = 'Nuevo Usuario';
    this.resetForm();
    this.showModal = true;
  }

  // Abrir modal para editar
  openEditModal(usuario: Usuario): void {
    this.modalMode = 'edit';
    this.modalTitle = 'Editar Usuario';

    this.usuarioFormData = {
      idUsuario: usuario.idUsuario,
      username: usuario.username,
      password: '',
      trabajadorId: usuario.trabajadorId,
      nivelId: usuario.nivelId,
      confirmPassword: ''
    };

    this.showModal = true;
  }

  // Resetear formulario
  resetForm(): void {
    this.usuarioFormData = {
      idUsuario: null,
      username: '',
      password: '',
      trabajadorId: null,
      nivelId: null,
      confirmPassword: ''
    };
  }

  // Guardar usuario (crear/editar)
  saveUsuario(): void {
    // Validaciones
    if (!this.usuarioFormData.username || !this.usuarioFormData.trabajadorId || !this.usuarioFormData.nivelId) {
      Swal.fire('Error', 'Por favor complete todos los campos obligatorios', 'warning');
      return;
    }

    if (this.modalMode === 'create') {
      if (!this.usuarioFormData.password) {
        Swal.fire('Error', 'La contraseña es obligatoria para nuevos usuarios', 'warning');
        return;
      }

      if (this.usuarioFormData.password !== this.usuarioFormData.confirmPassword) {
        Swal.fire('Error', 'Las contraseñas no coinciden', 'warning');
        return;
      }

      const usuarioRequest = {
        username: this.usuarioFormData.username,
        password: this.usuarioFormData.password,
        trabajadorId: this.usuarioFormData.trabajadorId,
        nivelId: this.usuarioFormData.nivelId
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

    } else {
      const usuarioUpdate = {
        username: this.usuarioFormData.username,
        trabajadorId: this.usuarioFormData.trabajadorId,
        nivelId: this.usuarioFormData.nivelId
      };

      this.usuarioService.updateUsuario(this.usuarioFormData.idUsuario, usuarioUpdate).subscribe({
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

  // Cerrar modal
  closeModal(): void {
    this.showModal = false;
    this.resetForm();
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

  // Obtener clase de estado
  getEstadoClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  // Obtener texto de estado
  getEstadoText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  // Obtener nombre completo del trabajador
  getTrabajadorNombre(usuario: Usuario): string {
    return usuario.nombreTrabajador ||
           (usuario.trabajadorNombre && usuario.trabajadorApellidos
            ? `${usuario.trabajadorNombre} ${usuario.trabajadorApellidos}`
            : 'N/A');
  }
}
