export interface Permiso {
  idPermiso: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  activo: boolean;
  niveles: Nivel[];

  areas: Area[];
  cargos: Cargo[];
}

export interface PermisoDTO {
  idPermiso?: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  nivelesIds: number[];
  areasIds: number[];
  cargosIds: number[];
}

export interface PermisoResponseDTO {
  idPermiso: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  activo: boolean;
  niveles: NivelDTO[];
  areas: AreaDTO[];
  cargos: CargoDTO[];
}
export interface VerificarPermisoDTO {
  codigoPermiso: string;
  idUsuario: number;
}

export interface PermisoUsuarioResponse {
  tienePermiso: boolean;
  codigoPermiso: string;
  mensaje?: string;
}
export interface Nivel {
  idNivel: number;
  codigo: string;
  nombre: string;
  descripcion: string;
}

export interface NivelDTO {
  idNivel: number;
  codigo: string;
  nombre: string;
  descripcion?: string; // <-- Hacerla opcional con '?'
}
export interface Area {
  idArea: number;
  nombre: string;
  activo: boolean;
}

export interface AreaDTO {
  idArea: number;
  nombre: string;
  activo: boolean;
}

export interface Cargo {
  idCargo: number;
  nombre: string;
  nivel: Nivel;
  activo: boolean;
}

export interface CargoDTO {
  idCargo: number;
  nombre: string;
  idNivel: number;
  activo: boolean;
  nombreNivel?: string;
}

export interface Cargo {
  idCargo: number;
  nombre: string;
  nivel: Nivel;
  activo: boolean;
}

export interface CargoDTO {
  idCargo: number;
  nombre: string;
  idNivel: number;
  activo: boolean;
  nombreNivel?: string;
}
export interface Usuario {
  idUsuario: number;
  username: string;
  activo: boolean;
  fechaCreacion: string;
  trabajador?: Trabajador;
  nivel: Nivel;
  area?: Area;
  cargo?: Cargo;
}

export interface Trabajador {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  area: Area;
  cargo: Cargo;
  activo: boolean;
}export interface PermisoFormData {
  idPermiso?: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  niveles: number[];
  areas: number[];
  cargos: number[];
}
export interface PermisoNivelTablaDTO {
  idNivel: number;
  codigo: string;
  nombre: string;
  // Sin 'descripcion' si no la necesitas en la tabla
}
export interface PermisoAreaTablaDTO {
  idArea: number;
  nombre: string;
  activo: boolean;
}

export interface PermisoCargoTablaDTO {
  idCargo: number;
  nombre: string;
  activo: boolean;
}
export interface PermisoTablaDTO {
  idPermiso: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  activo: boolean;
  niveles: PermisoNivelTablaDTO[];
  areas: PermisoAreaTablaDTO[];
  cargos: PermisoCargoTablaDTO[];
}
