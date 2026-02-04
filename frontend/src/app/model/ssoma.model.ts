export interface ATSParticipanteDTO {
  idTrabajador: number;
  idRol: number;
}

export interface ATSRiesgoDTO {
  idTarea: number;
  idPeligro: number;
  idRiesgo: number;
  idMedida: number;
}

export interface ATSRequestDTO {
  fecha: string;
  hora: string;
  empresa: string;
  lugarTrabajo: string;
  idTrabajo: number;
  participantes: ATSParticipanteDTO[];
  riesgos: ATSRiesgoDTO[];
  eppIds: number[];
  tipoRiesgoIds: number[];
}

// DTOs para formulario completo SST
export interface AsistenteDTO {
  idTrabajador: number;
  observaciones: string;
}

export interface InspeccionEPPDetalleDTO {
  idTrabajador: number;
  idEPP: number;
  cumple: boolean;
  observacion: string;
}

export interface InspeccionHerramientaDetalleDTO {
  idHerramienta: number;
  cumple: boolean;
  fotoUrl: string;
  observacion: string;
}

export interface PETARRespuestaDTO {
  idPregunta: number;
  respuesta: boolean;
}

export interface SSTFormRequestDTO {
  empresa: string;
  lugarTrabajo: string;
  fecha: string;
  hora: string;

  ats: {
    idTrabajo: number;
    participantes: ATSParticipanteDTO[];
    riesgos: ATSRiesgoDTO[];
    eppIds: number[];
    tipoRiesgoIds: number[];
  };

  capacitacion: {
    tema: string;
    fecha: string;
    hora: string;
    idCapacitador: number;
    asistentes: AsistenteDTO[];
  };

  inspeccionEPP: {
    idInspector: number;
    detalles: InspeccionEPPDetalleDTO[];
  };

  inspeccionHerramienta: {
    idSupervisor: number;
    detalles: InspeccionHerramientaDetalleDTO[];
  };

  petar: {
    respuestas: PETARRespuestaDTO[];
    trabajadoresAutorizadosIds: number[];
  };
}

// DTOs de respuesta
export interface ATSListDTO {
  idATS: number;
  fecha: string;
  hora: string;
  empresa: string;
  lugarTrabajo: string;
  trabajo: string;
  cantidadParticipantes: number;
}

export interface TrabajoDTO {
  idTrabajo: number;
  nombre: string;
}

export interface ParticipanteDTO {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  cargo: string;
  rol: string;
}

export interface RiesgoDTO {
  tarea: string;
  peligro: string;
  riesgo: string;
  medida: string;
}

export interface EPPDTO {
  idEPP: number;
  nombre: string;
}

export interface TipoRiesgoDTO {
  id: number;
  nombre: string;
}

export interface ATSResponseDTO {
  idATS: number;
  fecha: string;
  hora: string;
  empresa: string;
  lugarTrabajo: string;
  trabajo: TrabajoDTO;
  participantes: ParticipanteDTO[];
  riesgos: RiesgoDTO[];
  epps: EPPDTO[];
  tiposRiesgo: TipoRiesgoDTO[];
}

export interface SSTFormResponseDTO {
  idATS: number;
  idCapacitacion: number;
  idInspeccionEPP: number;
  idInspeccionHerramienta: number;
  idPETAR: number;

  numeroRegistroATS: string;
  numeroRegistroCapacitacion: string;
  numeroRegistroInspeccionEPP: string;
  numeroRegistroInspeccionHerramienta: string;
  numeroRegistroPETAR: string;

  fecha: string;
  hora: string;
  empresa: string;
  lugarTrabajo: string;

  mensaje: string;
}

export interface PageResponse<T> {
  content: T[];
  pageable: {
    pageNumber: number;
    pageSize: number;
    sort: {
      empty: boolean;
      sorted: boolean;
      unsorted: boolean;
    };
    offset: number;
    paged: boolean;
    unpaged: boolean;
  };
  last: boolean;
  totalElements: number;
  totalPages: number;
  first: boolean;
  size: number;
  number: number;
  sort: {
    empty: boolean;
    sorted: boolean;
    unsorted: boolean;
  };
  numberOfElements: number;
  empty: boolean;
}
// Agrega estos modelos al archivo ssoma.model.ts

export interface TrabajadorDTO {
  idTrabajador: number;
  nombres: string;
  apellidos: string;
  dni: string;
  cargo: string;
  area: string;
  firmaUrl?: string;
  telefono?: string;
  email?: string;
}

export interface TareaDTO {
  idTarea: number;
  idTrabajo: number;
  nombre: string;
  descripcion: string;
  orden: number;
}

export interface PeligroDTO {
  idPeligro: number;
  descripcion: string;
  categoria: string;
}

export interface RiesgoDTO {
  idRiesgo: number;
  idPeligro: number;
  descripcion: string;
  consecuencia: string;
  nivelRiesgo: 'BAJO' | 'MEDIO' | 'ALTO' | 'CRITICO';
}

export interface MedidaControlDTO {
  idMedida: number;
  idRiesgo: number;
  descripcion: string;
  tipo: 'PREVENTIVA' | 'CORRECTIVA' | 'MITIGACION';
}

export interface PreguntaPETARDTO {
  idPregunta: number;
  texto: string;
  categoria: string;
  orden: number;
}

export interface HerramientaDTO {
  idHerramienta: number;
  nombre: string;
  tipo: string;
  codigo: string;
  estado: 'DISPONIBLE' | 'EN_USO' | 'MANTENIMIENTO' | 'BAJA';
}

export interface ClienteDTO {
  idCliente: number;
  nombre: string;
  ruc: string;
  direccion: string;
  telefono: string;
  email: string;
}

export interface ProyectoDTO {
  idProyecto: number;
  idCliente: number;
  nombre: string;
  ubicacion: string;
  fechaInicio: string;
  fechaFin: string;
  estado: 'ACTIVO' | 'FINALIZADO' | 'SUSPENDIDO';
}
