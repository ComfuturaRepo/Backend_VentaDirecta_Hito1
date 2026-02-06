import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environment';
import { AtsRequest, AtsResponse, CapacitacionRequest, CapacitacionResponse, InspeccionEppRequest, InspeccionEppResponse, InspeccionHerramientaRequest, InspeccionHerramientaResponse, PetarRequest, PetarResponse } from '../model/ssoma.model';
import { SsomaCompletoRequest, SsomaCompletoResponse } from '../model/ssoma-completo.model';


@Injectable({
  providedIn: 'root'
})
export class SsomaService {
  private apiUrl = `${environment.baseUrl}/api/ssoma`;

  constructor(private http: HttpClient) { }

  // =====================================================
  // ATS - Análisis de Trabajo Seguro
  // =====================================================

  // Crear ATS
  crearAts(atsRequest: AtsRequest): Observable<AtsResponse> {
    return this.http.post<AtsResponse>(`${this.apiUrl}/ats`, atsRequest);
  }

  // Obtener ATS por ID
  obtenerAtsPorId(id: number): Observable<AtsResponse> {
    return this.http.get<AtsResponse>(`${this.apiUrl}/ats/${id}`);
  }

  // Listar todos los ATS
  listarTodosAts(): Observable<AtsResponse[]> {
    return this.http.get<AtsResponse[]>(`${this.apiUrl}/ats`);
  }

  // =====================================================
  // CAPACITACIÓN - Charla de 5 minutos
  // =====================================================

  // Crear Capacitación
  crearCapacitacion(capacitacionRequest: CapacitacionRequest): Observable<CapacitacionResponse> {
    return this.http.post<CapacitacionResponse>(`${this.apiUrl}/capacitacion`, capacitacionRequest);
  }

  // Obtener Capacitación por ID
  obtenerCapacitacionPorId(id: number): Observable<CapacitacionResponse> {
    return this.http.get<CapacitacionResponse>(`${this.apiUrl}/capacitacion/${id}`);
  }

  // Listar todas las Capacitaciones
  listarTodasCapacitaciones(): Observable<CapacitacionResponse[]> {
    return this.http.get<CapacitacionResponse[]>(`${this.apiUrl}/capacitacion`);
  }

  // =====================================================
  // INSPECCIÓN EPP
  // =====================================================

  // Crear Inspección EPP
  crearInspeccionEpp(inspeccionEppRequest: InspeccionEppRequest): Observable<InspeccionEppResponse> {
    return this.http.post<InspeccionEppResponse>(`${this.apiUrl}/inspeccion-epp`, inspeccionEppRequest);
  }

  // Obtener Inspección EPP por ID
  obtenerInspeccionEppPorId(id: number): Observable<InspeccionEppResponse> {
    return this.http.get<InspeccionEppResponse>(`${this.apiUrl}/inspeccion-epp/${id}`);
  }

  // Listar todas las Inspecciones EPP
  listarTodasInspeccionesEpp(): Observable<InspeccionEppResponse[]> {
    return this.http.get<InspeccionEppResponse[]>(`${this.apiUrl}/inspeccion-epp`);
  }

  // =====================================================
  // INSPECCIÓN HERRAMIENTAS
  // =====================================================

  // Crear Inspección Herramienta
  crearInspeccionHerramienta(inspeccionHerramientaRequest: InspeccionHerramientaRequest): Observable<InspeccionHerramientaResponse> {
    return this.http.post<InspeccionHerramientaResponse>(`${this.apiUrl}/inspeccion-herramienta`, inspeccionHerramientaRequest);
  }

  // Obtener Inspección Herramienta por ID
  obtenerInspeccionHerramientaPorId(id: number): Observable<InspeccionHerramientaResponse> {
    return this.http.get<InspeccionHerramientaResponse>(`${this.apiUrl}/inspeccion-herramienta/${id}`);
  }

  // Listar todas las Inspecciones Herramienta
  listarTodasInspeccionesHerramientas(): Observable<InspeccionHerramientaResponse[]> {
    return this.http.get<InspeccionHerramientaResponse[]>(`${this.apiUrl}/inspeccion-herramienta`);
  }

  // =====================================================
  // PETAR - Permisos para Trabajos de Alto Riesgo
  // =====================================================

  // Crear PETAR
  crearPetar(petarRequest: PetarRequest): Observable<PetarResponse> {
    return this.http.post<PetarResponse>(`${this.apiUrl}/petar`, petarRequest);
  }

  // Obtener PETAR por ID
  obtenerPetarPorId(id: number): Observable<PetarResponse> {
    return this.http.get<PetarResponse>(`${this.apiUrl}/petar/${id}`);
  }

  // Listar todos los PETAR
  listarTodosPetar(): Observable<PetarResponse[]> {
    return this.http.get<PetarResponse[]>(`${this.apiUrl}/petar`);
  }

  // =====================================================
  // ENDPOINTS COMBINADOS
  // =====================================================

  // Crear SSOMA completo (todas las hojas en un solo request)
  crearSsomaCompleto(ssomaCompletoRequest: SsomaCompletoRequest): Observable<SsomaCompletoResponse> {
    return this.http.post<SsomaCompletoResponse>(`${this.apiUrl}/completo`, ssomaCompletoRequest);
  }

  // Obtener SSOMA completo por OT
  obtenerSsomaCompletoPorOts(idOts: number): Observable<SsomaCompletoResponse> {
    return this.http.get<SsomaCompletoResponse>(`${this.apiUrl}/por-ots/${idOts}`);
  }

  // =====================================================
  // MÉTODOS DE UTILIDAD
  // =====================================================

  // Formatear fecha para el backend
  formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  // Formatear hora para el backend
  formatTime(date: Date): string {
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);
    const seconds = ('0' + date.getSeconds()).slice(-2);
    return `${hours}:${minutes}:${seconds}`;
  }

  // Crear objeto ATSRequest por defecto
  crearAtsRequestDefault(): AtsRequest {
    return {
      empresa: '',
      lugarTrabajo: '',
      coordenadas: '',
      participantes: [],
      riesgos: [],
      eppIds: [],
      tipoRiesgoIds: []
    };
  }

  // Crear objeto CapacitacionRequest por defecto
  crearCapacitacionRequestDefault(): CapacitacionRequest {
    return {
      tema: '',
      tipoCharla: 'CHARLA_5_MINUTOS',
      asistentes: []
    };
  }

  // Crear objeto InspeccionEppRequest por defecto
  crearInspeccionEppRequestDefault(): InspeccionEppRequest {
    return {
      tipoInspeccion: 'PLANIFICADA',
      detalles: []
    };
  }

  // Crear objeto InspeccionHerramientaRequest por defecto
  crearInspeccionHerramientaRequestDefault(): InspeccionHerramientaRequest {
    return {
      detalles: []
    };
  }

  // Crear objeto PetarRequest por defecto
  crearPetarRequestDefault(): PetarRequest {
    return {
      requiereEvaluacionAmbiente: false,
      aperturaLineaEquipos: false,
      respuestas: [],
      trabajadoresAutorizadosIds: []
    };
  }

  // Crear objeto SsomaCompletoRequest por defecto
  crearSsomaCompletoRequestDefault(): SsomaCompletoRequest {
    return {
      ats: this.crearAtsRequestDefault(),
      capacitacion: this.crearCapacitacionRequestDefault(),
      inspeccionEpp: this.crearInspeccionEppRequestDefault(),
      inspeccionHerramienta: this.crearInspeccionHerramientaRequestDefault(),
      petar: this.crearPetarRequestDefault()
    };
  }
}
