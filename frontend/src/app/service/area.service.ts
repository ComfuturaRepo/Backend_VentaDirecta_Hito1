// src/app/core/services/area.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

// Interfaces
export interface Area {
  idArea: number;
  nombre: string;
  activo: boolean;
  clientes?: ClienteSimple[];
}

export interface ClienteSimple {
  idCliente: number;
  razonSocial: string;
  ruc: string;
  activo: boolean;
}

export interface AreaCreateUpdateDTO {
  nombre: string;
}

export interface AreaSimpleDTO {
  idArea: number;
  nombre: string;
  activo: boolean;
}

export interface PageResponseDTO<T> {
  content: T[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  pageSize: number;
}

export interface PageRequestDTO {
  page: number;
  size: number;
  sortBy: string;
  sortDir: string;
}

@Injectable({
  providedIn: 'root'
})
export class AreaService {
  private apiUrl = `${environment.baseUrl}/api/areas`;

  constructor(private http: HttpClient) {}

  // =============================
  // CRUD BÁSICO
  // =============================

  /**
   * Obtener todas las áreas (con paginación)
   */
  getAll(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idArea',
    sortDir: string = 'asc'
  ): Observable<PageResponseDTO<Area>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponseDTO<Area>>(this.apiUrl, { params });
  }

  /**
   * Obtener áreas activas
   */
  getActivas(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idArea',
    sortDir: string = 'asc'
  ): Observable<PageResponseDTO<Area>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponseDTO<Area>>(`${this.apiUrl}/activas`, { params });
  }

  /**
   * Obtener área por ID
   */
  getById(id: number): Observable<Area> {
    return this.http.get<Area>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crear nueva área
   */
  create(area: AreaCreateUpdateDTO): Observable<Area> {
    return this.http.post<Area>(this.apiUrl, area);
  }

  /**
   * Actualizar área existente
   */
  update(id: number, area: AreaCreateUpdateDTO): Observable<Area> {
    return this.http.put<Area>(`${this.apiUrl}/${id}`, area);
  }

  /**
   * Activar/desactivar área (toggle)
   */
  toggleStatus(id: number): Observable<Area> {
    return this.http.patch<Area>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  /**
   * Eliminar área
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // =============================
  // BÚSQUEDA Y FILTROS
  // =============================

  /**
   * Buscar áreas por término
   */
  search(
    term: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idArea',
    sortDir: string = 'asc'
  ): Observable<PageResponseDTO<Area>> {
    let params = new HttpParams()
      .set('q', term)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponseDTO<Area>>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Verificar si un nombre de área ya existe
   */
  checkNombreExists(nombre: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/exists/nombre/${encodeURIComponent(nombre)}`);
  }

  /**
   * Verificar si un nombre de área ya existe (excluyendo un área específica)
   * Útil para validaciones en edición
   */
  checkNombreExistsForEdit(nombre: string, excludeId: number): Observable<boolean> {
    // Primero verificamos si existe
    return new Observable(observer => {
      this.checkNombreExists(nombre).subscribe({
        next: (exists) => {
          if (!exists) {
            observer.next(false);
            observer.complete();
            return;
          }

          // Si existe, verificamos si es la misma área
          this.search(nombre, 0, 1).subscribe({
            next: (result) => {
              const sameArea = result.content.some(area =>
                area.nombre.toLowerCase() === nombre.toLowerCase() && area.idArea !== excludeId
              );
              observer.next(sameArea);
              observer.complete();
            },
            error: (err) => observer.error(err)
          });
        },
        error: (err) => observer.error(err)
      });
    });
  }

  // =============================
  // DROPDOWNS Y LISTAS
  // =============================

  /**
   * Obtener todas las áreas activas sin paginación (para dropdowns)
   */
  getActivasForDropdown(): Observable<AreaSimpleDTO[]> {
    return this.http.get<AreaSimpleDTO[]>(`${this.apiUrl}/dropdown/activas`);
  }

  /**
   * Obtener todas las áreas sin paginación (para dropdowns)
   */
  getAllForDropdown(): Observable<AreaSimpleDTO[]> {
    return this.http.get<AreaSimpleDTO[]>(`${this.apiUrl}/dropdown/all`);
  }

  /**
   * Obtener todas las áreas activas sin paginación (para uso interno)
   */
  getAllActivas(): Observable<Area[]> {
    return this.http.get<Area[]>(`${this.apiUrl}/activas/all`);
  }

  /**
   * Obtener todas las áreas sin paginación (para uso interno)
   */
  getAllAreas(): Observable<Area[]> {
    return this.http.get<Area[]>(`${this.apiUrl}/all`);
  }

  // =============================
  // CONVERSIÓN PARA DROPDOWNS
  // =============================

  /**
   * Convertir área a formato para dropdown
   */
  areaToDropdownItem(area: Area | AreaSimpleDTO): { id: number; label: string; adicional?: string } {
    return {
      id: area.idArea,
      label: area.nombre,
      adicional: area.activo ? 'Activo' : 'Inactivo'
    };
  }

  /**
   * Convertir lista de áreas a formato para dropdown
   */
  areasToDropdownItems(areas: (Area | AreaSimpleDTO)[]): { id: number; label: string; adicional?: string }[] {
    return areas.map(area => this.areaToDropdownItem(area));
  }

  // =============================
  // VALIDACIONES
  // =============================

  /**
   * Validar formato de nombre
   */
  validateNombreFormat(nombre: string): { valid: boolean; error?: string } {
    if (!nombre || nombre.trim().length === 0) {
      return { valid: false, error: 'El nombre es requerido' };
    }

    if (nombre.trim().length < 2) {
      return { valid: false, error: 'El nombre debe tener al menos 2 caracteres' };
    }

    if (nombre.trim().length > 100) {
      return { valid: false, error: 'El nombre no puede exceder los 100 caracteres' };
    }

    return { valid: true };
  }

  /**
   * Validar datos del área antes de enviar
   */
  validateAreaData(data: AreaCreateUpdateDTO): { valid: boolean; errors: string[] } {
    const errors: string[] = [];

    if (!data.nombre || data.nombre.trim().length === 0) {
      errors.push('El nombre del área es requerido');
    } else {
      const validation = this.validateNombreFormat(data.nombre);
      if (!validation.valid && validation.error) {
        errors.push(validation.error);
      }
    }

    return {
      valid: errors.length === 0,
      errors
    };
  }

  /**
   * Preparar datos para crear/actualizar
   */
  prepareAreaData(formData: any): AreaCreateUpdateDTO {
    return {
      nombre: formData.nombre?.trim() || ''
    };
  }

  /**
   * Clonar área para edición
   */
  cloneForEdit(area: Area): AreaCreateUpdateDTO {
    return {
      nombre: area.nombre
    };
  }

  // =============================
  // MÉTODOS DE ESTADO
  // =============================

  /**
   * Obtener texto del estado
   */
  getStatusText(activo: boolean): string {
    return activo ? 'Activa' : 'Inactiva';
  }

  /**
   * Obtener clase CSS para el estado
   */
  getStatusClass(activo: boolean): string {
    return activo ? 'badge bg-success' : 'badge bg-danger';
  }

  /**
   * Obtener icono para el estado
   */
  getStatusIcon(activo: boolean): string {
    return activo ? 'bi bi-check-circle' : 'bi bi-x-circle';
  }

  // =============================
  // MÉTODOS DE CLIENTES
  // =============================

  /**
   * Obtener nombres de clientes como string
   */
  getClientesNames(clientes?: ClienteSimple[]): string {
    if (!clientes || clientes.length === 0) {
      return 'Sin clientes asignados';
    }

    const activeClients = clientes.filter(c => c.activo);
    const inactiveClients = clientes.filter(c => !c.activo);

    let result = `${activeClients.length} cliente(s) activo(s)`;

    if (inactiveClients.length > 0) {
      result += `, ${inactiveClients.length} inactivo(s)`;
    }

    return result;
  }

  /**
   * Obtener lista de nombres de clientes
   */
  getClientesList(clientes?: ClienteSimple[]): string[] {
    if (!clientes || clientes.length === 0) {
      return [];
    }
    return clientes.map(cliente => `${cliente.razonSocial} (${cliente.ruc})`);
  }

  // =============================
  // MÉTODOS AUXILIARES
  // =============================

  /**
   * Generar array de números de página para el paginador
   */
  generatePageNumbers(currentPage: number, totalPages: number): number[] {
    const pages: number[] = [];
    const maxPagesToShow = 5;

    if (totalPages <= maxPagesToShow) {
      // Mostrar todas las páginas
      for (let i = 0; i < totalPages; i++) {
        pages.push(i);
      }
    } else {
      // Mostrar páginas alrededor de la actual
      let start = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
      let end = start + maxPagesToShow;

      if (end > totalPages) {
        end = totalPages;
        start = Math.max(0, end - maxPagesToShow);
      }

      for (let i = start; i < end; i++) {
        pages.push(i);
      }
    }

    return pages;
  }

  /**
   * Verificar si hay más páginas
   */
  hasNextPage(currentPage: number, totalPages: number): boolean {
    return currentPage < totalPages - 1;
  }

  /**
   * Verificar si hay página anterior
   */
  hasPreviousPage(currentPage: number): boolean {
    return currentPage > 0;
  }

  /**
   * Capitalizar primera letra de cada palabra
   */
  capitalizeNombre(nombre: string): string {
    return nombre
      .toLowerCase()
      .split(' ')
      .map(word => word.charAt(0).toUpperCase() + word.slice(1))
      .join(' ');
  }

  // =============================
  // ESTADÍSTICAS Y CONTADORES
  // =============================

  /**
   * Contar clientes activos en un área
   */
  countActiveClientes(area: Area): number {
    if (!area.clientes) return 0;
    return area.clientes.filter(cliente => cliente.activo).length;
  }

  /**
   * Contar clientes totales en un área
   */
  countTotalClientes(area: Area): number {
    return area.clientes ? area.clientes.length : 0;
  }

  /**
   * Verificar si se puede desactivar el área
   */
  canDeactivate(area: Area): { canDeactivate: boolean; reason?: string } {
    const activeClients = this.countActiveClientes(area);

    if (activeClients > 0) {
      return {
        canDeactivate: false,
        reason: `No se puede desactivar porque tiene ${activeClients} cliente(s) activo(s) asignado(s)`
      };
    }

    return { canDeactivate: true };
  }

  /**
   * Verificar si se puede eliminar el área
   */
  canDelete(area: Area): { canDelete: boolean; reason?: string } {
    const totalClients = this.countTotalClientes(area);

    if (totalClients > 0) {
      return {
        canDelete: false,
        reason: `No se puede eliminar porque tiene ${totalClients} cliente(s) asignado(s)`
      };
    }

    return { canDelete: true };
  }

  // =============================
  // MANEJO DE ERRORES
  // =============================

  /**
   * Manejar errores del servicio
   */
  handleError(error: any): string {
    if (error.error && error.error.message) {
      return error.error.message;
    }

    if (error.status === 404) {
      return 'Recurso no encontrado';
    }

    if (error.status === 400) {
      return 'Datos inválidos enviados';
    }

    if (error.status === 409) {
      return 'Conflicto: El área ya existe o tiene dependencias';
    }

    return 'Error al procesar la solicitud. Por favor, intente nuevamente.';
  }

  // =============================
  // CACHÉ Y MEMORIZACIÓN
  // =============================

  private areasCache: AreaSimpleDTO[] | null = null;
  private lastCacheTime: number = 0;
  private readonly CACHE_DURATION = 5 * 60 * 1000; // 5 minutos en milisegundos

  /**
   * Obtener áreas con caché para dropdowns
   */
  getCachedActivasForDropdown(): Observable<AreaSimpleDTO[]> {
    const now = Date.now();

    // Si hay caché válido, devolverlo
    if (this.areasCache && (now - this.lastCacheTime < this.CACHE_DURATION)) {
      return new Observable(observer => {
        observer.next(this.areasCache!);
        observer.complete();
      });
    }

    // Si no hay caché o está expirado, obtener de la API
    return new Observable(observer => {
      this.getActivasForDropdown().subscribe({
        next: (areas) => {
          this.areasCache = areas;
          this.lastCacheTime = now;
          observer.next(areas);
          observer.complete();
        },
        error: (err) => observer.error(err)
      });
    });
  }

  /**
   * Invalidar caché
   */
  invalidateCache(): void {
    this.areasCache = null;
    this.lastCacheTime = 0;
  }

  // =============================
  // UTILIDADES DE BUSQUEDA
  // =============================

  /**
   * Filtrar áreas por término de búsqueda (localmente)
   */
  filterAreas(areas: Area[], searchTerm: string): Area[] {
    if (!searchTerm) return areas;

    const term = searchTerm.toLowerCase();
    return areas.filter(area =>
      area.nombre.toLowerCase().includes(term) ||
      area.idArea.toString().includes(term)
    );
  }

  /**
   * Ordenar áreas
   */
  sortAreas(areas: Area[], sortBy: string = 'nombre', sortDir: string = 'asc'): Area[] {
    return [...areas].sort((a, b) => {
      let valueA: any;
      let valueB: any;

      switch (sortBy) {
        case 'idArea':
          valueA = a.idArea;
          valueB = b.idArea;
          break;
        case 'nombre':
          valueA = a.nombre.toLowerCase();
          valueB = b.nombre.toLowerCase();
          break;
        case 'activo':
          valueA = a.activo;
          valueB = b.activo;
          break;
        default:
          valueA = a.nombre.toLowerCase();
          valueB = b.nombre.toLowerCase();
      }

      if (sortDir === 'asc') {
        return valueA > valueB ? 1 : valueA < valueB ? -1 : 0;
      } else {
        return valueA < valueB ? 1 : valueA > valueB ? -1 : 0;
      }
    });
  }

  // =============================
  // EXPORTACIÓN DE DATOS
  // =============================

  /**
   * Convertir área a CSV
   */
  areaToCSV(area: Area): string {
    const clientes = area.clientes
      ? area.clientes.map(c => `${c.razonSocial} (${c.ruc})`).join('; ')
      : 'Sin clientes';

    return `${area.idArea},${area.nombre},${area.activo ? 'Activa' : 'Inactiva'},${clientes}`;
  }

  /**
   * Obtener headers para CSV
   */
  getCSVHeaders(): string {
    return 'ID,Nombre,Estado,Clientes Asignados';
  }
}
