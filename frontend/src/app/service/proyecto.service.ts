
// servicios/proyecto.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ProyectoResponse } from '../model/proyecto.model';
import { PageResponse } from './trabajador.service';
import { environment } from '../../environment';

@Injectable({
  providedIn: 'root'
})
export class ProyectoService {
  private apiUrl = `${environment.baseUrl}/api/proyectos`;

  constructor(private http: HttpClient) { }

  /**
   * Listar proyectos con filtros avanzados
   */
  listarProyectos(
    page: number = 0,
    size: number = 10,
    sort: string = 'nombre',
    direction: string = 'asc',
    nombre?: string,
    activo?: boolean,
    todos?: boolean
  ): Observable<PageResponse<ProyectoResponse>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort)
      .set('direction', direction);

    if (nombre) {
      params = params.set('nombre', nombre);
    }

    if (activo !== undefined) {
      params = params.set('activo', activo.toString());
    }

    if (todos !== undefined) {
      params = params.set('todos', todos.toString());
    }

    return this.http.get<PageResponse<ProyectoResponse>>(this.apiUrl, { params });
  }

  /**
   * Buscar proyectos (búsqueda simple)
   */
  buscarProyectos(
    search: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<ProyectoResponse>> {
    const params = new HttpParams()
      .set('search', search)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<ProyectoResponse>>(`${this.apiUrl}/buscar`, { params });
  }

  /**
   * Listar proyectos (endpoint legacy)
   */
  listarProyectosLegacy(page: number = 0): Observable<any> {
    const params = new HttpParams().set('page', page.toString());
    return this.http.get<any>(`${this.apiUrl}/legacy`, { params });
  }

  /**
   * Obtener proyecto por ID
   */
  obtenerProyectoPorId(id: number): Observable<ProyectoResponse> {
    return this.http.get<ProyectoResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crear nuevo proyecto
   */
  crearProyecto(proyecto: any): Observable<ProyectoResponse> {
    return this.http.post<ProyectoResponse>(this.apiUrl, proyecto);
  }

  /**
   * Editar proyecto existente
   */
  editarProyecto(id: number, proyectoActualizado: any): Observable<ProyectoResponse> {
    return this.http.put<ProyectoResponse>(`${this.apiUrl}/${id}`, proyectoActualizado);
  }

  /**
   * Activar/Desactivar proyecto (toggle)
   */
  toggleProyecto(id: number): Observable<ProyectoResponse> {
    return this.http.patch<ProyectoResponse>(`${this.apiUrl}/${id}/toggle`, {});
  }
}
