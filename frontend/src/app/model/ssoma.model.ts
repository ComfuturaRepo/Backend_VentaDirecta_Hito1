export interface Ats {
  idAts?: number;
  fecha: Date;
  hora: string;
  numeroRegistro: string;
  empresa: string;
  lugarTrabajo: string;
  coordenadas: string;
  idOts?: number;
  codigoOt?: string;
  idTrabajo?: number;
  nombreTrabajo?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}

export interface AtsRequest {
  empresa: string;
  lugarTrabajo: string;
coordenadas: string;           // ← este falta en tu payload
  idOts?: number;
  idTrabajo?: number;
  idSupervisorTrabajo?: number;
  idResponsableLugar?: number;
  idSupervisorSst?: number;
  participantes?: AtsParticipanteRequest[];
  riesgos?: AtsRiesgoRequest[];
  eppIds?: number[];
  tipoRiesgoIds?: number[];
}

export interface AtsParticipanteRequest {
  idTrabajador: number;
  idRol: number;
}

export interface AtsRiesgoRequest {
  idTarea?: number;
  idPeligro?: number;
  idRiesgo?: number;
  idMedida?: number;
  observacion?: string;
}

export interface AtsResponse {
  idAts: number;
  fecha: Date;
  hora: string;
  numeroRegistro: string;
  empresa: string;
  lugarTrabajo: string;
  coordenadas: string;
  idOts?: number;
  codigoOt?: string;
  idTrabajo?: number;
  nombreTrabajo?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}
export interface Capacitacion {
  idCapacitacion?: number;
  numeroRegistro: string;
  tema: string;
  fecha: Date;
  hora: string;
  tipoCharla: string;
  idOts?: number;
  idCapacitador?: number;
  nombreCapacitador?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}

export interface CapacitacionRequest {
  tema: string;
  tipoCharla?: string;
  idOts?: number;
  idCapacitador?: number;
  idSupervisorTrabajo?: number;
  idResponsableLugar?: number;
  idSupervisorSst?: number;
  asistentes?: CapacitacionAsistenteRequest[];
}

export interface CapacitacionAsistenteRequest {
  idTrabajador: number;
  observaciones?: string;
}

export interface CapacitacionResponse {
  idCapacitacion: number;
  numeroRegistro: string;
  tema: string;
  fecha: Date;
  hora: string;
  tipoCharla: string;
  idCapacitador?: number;
  nombreCapacitador?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}export interface InspeccionEpp {
  id?: number;
  numeroRegistro: string;
  fecha: Date;
  tipoInspeccion: string;
  areaTrabajo?: string;
  idOts?: number;
  idInspector?: number;
  nombreInspector?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}

export interface InspeccionEppRequest {
  tipoInspeccion?: string;
  areaTrabajo?: string;
  idOts?: number;
  idInspector?: number;
  idSupervisorTrabajo?: number;
  idResponsableLugar?: number;
  idSupervisorSst?: number;
  detalles?: InspeccionEppDetalleRequest[];
}

export interface InspeccionEppDetalleRequest {
  idTrabajador: number;
  idEpp: number;
  cumple: boolean;
  observacion?: string;
  accionCorrectiva?: string;
}

export interface InspeccionEppResponse {
  id: number;
  numeroRegistro: string;
  fecha: Date;
  tipoInspeccion: string;
  areaTrabajo?: string;
  idInspector?: number;
  nombreInspector?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}export interface InspeccionHerramienta {
  id?: number;
  numeroRegistro: string;
  fecha: Date;
  ubicacionSede?: string;
  idOts?: number;
  idCliente?: number;
  idProyecto?: number;
  idSupervisor?: number;
  nombreSupervisor?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}

export interface InspeccionHerramientaRequest {
  ubicacionSede?: string;
  idOts?: number;
  idCliente?: number;
  idProyecto?: number;
  idSupervisor?: number;
  idSupervisorTrabajo?: number;
  idResponsableLugar?: number;
  idSupervisorSst?: number;
  detalles?: InspeccionHerramientaDetalleRequest[];
}

export interface InspeccionHerramientaDetalleRequest {
  idHerramienta: number;
  observacion?: string;
}

export interface InspeccionHerramientaResponse {
  id: number;
  numeroRegistro: string;
  fecha: Date;
  ubicacionSede?: string;
  idSupervisor?: number;
  nombreSupervisor?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
}export interface Petar {
  id?: number;
  numeroRegistro: string;
  fecha: Date;
  requiereEvaluacionAmbiente: boolean;
  aperturaLineaEquipos: boolean;
  horaInicio?: string;
  recursosNecesarios?: string;
  procedimiento?: string;
  idOts?: number;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
  idBrigadista?: number;
  nombreBrigadista?: string;
  idResponsableTrabajo?: number;
  nombreResponsableTrabajo?: string;
}

export interface PetarRequest {
  requiereEvaluacionAmbiente: boolean;
  aperturaLineaEquipos: boolean;
  horaInicio?: string;
  recursosNecesarios?: string;
  procedimiento?: string;
  idOts?: number;
  idSupervisorTrabajo?: number;
  idResponsableLugar?: number;
  idSupervisorSst?: number;
  idBrigadista?: number;
  idResponsableTrabajo?: number;
  conformidadRequerida?: boolean;
  respuestas?: PetarRespuestaRequest[];
  trabajadoresAutorizadosIds?: number[];
}

export interface PetarRespuestaRequest {
  idPregunta: number;
  respuesta: boolean;
  observacion?: string;
}

export interface PetarResponse {
  id: number;
  numeroRegistro: string;
  fecha: Date;
  requiereEvaluacionAmbiente: boolean;
  aperturaLineaEquipos: boolean;
  horaInicio?: string;
  recursosNecesarios?: string;
  procedimiento?: string;
  idSupervisorTrabajo?: number;
  nombreSupervisorTrabajo?: string;
  idResponsableLugar?: number;
  nombreResponsableLugar?: string;
  idSupervisorSst?: number;
  nombreSupervisorSst?: string;
  idBrigadista?: number;
  nombreBrigadista?: string;
  idResponsableTrabajo?: number;
  nombreResponsableTrabajo?: string;
}
