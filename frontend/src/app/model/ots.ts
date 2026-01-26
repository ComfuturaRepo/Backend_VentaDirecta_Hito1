// src/app/core/models/ots.ts

// DTO para el listado en tabla (liviano pero con idOts y activo)
// src/app/core/models/ots.ts
export interface OtListDto {
  idOts: number;
  ot: number;
  fechaApertura?: string | null;
  estadoOt?: string | null;
  cliente_id?: string | null;
  siteNombre?: string | null;
  site_descripcion?: string | null;
  region?: string | null;
  cliente?: string | null;
  proyecto?: string | null;
  activo: boolean;
}

// DTO para detalle completo (vista de detalle) - AHORA CON TODOS LOS IDs
export interface OtDetailResponse {
  idOts: number;
  ot: number;
  idOtsAnterior?: number | null;

  descripcion: string;
  fechaApertura?: string | null;
  diasAsignados: number;
  fechaCreacion: string;
  activo: boolean;

  // Entidades relacionadas (IDs + nombres)
  idCliente?: number | null;
  clienteRazonSocial?: string | null;

  idArea?: number | null;
  areaNombre?: string | null;

  idProyecto?: number | null;
  proyectoNombre?: string | null;

  idFase?: number | null;
  faseNombre?: string | null;

  idSite?: number | null;
  siteNombre?: string | null;

  idRegion?: number | null;
  regionNombre?: string | null;

  idJefaturaClienteSolicitante?: number | null;
  jefaturaClienteSolicitanteNombre?: string | null;

  idAnalistaClienteSolicitante?: number | null;
  analistaClienteSolicitanteNombre?: string | null;

  idCreador?: number | null;
  creadorNombre?: string | null;

  idCoordinadorTiCw?: number | null;
  coordinadorTiCwNombre?: string | null;

  idJefaturaResponsable?: number | null;
  jefaturaResponsableNombre?: string | null;

  idLiquidador?: number | null;
  liquidadorNombre?: string | null;

  idEjecutante?: number | null;
  ejecutanteNombre?: string | null;

  idAnalistaContable?: number | null;
  analistaContableNombre?: string | null;

  estadoOt?: string | null;
}

// Request para crear / editar OT (sin cambios, está bien)
export interface OtCreateRequest {
  idOts?: number;
  idOtsAnterior?: number | null;

  idCliente: number;
  idArea: number;
  idProyecto: number;
  idFase: number;
  idSite: number;
  idRegion: number;

  descripcion?: string;
  fechaApertura?: string;

  idJefaturaClienteSolicitante?: number | null;
  idAnalistaClienteSolicitante?: number | null;

  idCoordinadorTiCw?: number | null;
  idJefaturaResponsable?: number | null;
  idLiquidador?: number | null;
  idEjecutante?: number | null;
  idAnalistaContable?: number | null;
}

// Paginación genérica (sin cambios)
export interface Page<T> {
  content: T[];
  pageable: {
    sort: { sorted: boolean; unsorted: boolean; empty: boolean };
    offset: number;
    pageNumber: number;
    pageSize: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
  sort: { sorted: boolean; unsorted: boolean; empty: boolean };
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}
