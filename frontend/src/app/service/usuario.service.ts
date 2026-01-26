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
  trabajadorNombre?: string;
  trabajadorApellidos?: string;
  trabajadorDNI?: string;
  nivelCodigo?: string;
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

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.baseUrl}/api/usuarios`;

  constructor(private http: HttpClient) { }

  // Obtener todos los usuarios con paginación
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

  // Obtener usuario por ID
  getUsuarioById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Crear nuevo usuario
  createUsuario(usuario: UsuarioRequest): Observable<Usuario> {
    return this.http.post<Usuario>(this.apiUrl, usuario)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Actualizar usuario
  updateUsuario(id: number, usuario: UsuarioUpdate): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.apiUrl}/${id}`, usuario)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Cambiar contraseña
  changePassword(id: number, data: ChangePasswordRequest): Observable<any> {
    return this.http.patch(`${this.apiUrl}/${id}/cambiar-password`, data)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Activar/Desactivar usuario
  toggleActivo(id: number): Observable<Usuario> {
    return this.http.patch<Usuario>(`${this.apiUrl}/${id}/toggle-activo`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  // Eliminar usuario
  deleteUsuario(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Buscar por username
  searchByUsername(username: string): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.apiUrl}/buscar`, {
      params: { username }
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Manejo de errores
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

    Swal.fire('Error', errorMessage, 'error');
    return throwError(() => new Error(errorMessage));
  }

  // Mostrar mensaje de éxito
  showSuccess(message: string): void {
    Swal.fire({
      icon: 'success',
      title: 'Éxito',
      text: message,
      timer: 2000,
      showConfirmButton: false
    });
  }

  // Mostrar confirmación
  showConfirm(message: string): Promise<any> {
    return Swal.fire({
      title: '¿Estás seguro?',
      text: message,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Sí, continuar',
      cancelButtonText: 'Cancelar'
    });
  }
}
