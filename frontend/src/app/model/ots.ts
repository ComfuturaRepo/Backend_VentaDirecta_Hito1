export interface OtResponse {
  idOts: number;
  ot: number;
  descripcion: string;
  activo: boolean;
  fechaCreacion: string; // ISO date string
}

export interface OtFullResponse {
  idOts: number;
  ot: number;
  idOtsAnterior?: number | null;

  idCliente?: number | null;
  idArea?: number | null;
  idProyecto?: number | null;
  idFase?: number | null;
  idSite?: number | null;
  idRegion?: number | null;

  descripcion?: string;
  fechaApertura?: string; // 'YYYY-MM-DD'

  idJefaturaClienteSolicitante?: number | null;
  idAnalistaClienteSolicitante?: number | null;
  idCoordinadorTiCw?: number | null;
  idJefaturaResponsable?: number | null;
  idLiquidador?: number | null;
  idEjecutante?: number | null;
  idAnalistaContable?: number | null;

  activo: boolean;
  fechaCreacion: string;
  // NO incluye trabajadores (como lo decidiste)
}

export interface CrearOtCompletaRequest {
  ot: OtCreateRequest;
  trabajadores?: Array<{ idTrabajador: number; rolEnOt: string }>; // opcional
}

export interface OtCreateRequest {
  idOts?: number;               // solo para edición
  idOtsAnterior?: number | null;
  idCliente?: number;
  idArea?: number;
  idProyecto?: number;
  idFase?: number;
  idSite?: number;
  idRegion?: number;
  descripcion?: string;
  fechaApertura?: string;       // 'YYYY-MM-DD'
  idJefaturaClienteSolicitante?: number | null;
  idAnalistaClienteSolicitante?: number | null;
  idCoordinadorTiCw?: number | null;
  idJefaturaResponsable?: number | null;
  idLiquidador?: number | null;
  idEjecutante?: number | null;
  idAnalistaContable?: number | null;
}

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
