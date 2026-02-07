import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';

// Interface basada en el controller Spring Boot
export interface SsomaResponseDTO {
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
  
  participantes: ParticipanteResponseDTO[];
  secuenciasTarea: SecuenciaTareaResponseDTO[];
  checklistSeguridad: ChecklistSeguridadResponseDTO[];
  eppChecks: EppCheckResponseDTO[];
  charla?: CharlaResponseDTO;
  inspeccionesTrabajador: InspeccionTrabajadorResponseDTO[];
  herramientasInspeccion: HerramientaInspeccionResponseDTO[];
  petar?: PetarResponseDTO;
}

// Sub-interfaces para la respuesta
export interface ParticipanteResponseDTO {
  idParticipante: number;
  nombre: string;
  cargo: string;
  firmaUrl?: string;
  fotoUrls: string[];
}

export interface SecuenciaTareaResponseDTO {
  secuenciaTarea: string;
  peligro: string;
  riesgo: string;
  consecuencias: string;
  medidasControl: string;
}

export interface ChecklistSeguridadResponseDTO {
  itemNombre: string;
  usado: boolean;
  observaciones?: string;
  fotoUrls: string[];
}

export interface EppCheckResponseDTO {
  eppNombre: string;
  usado: boolean;
  fotoUrls: string[];
}

export interface CharlaResponseDTO {
  temaNombre?: string;
  fechaCharla: Date;
  duracionHoras?: number;
  capacitadorNombre?: string;
  videoUrl?: string;
  duracionSegundos?: number;
}

export interface InspeccionTrabajadorResponseDTO {
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

export interface HerramientaInspeccionResponseDTO {
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

export interface PetarRespuestaResponseDTO {
  pregunta?: string;
  respuesta?: boolean;
  observaciones?: string;
}

export interface EquipoProteccionResponseDTO {
  equipoNombre: string;
  usado: boolean;
}

export interface PetarResponseDTO {
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
  respuestas: PetarRespuestaResponseDTO[];
  equiposProteccion: EquipoProteccionResponseDTO[];
}

// Interface para la request basada en SsomaRequestDTO del Spring Boot
export interface SsomaRequestDTO {
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
  participantes: ParticipanteRequestDTO[];
  
  // Secuencias de tarea
  secuenciasTarea: SecuenciaTareaRequestDTO[];
  
  // Checklist seguridad
  checklistSeguridad: ChecklistSeguridadRequestDTO[];
  
  // EPP checks
  eppChecks: EppCheckRequestDTO[];
  
  // Charla
  charla?: CharlaRequestDTO;
  
  // Inspecciones trabajador
  inspeccionesTrabajador: InspeccionTrabajadorRequestDTO[];
  
  // Herramientas inspección
  herramientasInspeccion: HerramientaInspeccionRequestDTO[];
  
  // PETAR
  petar?: PetarRequestDTO;
  
  // Fotos y videos (se manejan separadamente como archivos)
  fotosParticipantes?: FotoParticipanteRequestDTO[];
  videoCharla?: VideoCharlaRequestDTO;
  fotosEpp?: FotoEppRequestDTO[];
  fotosHerramientas?: FotoHerramientaRequestDTO[];
}

// Sub-interfaces para la request
export interface ParticipanteRequestDTO {
  trabajadorId?: number;
  nombre: string;
  cargo: string;
  firma?: File;
}

export interface SecuenciaTareaRequestDTO {
  secuenciaTarea: string;
  peligro: string;
  riesgo: string;
  consecuencias: string;
  medidasControl: string;
  orden: number;
}

export interface ChecklistSeguridadRequestDTO {
  itemNombre: string;
  usado: boolean;
  observaciones?: string;
  foto?: File;
}

export interface EppCheckRequestDTO {
  eppNombre: string;
  usado: boolean;
  foto?: File;
}

export interface CharlaRequestDTO {
  temaId?: number;
  fechaCharla?: Date;
  duracionHoras?: number;
  capacitadorId?: number;
}

export interface InspeccionTrabajadorRequestDTO {
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

export interface HerramientaInspeccionRequestDTO {
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

export interface PetarRespuestaRequestDTO {
  preguntaId?: number;
  respuesta?: boolean;
  observaciones?: string;
}

export interface EquipoProteccionRequestDTO {
  equipoNombre: string;
  usado: boolean;
}

export interface PetarRequestDTO {
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
  respuestas?: PetarRespuestaRequestDTO[];
  equiposProteccion: EquipoProteccionRequestDTO[];
}

// Interfaces para archivos
export interface FotoParticipanteRequestDTO {
  participanteIndex: number;
  foto: File;
  tipoFoto: 'FRONTAL' | 'CREDENCIAL';
}

export interface VideoCharlaRequestDTO {
  video: File;
  duracionSegundos?: number;
}

export interface FotoEppRequestDTO {
  eppIndex: number;
  foto: File;
}

export interface FotoHerramientaRequestDTO {
  herramientaIndex: number;
  foto: File;
}

@Injectable({
  providedIn: 'root'
})
export class SsomaService {
  
  private apiUrl = `${environment.baseUrl}/api/ssoma`;
  
  constructor(private http: HttpClient) { }
  
  /**
   * POST /api/ssoma/crear
   * Crea todo el formulario SSOMA completo (las 5 hojas)
   */
  crearFormularioCompleto(request: SsomaRequestDTO): Observable<SsomaResponseDTO> {
    const formData = this.convertirRequestAFormData(request);
    return this.http.post<SsomaResponseDTO>(`${this.apiUrl}/crear`, formData);
  }
  
  /**
   * GET /api/ssoma/{idSsoma}
   * Obtiene todo el formulario SSOMA por ID
   */
  obtenerFormularioCompleto(idSsoma: number): Observable<SsomaResponseDTO> {
    return this.http.get<SsomaResponseDTO>(`${this.apiUrl}/${idSsoma}`);
  }
  
  /**
   * GET /api/ssoma/ots/{idOts}
   * Obtiene todos los formularios SSOMA por OT
   */
  obtenerFormulariosPorOt(idOts: number): Observable<SsomaResponseDTO[]> {
    return this.http.get<SsomaResponseDTO[]>(`${this.apiUrl}/ots/${idOts}`);
  }
  
  /**
   * Convierte el request SSOMA a FormData para enviar archivos
   */
  private convertirRequestAFormData(request: SsomaRequestDTO): FormData {
    const formData = new FormData();
    
    // 1. Agregar todos los campos simples primero
    formData.append('idOts', request.idOts.toString());
    if (request.empresaId) formData.append('empresaId', request.empresaId.toString());
    if (request.trabajoId) formData.append('trabajoId', request.trabajoId.toString());
    if (request.latitud) formData.append('latitud', request.latitud.toString());
    if (request.longitud) formData.append('longitud', request.longitud.toString());
    if (request.fecha) formData.append('fecha', request.fecha.toISOString());
    if (request.horaInicio) formData.append('horaInicio', request.horaInicio);
    if (request.horaFin) formData.append('horaFin', request.horaFin);
    if (request.horaInicioTrabajo) formData.append('horaInicioTrabajo', request.horaInicioTrabajo);
    if (request.horaFinTrabajo) formData.append('horaFinTrabajo', request.horaFinTrabajo);
    if (request.supervisorTrabajo) formData.append('supervisorTrabajo', request.supervisorTrabajo);
    if (request.supervisorSst) formData.append('supervisorSst', request.supervisorSst);
    if (request.responsableArea) formData.append('responsableArea', request.responsableArea);
    
    // 2. Agregar arrays como elementos individuales del FormData
    this.agregarArrayAFormData(formData, 'participantes', request.participantes);
    this.agregarArrayAFormData(formData, 'secuenciasTarea', request.secuenciasTarea);
    this.agregarArrayAFormData(formData, 'checklistSeguridad', request.checklistSeguridad);
    this.agregarArrayAFormData(formData, 'eppChecks', request.eppChecks);
    this.agregarArrayAFormData(formData, 'inspeccionesTrabajador', request.inspeccionesTrabajador);
    this.agregarArrayAFormData(formData, 'herramientasInspeccion', request.herramientasInspeccion);
    
    // 3. Agregar objetos complejos
    if (request.charla) {
      this.agregarObjetoAFormData(formData, 'charla', request.charla);
    }
    
    if (request.petar) {
      if (request.petar.respuestas) {
        this.agregarArrayAFormData(formData, 'petar.respuestas', request.petar.respuestas);
      }
      if (request.petar.equiposProteccion) {
        this.agregarArrayAFormData(formData, 'petar.equiposProteccion', request.petar.equiposProteccion);
      }
      this.agregarObjetoPetarAFormData(formData, 'petar', request.petar);
    }
    
    // 4. Agregar archivos
    this.agregarArchivos(formData, request);
    
    return formData;
  }
  
  /**
   * Método auxiliar para agregar arrays al FormData
   */
  private agregarArrayAFormData(formData: FormData, prefix: string, array: any[]): void {
    if (!array || array.length === 0) return;
    
    array.forEach((item, index) => {
      const itemPrefix = `${prefix}[${index}]`;
      
      if (typeof item === 'object') {
        // Si es un objeto, agregar sus propiedades
        Object.keys(item).forEach(key => {
          const value = item[key];
          if (value !== undefined && value !== null && key !== 'foto' && key !== 'firma') {
            const fieldName = `${itemPrefix}.${key}`;
            if (value instanceof Date) {
              formData.append(fieldName, value.toISOString());
            } else if (typeof value === 'boolean') {
              formData.append(fieldName, value.toString());
            } else {
              formData.append(fieldName, value.toString());
            }
          }
        });
      } else {
        // Si es un valor simple
        formData.append(`${prefix}[]`, item.toString());
      }
    });
  }
  
  /**
   * Método auxiliar para agregar objetos al FormData
   */
  private agregarObjetoAFormData(formData: FormData, prefix: string, obj: any): void {
    if (!obj) return;
    
    Object.keys(obj).forEach(key => {
      const value = obj[key];
      if (value !== undefined && value !== null) {
        const fieldName = `${prefix}.${key}`;
        if (value instanceof Date) {
          formData.append(fieldName, value.toISOString());
        } else if (typeof value === 'boolean') {
          formData.append(fieldName, value.toString());
        } else if (typeof value === 'object' && !Array.isArray(value)) {
          // Objeto anidado
          this.agregarObjetoAFormData(formData, fieldName, value);
        } else {
          formData.append(fieldName, value.toString());
        }
      }
    });
  }
  
  /**
   * Método especial para PETAR ya que tiene arrays dentro
   */
  private agregarObjetoPetarAFormData(formData: FormData, prefix: string, petar: any): void {
    if (!petar) return;
    
    // Agregar campos simples del PETAR
    const camposSimples = [
      'energiaPeligrosa', 'trabajoAltura', 'izaje', 'excavacion',
      'espaciosConfinados', 'trabajoCaliente', 'otros',
      'otrosDescripcion', 'velocidadAire', 'contenidoOxigeno',
      'horaInicioPetar', 'horaFinPetar'
    ];
    
    camposSimples.forEach(campo => {
      const value = petar[campo];
      if (value !== undefined && value !== null) {
        const fieldName = `${prefix}.${campo}`;
        if (typeof value === 'boolean') {
          formData.append(fieldName, value.toString());
        } else {
          formData.append(fieldName, value.toString());
        }
      }
    });
  }
  
  /**
   * Método para agregar todos los archivos al FormData
   */
  private agregarArchivos(formData: FormData, request: SsomaRequestDTO): void {
    // Fotos de participantes
    if (request.fotosParticipantes) {
      request.fotosParticipantes.forEach((foto, index) => {
        formData.append(`fotosParticipantes[${index}].foto`, foto.foto);
        formData.append(`fotosParticipantes[${index}].participanteIndex`, foto.participanteIndex.toString());
        formData.append(`fotosParticipantes[${index}].tipoFoto`, foto.tipoFoto);
      });
    }
    
    // Video de charla
    if (request.videoCharla) {
      formData.append('videoCharla.video', request.videoCharla.video);
      if (request.videoCharla.duracionSegundos) {
        formData.append('videoCharla.duracionSegundos', request.videoCharla.duracionSegundos.toString());
      }
    }
    
    // Fotos de EPP
    if (request.fotosEpp) {
      request.fotosEpp.forEach((foto, index) => {
        formData.append(`fotosEpp[${index}].foto`, foto.foto);
        formData.append(`fotosEpp[${index}].eppIndex`, foto.eppIndex.toString());
      });
    }
    
    // Fotos de herramientas
    if (request.fotosHerramientas) {
      request.fotosHerramientas.forEach((foto, index) => {
        formData.append(`fotosHerramientas[${index}].foto`, foto.foto);
        formData.append(`fotosHerramientas[${index}].herramientaIndex`, foto.herramientaIndex.toString());
      });
    }
    
    // Firmas de participantes
    request.participantes.forEach((participante, index) => {
      if (participante.firma) {
        formData.append(`firmaParticipante${index}`, participante.firma);
      }
    });
    
    // Fotos de checklist
    request.checklistSeguridad.forEach((item, index) => {
      if (item.foto) {
        formData.append(`fotoChecklist${index}`, item.foto);
      }
    });
    
    // Fotos de EPP (directas)
    request.eppChecks.forEach((epp, index) => {
      if (epp.foto) {
        formData.append(`fotoEppDirecto${index}`, epp.foto);
      }
    });
    
    // Fotos de herramientas (directas)
    request.herramientasInspeccion.forEach((herramienta, index) => {
      if (herramienta.foto) {
        formData.append(`fotoHerramientaDirecto${index}`, herramienta.foto);
      }
    });
  }
  
  /**
   * Helper para crear un request SSOMA vacío
   */
  crearRequestVacio(): SsomaRequestDTO {
    return {
      idOts: 0,
      participantes: [],
      secuenciasTarea: [],
      checklistSeguridad: [],
      eppChecks: [],
      inspeccionesTrabajador: [],
      herramientasInspeccion: []
    };
  }
  
  /**
   * Helper para crear un participante vacío
   */
  crearParticipanteVacio(): ParticipanteRequestDTO {
    return {
      nombre: '',
      cargo: ''
    };
  }
  
  /**
   * Helper para crear una secuencia de tarea vacía
   */
  crearSecuenciaTareaVacia(): SecuenciaTareaRequestDTO {
    return {
      secuenciaTarea: '',
      peligro: '',
      riesgo: '',
      consecuencias: '',
      medidasControl: '',
      orden: 0
    };
  }
  
  /**
   * Helper para crear un checklist vacío
   */
  crearChecklistVacio(): ChecklistSeguridadRequestDTO {
    return {
      itemNombre: '',
      usado: false,
      observaciones: ''
    };
  }
  
  /**
   * Helper para crear un EPP check vacío
   */
  crearEppCheckVacio(): EppCheckRequestDTO {
    return {
      eppNombre: '',
      usado: false
    };
  }
}