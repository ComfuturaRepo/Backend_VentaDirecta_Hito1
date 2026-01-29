// src/app/core/services/ot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { OtCreateRequest, OtDetailResponse, OtListDto, Page } from '../model/ots';
import { environment } from '../../environment';
import { PageResponse } from '../model/page.interface';

@Injectable({
  providedIn: 'root'
})
export class OtService {
  private apiUrl = `${environment.baseUrl}/api/ots`;

  constructor(private http: HttpClient) {}

  /**
   * Crea o actualiza una OT
   * Endpoint: POST /api/ots
   * Devuelve detalle completo después de guardar
   */
  saveOt(payload: OtCreateRequest): Observable<OtDetailResponse> {
    return this.http.post<OtDetailResponse>(this.apiUrl, payload).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Lista paginada de OTs con filtro de texto
   * Endpoint: GET /api/ots?search=texto&page=0&size=10&sort=ot,desc
   */
  listarOts(
    search?: string,
    page: number = 0,
    size: number = 10,
    sort: string = 'ot,desc'
  ): Observable<PageResponse<OtListDto>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }

    return this.http.get<PageResponse<OtListDto>>(this.apiUrl, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene detalle completo por ID interno
   * Endpoint: GET /api/ots/{id}/detail
   */
  getOtById(id: number): Observable<OtDetailResponse> {
    return this.http.get<OtDetailResponse>(`${this.apiUrl}/${id}/detail`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene detalle completo por número OT legible (ej: 20250001)
   * Endpoint: GET /api/ots/numero/{numeroOt}/detail
   */
  getOtByNumeroOt(numeroOt: number): Observable<OtDetailResponse> {
    return this.http.get<OtDetailResponse>(`${this.apiUrl}/numero/${numeroOt}/detail`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Alterna activo ↔ inactivo
   * Endpoint: PATCH /api/ots/{id}/toggle-activo
   */
  toggleActivo(id: number): Observable<void> {
    return this.http.patch<void>(`${this.apiUrl}/${id}/toggle-activo`, {}).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene datos para formulario de edición (solo IDs + básicos)
   * Endpoint: GET /api/ots/{id}/edit
   * Retorna OtCreateRequest directamente
   */
  getOtParaEdicion(id: number): Observable<OtCreateRequest> {
    return this.http.get<OtCreateRequest>(`${this.apiUrl}/${id}/edit`).pipe(
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
      if (error.status === 403) errorMessage = 'No tienes permisos para esta acción';
    }

    console.error('[OtService]', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
