// src/app/services/jefatura-cliente-solicitante.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { JefaturaClienteSolicitante } from '../model/jefatura-cliente-solicitante.interface';

@Injectable({
  providedIn: 'root'
})
export class JefaturaClienteSolicitanteService {
  private apiUrl = `${environment.baseUrl}/api/jefatura-cliente-solicitante`;

  constructor(private http: HttpClient) { }

  // Crear o editar una Jefatura
  guardar(jefatura: JefaturaClienteSolicitante): Observable<JefaturaClienteSolicitante> {
    return this.http.post<JefaturaClienteSolicitante>(this.apiUrl, jefatura);
  }

  // Listar todas las Jefaturas
  listar(): Observable<JefaturaClienteSolicitante[]> {
    return this.http.get<JefaturaClienteSolicitante[]>(this.apiUrl);
  }

  // Toggle activo/inactivo
  toggle(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, null);
  }
}
