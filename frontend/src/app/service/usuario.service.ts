// src/app/services/usuario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { environment } from '../../environment';
import { PageResponse, Trabajador } from './trabajador.service';
import {  PaginationParams, UsuarioDetail, UsuarioRequest, UsuarioSimple, UsuarioUpdate } from '../model/usuario';


@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  private apiUrl = `${environment.baseUrl}/api/usuarios`;
  private trabajadorUrl = `${environment.baseUrl}/api/trabajadores`;

  constructor(private http: HttpClient) { }

  // ========== PAGINACIÓN CON FILTROS ==========

  /**
   * Obtener todos los usuarios con paginación y filtros
   */
  getUsuarios(params: PaginationParams): Observable<PageResponse<UsuarioSimple>> {
    let httpParams = new HttpParams()
      .set('page', params.page.toString())
      .set('size', params.size.toString())
      .set('sortBy', params.sortBy || 'idUsuario')
      .set('direction', params.direction || 'asc');

    if (params.search) {
      httpParams = httpParams.set('search', params.search);
    }
    if (params.activos !== undefined) {
      httpParams = httpParams.set('activos', params.activos.toString());
    }
    if (params.nivelId) {
      httpParams = httpParams.set('nivelId', params.nivelId.toString());
    }

    return this.http.get<PageResponse<UsuarioSimple>>(this.apiUrl, { params: httpParams })
      .pipe(catchError(this.handleError));
  }

  // ========== CRUD ==========

  /**
   * Obtener usuario por ID
   */
  getUsuarioById(id: number): Observable<UsuarioDetail> {
    return this.http.get<UsuarioDetail>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  /**
   * Crear nuevo usuario
   */
  createUsuario(usuario: UsuarioRequest): Observable<UsuarioDetail> {
    return this.http.post<UsuarioDetail>(this.apiUrl, usuario)
      .pipe(catchError(this.handleError));
  }

  /**
   * Actualizar usuario (parcial)
   */
  updateUsuario(id: number, usuario: UsuarioUpdate): Observable<UsuarioDetail> {
    return this.http.put<UsuarioDetail>(`${this.apiUrl}/${id}`, usuario)
      .pipe(catchError(this.handleError));
  }

  /**
   * Actualizar usuario completo (incluye contraseña)
   */
  updateUsuarioCompleto(id: number, usuario: UsuarioRequest): Observable<UsuarioDetail> {
    return this.http.put<UsuarioDetail>(`${this.apiUrl}/${id}/completo`, usuario)
      .pipe(catchError(this.handleError));
  }

  /**
   * Activar/desactivar usuario
   */
  toggleActivo(id: number): Observable<UsuarioDetail> {
    return this.http.patch<UsuarioDetail>(`${this.apiUrl}/${id}/toggle-activo`, {})
      .pipe(catchError(this.handleError));
  }

  // ========== HELPERS UI ==========

  private handleError(error: any) {
    console.error('Error en servicio Usuario:', error);

    let errorMessage = 'Ocurrió un error inesperado';

    if (error.error?.message) {
      errorMessage = error.error.message;
    } else if (error.status === 404) {
      errorMessage = 'Recurso no encontrado';
    } else if (error.status === 400) {
      errorMessage = 'Solicitud incorrecta';
    } else if (error.status === 409) {
      errorMessage = 'El nombre de usuario ya está en uso';
    }

    this.showError('Error', errorMessage);
    return throwError(() => new Error(errorMessage));
  }

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

  getEstadoBadgeClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  getEstadoText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
  }

  getNivelBadgeClass(codigo?: string): string {
    if (!codigo) return 'badge bg-secondary';

    switch (codigo.toUpperCase()) {
      case 'ADMIN': return 'badge bg-danger';
      case 'SUPERVISOR': return 'badge bg-warning';
      case 'TECNICO': return 'badge bg-info';
      case 'OPERADOR': return 'badge bg-primary';
      default: return 'badge bg-secondary';
    }
  }
}
