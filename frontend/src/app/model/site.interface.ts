export interface Site {
  idSite?: number;
  codigoSitio?: string;
  activo?: boolean;
  fechaCreacion?: string;
  descripciones: SiteDescripcion[];
}

export interface SiteDescripcion {
  idSiteDescripcion?: number;
  descripcion: string;
  activo?: boolean;
  fechaCreacion?: string;
}

export interface SiteRequest {
  codigoSitio?: string;
  activo?: boolean;
  descripciones: SiteDescripcionRequest[];
}

export interface SiteDescripcionRequest {
  descripcion: string;
  activo?: boolean;
}
