// src/app/services/orden-compra.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Page } from '../model/page.interface';
import { environment } from '../../environment';
import { OcDetalleResponse, OrdenCompraRequest, OrdenCompraResponse, PageOrdenCompra } from '../model/orden-compra.model';

@Injectable({
  providedIn: 'root'
})
export class OrdenCompraService {

  private apiUrl = `${environment.baseUrl}/api/ordenes-compra`; // ej: http://localhost:8080/api/ordenes-compra
  private apiDetalleUrl = `${environment.baseUrl}/api/oc-detalles`;

  constructor(private http: HttpClient) {}

  // Crear nueva orden de compra
  crear(request: OrdenCompraRequest): Observable<OrdenCompraResponse> {
    return this.http.post<OrdenCompraResponse>(this.apiUrl, request)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Actualizar orden de compra
  actualizar(id: number, request: OrdenCompraRequest): Observable<OrdenCompraResponse> {
    return this.http.put<OrdenCompraResponse>(`${this.apiUrl}/${id}`, request)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Obtener una orden por ID
  obtenerPorId(id: number): Observable<OrdenCompraResponse> {
    return this.http.get<OrdenCompraResponse>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  // Listar con paginación
  listar(page: number = 0, size: number = 10): Observable<PageOrdenCompra> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageOrdenCompra>(this.apiUrl, { params })
      .pipe(
        catchError(this.handleError)
      );
  }

 // ───────────────────────────────
  // NUEVO: obtener detalles de una OC
obtenerDetallesPorOc(idOc: number): Observable<Page<OcDetalleResponse>> {
  return this.http.get<Page<OcDetalleResponse>>(
`${this.apiDetalleUrl}/${idOc}`,
    this.getAuthHeaders()
  );
}

private getAuthHeaders() {
  const token = localStorage.getItem('token');
  return {
    headers: {
      Authorization: `Bearer ${token}`
    }
  };
}

  // Manejo básico de errores
  private handleError(error: any): Observable<never> {
    let errorMessage = 'Ocurrió un error inesperado';

    if (error.error instanceof ErrorEvent) {
      // Error del lado del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del backend
      errorMessage = error.error?.message || `Código: ${error.status} - ${error.message}`;
    }

    console.error(errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
