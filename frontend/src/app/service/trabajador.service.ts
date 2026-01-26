import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import Swal from 'sweetalert2';
import { environment } from '../../environment';

export interface Trabajador {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  empresaId?: number;
  empresaNombre?: string;
  areaId: number;
  areaNombre: string;
  cargoId: number;
  cargoNombre: string;
  activo: boolean;
  fechaCreacion: string;
}

export interface TrabajadorRequest {
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  areaId: number;
  cargoId: number;
  empresaId?: number;
  activo?: boolean;
}

export interface TrabajadorUpdate {
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  areaId: number;
  cargoId: number;
  empresaId?: number;
}

export interface Area {
  idArea: number;
  nombre: string;
  activo: boolean;
}

export interface Cargo {
  idCargo: number;
  nombre: string;
  nivelId: number;
  activo: boolean;
}

export interface Empresa {
  idEmpresa: number;
  nombre: string;
  activo: boolean;
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
  areaId?: number;
  cargoId?: number;
  empresaId?: number;
}

@Injectable({
  providedIn: 'root'
})
export class TrabajadorService {
  private dropdownApiUrl = `${environment.baseUrl}/api/dropdown`;
  private apiUrl = `${environment.baseUrl}/api/trabajadores`;

  constructor(private http: HttpClient) { }

  // Obtener todos los trabajadores con paginación
  getTrabajadores(params: PaginationParams): Observable<PageResponse<Trabajador>> {
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
    if (params.areaId) {
      httpParams = httpParams.set('areaId', params.areaId.toString());
    }
    if (params.cargoId) {
      httpParams = httpParams.set('cargoId', params.cargoId.toString());
    }
    if (params.empresaId) {
      httpParams = httpParams.set('empresaId', params.empresaId.toString());
    }

    return this.http.get<PageResponse<Trabajador>>(this.apiUrl, { params: httpParams })
      .pipe(
        catchError(this.handleError)
      );
  }

  // Obtener trabajador por ID
  getTrabajadorById(id: number): Observable<Trabajador> {
    return this.http.get<Trabajador>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Crear nuevo trabajador
  createTrabajador(trabajador: TrabajadorRequest): Observable<Trabajador> {
    return this.http.post<Trabajador>(this.apiUrl, trabajador)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Actualizar trabajador
  updateTrabajador(id: number, trabajador: TrabajadorUpdate): Observable<Trabajador> {
    return this.http.put<Trabajador>(`${this.apiUrl}/${id}`, trabajador)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Activar/Desactivar trabajador
  toggleActivo(id: number): Observable<Trabajador> {
    return this.http.patch<Trabajador>(`${this.apiUrl}/${id}/toggle-activo`, {})
      .pipe(
        catchError(this.handleError)
      );
  }

  // Buscar por DNI
  searchByDni(dni: string): Observable<Trabajador> {
    return this.http.get<Trabajador>(`${this.apiUrl}/buscar/dni`, {
      params: { dni }
    }).pipe(
      catchError(this.handleError)
    );
  }

  // Obtener estadísticas
  getEstadisticas(): Observable<any> {
    return this.http.get(`${this.apiUrl}/estadisticas`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Manejo de errores
  private handleError(error: any) {
    console.error('Error en servicio Trabajador:', error);

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

  // Formatear fecha
  formatFecha(fecha: string): string {
    if (!fecha) return '';
    const date = new Date(fecha);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // Obtener nombre completo
  getNombreCompleto(trabajador: Trabajador): string {
    return `${trabajador.nombres} ${trabajador.apellidos}`.trim();
  }
}
