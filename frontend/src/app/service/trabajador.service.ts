import { TrabajadorRequest, TrabajadorStats } from './../model/trabajador.model';
// src/app/services/trabajador.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { PageResponse } from '../model/page.interface';
import { TrabajadorDetail, TrabajadorFilter, TrabajadorSimple, TrabajadorUpdate } from '../model/trabajador.model';


@Injectable({
  providedIn: 'root'
})
export class TrabajadorService {
  private apiUrl = `${environment.baseUrl}/api/trabajadores`;

  constructor(private http: HttpClient) {}

  // GET: Obtener todos los trabajadores (paginado)
  findAll(page: number = 0, size: number = 10, sort: string = 'fechaCreacion,desc'): Observable<PageResponse<TrabajadorSimple>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<PageResponse<TrabajadorSimple>>(this.apiUrl, { params });
  }

  // GET: Obtener trabajadores activos
  findActivos(page: number = 0, size: number = 10): Observable<PageResponse<TrabajadorSimple>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<TrabajadorSimple>>(`${this.apiUrl}/activos`, { params });
  }

  // POST: Búsqueda avanzada con filtros
  searchAdvanced(filter: TrabajadorFilter): Observable<PageResponse<TrabajadorSimple>> {
    return this.http.post<PageResponse<TrabajadorSimple>>(`${this.apiUrl}/search`, filter);
  }

  // GET: Búsqueda por texto
  searchByText(search: string, page: number = 0, size: number = 10): Observable<PageResponse<TrabajadorSimple>> {
    const params = new HttpParams()
      .set('search', search)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<TrabajadorSimple>>(`${this.apiUrl}/search`, { params });
  }

  // GET: Obtener trabajador por ID
  findById(id: number): Observable<TrabajadorDetail> {
    return this.http.get<TrabajadorDetail>(`${this.apiUrl}/${id}`);
  }

  // GET: Obtener trabajador por DNI
  findByDni(dni: string): Observable<TrabajadorDetail> {
    return this.http.get<TrabajadorDetail>(`${this.apiUrl}/dni/${dni}`);
  }

  // GET: Verificar si DNI existe
  checkDniExists(dni: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/check-dni`, {
      params: new HttpParams().set('dni', dni)
    });
  }

  // GET: Verificar si correo existe
  checkEmailExists(email: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/check-email`, {
      params: new HttpParams().set('email', email)
    });
  }

  // POST: Crear nuevo trabajador
  create(trabajador: TrabajadorRequest): Observable<TrabajadorDetail> {
    return this.http.post<TrabajadorDetail>(this.apiUrl, trabajador);
  }

  // PUT: Actualizar trabajador
  update(id: number, trabajador: TrabajadorUpdate): Observable<TrabajadorDetail> {
    return this.http.put<TrabajadorDetail>(`${this.apiUrl}/${id}`, trabajador);
  }

  // PATCH: Cambiar estado activo/inactivo
  toggleActivo(id: number): Observable<TrabajadorDetail> {
    return this.http.patch<TrabajadorDetail>(`${this.apiUrl}/${id}/toggle-activo`, {});
  }

  // GET: Estadísticas
  getStats(): Observable<TrabajadorStats> {
    return this.http.get<TrabajadorStats>(`${this.apiUrl}/stats`);
  }

  // GET: Contar activos por área
  countActivosByArea(areaId: number): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/stats/area/${areaId}/activos`);
  }

  // GET: Obtener trabajadores por filtros simples (para combos)
  getByFilters(filters: {
    search?: string,
    activo?: boolean,
    areaIds?: number[],
    cargoIds?: number[],
    roles?: boolean[]
  }): Observable<TrabajadorSimple[]> {
    let params = new HttpParams();

    if (filters.search) params = params.set('search', filters.search);
    if (filters.activo !== undefined) params = params.set('activo', filters.activo.toString());
    if (filters.areaIds) params = params.set('areaIds', filters.areaIds.join(','));
    if (filters.cargoIds) params = params.set('cargoIds', filters.cargoIds.join(','));
    if (filters.roles) params = params.set('roles', filters.roles.join(','));

    return this.http.get<TrabajadorSimple[]>(`${this.apiUrl}/filter`, { params });
  }

  // GET: Obtener trabajadores por rol específico
  getByRole(role: string): Observable<TrabajadorSimple[]> {
    return this.http.get<TrabajadorSimple[]>(`${this.apiUrl}/role/${role}`);
  }
}
