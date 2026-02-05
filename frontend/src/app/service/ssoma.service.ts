import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { SsomaRequest, SsomaResponse } from '../model/ssoma.model';

// Importar interfaces desde archivo separado

@Injectable({
  providedIn: 'root'
})
export class SsomaService {
  private apiUrl = `${environment.baseUrl}/api/ssoma`;

  constructor(private http: HttpClient) { }

  /**
   * 1. Crear todas las 5 hojas del SSOMA en una sola transacción
   * POST /api/ssoma/crear-completo
   */
  crearSsomaCompleto(request: SsomaRequest): Observable<SsomaResponse> {
    return this.http.post<SsomaResponse>(`${this.apiUrl}/crear-completo`, request);
  }

  /**
   * 2. Listar todos los ATS sin paginación
   * GET /api/ssoma/ats
   */
  listarTodosAts(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/ats`);
  }

  /**
   * 3. Listar todas las capacitaciones sin paginación
   * GET /api/ssoma/capacitaciones
   */
  listarTodasCapacitaciones(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/capacitaciones`);
  }

  /**
   * 4. Listar todas las inspecciones EPP sin paginación
   * GET /api/ssoma/inspecciones-epp
   */
  listarTodasInspeccionesEpp(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/inspecciones-epp`);
  }

  /**
   * 5. Listar todas las inspecciones de herramientas sin paginación
   * GET /api/ssoma/inspecciones-herramientas
   */
  listarTodasInspeccionesHerramientas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/inspecciones-herramientas`);
  }

  /**
   * 6. Listar todos los PETAR sin paginación
   * GET /api/ssoma/petar
   */
  listarTodosPetar(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/petar`);
  }

  /**
   * 7. Obtener todo el SSOMA por fecha
   * GET /api/ssoma/por-fecha/{fecha}
   */
  obtenerSsomaPorFecha(fecha: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/por-fecha/${fecha}`);
  }

  /**
   * 8. Endpoint de prueba
   * GET /api/ssoma/test
   */
  testConnection(): Observable<string> {
    return this.http.get(`${this.apiUrl}/test`, { responseType: 'text' });
  }
}
