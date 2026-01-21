// src/app/core/services/ot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environment';

import { CrearOtCompletaRequest, OtResponse, OtFullDetailResponse, Page } from '../model/ots'; // ← ajusta la carpeta si es necesario

@Injectable({
  providedIn: 'root'
})
export class OtService {
  private apiUrl = `${environment.baseUrl}/api/ots`;

  constructor(private http: HttpClient) {}

  /**
   * Crea o actualiza una OT completa (upsert)
   */
  saveOtCompleta(payload: CrearOtCompletaRequest): Observable<OtResponse> {
    return this.http.post<OtResponse>(`${this.apiUrl}/completa`, payload).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Lista paginada de OTs (básica)
   */
  listarOts(
    activo?: boolean | null,
    page: number = 0,
    size: number = 10,
    sort: string = 'idOts,desc'
  ): Observable<Page<OtResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (activo !== undefined && activo !== null) {
      params = params.set('activo', activo.toString());
    }

    return this.http.get<Page<OtResponse>>(this.apiUrl, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene el detalle COMPLETO de una OT buscando por su número legible (ej: 1456)
   * Este es el endpoint principal cuando el usuario ingresa el número de OT
   */
  getOtByNumeroOt(ot: number): Observable<OtFullDetailResponse> {
    const params = new HttpParams().set('ot', ot.toString());
    return this.http.get<OtFullDetailResponse>(`${this.apiUrl}/by-ot`, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene una OT básica por su ID interno (id_ots)
   */
  getOtById(id: number): Observable<OtResponse> {
    return this.http.get<OtResponse>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene los datos necesarios para editar una OT (solo IDs + campos editables)
   * Útil para precargar un formulario de edición
   */
  getOtParaEdicion(id: number): Observable<OtFullDetailResponse> {   // ← puedes cambiar a OtFullResponse si prefieres la versión sin nombres
    return this.http.get<OtFullDetailResponse>(`${this.apiUrl}/${id}/full`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Alterna activo/inactivo
   */
  toggleEstado(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, {}).pipe(
      catchError(this.handleError)
    );
  }
getOtDetalleCompleto(id: number): Observable<OtFullDetailResponse> {
  return this.http.get<OtFullDetailResponse>(`${this.apiUrl}/${id}`).pipe(
    catchError(this.handleError)
  );
}
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del backend
      errorMessage = error.error?.message || `Error ${error.status}: ${error.statusText}`;
      if (error.status === 404) errorMessage = 'OT no encontrada';
      if (error.status === 400) errorMessage = 'Datos inválidos o incompletos';
    }

    console.error('[OtService]', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
