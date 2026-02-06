// src/app/models/trabajador.model.ts
export interface Trabajador {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  activo: boolean;
  fechaCreacion: string;
  puedeSerLiquidador: boolean;
  puedeSerEjecutante: boolean;
  puedeSerAnalistaContable: boolean;
  puedeSerJefaturaResponsable: boolean;
  puedeSerCoordinadorTiCw: boolean;

  // Relaciones
  empresaId?: number;
  empresaNombre?: string;
  areaId: number;
  areaNombre: string;
  cargoId: number;
  cargoNombre: string;
}

export interface TrabajadorDetail extends Trabajador {
  cargoNivel?: string;
  fechaModificacion?: string;
}

export interface TrabajadorSimple {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  empresaNombre?: string;
  areaNombre: string;
  cargoNombre: string;
  activo: boolean;
  fechaCreacion: string;
  nombreCompleto?: string;
}

export interface TrabajadorStats {
  totalTrabajadores: number;
  trabajadoresActivos: number;
  porcentajeActivos: number;
}
// src/app/models/filter.model.ts
export interface TrabajadorFilter {
  search?: string;
  activo?: boolean;
  areaIds?: number[];
  cargoIds?: number[];
  empresaIds?: number[];
  puedeSerLiquidador?: boolean[];
  puedeSerEjecutante?: boolean[];
  puedeSerAnalistaContable?: boolean[];
  puedeSerJefaturaResponsable?: boolean[];
  puedeSerCoordinadorTiCw?: boolean[];

  // Paginación
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: string;
}


// src/app/models/request.model.ts
export interface TrabajadorRequest {
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  areaId: number;
  cargoId: number;
  empresaId?: number;
  activo?: boolean;
  puedeSerLiquidador?: boolean;
  puedeSerEjecutante?: boolean;
  puedeSerAnalistaContable?: boolean;
  puedeSerJefaturaResponsable?: boolean;
  puedeSerCoordinadorTiCw?: boolean;
}

export interface TrabajadorUpdate {
  nombres: string;
  apellidos: string;
  dni: string;
  celular: string;
  correoCorporativo: string;
  areaId: number;
  cargoId: number;
  empresaId?: number;
  puedeSerLiquidador?: boolean;
  puedeSerEjecutante?: boolean;
  puedeSerAnalistaContable?: boolean;
  puedeSerJefaturaResponsable?: boolean;
  puedeSerCoordinadorTiCw?: boolean;
}
