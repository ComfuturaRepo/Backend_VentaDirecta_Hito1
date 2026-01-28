// src/app/pages/usuario-perfil/usuario-perfil-component.ts
import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { PaginationComponent } from '../../component/pagination.component/pagination.component';

import {
  Usuario,
  UsuarioService,
  Trabajador,
  Nivel,
  EstadisticasUsuario,
  Actividad,
  ChangePasswordRequest,
  UsuarioUpdate
} from '../../service/usuario.service';
@Component({
  selector: 'app-usuario-perfil',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './usuario-perfil-component.html',
  styleUrls: ['./usuario-perfil-component.css']
})
export class UsuarioPerfilComponent implements OnInit, OnDestroy {
  // Datos principales
  usuario: Usuario | null = null;
  trabajador: Trabajador | null = null;
  niveles: Nivel[] = [];

  // Estadísticas
  estadisticas: EstadisticasUsuario = {
    otsCompletadas: 0,
    otsPendientes: 0,
    proyectosActivos: 0,
    diasTrabajados: 0,
    horasTrabajadasMes: 0,
    tareasCompletadas: 0
  };

  // Actividades
  actividades: Actividad[] = [];

  // Formularios
  editProfileForm = {
    email: '',
    telefono: '',
    direccion: '',
    cargo: '',
    area: ''
  };

  changePasswordForm: ChangePasswordRequest = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  editUsuarioForm = {
    username: '',
    nivelId: 0
  };

  // Estados
  isLoading = false;
  isUpdating = false;
  showEditProfile = false;
  showChangePassword = false;
  showEditUsuario = false;

  // Paginación (para actividades si hubiera muchas)
  currentPage = 0;
  pageSize = 5;
  totalItems = 0;
  totalPages = 0;

  // Subscripciones
  private subscriptions: Subscription[] = [];

  // Configuración de paginación
  paginationConfig = {
    showInfo: true,
    showSizeSelector: false,
    showNavigation: true,
    showJumpToPage: false,
    showPageNumbers: false,
    pageSizes: [5, 10, 20],
    align: 'center' as const,
    size: 'sm' as const
  };

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.loadData();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach(sub => sub.unsubscribe());
  }

  // ========== CARGA DE DATOS ==========

  loadData(): void {
    this.isLoading = true;

    // Cargar datos en paralelo
    const sub1 = this.usuarioService.getPerfilUsuario().subscribe({
      next: (usuario) => {
        this.usuario = usuario;
        this.loadTrabajador(usuario.trabajadorId);
        this.loadEstadisticas(usuario.idUsuario);
        this.loadActividades(usuario.idUsuario);
        this.initializeEditUsuarioForm(usuario);
      },
      error: (error) => {
        this.usuarioService.showError('Error', 'No se pudo cargar el perfil');
        console.error('Error cargando usuario:', error);
      }
    });



  }

  loadTrabajador(trabajadorId: number): void {
    this.usuarioService.getTrabajadorById(trabajadorId).subscribe({
      next: (trabajador) => {
        this.trabajador = trabajador;
        this.initializeEditProfileForm(trabajador);
        this.isLoading = false;
      },
      error: (error) => {
        this.usuarioService.showError('Error', 'No se pudo cargar información del trabajador');
        console.error('Error cargando trabajador:', error);
        this.isLoading = false;
      }
    });
  }

  loadEstadisticas(userId: number): void {
    this.usuarioService.getEstadisticasUsuario(userId).subscribe({
      next: (estadisticas) => {
        this.estadisticas = estadisticas;
      },
      error: (error) => {
        console.error('Error cargando estadísticas:', error);
      }
    });
  }

  loadActividades(userId: number): void {
    this.usuarioService.getActividadesUsuario(userId).subscribe({
      next: (actividades) => {
        this.actividades = actividades;
        this.totalItems = actividades.length;
        this.totalPages = Math.ceil(actividades.length / this.pageSize);
      },
      error: (error) => {
        console.error('Error cargando actividades:', error);
      }
    });
  }

  // ========== INICIALIZACIÓN DE FORMULARIOS ==========

  initializeEditProfileForm(trabajador: Trabajador): void {
    this.editProfileForm = {
      email: trabajador.email || '',
      telefono: trabajador.telefono || '',
      direccion: trabajador.direccion || '',
      cargo: trabajador.cargo || '',
      area: trabajador.area || ''
    };
  }

  initializeEditUsuarioForm(usuario: Usuario): void {
    this.editUsuarioForm = {
      username: usuario.username || '',
      nivelId: usuario.nivelId || 0
    };
  }

  // ========== FORMULARIO DE PERFIL ==========

  openEditProfile(): void {
    this.showEditProfile = true;
  }

  saveProfile(): void {
    if (!this.trabajador) {
      this.usuarioService.showError('Error', 'No se encontró información del trabajador');
      return;
    }

    // Validaciones
    if (!this.editProfileForm.email) {
      this.usuarioService.showError('Error', 'El email es obligatorio');
      return;
    }

    if (!this.validateEmail(this.editProfileForm.email)) {
      this.usuarioService.showError('Error', 'El email no es válido');
      return;
    }

    this.isUpdating = true;

    const updatedData = {
      email: this.editProfileForm.email,
      telefono: this.editProfileForm.telefono,
      direccion: this.editProfileForm.direccion,
      cargo: this.editProfileForm.cargo,
      area: this.editProfileForm.area
    };

    this.usuarioService.updateTrabajador(this.trabajador.idTrabajador, updatedData).subscribe({
      next: (trabajadorActualizado) => {
        this.trabajador = trabajadorActualizado;
        this.usuarioService.showSuccess('Éxito', 'Perfil actualizado correctamente');
        this.showEditProfile = false;
        this.isUpdating = false;
      },
      error: (error) => {
        this.usuarioService.showError('Error', 'No se pudo actualizar el perfil');
        console.error('Error actualizando perfil:', error);
        this.isUpdating = false;
      }
    });
  }

  cancelEditProfile(): void {
    if (this.trabajador) {
      this.initializeEditProfileForm(this.trabajador);
    }
    this.showEditProfile = false;
  }

  // ========== FORMULARIO DE USUARIO ==========

  openEditUsuario(): void {
    this.showEditUsuario = true;
  }

  saveUsuario(): void {
    if (!this.usuario) {
      this.usuarioService.showError('Error', 'No se encontró información del usuario');
      return;
    }

    // Validaciones
    if (!this.editUsuarioForm.username) {
      this.usuarioService.showError('Error', 'El username es obligatorio');
      return;
    }

    if (!this.editUsuarioForm.nivelId) {
      this.usuarioService.showError('Error', 'Debe seleccionar un nivel');
      return;
    }

    this.isUpdating = true;

    const usuarioUpdate: UsuarioUpdate = {
      username: this.editUsuarioForm.username,
      trabajadorId: this.usuario.trabajadorId,
      nivelId: this.editUsuarioForm.nivelId
    };

    this.usuarioService.updatePerfil(usuarioUpdate).subscribe({
      next: (usuarioActualizado) => {
        this.usuario = usuarioActualizado;
        this.usuarioService.showSuccess('Éxito', 'Configuración de usuario actualizada');
        this.showEditUsuario = false;
        this.isUpdating = false;
      },
      error: (error) => {
        this.usuarioService.showError('Error', 'No se pudo actualizar la configuración');
        console.error('Error actualizando usuario:', error);
        this.isUpdating = false;
      }
    });
  }

  cancelEditUsuario(): void {
    if (this.usuario) {
      this.initializeEditUsuarioForm(this.usuario);
    }
    this.showEditUsuario = false;
  }

  // ========== CAMBIO DE CONTRASEÑA ==========

  openChangePassword(): void {
    this.showChangePassword = true;
    this.changePasswordForm = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  }

  savePassword(): void {
    // Validaciones
    if (!this.changePasswordForm.currentPassword) {
      this.usuarioService.showError('Error', 'La contraseña actual es obligatoria');
      return;
    }

    if (!this.changePasswordForm.newPassword) {
      this.usuarioService.showError('Error', 'La nueva contraseña es obligatoria');
      return;
    }

    if (this.changePasswordForm.newPassword.length < 6) {
      this.usuarioService.showError('Error', 'La nueva contraseña debe tener al menos 6 caracteres');
      return;
    }

    if (this.changePasswordForm.newPassword !== this.changePasswordForm.confirmPassword) {
      this.usuarioService.showError('Error', 'Las contraseñas no coinciden');
      return;
    }

    this.isUpdating = true;

    const passwordData: ChangePasswordRequest = {
      currentPassword: this.changePasswordForm.currentPassword,
      newPassword: this.changePasswordForm.newPassword
    };

    this.usuarioService.changePassword(passwordData).subscribe({
      next: () => {
        this.usuarioService.showSuccess('Éxito', 'Contraseña cambiada correctamente');
        this.showChangePassword = false;
        this.isUpdating = false;
      },
      error: (error) => {
        const errorMessage = error.message || 'No se pudo cambiar la contraseña';
        this.usuarioService.showError('Error', errorMessage);
        console.error('Error cambiando contraseña:', error);
        this.isUpdating = false;
      }
    });
  }

  cancelChangePassword(): void {
    this.showChangePassword = false;
  }

  // ========== MÉTODOS AUXILIARES ==========

  getNombreCompleto(): string {
    if (!this.trabajador) return 'Usuario';
    return `${this.trabajador.nombres} ${this.trabajador.apellidos}`;
  }

  getAvatarText(): string {
    if (!this.trabajador) return 'U';
    return this.usuarioService.getAvatarText(
      this.trabajador.nombres,
      this.trabajador.apellidos
    );
  }

  getNivelNombre(): string {
    if (this.usuario?.nivelNombre) {
      return this.usuario.nivelNombre;
    }
    const nivel = this.niveles.find(n => n.idNivel === this.usuario?.nivelId);
    return nivel?.descripcion || 'Sin nivel';
  }

  getNivelCodigo(): string {
    if (this.usuario?.nivelCodigo) {
      return this.usuario.nivelCodigo;
    }
    const nivel = this.niveles.find(n => n.idNivel === this.usuario?.nivelId);
    return nivel?.codigo || 'N/A';
  }

  formatFecha(fecha: string): string {
    return this.usuarioService.formatFecha(fecha);
  }

  formatFechaRelativa(fecha: string): string {
    return this.usuarioService.formatFechaRelativa(fecha);
  }

  getEdadLaboral(): string {
    if (!this.trabajador?.fechaIngreso) return 'N/A';

    const ingreso = new Date(this.trabajador.fechaIngreso);
    const ahora = new Date();
    const diffMs = ahora.getTime() - ingreso.getTime();
    const diffYears = Math.floor(diffMs / (1000 * 60 * 60 * 24 * 365));

    if (diffYears === 0) {
      const diffMonths = Math.floor(diffMs / (1000 * 60 * 60 * 24 * 30));
      return `${diffMonths} mes${diffMonths !== 1 ? 'es' : ''}`;
    }

    return `${diffYears} año${diffYears !== 1 ? 's' : ''}`;
  }

  validateEmail(email: string): boolean {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  }

  getColorByEstado(activo: boolean): string {
    return activo ? 'success' : 'danger';
  }

  getTextoEstado(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  // ========== PAGINACIÓN ==========

  onPageChange(page: number): void {
    this.currentPage = page;
  }

  onPageSizeChange(size: number): void {
    this.pageSize = size;
    this.currentPage = 0;
    this.totalPages = Math.ceil(this.totalItems / size);
  }

  getPaginatedActividades(): Actividad[] {
    const start = this.currentPage * this.pageSize;
    const end = start + this.pageSize;
    return this.actividades.slice(start, end);
  }

  // ========== SEGURIDAD ==========

  confirmLogout(): void {
    this.usuarioService.showConfirm('Cerrar sesión', '¿Está seguro de que desea cerrar sesión?')
      .then((result) => {
        if (result.isConfirmed) {
          this.logout();
        }
      });
  }

  logout(): void {
    // En producción, llamar al servicio de autenticación
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
    window.location.href = '/login';
  }
}
