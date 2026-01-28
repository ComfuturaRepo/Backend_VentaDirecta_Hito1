// src/app/services/usuario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { environment } from '../../environment';

export interface Usuario {
  idUsuario: number;
  username: string;
  password?: string;
  trabajadorId: number;
  nivelId: number;
  activo: boolean;
  fechaCreacion: string;
  nombreTrabajador?: string;
  nivelNombre?: string;
  nivelCodigo?: string;
  trabajadorNombre?: string;
  trabajadorApellidos?: string;
  trabajadorDNI?: string;
  trabajadorEmail?: string;
  trabajadorTelefono?: string;
  trabajadorDireccion?: string;
  trabajadorCargo?: string;
  trabajadorArea?: string;
  trabajadorFechaIngreso?: string;
  trabajadorClienteAsignado?: string;
}

export interface Trabajador {
  idTrabajador: number;
  dni: string;
  nombres: string;
  apellidos: string;
  email: string;
  telefono: string;
  direccion: string;
  cargo: string;
  area: string;
  fechaIngreso: string;
  clienteAsignado: string;
  activo: boolean;
}

export interface Nivel {
  idNivel: number;
  codigo: string;
  descripcion: string;
  activo: boolean;
}

export interface UsuarioRequest {
  username: string;
  password: string;
  trabajadorId: number;
  nivelId: number;
}

export interface UsuarioUpdate {
  username: string;
  trabajadorId: number;
  nivelId: number;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
  confirmPassword?:string;
}

export interface PageResponse<T> {
  content: T[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  pageSize: number;
}

export interface PaginationParams {
  page: number;
  size: number;
  sortBy: string;
  direction: string;
  search?: string;
  activos?: boolean;
  nivelId?: number;
}

export interface EstadisticasUsuario {
  otsCompletadas: number;
  otsPendientes: number;
  proyectosActivos: number;
  diasTrabajados: number;
  ultimaConexion?: string;
  horasTrabajadasMes: number;
  tareasCompletadas: number;
}

export interface Actividad {
  id: number;
  tipo: string;
  titulo: string;
  descripcion: string;
  fecha: string;
  icono: string;
  color: string;
}

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.baseUrl}/api/usuarios`;
  private trabajadorUrl = `${environment.baseUrl}/api/trabajadores`;

  constructor(private http: HttpClient) { }

  // ========== USUARIOS ==========

  getUsuarios(params: PaginationParams): Observable<PageResponse<Usuario>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString())
      .set('sortBy', params.sortBy)
      .set('direction', params.direction);

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.activos !== undefined) {
      httpParams = httpParams.set('activos', params.activos.toString());
    }
    if (params.nivelId) {
      httpParams = httpParams.set('nivelId', params.nivelId.toString());
    }

    return this.http.get<PageResponse<Usuario>>(this.apiUrl, { params: httpParams })
      .pipe(
        catchError(this.handleError)
      );
  }

  getUsuarioById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getPerfilUsuario(): Observable<Usuario> {
    // En producción, obtener ID del usuario autenticado
    const userId = this.getUsuarioIdFromStorage();
    return this.getUsuarioById(userId);
  }

  getUsuarioByUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/buscar`, {
      params: { username }
    }).pipe(
      catchError(this.handleError)
    );
  }

  createUsuario(usuario: UsuarioRequest): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateUsuario(id: number, usuario: UsuarioUpdate): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuario)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateUsuarioCompleto(id: number, usuario: UsuarioRequest): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}/completo`, usuario)
      .pipe(
        catchError(this.handleError)
      );
  }

  updatePerfil(usuario: UsuarioUpdate): Observable<Usuario> {
    const userId = this.getUsuarioIdFromStorage();
    return this.updateUsuario(userId, usuario);
  }

  changePassword(data: ChangePasswordRequest): Observable<any> {
    const userId = this.getUsuarioIdFromStorage();
    return this.http.patch(`${this.apiUrl}/${userId}/cambiar-password`, data)
      .pipe(
        catchError(this.handleError)
      );
  }

  toggleActivo(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.apiUrl}/${id}/toggle-activo`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // ========== TRABAJADORES ==========

  getTrabajadorById(id: number): Observable<Trabajador> {
    return this.http.get<Trabajador>(`${this.trabajadorUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateTrabajador(id: number, trabajador: Partial<Trabajador>): Observable<Trabajador> {
    return this.http.put<Trabajador>(`${this.trabajadorUrl}/${id}`, trabajador)
      .pipe(
        catchError(this.handleError)
      );
  }


  // ========== ESTADÍSTICAS ==========

  getEstadisticasUsuario(userId: number): Observable<EstadisticasUsuario> {
    // Simular estadísticas (en producción sería un endpoint real)
    const estadisticas: EstadisticasUsuario = {
      otsCompletadas: Math.floor(Math.random() * 20) + 5,
      otsPendientes: Math.floor(Math.random() * 10) + 1,
      proyectosActivos: Math.floor(Math.random() * 8) + 1,
      diasTrabajados: Math.floor(Math.random() * 365) + 100,
      ultimaConexion: new Date().toISOString(),
      horasTrabajadasMes: Math.floor(Math.random() * 160) + 80,
      tareasCompletadas: Math.floor(Math.random() * 50) + 20
    };

    return new Observable(observer => {
      setTimeout(() => {
        observer.next(estadisticas);
        observer.complete();
      }, 300);
    });
  }

  // ========== ACTIVIDADES ==========

  getActividadesUsuario(userId: number): Observable<Actividad[]> {
    const actividades: Actividad[] = [
      {
        id: 1,
        tipo: 'OT',
        titulo: 'OT #2024-001 completada',
        descripcion: 'Finalizaste la orden de trabajo para instalación de servidores',
        fecha: new Date(Date.now() - 2 * 60 * 60 * 1000).toISOString(),
        icono: 'bi-check-circle',
        color: 'success'
      },
      {
        id: 2,
        tipo: 'Proyecto',
        titulo: 'Nuevo proyecto asignado',
        descripcion: 'Te asignaron el proyecto "Modernización de Infraestructura"',
        fecha: new Date(Date.now() - 24 * 60 * 60 * 1000).toISOString(),
        icono: 'bi-folder-plus',
        color: 'info'
      },
      {
        id: 3,
        tipo: 'Perfil',
        titulo: 'Actualización de perfil',
        descripcion: 'Actualizaste tu información de contacto',
        fecha: new Date(Date.now() - 2 * 24 * 60 * 60 * 1000).toISOString(),
        icono: 'bi-person-badge',
        color: 'warning'
      },
      {
        id: 4,
        tipo: 'Reunión',
        titulo: 'Reunión de equipo',
        descripcion: 'Participaste en la reunión de revisión de proyectos Q1',
        fecha: new Date(Date.now() - 7 * 24 * 60 * 60 * 1000).toISOString(),
        icono: 'bi-calendar-check',
        color: 'primary'
      },
      {
        id: 5,
        tipo: 'Capacitación',
        titulo: 'Capacitación completada',
        descripcion: 'Completaste el curso de Seguridad Informática',
        fecha: new Date(Date.now() - 10 * 24 * 60 * 60 * 1000).toISOString(),
        icono: 'bi-award',
        color: 'purple'
      }
    ];

    return new Observable(observer => {
      setTimeout(() => {
        observer.next(actividades);
        observer.complete();
      }, 300);
    });
  }

  // ========== MÉTODOS AUXILIARES ==========

  private getUsuarioIdFromStorage(): number {
    // En producción, obtener del localStorage o AuthService
    const userData = localStorage.getItem('usuario');
    if (userData) {
      try {
        const usuario = JSON.parse(userData);
        return usuario.idUsuario || 1;
      } catch (error) {
        return 1;
      }
    }
    return 1; // Default para desarrollo
  }

  // ========== MANEJO DE ERRORES ==========

  private handleError(error: any) {
    console.error('Error en servicio Usuario:', error);

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
    }

    return throwError(() => new Error(errorMessage));
  }

  // ========== HELPERS ==========

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
      color: 'white',
      showClass: {
        popup: 'animate__animated animate__fadeInDown'
      },
      hideClass: {
        popup: 'animate__animated animate__fadeOutUp'
      }
    });
  }

  showError(title: string, message: string): void {
    Swal.fire({
      icon: 'error',
      title: title,
      text: message,
      confirmButtonColor: '#ef4444',
      background: '#1f2937',
      color: 'white',
      showClass: {
        popup: 'animate__animated animate__shakeX'
      }
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
      cancelButtonText: 'Cancelar',
      background: '#1f2937',
      color: 'white',
      showClass: {
        popup: 'animate__animated animate__zoomIn'
      }
    });
  }

  getAvatarText(nombre: string, apellidos: string): string {
    if (!nombre || !apellidos) return 'U';
    return (nombre.charAt(0) + apellidos.charAt(0)).toUpperCase();
  }

  formatFecha(fecha: string): string {
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  }

  formatFechaRelativa(fecha: string): string {
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
  }
}
