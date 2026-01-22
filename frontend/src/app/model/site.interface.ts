// src/app/models/site.interface.ts
export interface Site {
  idSite?: number; // Optional for new creations
  codigoSitio: string;
  descripcion?: string;
  activo?: boolean;
}
