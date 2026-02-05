// ==================== INTERFACES ====================

// Interfaz para ATS
export interface AtsParticipante {
  idTrabajador: number;
  idRol: number;
}

export interface AtsRiesgo {
  idTarea: number;
  idPeligro: number;
  idRiesgo: number;
  idMedida: number;
}

export interface AtsRequest {
  fecha: string;
  hora: string;
  empresa: string;
  lugarTrabajo: string;
  idTrabajo: number;
  participantes: AtsParticipante[];
  riesgos: AtsRiesgo[];
  eppIds: number[];
  tipoRiesgoIds: number[];
}

export interface AtsResponse {
  idAts: number;
  fecha: string;
  hora: string;
  empresa: string;
  lugarTrabajo: string;
  idTrabajo: number;
  nombreTrabajo?: string;
  participantes: AtsParticipante[];
  riesgos: AtsRiesgo[];
  eppIds: number[];
  tipoRiesgoIds: number[];
}

// Interfaz para Capacitación
export interface CapacitacionAsistente {
  idTrabajador: number;
  observaciones?: string;
}

export interface CapacitacionRequest {
  numeroRegistro: string;
  tema: string;
  fecha: string;
  hora: string;
  idCapacitador: number;
  asistentes: CapacitacionAsistente[];
}

export interface CapacitacionResponse {
  idCapacitacion: number;
  numeroRegistro: string;
  tema: string;
  fecha: string;
  hora: string;
  idCapacitador: number;
  nombreCapacitador?: string;
  asistentes: CapacitacionAsistente[];
}

// Interfaz para Inspección EPP
export interface InspeccionEppDetalle {
  idTrabajador: number;
  idEpp: number;
  cumple: boolean;
  observacion?: string;
}

export interface InspeccionEppRequest {
  numeroRegistro: string;
  fecha: string;
  idInspector: number;
  detalles: InspeccionEppDetalle[];
}

export interface InspeccionEppResponse {
  id: number;
  numeroRegistro: string;
  fecha: string;
  idInspector: number;
  nombreInspector?: string;
  detalles: InspeccionEppDetalle[];
}

// Interfaz para Inspección Herramientas
export interface InspeccionHerramientaDetalle {
  idHerramienta: number;
  cumple: boolean;
  fotoUrl?: string;
  observacion?: string;
}

export interface InspeccionHerramientaRequest {
  numeroRegistro: string;
  fecha: string;
  idSupervisor: number;
  detalles: InspeccionHerramientaDetalle[];
}

export interface InspeccionHerramientaResponse {
  id: number;
  numeroRegistro: string;
  fecha: string;
  idSupervisor: number;
  nombreSupervisor?: string;
  detalles: InspeccionHerramientaDetalle[];
}

// Interfaz para PETAR
export interface PetarRespuesta {
  idPregunta: number;
  respuesta: boolean;
}

export interface PetarRequest {
  numeroRegistro: string;
  fecha: string;
  respuestas: PetarRespuesta[];
  trabajadoresAutorizadosIds: number[];
}

export interface PetarResponse {
  id: number;
  numeroRegistro: string;
  fecha: string;
  respuestas: PetarRespuesta[];
  trabajadoresAutorizadosIds: number[];
}

// Interfaz para SSOMA Completo
export interface SsomaRequest {
  ats: AtsRequest;
  capacitacion: CapacitacionRequest;
  inspeccionEpp: InspeccionEppRequest;
  inspeccionHerramienta: InspeccionHerramientaRequest;
  petar: PetarRequest;
}

export interface SsomaResponse {
  mensaje: string;
  transaccionId: string;
  ats: AtsResponse;
  capacitacion: CapacitacionResponse;
  inspeccionEpp: InspeccionEppResponse;
  inspeccionHerramienta: InspeccionHerramientaResponse;
  petar: PetarResponse;
}

// Interfaz para Catálogos
export interface Catalogo {
  id: number;
  nombre: string;
  descripcion?: string;
}

export interface Trabajador {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  cargo: string;
}
