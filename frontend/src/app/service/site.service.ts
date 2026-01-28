import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Site } from '../model/site.interface';
import { PageResponse } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class SiteService {
  private apiUrl = 'http://localhost:8080/api/site'; // Ajusta la URL según tu backend

  constructor(private http: HttpClient) {}

  // Crear o actualizar site
  guardar(site: Site): Observable<Site> {
    if (site.idSite) {
      // Si tiene ID, es una actualización
      return this.http.put<Site>(`${this.apiUrl}/${site.idSite}`, site);
    } else {
      // Si no tiene ID, es una creación
      return this.http.post<Site>(this.apiUrl, site);
    }
  }

  // Listar con paginación y filtros
  listar(
    page: number = 0,
    size: number = 10,
    sort: string = 'codigoSitio',
    direction: string = 'asc',
    activos?: boolean
  ): Observable<PageResponse<Site>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort)
      .set('direction', direction);

    if (activos !== undefined) {
      params = params.set('activos', activos.toString());
    }

    return this.http.get<PageResponse<Site>>(this.apiUrl, { params });
  }

  // Buscar sites con texto
  buscar(search: string, page: number = 0, size: number = 10): Observable<PageResponse<Site>> {
    let params = new HttpParams()
      .set('search', search)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<Site>>(`${this.apiUrl}/buscar`, { params });
  }

  // Obtener site por ID
  obtenerPorId(id: number): Observable<Site> {
    return this.http.get<Site>(`${this.apiUrl}/${id}`);
  }

  // Toggle activo/inactivo
  toggle(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, {});
  }

  // Eliminar site (soft delete) - usa el mismo toggle
  eliminar(id?: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
