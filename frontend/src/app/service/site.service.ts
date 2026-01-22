// src/app/services/site.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { Site } from '../model/site.interface';

@Injectable({
  providedIn: 'root'
})
export class SiteService {
  private apiUrl = `${environment.baseUrl}/api/site`;

  constructor(private http: HttpClient) { }

  // Crear o editar un Site
  guardar(site: Site): Observable<Site> {
    return this.http.post<Site>(this.apiUrl, site);
  }

  // Listar Sites con paginación
  listar(page: number, size: number): Observable<any> { // Usa 'any' o define una interfaz para Page<Site>
    return this.http.get<any>(`${this.apiUrl}?page=${page}&size=${size}`);
  }

  // Toggle activo/inactivo
  toggle(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, null);
  }
}
