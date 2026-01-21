// ────────────────────────────────────────────────
// Interfaces de REQUEST
// ────────────────────────────────────────────────

export interface OtCreateRequest {
  idCliente: number;
  idArea: number;
  idProyecto: number;
  idFase: number;
  idSite: number;
  idRegion: number;
  descripcion: string;
  idOtsAnterior: number | null;
  fechaApertura: string;
  idJefaturaClienteSolicitante?: number | null;
  idAnalistaClienteSolicitante?: number | null;
  idCoordinadorTiCw?: number | null;
  coordinadoresTiCwPextEnergia?: string | null;
  idJefaturaResponsable?: number | null;
  idLiquidador?: number | null;
  idEjecutante?: number | null;
  idAnalistaContable?: number | null;
}

export interface OtTrabajadorRequest {
  idTrabajador: number;
  rolEnOt: string;
}

export interface OtDetalleRequest {
  idMaestro: number;
  idProveedor: number;
  cantidad: number;
  precioUnitario: number;
}

export interface CrearOtCompletaRequest {
  ot: OtCreateRequest;
  trabajadores?: OtTrabajadorRequest[];
  detalles?: OtDetalleRequest[];
}

// ────────────────────────────────────────────────
// Interfaces de RESPONSE
// ────────────────────────────────────────────────

export interface OtResponse {
  idOts: number;
  ot: number;
  descripcion?: string;
  fechaApertura: string | null;
  diasAsignados: number | null;
  activo: boolean;
  fechaCreacion: string;

  // Campos opcionales que podrías mapear si el backend los empieza a devolver
  jefaturaClienteSolicitante?: string;
  analistaClienteSolicitante?: string;
  coordinadoresTiCwPextEnergia?: string;
  jefaturaResponsable?: string;
  liquidador?: string;
  ejecutante?: string;
  analistaContable?: string;
}

export interface Page<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  totalElements: number;
  totalPages: number;
  last: boolean;
  size: number;
  number: number;
  numberOfElements: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  first: boolean;
  empty: boolean;
}
