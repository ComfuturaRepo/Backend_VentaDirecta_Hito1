// src/app/core/services/cliente.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

// Interfaces
export interface Cliente {
  idCliente: number;
  razonSocial: string;
  ruc: string;
  activo: boolean;
  areas: AreaSimple[];
}

export interface AreaSimple {
  idArea: number;
  nombre: string;
  activo: boolean;
}

export interface ClienteCreateUpdateDTO {
  razonSocial: string;
  ruc: string;
  areaIds: number[];
}

export interface ClienteDetailDTO extends Cliente {
  fechaCreacion?: string;
  fechaModificacion?: string;
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
export class ClienteService {
  private apiUrl = `${environment.baseUrl}/api/clientes`;

  constructor(private http: HttpClient) {}

  // =============================
  // CRUD BÁSICO
  // =============================

  /**
   * Obtener todos los clientes (con paginación)
   */
  getAll(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idCliente',
    sortDir: string = 'asc'
  ): Observable<PageResponseDTO<Cliente>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponseDTO<Cliente>>(this.apiUrl, { params });
  }

  /**
   * Obtener clientes activos
   */
  getActivos(
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idCliente',
    sortDir: string = 'asc'
  ): Observable<PageResponseDTO<Cliente>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponseDTO<Cliente>>(`${this.apiUrl}/activos`, { params });
  }

  /**
   * Obtener cliente por ID
   */
  getById(id: number): Observable<ClienteDetailDTO> {
    return this.http.get<ClienteDetailDTO>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crear nuevo cliente
   */
  create(cliente: ClienteCreateUpdateDTO): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }

  /**
   * Actualizar cliente existente
   */
  update(id: number, cliente: ClienteCreateUpdateDTO): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${id}`, cliente);
  }

  /**
   * Activar/desactivar cliente (toggle)
   */
  toggleStatus(id: number): Observable<Cliente> {
    return this.http.patch<Cliente>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  /**
   * Eliminar cliente
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // =============================
  // BÚSQUEDA Y FILTROS
  // =============================

  /**
   * Buscar clientes por término
   */
  search(
    term: string,
    page: number = 0,
    size: number = 10,
    sortBy: string = 'idCliente',
    sortDir: string = 'asc'
  ): Observable<PageResponseDTO<Cliente>> {
    let params = new HttpParams()
      .set('q', term)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);

    return this.http.get<PageResponseDTO<Cliente>>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Verificar si un RUC ya existe
   */
  checkRucExists(ruc: string): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/exists/ruc/${ruc}`);
  }

  /**
   * Verificar si un RUC ya existe (excluyendo un cliente específico)
   * Útil para validaciones en edición
   */
  checkRucExistsForEdit(ruc: string, excludeId: number): Observable<boolean> {
    // Primero verificamos si existe
    return new Observable(observer => {
      this.checkRucExists(ruc).subscribe({
        next: (exists) => {
          if (!exists) {
            observer.next(false);
            observer.complete();
            return;
          }

          // Si existe, verificamos si es el mismo cliente
          this.search(ruc, 0, 1).subscribe({
            next: (result) => {
              const sameClient = result.content.some(cliente =>
                cliente.ruc === ruc && cliente.idCliente !== excludeId
              );
              observer.next(sameClient);
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
  // MÉTODOS AUXILIARES
  // =============================

  /**
   * Obtener todos los clientes sin paginación (para dropdowns)
   */
  getAllForDropdown(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.apiUrl}/all`);
  }

  /**
   * Obtener clientes activos sin paginación
   */
  getActivosForDropdown(): Observable<Cliente[]> {
    return this.http.get<Cliente[]>(`${this.apiUrl}/activos/all`);
  }

  /**
   * Convertir cliente a formato para dropdown
   */
  clienteToDropdownItem(cliente: Cliente): { id: number; label: string; adicional?: string } {
    return {
      id: cliente.idCliente,
      label: cliente.razonSocial,
      adicional: cliente.ruc
    };
  }

  /**
   * Convertir lista de clientes a formato para dropdown
   */
  clientesToDropdownItems(clientes: Cliente[]): { id: number; label: string; adicional?: string }[] {
    return clientes.map(cliente => this.clienteToDropdownItem(cliente));
  }

  /**
   * Validar formato de RUC (11 dígitos)
   */
  validateRucFormat(ruc: string): boolean {
    return /^\d{11}$/.test(ruc);
  }

  /**
   * Preparar datos para crear/actualizar
   */
  prepareClienteData(formData: any): ClienteCreateUpdateDTO {
    return {
      razonSocial: formData.razonSocial?.trim() || '',
      ruc: formData.ruc?.trim() || '',
      areaIds: formData.areaIds || []
    };
  }

  /**
   * Clonar cliente para edición
   */
  cloneForEdit(cliente: ClienteDetailDTO): ClienteCreateUpdateDTO {
    return {
      razonSocial: cliente.razonSocial,
      ruc: cliente.ruc,
      areaIds: cliente.areas?.map(area => area.idArea) || []
    };
  }

  // =============================
  // VALIDACIONES
  // =============================

  /**
   * Validar datos del cliente antes de enviar
   */
  validateClienteData(data: ClienteCreateUpdateDTO): { valid: boolean; errors: string[] } {
    const errors: string[] = [];

    if (!data.razonSocial || data.razonSocial.trim().length === 0) {
      errors.push('La razón social es requerida');
    }

    if (!data.ruc || data.ruc.trim().length === 0) {
      errors.push('El RUC es requerido');
    } else if (!this.validateRucFormat(data.ruc)) {
      errors.push('El RUC debe tener exactamente 11 dígitos');
    }

    return {
      valid: errors.length === 0,
      errors
    };
  }

  // =============================
  // MÉTODOS DE ESTADO
  // =============================

  /**
   * Obtener texto del estado
   */
  getStatusText(activo: boolean): string {
    return activo ? 'Activo' : 'Inactivo';
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
  // MANEJO DE ÁREAS
  // =============================

  /**
   * Obtener nombres de áreas como string
   */
  getAreasNames(areas: AreaSimple[]): string {
    if (!areas || areas.length === 0) {
      return 'Sin áreas asignadas';
    }
    return areas.map(area => area.nombre).join(', ');
  }

  /**
   * Verificar si un área está asignada al cliente
   */
  hasArea(cliente: Cliente, areaId: number): boolean {
    return cliente.areas?.some(area => area.idArea === areaId) || false;
  }

  /**
   * Agregar área al cliente (localmente)
   */
  addAreaToCliente(cliente: Cliente, area: AreaSimple): Cliente {
    if (!cliente.areas.some(a => a.idArea === area.idArea)) {
      cliente.areas.push(area);
    }
    return cliente;
  }

  /**
   * Remover área del cliente (localmente)
   */
  removeAreaFromCliente(cliente: Cliente, areaId: number): Cliente {
    cliente.areas = cliente.areas.filter(area => area.idArea !== areaId);
    return cliente;
  }

  // =============================
  // MANEJO DE PAGINACIÓN
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
}
