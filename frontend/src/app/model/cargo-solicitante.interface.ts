export interface CargoSolicitante {
  id?: number;
  descripcion: string;
  activo?: boolean;
  tipo: 'ANALISTA' | 'JEFATURA';   // opcional
}
