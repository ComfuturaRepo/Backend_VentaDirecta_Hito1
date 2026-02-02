export interface Aprobacion {
  nivel: number;
  estado: 'PENDIENTE' | 'APROBADO' | 'RECHAZADO' | 'BLOQUEADO';
  aprobadoPor?: string;
  fechaInicio: string;  // ISO string desde backend
  fechaFin?: string;    // ISO string desde backend
  diasEnEstado: number;
  comentario?: string;
}
