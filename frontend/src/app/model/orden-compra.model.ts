// Detalle para request (crear/actualizar OC)
export interface OcDetalleRequest {
idMaestro?: number;
cantidad?: number;
precioUnitario?: number;

}

// Detalle para response (cuando recibes OC del backend)
export interface OcDetalleResponse {
idOcDetalle?: number;


  cantidad?: number;
  precioUnitario?: number;
  total?: number;

  idMaestro?: number;
  codigo?: string;
  descripcion?: string;
  unidad?: string;
}

// DTO para crear o actualizar OC (request)
export interface OrdenCompraRequest {
  idEstadoOc: number;
  idOts: number;
  idProveedor: number;
  formaPago: string;
  igvPorcentaje: number;
  fechaOc: string;
  observacion?: string;
  detalles: OcDetalleRequest[];
  aplicarIgv: true // 🔹 asegurarte de que está aquí


}

// DTO de respuesta de OC (response)
export interface OrdenCompraResponse {
  idOc: number;
  idEstadoOc: number;
  estadoNombre: string;
  idOts: number;
  otsDescripcion: string;
  ot: string;


  idProveedor: number;
  proveedorNombre: string;
  proveedorRuc: string;
  proveedorDireccion: string;
  proveedorContacto: string;
  proveedorBanco: string;
  formaPago: string;
  subtotal: number;
  igvPorcentaje: number;
  igvTotal: number;
  total: number;
  fechaOc: string;
  observacion?: string;
  detalles: OcDetalleResponse[];
}

// Paginación
export interface PageInfo {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

export interface PageOrdenCompra {
  content: OrdenCompraResponse[];
  page: PageInfo;
}
