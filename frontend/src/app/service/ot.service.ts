// src/app/core/services/ot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

// Interfaces / modelos (ajusta según tu estructura real)
import { environment } from '../../environment';
import { CrearOtCompletaRequest, OtResponse, OtFullResponse, Page } from '../model/ots'; // o la carpeta donde los tengas


@Injectable({
  providedIn: 'root'
})
export class OtService {
  private apiUrl = `${environment.baseUrl}/api/ots`;

  constructor(private http: HttpClient) {}

  /**
   * Crea o actualiza una OT completa (upsert)
   * - Si request.ot.idOts existe → edición
   * - Si no → creación nueva (genera ot secuencial en backend)
   */
  saveOtCompleta(payload: CrearOtCompletaRequest): Observable<OtResponse> {
    return this.http.post<OtResponse>(`${this.apiUrl}/completa`, payload).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Lista OTs con filtros flexibles
   * @param ot - número de OT exacto (opcional)
   * @param activo - true = activas, false = inactivas, null/undefined = todas
   * @param page - página (0-based)
   * @param size - registros por página
   * @param sort - formato "campo,direccion" ej: "ot,asc" o "fechaCreacion,desc"
   */
  listarOts(
    ot?: number,
    activo?: boolean | null,
    page: number = 0,
    size: number = 10,
    sort: string = 'idOts,desc'
  ): Observable<Page<OtResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (ot !== undefined && ot !== null) {
      params = params.set('ot', ot.toString());
    }
    if (activo !== undefined && activo !== null) {
      params = params.set('activo', activo.toString());
    }
    // Si activo es null/undefined → no se envía → backend devuelve todas

    return this.http.get<Page<OtResponse>>(this.apiUrl, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene una OT básica por su ID interno (id_ots)
   * Endpoint: GET /api/ots/{id}
   */
  obtenerPorId(id: number): Observable<OtResponse> {
    return this.http.get<OtResponse>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene TODA la información necesaria para editar una OT
   * (IDs de selects, descripción, fecha, responsables, etc.)
   * Endpoint: GET /api/ots/{id}/full
   */
  obtenerOtParaEdicion(id: number): Observable<OtFullResponse> {
    return this.http.get<OtFullResponse>(`${this.apiUrl}/${id}/full`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Alterna el estado activo/inactivo de una OT
   * Endpoint: POST /api/ots/{id}/toggle
   */
  toggleEstado(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, {}).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Manejo centralizado de errores HTTP
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del cliente (network, etc.)
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del servidor
      errorMessage = error.error?.message || `Error ${error.status}: ${error.statusText}`;

      // Puedes personalizar mensajes conocidos
      if (error.status === 404) {
        errorMessage = 'Recurso no encontrado';
      } else if (error.status === 400) {
        errorMessage = 'Datos inválidos';
      }
    }

    console.error('[OtService]', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
