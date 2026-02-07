// =========================
// INTERFACES PRINCIPALES
// =========================

export interface SsomaFormulario {
  idSsoma: number;
  idOts: number;
  empresaId?: number;
  trabajoId?: number;
  latitud?: number;
  longitud?: number;
  fecha: Date;
  horaInicio?: string;
  horaFin?: string;
  horaInicioTrabajo?: string;
  horaFinTrabajo?: string;
  supervisorTrabajo?: string;
  supervisorSst?: string;
  responsableArea?: string;
}

export interface Participante {
  idParticipante?: number;
  idSsoma: number;
  idTrabajador?: number;
  nombre: string;
  cargo: string;
}

export interface ParticipanteFirma {
  idFirma?: number;
  idParticipante: number;
  firmaUrl: string;
  fechaFirma: Date;
}

export interface ParticipanteFoto {
  idFoto?: number;
  idParticipante: number;
  fotoUrl: string;
  tipoFoto: 'FRONTAL' | 'CREDENCIAL';
}

export interface SecuenciaTarea {
  idSecuencia?: number;
  idSsoma: number;
  secuenciaTarea: string;
  peligro: string;
  riesgo: string;
  consecuencias: string;
  medidasControl: string;
  orden: number;
}

export interface ChecklistSeguridad {
  idChecklist?: number;
  idSsoma: number;
  itemNombre: string;
  usado: boolean;
  observaciones?: string;
}

export interface ChecklistFoto {
  idFoto?: number;
  idChecklist: number;
  fotoUrl: string;
}

export interface EppCheck {
  idEpp?: number;
  idSsoma: number;
  eppNombre: string;
  usado: boolean;
}

export interface EppFoto {
  idFoto?: number;
  idEpp: number;
  fotoUrl: string;
}

export interface TemaCharla {
  idTema?: number;
  nombre: string;
  descripcion?: string;
  duracionMinutos?: number;
  activo: boolean;
}

export interface Charla {
  idCharla?: number;
  idSsoma: number;
  idTema?: number;
  fechaCharla: Date;
  duracionHoras?: number;
  capacitadorId?: number;
}

export interface CharlaVideo {
  idVideo?: number;
  idCharla: number;
  videoUrl: string;
  duracionSegundos?: number;
  fechaSubida: Date;
}

export interface InspeccionTrabajador {
  idInspeccion?: number;
  idSsoma: number;
  tipoInspeccion: 'PLANIFICADA' | 'NO_PLANIFICADA';
  trabajadorId?: number;
  trabajadorNombre?: string;
  casco?: boolean;
  lentes?: boolean;
  orejeras?: boolean;
  tapones?: boolean;
  guantes?: boolean;
  botas?: boolean;
  arnes?: boolean;
  chaleco?: boolean;
  mascarilla?: boolean;
  gafas?: boolean;
  otros?: string;
  accionCorrectiva?: string;
  seguimiento?: string;
  responsableId?: number;
}

export interface HerramientaMaestra {
  idHerramientaMaestra?: number;
  nombre: string;
  descripcion?: string;
  activo: boolean;
}

export interface HerramientaInspeccion {
  idHerramienta?: number;
  idSsoma: number;
  idHerramientaMaestra?: number;
  herramientaNombre: string;
  p1?: boolean;
  p2?: boolean;
  p3?: boolean;
  p4?: boolean;
  p5?: boolean;
  p6?: boolean;
  p7?: boolean;
  p8?: boolean;
  observaciones?: string;
}

export interface HerramientaFoto {
  idFoto?: number;
  idHerramienta: number;
  fotoUrl: string;
}

export interface Petar {
  idPetar?: number;
  idSsoma: number;
  energiaPeligrosa?: boolean;
  trabajoAltura?: boolean;
  izaje?: boolean;
  excavacion?: boolean;
  espaciosConfinados?: boolean;
  trabajoCaliente?: boolean;
  otros?: boolean;
  otrosDescripcion?: string;
  velocidadAire?: string;
  contenidoOxigeno?: string;
  horaInicioPetar?: string;
  horaFinPetar?: string;
}

export interface PetarPreguntaMaestra {
  idPregunta?: number;
  categoria?: string;
  pregunta: string;
  orden?: number;
  activo: boolean;
}

export interface PetarRespuesta {
  idRespuesta?: number;
  idPetar: number;
  idPregunta?: number;
  respuesta?: boolean;
  observaciones?: string;
}

export interface EquipoProteccion {
  idEquipo?: number;
  idPetar: number;
  equipoNombre: string;
  usado: boolean;
}

// =========================
// DTOs PARA REQUEST
// =========================

export interface FotoParticipanteRequest {
  participanteIndex: number;
  foto: File;
  tipoFoto: 'FRONTAL' | 'CREDENCIAL';
}

export interface VideoCharlaRequest {
  video: File;
  duracionSegundos?: number;
}

export interface FotoEppRequest {
  eppIndex: number;
  foto: File;
}

export interface FotoHerramientaRequest {
  herramientaIndex: number;
  foto: File;
}

export interface ParticipanteRequest {
  trabajadorId?: number;
  nombre: string;
  cargo: string;
  firma?: File;
}

export interface SecuenciaTareaRequest {
  secuenciaTarea: string;
  peligro: string;
  riesgo: string;
  consecuencias: string;
  medidasControl: string;
  orden: number;
}

export interface ChecklistSeguridadRequest {
  itemNombre: string;
  usado: boolean;
  observaciones?: string;
  foto?: File;
}

export interface EppCheckRequest {
  eppNombre: string;
  usado: boolean;
  foto?: File;
}

export interface CharlaRequest {
  temaId?: number;
  fechaCharla?: Date;
  duracionHoras?: number;
  capacitadorId?: number;
}

export interface InspeccionTrabajadorRequest {
  tipoInspeccion: 'PLANIFICADA' | 'NO_PLANIFICADA';
  trabajadorId?: number;
  trabajadorNombre?: string;
  casco?: boolean;
  lentes?: boolean;
  orejeras?: boolean;
  tapones?: boolean;
  guantes?: boolean;
  botas?: boolean;
  arnes?: boolean;
  chaleco?: boolean;
  mascarilla?: boolean;
  gafas?: boolean;
  otros?: string;
  accionCorrectiva?: string;
  seguimiento?: string;
  responsableId?: number;
}

export interface HerramientaInspeccionRequest {
  herramientaMaestraId?: number;
  herramientaNombre: string;
  p1?: boolean;
  p2?: boolean;
  p3?: boolean;
  p4?: boolean;
  p5?: boolean;
  p6?: boolean;
  p7?: boolean;
  p8?: boolean;
  observaciones?: string;
  foto?: File;
}

export interface PetarRespuestaRequest {
  preguntaId?: number;
  respuesta?: boolean;
  observaciones?: string;
}

export interface EquipoProteccionRequest {
  equipoNombre: string;
  usado: boolean;
}

export interface PetarRequest {
  energiaPeligrosa?: boolean;
  trabajoAltura?: boolean;
  izaje?: boolean;
  excavacion?: boolean;
  espaciosConfinados?: boolean;
  trabajoCaliente?: boolean;
  otros?: boolean;
  otrosDescripcion?: string;
  velocidadAire?: string;
  contenidoOxigeno?: string;
  horaInicioPetar?: string;
  horaFinPetar?: string;
  respuestas?: PetarRespuestaRequest[];
  equiposProteccion?: EquipoProteccionRequest[];
}

export interface SsomaRequest {
  // Datos del formulario principal
  idOts: number;
  empresaId?: number;
  trabajoId?: number;
  latitud?: number;
  longitud?: number;
  fecha?: Date;
  horaInicio?: string;
  horaFin?: string;
  horaInicioTrabajo?: string;
  horaFinTrabajo?: string;
  supervisorTrabajo?: string;
  supervisorSst?: string;
  responsableArea?: string;
  
  // Datos de participantes
  participantes: ParticipanteRequest[];
  
  // Secuencias de tarea
  secuenciasTarea: SecuenciaTareaRequest[];
  
  // Checklist seguridad
  checklistSeguridad: ChecklistSeguridadRequest[];
  
  // EPP checks
  eppChecks: EppCheckRequest[];
  
  // Charla
  charla?: CharlaRequest;
  
  // Inspecciones trabajador
  inspeccionesTrabajador: InspeccionTrabajadorRequest[];
  
  // Herramientas inspección
  herramientasInspeccion: HerramientaInspeccionRequest[];
  
  // PETAR
  petar?: PetarRequest;
  
  // Fotos y videos
  fotosParticipantes?: FotoParticipanteRequest[];
  videoCharla?: VideoCharlaRequest;
  fotosEpp?: FotoEppRequest[];
  fotosHerramientas?: FotoHerramientaRequest[];
}

// =========================
// DTOs PARA RESPONSE
// =========================

export interface ParticipanteResponse {
  idParticipante: number;
  nombre: string;
  cargo: string;
  firmaUrl?: string;
  fotoUrls: string[];
}

export interface SecuenciaTareaResponse {
  secuenciaTarea: string;
  peligro: string;
  riesgo: string;
  consecuencias: string;
  medidasControl: string;
}

export interface ChecklistSeguridadResponse {
  itemNombre: string;
  usado: boolean;
  observaciones?: string;
  fotoUrls: string[];
}

export interface EppCheckResponse {
  eppNombre: string;
  usado: boolean;
  fotoUrls: string[];
}

export interface CharlaResponse {
  temaNombre?: string;
  fechaCharla: Date;
  duracionHoras?: number;
  capacitadorNombre?: string;
  videoUrl?: string;
  duracionSegundos?: number;
}

export interface InspeccionTrabajadorResponse {
  tipoInspeccion: string;
  trabajadorNombre?: string;
  casco?: boolean;
  lentes?: boolean;
  orejeras?: boolean;
  tapones?: boolean;
  guantes?: boolean;
  botas?: boolean;
  arnes?: boolean;
  chaleco?: boolean;
  mascarilla?: boolean;
  gafas?: boolean;
  otros?: string;
  accionCorrectiva?: string;
  seguimiento?: string;
  responsableNombre?: string;
}

export interface HerramientaInspeccionResponse {
  herramientaNombre: string;
  p1?: boolean;
  p2?: boolean;
  p3?: boolean;
  p4?: boolean;
  p5?: boolean;
  p6?: boolean;
  p7?: boolean;
  p8?: boolean;
  observaciones?: string;
  fotoUrls: string[];
}

export interface PetarRespuestaResponse {
  pregunta?: string;
  respuesta?: boolean;
  observaciones?: string;
}

export interface EquipoProteccionResponse {
  equipoNombre: string;
  usado: boolean;
}

export interface PetarResponse {
  energiaPeligrosa?: boolean;
  trabajoAltura?: boolean;
  izaje?: boolean;
  excavacion?: boolean;
  espaciosConfinados?: boolean;
  trabajoCaliente?: boolean;
  otros?: boolean;
  otrosDescripcion?: string;
  velocidadAire?: string;
  contenidoOxigeno?: string;
  horaInicioPetar?: string;
  horaFinPetar?: string;
  respuestas: PetarRespuestaResponse[];
  equiposProteccion: EquipoProteccionResponse[];
}

export interface SsomaResponse {
  idSsoma: number;
  idOts: number;
  empresaNombre?: string;
  trabajoNombre?: string;
  latitud?: number;
  longitud?: number;
  fecha: Date;
  horaInicio?: string;
  horaFin?: string;
  horaInicioTrabajo?: string;
  horaFinTrabajo?: string;
  supervisorTrabajo?: string;
  supervisorSst?: string;
  responsableArea?: string;
  
  participantes: ParticipanteResponse[];
  secuenciasTarea: SecuenciaTareaResponse[];
  checklistSeguridad: ChecklistSeguridadResponse[];
  eppChecks: EppCheckResponse[];
  charla?: CharlaResponse;
  inspeccionesTrabajador: InspeccionTrabajadorResponse[];
  herramientasInspeccion: HerramientaInspeccionResponse[];
  petar?: PetarResponse;
}

// =========================
// RESPONSE API
// =========================

export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
  error?: string;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}