// interfaces/proyecto-response.interface.ts
export interface ProyectoResponse {
  idProyecto: number;
  nombre: string;
  activo: boolean;
}

// models/proyecto.model.ts
export class Proyecto {
  constructor(
    public idProyecto?: number,
    public nombre: string = '',
    public activo: boolean = true
  ) {}
}
// interfaces/proyecto-filtros.interface.ts
export interface ProyectoFiltros {
  page?: number;
  size?: number;
  sort?: string;
  direction?: string;
  nombre?: string;
  activo?: boolean;
  todos?: boolean;
}
