// src/app/services/analista-cliente-solicitante.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { AnalistaClienteSolicitante } from '../model/analista-cliente-solicitante.interface';

@Injectable({
  providedIn: 'root'
})
export class AnalistaClienteSolicitanteService {
  private apiUrl = `${environment.baseUrl}/api/analista-cliente-solicitante`;

  constructor(private http: HttpClient) { }

  // Crear o editar un Analista
  guardar(analista: AnalistaClienteSolicitante): Observable<AnalistaClienteSolicitante> {
    return this.http.post<AnalistaClienteSolicitante>(this.apiUrl, analista);
  }

  // Listar todos los Analistas activos
  listar(): Observable<AnalistaClienteSolicitante[]> {
    return this.http.get<AnalistaClienteSolicitante[]>(this.apiUrl);
  }

  // Toggle activo/inactivo
  toggle(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, null);
  }
}
