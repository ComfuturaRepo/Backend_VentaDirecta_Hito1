// src/app/core/models/ots.ts

export interface OtResponse {
  idOts: number;
  ot: number;
  descripcion: string;
  activo: boolean;
  fechaCreacion: string; // ISO string
}

export interface OtFullDetailResponse {
  idOts: number;
  ot: number;
  idOtsAnterior?: number | null;

  descripcion: string;
  fechaApertura?: string;      // puede ser null
  fechaCreacion: string;
  activo: boolean;

  // Entidades relacionadas con nombres
  idCliente?: number;
  clienteRazonSocial?: string;

  idArea?: number;
  areaNombre?: string;

  idProyecto?: number;
  proyectoNombre?: string;

  idFase?: number;
  faseNombre?: string;

  idSite?: number;
  siteNombre?: string;

  idRegion?: number;
  regionNombre?: string;

  // Responsables (IDs + nombres descriptivos)
  idJefaturaClienteSolicitante?: number;
  jefaturaClienteSolicitanteNombre?: string;

  idAnalistaClienteSolicitante?: number;
  analistaClienteSolicitanteNombre?: string;

  idCoordinadorTiCw?: number;
  coordinadorTiCwNombre?: string;

  idJefaturaResponsable?: number;
  jefaturaResponsableNombre?: string;

  idLiquidador?: number;
  liquidadorNombre?: string;

  idEjecutante?: number;
  ejecutanteNombre?: string;

  idAnalistaContable?: number;
  analistaContableNombre?: string;

  // Lista de trabajadores asignados a esta OT
  trabajadoresAsignados: TrabajadorEnOtDto[];
}

export interface TrabajadorEnOtDto {
  idTrabajador: number;
  nombresCompletos: string;
  cargoNombre?: string | null;
  areaTrabajadorNombre?: string | null;
  rolEnOt: string;
  activo: boolean;
}

// Para el guardado (lo que envías al backend)
export interface CrearOtCompletaRequest {
  ot: OtCreateRequest;
  trabajadores?: Array<{ idTrabajador: number; rolEnOt: string }>;
}

export interface OtCreateRequest {
  idOts?: number;               // si es edición
  idOtsAnterior?: number | null;
  idCliente: number;            // requeridos en creación
  idArea: number;
  idProyecto: number;
  idFase: number;
  idSite: number;
  idRegion: number;
  descripcion?: string;
  fechaApertura?: string;       // formato 'YYYY-MM-DD'
  idJefaturaClienteSolicitante?: number | null;
  idAnalistaClienteSolicitante?: number | null;
  idCoordinadorTiCw?: number | null;
  idJefaturaResponsable?: number | null;
  idLiquidador?: number | null;
  idEjecutante?: number | null;
  idAnalistaContable?: number | null;
}

// Para la paginación
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
