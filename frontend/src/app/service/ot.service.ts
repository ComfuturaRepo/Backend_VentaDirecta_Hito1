// src/app/core/services/ot.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environment';

// ────────────────────────────────────────────────
// Interfaces de REQUEST (lo que envías al backend)
// ────────────────────────────────────────────────

export interface OtCreateRequest {
  idCliente: number;
  idArea: number;
  idProyecto: number;
  idFase: number;
  idSite: number;
  idRegion: number;
  descripcion: string;
  idOtsAnterior: number | null;
fechaApertura: string;  // Enviamos como string "YYYY-MM-DD" porque es lo que <input type="date"> genera
  idJefaturaClienteSolicitante?: number | null;
  idAnalistaClienteSolicitante?: number | null;
  idCoordinadorTiCw?: number | null;               // si solo uno
  coordinadoresTiCwPextEnergia?: string | null;    // si permites varios en texto
  idJefaturaResponsable?: number | null;
  idLiquidador?: number | null;
  idEjecutante?: number | null;
  idAnalistaContable?: number | null;
}

export interface OtTrabajadorRequest {
  idTrabajador: number;
  rolEnOt: string;
}

export interface OtDetalleRequest {
  idMaestro: number;
  idProveedor: number;
  cantidad: number;
  precioUnitario: number;
}

export interface CrearOtCompletaRequest {
  ot: OtCreateRequest;
  trabajadores?: OtTrabajadorRequest[];
  detalles?: OtDetalleRequest[];
}

// ────────────────────────────────────────────────
// Interfaces de RESPONSE
// ────────────────────────────────────────────────

export interface OtResponse {
  idOts: number;
  ot: number;                      // generado automáticamente
  descripcion?: string;
  diasAsignados: number;
  activo: boolean;
  fechaCreacion: string;

  // Campos de responsables (si el backend los devuelve)
  jefaturaClienteSolicitante?: string;
  analistaClienteSolicitante?: string;
  coordinadoresTiCwPextEnergia?: string;
  jefaturaResponsable?: string;
  liquidador?: string;
  ejecutante?: string;
  analistaContable?: string;
}

@Injectable({
  providedIn: 'root'
})
export class OtService {
  private apiUrl = `${environment.baseUrl}/api/ots`;

  constructor(private http: HttpClient) {}

  crearOtCompleta(payload: CrearOtCompletaRequest): Observable<OtResponse> {
    return this.http.post<OtResponse>(`${this.apiUrl}/completa`, payload).pipe(
      catchError(this.handleError)
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = 'Ocurrió un error desconocido';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Error: ${error.error.message}`;
    } else {
      errorMessage = error.error?.message || `Error ${error.status}: ${error.statusText}`;
    }
    console.error(errorMessage, error);
    return throwError(() => new Error(errorMessage));
  }
}
