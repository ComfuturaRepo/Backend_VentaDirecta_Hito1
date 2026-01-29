// orden-compra.model.ts

// 🔹 Detalle para request (crear/actualizar OC)
// src/app/model/orden-compra.model.ts

export interface OcDetalleRequest {
  idMaestro: number;
  cantidad: number;
  precioUnitario: number;
  subtotal: number;
  igv: number;
  total: number;
    tipo?: ''; // <-- NUEVO CAMPO

}


// 🔹 Detalle para response (cuando recibes OC del backend)
export interface OcDetalleResponse {
  idDetalle?: number;         // ID del detalle
  idProducto?: number;        // ID del producto
  productoNombre?: string;    // Nombre del producto
  cantidad?: number;          // Cantidad solicitada
  precioUnitario?: number;    // Precio por unidad
  total?: number;             // Total de la línea
  observacion?: string;       // Observación del detalle
    // Maestro / Producto
  idMaestro?: number;
  codigo?: string;        // 🔹 el código del maestro
  descripcion?: string;   // 🔹 la descripción del maestro
  unidad?: string;        // 🔹 la unidad
}

// 🔹 DTO para crear o actualizar OC (request)
export interface OrdenCompraRequest {
  idEstadoOc: number;
  idOts: number;
  idProveedor: number;
  formaPago: string;

  subtotal: number;
  igvPorcentaje: number;
  igvTotal: number;
  total: number;

  fechaOc: string;            // ISO string (ej: "2026-01-28T15:00:00")
  observacion?: string;

  detalles: OcDetalleRequest[];

  aplicarIgv: boolean;
}

// 🔹 DTO de respuesta de OC (response)
export interface OrdenCompraResponse {
  idOc: number;

  // Estado OC
  idEstadoOc: number;
  estadoNombre: string;

  // OTS
  idOts: number;
  otsDescripcion: string;
  ot: string;          // <-- Cambiado a string

  // Cliente
  clienteNombre: string;
  clienteRuc: string;

  // Proveedor
  idProveedor: number;
  proveedorNombre: string;   // <-- nombre de la razon social
  proveedorRuc: string;
  proveedorDireccion: string;
  proveedorContacto: string;
  proveedorBanco: string;

  // Forma de pago y montos
  formaPago: string;
  subtotal: number;
  igvPorcentaje: number;
  igvTotal: number;
  total: number;

  fechaOc: string;            // ISO string
  observacion?: string;

  detalles: OcDetalleResponse[];
}



// 🔹 Paginación de OC (Page de Spring Data)
export interface PageOrdenCompra {
  content: OrdenCompraResponse[];
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
