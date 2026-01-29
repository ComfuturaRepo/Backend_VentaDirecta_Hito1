// Interfaces
export interface UsuarioSimple {
  idUsuario: number;
  username: string;
  nombreTrabajador: string;
  nivelNombre: string;
  activo: boolean;
  fechaCreacion: string;
}

export interface UsuarioDetail {
  idUsuario: number;
  username: string;
  trabajadorId: number;
  trabajadorNombre: string;
  trabajadorApellidos: string;
  trabajadorDNI: string;
  nivelId: number;
  nivelNombre: string;
  nivelCodigo: string;
  activo: boolean;
  fechaCreacion: string;
}



export interface UsuarioRequest {
  username: string;
  password: string;
  trabajadorId: number;
  nivelId: number;
  activo?: boolean;
}

export interface UsuarioUpdate {
  username?: string;
  trabajadorId?: number;
  nivelId?: number;
}

export interface PaginationParams {
  page: number;
  size: number;
  sortBy?: string;
  direction?: string;
  search?: string;
  activos?: boolean;
  nivelId?: number;
}

export interface MessageResponse {
  message: string;
  data: any;
}
