// src/app/core/services/ot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environment';  // Ajusta la ruta si es necesario
import { CrearOtCompletaRequest, OtResponse, Page } from '../model/ots';


@Injectable({
  providedIn: 'root'
})
export class OtService {
  private apiUrl = `${environment.baseUrl}/api/ots`;

  constructor(private http: HttpClient) {}

  /**
   * Crea una OT completa (con trabajadores y detalles opcionales)
   */
  crearOtCompleta(payload: CrearOtCompletaRequest): Observable<OtResponse> {
    return this.http.post<OtResponse>(`${this.apiUrl}/completa`, payload).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene el listado paginado de OT filtrando por estado activo/inactivo
   * @param activo true = activas, false = inactivas
   * @param page número de página (0-based)
   * @param size registros por página
   */
  listarOts(activo: boolean, page: number = 0, size: number = 10): Observable<Page<OtResponse>> {
    let params = new HttpParams()
      .set('activo', activo.toString())
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<Page<OtResponse>>(this.apiUrl, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene una OT por su ID
   */
  obtenerOtPorId(id: number): Observable<OtResponse> {
    return this.http.get<OtResponse>(`${this.apiUrl}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Manejo centralizado de errores HTTP
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del backend
      errorMessage = error.error?.message || `Error ${error.status}: ${error.statusText}`;
    }

    console.error('[OtService]', errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
