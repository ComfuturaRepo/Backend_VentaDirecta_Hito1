// src/app/services/cargo-solicitante.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, forkJoin, map } from 'rxjs';
import { environment } from '../../environment';
import { CargoSolicitante } from '../model/cargo-solicitante.interface';
import { PageResponse } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class CargoSolicitanteService {

  constructor(private http: HttpClient) {}

  private getApiUrl(tipo: 'ANALISTA' | 'JEFATURA'): string {
    const recurso = tipo === 'ANALISTA' ? 'analista-cliente-solicitante' : 'jefaturas-cliente';
    return `${environment.baseUrl}/api/${recurso}`;
  }

  // ========== CRUD BÁSICO (sin paginación) ==========

  guardar(cargo: CargoSolicitante): Observable<CargoSolicitante> {
    const url = this.getApiUrl(cargo.tipo);

    if (cargo.id) {
      // Si tiene ID, usar PUT para actualizar
      return this.http.put<CargoSolicitante>(`${url}/${cargo.id}`, cargo);
    } else {
      // Si no tiene ID, usar POST para crear
      return this.http.post<CargoSolicitante>(url, cargo);
    }
  }

  listar(tipo: 'ANALISTA' | 'JEFATURA'): Observable<CargoSolicitante[]> {
    const url = this.getApiUrl(tipo);
    return this.http.get<CargoSolicitante[]>(url);
  }

  obtenerPorId(id: number, tipo: 'ANALISTA' | 'JEFATURA'): Observable<CargoSolicitante> {
    const url = this.getApiUrl(tipo);
    return this.http.get<CargoSolicitante>(`${url}/${id}`);
  }

  toggle(id: number, tipo: 'ANALISTA' | 'JEFATURA'): Observable<void> {
    const url = this.getApiUrl(tipo);

    // Para Jefatura usar PATCH, para Analista usar POST
    if (tipo === 'JEFATURA') {
      return this.http.patch<void>(`${url}/${id}/toggle`, {});
    } else {
      return this.http.post<void>(`${url}/${id}/toggle`, {});
    }
  }

  // ========== PAGINACIÓN POR TIPO ==========

  /**
   * Listar con paginación por tipo
   */
  listarPaginated(
    tipo: 'ANALISTA' | 'JEFATURA',
    page: number = 0,
    size: number = 10,
    sort: string = 'descripcion',
    direction: string = 'asc',
    activos?: boolean
  ): Observable<PageResponse<CargoSolicitante>> {
    const endpoint = tipo === 'ANALISTA' ? 'paginated' : '';
    const url = `${this.getApiUrl(tipo)}${endpoint ? '/' + endpoint : ''}`;

    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort)
      .set('direction', direction);

    if (activos !== undefined && activos !== null) {
      params = params.set('activos', activos.toString());
    }

    return this.http.get<PageResponse<CargoSolicitante>>(url, { params }).pipe(
      map(response => ({
        ...response,
        content: response.content.map(item => ({
          ...item,
          tipo // Añadir el tipo al resultado
        }))
      }))
    );
  }

  /**
   * Buscar con paginación por tipo
   */
  buscar(
    tipo: 'ANALISTA' | 'JEFATURA',
    search: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<CargoSolicitante>> {
    const url = `${this.getApiUrl(tipo)}/buscar`;

    let params = new HttpParams()
      .set('search', search)
      .set('page', page.toString())
      .set('size', size.toString());

    return this.http.get<PageResponse<CargoSolicitante>>(url, { params }).pipe(
      map(response => ({
        ...response,
        content: response.content.map(item => ({
          ...item,
          tipo // Añadir el tipo al resultado
        }))
      }))
    );
  }

  // ========== PAGINACIÓN COMBINADA (AMBOS TIPOS) ==========

  /**
   * Listar ambos tipos combinados con paginación
   */
  listarAmbosPaginated(
    page: number = 0,
    size: number = 10,
    sort: string = 'descripcion',
    direction: string = 'asc',
    activos?: boolean
  ): Observable<PageResponse<CargoSolicitante>> {
    return forkJoin({
      analistas: this.listarPaginated('ANALISTA', page, size, sort, direction, activos),
      jefaturas: this.listarPaginated('JEFATURA', page, size, sort, direction, activos)
    }).pipe(
      map(({ analistas, jefaturas }) => {
        const combinedContent = [...analistas.content, ...jefaturas.content];
        const totalItems = analistas.totalItems + jefaturas.totalItems;

        // Ordenar combinado si es necesario
        if (sort) {
          combinedContent.sort((a, b) => {
            const valueA = a[sort as keyof CargoSolicitante];
            const valueB = b[sort as keyof CargoSolicitante];

            if (typeof valueA === 'string' && typeof valueB === 'string') {
              return direction === 'desc'
                ? valueB.localeCompare(valueA)
                : valueA.localeCompare(valueB);
            }
            return 0;
          });
        }

        return {
          content: combinedContent.slice(0, size), // Limitar al tamaño de página
          currentPage: page,
          totalItems: totalItems,
          totalPages: Math.ceil(totalItems / size),
          first: page === 0,
          last: (page + 1) * size >= totalItems,
          pageSize: size
        };
      })
    );
  }

  /**
   * Buscar en ambos tipos combinados
   */
  buscarEnAmbos(
    search: string,
    page: number = 0,
    size: number = 10
  ): Observable<PageResponse<CargoSolicitante>> {
    return forkJoin({
      analistas: this.buscar('ANALISTA', search, page, size),
      jefaturas: this.buscar('JEFATURA', search, page, size)
    }).pipe(
      map(({ analistas, jefaturas }) => {
        const combinedContent = [...analistas.content, ...jefaturas.content];
        const totalItems = analistas.totalItems + jefaturas.totalItems;

        return {
          content: combinedContent.slice(0, size),
          currentPage: page,
          totalItems: totalItems,
          totalPages: Math.ceil(totalItems / size),
          first: page === 0,
          last: (page + 1) * size >= totalItems,
          pageSize: size
        };
      })
    );
  }

  // ========== MÉTODOS AUXILIARES ==========

  /**
   * Listar todos los cargos (sin paginación)
   */
  listarTodos(): Observable<CargoSolicitante[]> {
    return forkJoin({
      analistas: this.listar('ANALISTA'),
      jefaturas: this.listar('JEFATURA')
    }).pipe(
      map(({ analistas, jefaturas }) => {
        const analistasConTipo = analistas.map(item => ({ ...item, tipo: 'ANALISTA' as const }));
        const jefaturasConTipo = jefaturas.map(item => ({ ...item, tipo: 'JEFATURA' as const }));

        return [...analistasConTipo, ...jefaturasConTipo];
      })
    );
  }

  /**
   * Obtener estadísticas de ambos tipos
   */
  obtenerEstadisticas(): Observable<{
    totalAnalistas: number;
    totalJefaturas: number;
    analistasActivos: number;
    jefaturasActivas: number;
    total: number;
    totalActivos: number;
  }> {
    return this.listarTodos().pipe(
      map(cargos => {
        const analistas = cargos.filter(c => c.tipo === 'ANALISTA');
        const jefaturas = cargos.filter(c => c.tipo === 'JEFATURA');

        return {
          totalAnalistas: analistas.length,
          totalJefaturas: jefaturas.length,
          analistasActivos: analistas.filter(a => a.activo === true).length,
          jefaturasActivas: jefaturas.filter(j => j.activo === true).length,
          total: cargos.length,
          totalActivos: cargos.filter(c => c.activo === true).length
        };
      })
    );
  }

  /**
   * Eliminar cargo (toggle a inactivo)
   */
  eliminar(id: number, tipo: 'ANALISTA' | 'JEFATURA'): Observable<void> {
    return this.toggle(id, tipo);
  }

  /**
   * Buscar por descripción en ambos tipos (sin paginación)
   */
  buscarPorDescripcion(descripcion: string): Observable<CargoSolicitante[]> {
    return this.listarTodos().pipe(
      map(cargos => cargos.filter(cargo =>
        cargo.descripcion.toLowerCase().includes(descripcion.toLowerCase())
      ))
    );
  }

  /**
   * Ordenar cargos por descripción
   */
  ordenarPorDescripcion(cargos: CargoSolicitante[]): CargoSolicitante[] {
    return [...cargos].sort((a, b) =>
      (a.descripcion || '').localeCompare(b.descripcion || '')
    );
  }

  /**
   * Filtrar cargos por tipo
   */
  filtrarPorTipo(cargos: CargoSolicitante[], tipo: 'ANALISTA' | 'JEFATURA'): CargoSolicitante[] {
    return cargos.filter(cargo => cargo.tipo === tipo);
  }

  /**
   * Filtrar cargos por estado activo
   */
  filtrarPorActivo(cargos: CargoSolicitante[], activo: boolean = true): CargoSolicitante[] {
    return cargos.filter(cargo => cargo.activo === activo);
  }

  /**
   * Contar cargos por tipo
   */
  contarPorTipo(cargos: CargoSolicitante[]): { analistas: number; jefaturas: number } {
    return {
      analistas: cargos.filter(c => c.tipo === 'ANALISTA').length,
      jefaturas: cargos.filter(c => c.tipo === 'JEFATURA').length
    };
  }
}
