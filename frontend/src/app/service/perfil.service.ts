// src/app/services/perfil.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { environment } from '../../environment';

// Interfaces
export interface PerfilResponseDTO {
  idUsuario: number;
  username: string;
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  correoCorporativo: string;
  celular: string;
  idNivel: number;
  nivelNombre: string;
  nivelCodigo: string;
  empresaNombre: string;
  areaNombre: string;
  cargoNombre: string;
  activo: boolean;
  fechaCreacion: string;
  ultimaConexion: string;
  totalProyectos: number;
  tareasPendientes: number;
  tareasCompletadas: number;
}

export interface UpdatePerfilRequestDTO {
  nombres: string;
  apellidos: string;
  correoCorporativo: string;
  celular: string;
}

export interface ChangePasswordRequestDTO {
  currentPassword: string;
  newPassword: string;
  confirmPassword: string;
}

export interface UserInfoResponse {
  idUsuario: number;
  username: string;
  nivelCodigo: string;
  nivelNombre: string;
  idTrabajador: number;
}

export interface MessageResponse {
  message: string;
  data: any;
}

@Injectable({
  providedIn: 'root'
})
export class PerfilService {
  private apiUrl = `${environment.baseUrl}/api/perfil`;

  constructor(private http: HttpClient) { }

  // ========== PERFIL ==========

  /**
   * Obtener perfil del usuario autenticado
   */
  getMiPerfil(): Observable<PerfilResponseDTO> {
    return this.http.get<PerfilResponseDTO>(`${this.apiUrl}/me`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Actualizar datos personales
   */
  updateMiPerfil(data: UpdatePerfilRequestDTO): Observable<PerfilResponseDTO> {
    return this.http.put<PerfilResponseDTO>(`${this.apiUrl}/me`, data)
      .pipe(catchError(this.handleError));
  }

  /**
   * Cambiar contraseña
   */
  cambiarPassword(data: ChangePasswordRequestDTO): Observable<MessageResponse> {
    return this.http.patch<MessageResponse>(`${this.apiUrl}/me/cambiar-password`, data)
      .pipe(catchError(this.handleError));
  }

  /**
   * Obtener información básica del usuario
   */
  getInfoUsuario(): Observable<UserInfoResponse> {
    return this.http.get<UserInfoResponse>(`${this.apiUrl}/me/info`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Actualizar última conexión
   */
  updateUltimaConexion(): Observable<PerfilResponseDTO> {
    return this.http.post<PerfilResponseDTO>(`${this.apiUrl}/me/actualizar-conexion`, {})
      .pipe(catchError(this.handleError));
  }

  // ========== MÉTODOS AUXILIARES ==========

  /**
   * Manejo de errores HTTP
   */
  private handleError(error: any) {
    console.error('Error en servicio Perfil:', error);

    let errorMessage = 'Ocurrió un error inesperado';

    if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.status === 404) {
      errorMessage = 'Recurso no encontrado';
    } else if (error.status === 400) {
      errorMessage = 'Solicitud incorrecta';
    } else if (error.status === 401) {
      errorMessage = 'No autorizado';
    } else if (error.status === 403) {
      errorMessage = 'Acceso denegado';
    } else if (error.status === 500) {
      errorMessage = 'Error interno del servidor';
    }

    this.showError('Error', errorMessage);
    return throwError(() => new Error(errorMessage));
  }

  // ========== HELPERS UI ==========

  showSuccess(title: string, message: string): void {
    Swal.fire({
      icon: 'success',
      title: title,
      text: message,
      timer: 3000,
      showConfirmButton: false,
      position: 'top-end',
      toast: true,
      background: '#10b981',
      color: 'white'
    });
  }

  showError(title: string, message: string): void {
    Swal.fire({
      icon: 'error',
      title: title,
      text: message,
      confirmButtonColor: '#ef4444'
    });
  }

  showConfirm(title: string, message: string): Promise<any> {
    return Swal.fire({
      title: title,
      text: message,
      icon: 'question',
      showCancelButton: true,
      confirmButtonColor: '#0ea5e9',
      cancelButtonColor: '#6b7280',
      confirmButtonText: 'Sí, continuar',
      cancelButtonText: 'Cancelar'
    });
  }

  /**
   * Validar fortaleza de contraseña
   */
  validatePasswordStrength(password: string): { valid: boolean; message: string } {
    if (!password) {
      return { valid: false, message: 'La contraseña es requerida' };
    }

    if (password.length < 6) {
      return { valid: false, message: 'La contraseña debe tener al menos 6 caracteres' };
    }

    if (password.length > 100) {
      return { valid: false, message: 'La contraseña no puede exceder 100 caracteres' };
    }

    const hasUpperCase = /[A-Z]/.test(password);
    const hasLowerCase = /[a-z]/.test(password);
    const hasNumbers = /\d/.test(password);
    const hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);

    if (!hasUpperCase) {
      return { valid: false, message: 'Debe contener al menos una mayúscula' };
    }

    if (!hasLowerCase) {
      return { valid: false, message: 'Debe contener al menos una minúscula' };
    }

    if (!hasNumbers) {
      return { valid: false, message: 'Debe contener al menos un número' };
    }

    if (!hasSpecialChar) {
      return { valid: false, message: 'Debe contener al menos un carácter especial' };
    }

    return { valid: true, message: 'Contraseña válida' };
  }

  /**
   * Validar formato de email
   */
  validateEmail(email: string): { valid: boolean; message: string } {
    if (!email) {
      return { valid: false, message: 'El email es requerido' };
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      return { valid: false, message: 'Debe ser un email válido' };
    }

    return { valid: true, message: 'Email válido' };
  }

  /**
   * Validar formato de celular
   */
  validateCelular(celular: string): { valid: boolean; message: string } {
    if (!celular) {
      return { valid: false, message: 'El celular es requerido' };
    }

    const celularRegex = /^[0-9]{9}$/;
    if (!celularRegex.test(celular)) {
      return { valid: false, message: 'El celular debe tener 9 dígitos' };
    }

    return { valid: true, message: 'Celular válido' };
  }

  /**
   * Formatear fecha
   */
  formatFecha(fecha: string): string {
    if (!fecha) return '';

    try {
      const date = new Date(fecha);
      return date.toLocaleDateString('es-ES', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    } catch (error) {
      return fecha;
    }
  }

  /**
   * Formatear fecha relativa
   */
  formatFechaRelativa(fecha: string): string {
    if (!fecha) return '';

    try {
      const date = new Date(fecha);
      const now = new Date();
      const diffMs = now.getTime() - date.getTime();
      const diffDays = Math.floor(diffMs / (1000 * 60 * 60 * 24));

      if (diffDays === 0) {
        const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
        if (diffHours === 0) {
          const diffMinutes = Math.floor(diffMs / (1000 * 60));
          return `${diffMinutes} min atrás`;
        }
        return `${diffHours} horas atrás`;
      } else if (diffDays === 1) {
        return 'Ayer';
      } else if (diffDays < 7) {
        return `${diffDays} días atrás`;
      } else if (diffDays < 30) {
        const weeks = Math.floor(diffDays / 7);
        return `${weeks} semana${weeks > 1 ? 's' : ''} atrás`;
      } else {
        return this.formatFecha(fecha);
      }
    } catch (error) {
      return fecha;
    }
  }

  /**
   * Obtener clase CSS para el nivel
   */
  getNivelBadgeClass(codigo?: string): string {
    if (!codigo) return 'badge bg-secondary';

    switch (codigo.toUpperCase()) {
      case 'ADMIN': return 'badge bg-danger';
      case 'SUPERVISOR': return 'badge bg-warning';
      case 'TECNICO': return 'badge bg-info';
      case 'OPERADOR': return 'badge bg-primary';
      case 'GESTOR': return 'badge bg-purple';
      case 'AUDITOR': return 'badge bg-indigo';
      default: return 'badge bg-secondary';
    }
  }

  /**
   * Obtener icono para el nivel
   */
  getNivelIcon(codigo?: string): string {
    if (!codigo) return 'bi-person';

    switch (codigo.toUpperCase()) {
      case 'ADMIN': return 'bi-shield-check';
      case 'SUPERVISOR': return 'bi-eye';
      case 'TECNICO': return 'bi-tools';
      case 'OPERADOR': return 'bi-person-workspace';
      case 'GESTOR': return 'bi-clipboard-data';
      case 'AUDITOR': return 'bi-search';
      default: return 'bi-person';
    }
  }
}
