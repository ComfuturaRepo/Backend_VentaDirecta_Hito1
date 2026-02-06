// Interfaces en el mismo archivo
export interface Site {
  idSite?: number;
  codigoSitio?: string;
  activo?: boolean;
  fechaCreacion?: string;
  descripciones: SiteDescripcion[];
}

export interface SiteDescripcion {
  idSiteDescripcion?: number;
  descripcion: string;
  activo?: boolean;
  fechaCreacion?: string;
}

export interface SiteRequest {
  codigoSitio?: string;
  activo?: boolean;
  descripciones: SiteDescripcionRequest[];
}

export interface SiteDescripcionRequest {
  descripcion: string;
  activo?: boolean;
}

// Nueva interfaz para filtros
export interface SiteFilter {
  search?: string;         // Búsqueda general en código, descripción, dirección
  activo?: boolean | null; // null = todos, true = activos, false = inactivos
  codigoSitio?: string;    // Búsqueda exacta de código
  descripcion?: string;    // Búsqueda específica en descripción
  direccion?: string;      // Búsqueda específica en dirección
}

// Si no tienes PageResponse, aquí está:
export interface PageResponse<T> {
  content: T[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  pageSize: number;
  filters?: { [key: string]: any }; // Filtros aplicados (opcional)
}
// site.interface.ts
export interface SiteFilter {
  search?: string;         // Búsqueda general en código o descripción
  activo?: boolean | null; // null = todos, true = activos, false = inactivos
  codigoSitio?: string;    // Búsqueda exacta de código
  descripcion?: string;    // Búsqueda específica en descripción
  direccion?: string;      // Búsqueda específica en dirección (si existe en tu backend)
}
// Y asegúrate que PageResponse tenga el campo filters
export interface PageResponse<T> {
  content: T[];
  currentPage: number;
  totalItems: number;
  totalPages: number;
  first: boolean;
  last: boolean;
  pageSize: number;
  filters?: { [key: string]: any };
}
