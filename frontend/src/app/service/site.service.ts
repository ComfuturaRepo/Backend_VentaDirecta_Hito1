import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Site, SiteRequest } from '../model/site.interface';
import { PageResponse } from '../model/page.interface';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class SiteService {
  private apiUrl = `${environment.baseUrl}/api/site`; // Cambiado a plural

  constructor(private http: HttpClient) {}

  // Crear nuevo site con descripciones
  crear(siteRequest: SiteRequest): Observable<Site> {
    return this.http.post<Site>(this.apiUrl, siteRequest);
  }

  // Actualizar site existente
  actualizar(id: number, siteRequest: SiteRequest): Observable<Site> {
    return this.http.put<Site>(`${this.apiUrl}/${id}`, siteRequest);
  }

  // Método de conveniencia para guardar (crear o actualizar)
  guardar(siteRequest: SiteRequest, id?: number): Observable<Site> {
    if (id) {
      return this.actualizar(id, siteRequest);
    } else {
      return this.crear(siteRequest);
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



  // Verificar si código existe
  verificarCodigo(codigo: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/verificar-codigo/${codigo}`);
  }
}
