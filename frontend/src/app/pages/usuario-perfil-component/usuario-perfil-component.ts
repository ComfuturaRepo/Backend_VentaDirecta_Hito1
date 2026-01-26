import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { Usuario, UsuarioService } from '../../service/usuario.service';

@Component({
  selector: 'app-usuario-perfil-component',
  imports: [CommonModule, FormsModule],
  templateUrl: './usuario-perfil-component.html',
  styleUrls: ['./usuario-perfil-component.css']
})
export class UsuarioPerfilComponent implements OnInit {
  // Datos del usuario
  usuario: Usuario | null = null;

  // Información personal
  personalInfo: any = {
    nombre: 'Juan Pérez',
    email: 'juan.perez@empresa.com',
    telefono: '+51 987 654 321',
    direccion: 'Av. Principal 123, Lima',
    fechaIngreso: '2023-01-15',
    cargo: 'Desarrollador Senior',
    area: 'TI',
    cliente: 'Cliente Principal'
  };

  // Formularios
  changePasswordForm: any = {
    currentPassword: '',
    newPassword: '',
    confirmPassword: ''
  };

  editProfileForm: any = {
    email: '',
    telefono: '',
    direccion: ''
  };

  // Estados
  isLoading: boolean = false;
  showChangePassword: boolean = false;
  showEditProfile: boolean = false;

  // Estadísticas
  estadisticas = {
    otsCompletadas: 15,
    otsPendientes: 3,
    proyectosActivos: 5,
    diasTrabajados: 120
  };

  constructor(private usuarioService: UsuarioService) {}

  ngOnInit(): void {
    this.loadUserProfile();
    this.loadUserData();
  }

  // Cargar perfil del usuario actual (ejemplo con ID 1)
  loadUserProfile(): void {
    this.isLoading = true;

    // En una aplicación real, obtendrías el ID del usuario logueado
    const userId = 1; // Ejemplo

    this.usuarioService.getUsuarioById(userId).subscribe({
      next: (response) => {
        this.usuario = response;
        this.isLoading = false;

        // Inicializar formulario de edición
        this.editProfileForm = {
          email: this.personalInfo.email,
          telefono: this.personalInfo.telefono,
          direccion: this.personalInfo.direccion
        };
      },
      error: (error) => {
        console.error('Error al cargar perfil:', error);
        this.isLoading = false;
      }
    });
  }

  // Cargar datos adicionales (simulado)
  loadUserData(): void {
    // En una aplicación real, harías llamadas adicionales a servicios
    // para obtener información del trabajador, proyectos, etc.
  }

  // Cambiar contraseña
  cambiarContrasena(): void {
    // Validaciones
    if (!this.changePasswordForm.currentPassword ||
        !this.changePasswordForm.newPassword ||
        !this.changePasswordForm.confirmPassword) {
      Swal.fire('Error', 'Todos los campos son obligatorios', 'warning');
      return;
    }

    if (this.changePasswordForm.newPassword !== this.changePasswordForm.confirmPassword) {
      Swal.fire('Error', 'Las contraseñas no coinciden', 'warning');
      return;
    }

    if (this.changePasswordForm.newPassword.length < 6) {
      Swal.fire('Error', 'La contraseña debe tener al menos 6 caracteres', 'warning');
      return;
    }

    // En una aplicación real, usarías el ID del usuario logueado
    const userId = this.usuario?.idUsuario || 1;

    this.usuarioService.changePassword(userId, {
      currentPassword: this.changePasswordForm.currentPassword,
      newPassword: this.changePasswordForm.newPassword
    }).subscribe({
      next: (response) => {
        this.usuarioService.showSuccess('Contraseña cambiada exitosamente');
        this.cancelarCambioContrasena();
      },
      error: (error) => {
        console.error('Error al cambiar contraseña:', error);
      }
    });
  }

  // Guardar cambios del perfil
  guardarPerfil(): void {
    // Validaciones
    if (!this.editProfileForm.email) {
      Swal.fire('Error', 'El email es obligatorio', 'warning');
      return;
    }

    // En una aplicación real, aquí harías una llamada al backend
    this.personalInfo.email = this.editProfileForm.email;
    this.personalInfo.telefono = this.editProfileForm.telefono;
    this.personalInfo.direccion = this.editProfileForm.direccion;

    this.usuarioService.showSuccess('Perfil actualizado exitosamente');
    this.cancelarEdicionPerfil();
  }

  // Cancelar edición de perfil
  cancelarEdicionPerfil(): void {
    this.showEditProfile = false;
    this.editProfileForm = {
      email: this.personalInfo.email,
      telefono: this.personalInfo.telefono,
      direccion: this.personalInfo.direccion
    };
  }

  // Cancelar cambio de contraseña
  cancelarCambioContrasena(): void {
    this.showChangePassword = false;
    this.changePasswordForm = {
      currentPassword: '',
      newPassword: '',
      confirmPassword: ''
    };
  }

  // Formatear fecha
  formatFecha(fecha: string): string {
    return new Date(fecha).toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  // Obtener iniciales para avatar
  getIniciales(): string {
    if (!this.personalInfo.nombre) return 'U';
    const nombres = this.personalInfo.nombre.split(' ');
    return (nombres[0].charAt(0) + (nombres[1] ? nombres[1].charAt(0) : '')).toUpperCase();
  }
}
