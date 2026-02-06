import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Site, SiteRequest, SiteFilter } from '../model/site.interface';
import { PageResponse } from '../model/page.interface';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class SiteService {
  private apiUrl = `${environment.baseUrl}/api/site`;

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

  // Listar con paginación y filtros BÁSICOS (compatibilidad)
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

  // FILTROS AVANZADOS - Nuevo método
  filtrar(
    filters: SiteFilter,
    page: number = 0,
    size: number = 10,
    sort: string = 'codigoSitio',
    direction: string = 'asc'
  ): Observable<PageResponse<Site>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort)
      .set('direction', direction);

    // Agregar filtros si existen
    if (filters.search) {
      params = params.set('search', filters.search);
    }
    if (filters.activo !== undefined && filters.activo !== null) {
      params = params.set('activo', filters.activo.toString());
    }
    if (filters.codigoSitio) {
      params = params.set('codigoSitio', filters.codigoSitio);
    }
    if (filters.descripcion) {
      params = params.set('descripcion', filters.descripcion);
    }
    if (filters.direccion) {
      params = params.set('direccion', filters.direccion);
    }

    return this.http.get<PageResponse<Site>>(`${this.apiUrl}/filtrar`, { params });
  }

  // Buscar sites con texto (método simplificado)
  buscar(search: string, page: number = 0, size: number = 10): Observable<PageResponse<Site>> {
    const filters: SiteFilter = { search };
    return this.filtrar(filters, page, size);
  }

  // Obtener site por ID
  obtenerPorId(id: number): Observable<Site> {
    return this.http.get<Site>(`${this.apiUrl}/${id}`);
  }

  // Toggle activo/inactivo
  toggle(id: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${id}/toggle`, {});
  }

  // Eliminar site (soft delete)
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Verificar si código existe
  verificarCodigo(codigo: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/verificar-codigo/${codigo}`);
  }

  // Método para limpiar filtros
  crearFiltrosVacios(): SiteFilter {
    return {
      search: '',
      activo: null,
      codigoSitio: '',
      descripcion: '',
      direccion: ''
    };
  }
}

