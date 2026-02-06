export interface Aprobacion {
  nivel: number;
  estado: 'PENDIENTE' | 'APROBADO' | 'RECHAZADO' | 'BLOQUEADO';
  aprobadoPor?: string;
  fechaInicio: string;
  fechaFin?: string;
  diasEnEstado: number;
  comentario?: string;
  puedeAprobar: boolean;
  
}


