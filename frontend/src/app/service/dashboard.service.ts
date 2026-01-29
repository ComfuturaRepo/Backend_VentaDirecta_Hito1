import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, catchError, map, throwError } from 'rxjs';
import { environment } from '../../environment';

// Interfaces para el dashboard
export interface DashboardOTDTO {
  generales?: EstadisticasGeneralesDTO;
  otsPorEstado?: OTPorEstadoDTO[];
  otsPorCliente?: OTPorClienteDTO[];
  otsPorArea?: OTPorAreaDTO[];
  otsPorProyecto?: OTPorProyectoDTO[];
  otsPorRegion?: OTPorRegionDTO[];
  evolucionMensual?: EvolucionMensualDTO;
  resumenCostos?: ResumenCostosDTO;
  otsPendientes?: OTPendienteDTO[];
}

export interface EstadisticasGeneralesDTO {
  totalOTs: number;
  otsActivas: number;
  otsCompletadas: number;
  otsPendientes: number;
  otsCanceladas: number;
  costoTotalOTs: number;
  costoPromedioOT: number;
  otsEsteMes: number;
  otsEsteAnio: number;
}

export interface OTPorEstadoDTO {
  estado: string;
  cantidad: number;
  porcentaje: number;
}

export interface OTPorClienteDTO {
  cliente: string;
  cantidad: number;
  porcentaje: number;
  costoTotal: number;
}

export interface OTPorAreaDTO {
  area: string;
  cantidad: number;
  porcentaje: number;
  costoTotal: number;
}

export interface OTPorProyectoDTO {
  proyecto: string;
  cantidad: number;
  porcentaje: number;
  costoTotal: number;
}

export interface OTPorRegionDTO {
  region: string;
  cantidad: number;
  porcentaje: number;
  costoTotal: number;
}

export interface EvolucionMensualDTO {
  meses: MesDTO[];
  costos: number[];
  cantidadOTs: number[];
}

export interface MesDTO {
  mes: string;
  anio: number;
}

export interface ResumenCostosDTO {
  costoMateriales: number;
  costoContratistas: number;
  costoGastosLogisticos: number;
  costoViaticos: number;
  costoPlanillas: number;
  costoTotal: number;
  porcentajeMateriales: number;
  porcentajeContratistas: number;
  porcentajeGastosLogisticos: number;
  porcentajeViaticos: number;
  porcentajePlanillas: number;
}

export interface OTPendienteDTO {
  ot: number;
  cliente: string;
  descripcion: string;
  estado: string;
  fechaApertura: string;
  diasPendientes: number;
  costoEstimado: number;
  responsable: string;
}

export interface FiltroDashboardDTO {
  fechaInicio?: string;
  fechaFin?: string;
  clienteId?: number;
  areaId?: number;
  proyectoId?: number;
  estadoId?: number;
  regionId?: number;
  siteId?: number;
  faseId?: number;
}

// Interface para respuesta de salud
export interface HealthResponse {
  status: string;
  timestamp: string;
}

@Injectable({
  providedIn: 'root'
})
export class DashboardService {
  private apiUrl = `${environment.baseUrl}/api/dashboard`;

  constructor(private http: HttpClient) {}

  // ===========================================
  // MÉTODOS PRINCIPALES
  // ===========================================

  /**
   * Obtener dashboard completo con filtros
   */
  getDashboard(filtro?: FiltroDashboardDTO): Observable<DashboardOTDTO> {
    const filtroBody = filtro || {};

    return this.http.post<DashboardOTDTO>(`${this.apiUrl}/ots`, filtroBody).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtener dashboard por cliente específico
   */
  getDashboardByCliente(clienteId: number): Observable<DashboardOTDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/cliente/${clienteId}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtener dashboard por área específica
   */
  getDashboardByArea(areaId: number): Observable<DashboardOTDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/area/${areaId}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtener dashboard por proyecto específico
   */
  getDashboardByProyecto(proyectoId: number): Observable<DashboardOTDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/proyecto/${proyectoId}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtener dashboard por estado específico
   */
  getDashboardByEstado(estadoId: number): Observable<DashboardOTDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/estado/${estadoId}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtener dashboard por rango de fechas personalizado
   */
  getDashboardByFecha(fechaInicio?: Date, fechaFin?: Date): Observable<DashboardOTDTO> {
    let params = new URLSearchParams();

    if (fechaInicio) {
      params.append('fechaInicio', fechaInicio.toISOString().split('T')[0]);
    }

    if (fechaFin) {
      params.append('fechaFin', fechaFin.toISOString().split('T')[0]);
    }

    const queryParams = params.toString();
    const url = `${this.apiUrl}/ots/fecha${queryParams ? '?' + queryParams : ''}`;

    return this.http.get<DashboardOTDTO>(url).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtener dashboard por rango de tiempo predefinido
   */
  getDashboardByRango(rango: string): Observable<DashboardOTDTO> {
    const rangosValidos = ['HOY', 'SEMANA', 'MES', 'TRIMESTRE', 'SEMESTRE', 'ANIO'];

    if (!rangosValidos.includes(rango.toUpperCase())) {
      return throwError(() => new Error(`Rango inválido. Use: ${rangosValidos.join(', ')}`));
    }

    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/rango/${rango}`).pipe(
      catchError(this.handleError)
    );
  }

  // ===========================================
  // MÉTODOS ESPECÍFICOS PARA COMPONENTES
  // ===========================================

  /**
   * Obtener solo las estadísticas generales
   */
  getEstadisticasGenerales(): Observable<EstadisticasGeneralesDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/estadisticas-generales`).pipe(
      map(response => response.generales || this.getEstadisticasDefaults()),
      catchError(this.handleError)
    );
  }

  /**
   * Obtener solo las OTs pendientes
   */
  getOTsPendientes(): Observable<OTPendienteDTO[]> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/pendientes`).pipe(
      map(response => response.otsPendientes || []),
      catchError(this.handleError)
    );
  }

  /**
   * Obtener solo el resumen de costos
   */
  getResumenCostos(): Observable<ResumenCostosDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/resumen-costos`).pipe(
      map(response => response.resumenCostos || this.getResumenCostosDefaults()),
      catchError(this.handleError)
    );
  }

  /**
   * Obtener solo OTs por estado (para gráficos)
   */
  getOTsPorEstado(): Observable<OTPorEstadoDTO[]> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/por-estado`).pipe(
      map(response => response.otsPorEstado || []),
      catchError(this.handleError)
    );
  }

  /**
   * Obtener solo OTs por cliente (para top 10)
   */
  getOTsPorCliente(): Observable<OTPorClienteDTO[]> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/por-cliente`).pipe(
      map(response => response.otsPorCliente || []),
      catchError(this.handleError)
    );
  }

  /**
   * Obtener evolución mensual (para gráficos de línea)
   */
  getEvolucionMensual(): Observable<EvolucionMensualDTO> {
    return this.http.get<DashboardOTDTO>(`${this.apiUrl}/ots/evolucion-mensual`).pipe(
      map(response => response.evolucionMensual || this.getEvolucionMensualDefaults()),
      catchError(this.handleError)
    );
  }

  // ===========================================
  // MÉTODOS DE UTILIDAD
  // ===========================================

  /**
   * Verificar salud del servicio de dashboard
   */
  healthCheck(): Observable<string> {
    return this.http.get(`${this.apiUrl}/health`, { responseType: 'text' }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Formatear moneda
   */
  formatCurrency(value: number): string {
    if (value === null || value === undefined) return 'S/ 0.00';

    return new Intl.NumberFormat('es-PE', {
      style: 'currency',
      currency: 'PEN',
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }).format(value);
  }

  /**
   * Formatear número
   */
  formatNumber(value: number): string {
    if (value === null || value === undefined) return '0';

    return new Intl.NumberFormat('es-PE', {
      minimumFractionDigits: 0,
      maximumFractionDigits: 2
    }).format(value);
  }

  /**
   * Obtener color por estado
   */
  getColorPorEstado(estado: string): string {
    const colores: { [key: string]: string } = {
      'PENDIENTE': '#ffc107',      // Amarillo
      'EN PROCESO': '#17a2b8',     // Azul claro
      'COMPLETADO': '#28a745',     // Verde
      'CANCELADO': '#dc3545',      // Rojo
      'APROBADO': '#007bff',       // Azul
      'REVISION': '#6c757d',       // Gris
      'ASIGNACION': '#6f42c1',     // Púrpura
      'EJECUCION': '#fd7e14',      // Naranja
      'FINALIZADO': '#20c997'      // Verde agua
    };
    return colores[estado.toUpperCase()] || '#6c757d';
  }

  /**
   * Obtener icono por estado
   */
  getIconPorEstado(estado: string): string {
    const iconos: { [key: string]: string } = {
      'PENDIENTE': 'bi-clock',
      'EN PROCESO': 'bi-gear',
      'COMPLETADO': 'bi-check-circle',
      'CANCELADO': 'bi-x-circle',
      'APROBADO': 'bi-check-lg',
      'REVISION': 'bi-eye',
      'ASIGNACION': 'bi-person-check',
      'EJECUCION': 'bi-lightning',
      'FINALIZADO': 'bi-flag'
    };
    return iconos[estado.toUpperCase()] || 'bi-question-circle';
  }

  /**
   * Calcular porcentaje
   */
  calcularPorcentaje(parcial: number, total: number, decimales: number = 2): number {
    if (!total || total === 0) return 0;

    const porcentaje = (parcial / total) * 100;
    return parseFloat(porcentaje.toFixed(decimales));
  }

  /**
   * Obtener rangos disponibles
   */
  getRangosDisponibles(): { value: string, label: string }[] {
    return [
      { value: 'HOY', label: 'Hoy' },
      { value: 'SEMANA', label: 'Esta semana' },
      { value: 'MES', label: 'Este mes' },
      { value: 'TRIMESTRE', label: 'Este trimestre' },
      { value: 'SEMESTRE', label: 'Este semestre' },
      { value: 'ANIO', label: 'Este año' }
    ];
  }

  /**
   * Obtener estados disponibles (para filtros)
   */
  getEstadosDisponibles(): { id: number, nombre: string }[] {
    return [
      { id: 1, nombre: 'PENDIENTE' },
      { id: 2, nombre: 'EN PROCESO' },
      { id: 3, nombre: 'COMPLETADO' },
      { id: 4, nombre: 'CANCELADO' },
      { id: 5, nombre: 'APROBADO' },
      { id: 6, nombre: 'REVISION' }
    ];
  }

  /**
   * Formatear fecha para visualización
   */
  formatFecha(fecha: string | Date): string {
    if (!fecha) return 'N/A';

    const fechaObj = typeof fecha === 'string' ? new Date(fecha) : fecha;

    return new Intl.DateTimeFormat('es-PE', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric'
    }).format(fechaObj);
  }

  /**
   * Calcular días desde fecha
   */
  calcularDiasDesde(fecha: string | Date): number {
    if (!fecha) return 0;

    const fechaObj = typeof fecha === 'string' ? new Date(fecha) : fecha;
    const hoy = new Date();
    const diffTime = Math.abs(hoy.getTime() - fechaObj.getTime());
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

    return diffDays;
  }

  // ===========================================
  // MÉTODOS PRIVADOS
  // ===========================================

  /**
   * Manejo de errores
   */
  private handleError(error: any): Observable<never> {
    console.error('Error en DashboardService:', error);

    let errorMessage = 'Error desconocido';

    if (error.error instanceof ErrorEvent) {
      // Error del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del servidor
      errorMessage = `Código: ${error.status}\nMensaje: ${error.message}`;
    }

    return throwError(() => new Error(errorMessage));
  }

  /**
   * Valores por defecto para estadísticas
   */
  private getEstadisticasDefaults(): EstadisticasGeneralesDTO {
    return {
      totalOTs: 0,
      otsActivas: 0,
      otsCompletadas: 0,
      otsPendientes: 0,
      otsCanceladas: 0,
      costoTotalOTs: 0,
      costoPromedioOT: 0,
      otsEsteMes: 0,
      otsEsteAnio: 0
    };
  }

  /**
   * Valores por defecto para resumen de costos
   */
  private getResumenCostosDefaults(): ResumenCostosDTO {
    return {
      costoMateriales: 0,
      costoContratistas: 0,
      costoGastosLogisticos: 0,
      costoViaticos: 0,
      costoPlanillas: 0,
      costoTotal: 0,
      porcentajeMateriales: 0,
      porcentajeContratistas: 0,
      porcentajeGastosLogisticos: 0,
      porcentajeViaticos: 0,
      porcentajePlanillas: 0
    };
  }

  /**
   * Valores por defecto para evolución mensual
   */
  private getEvolucionMensualDefaults(): EvolucionMensualDTO {
    return {
      meses: [],
      costos: [],
      cantidadOTs: []
    };
  }

  // ===========================================
  // MÉTODOS PARA CACHE Y OPTIMIZACIÓN
  // ===========================================

  private cache: Map<string, { data: any, timestamp: number }> = new Map();
  private cacheDuration = 5 * 60 * 1000; // 5 minutos en milisegundos

  /**
   * Obtener datos con cache
   */
  getWithCache<T>(key: string, fetchFn: () => Observable<T>): Observable<T> {
    const cached = this.cache.get(key);
    const now = Date.now();

    if (cached && (now - cached.timestamp) < this.cacheDuration) {
      return new Observable(observer => {
        observer.next(cached.data);
        observer.complete();
      });
    }

    return fetchFn().pipe(
      map(data => {
        this.cache.set(key, { data, timestamp: now });
        return data;
      })
    );
  }

  /**
   * Limpiar cache
   */
  clearCache(key?: string): void {
    if (key) {
      this.cache.delete(key);
    } else {
      this.cache.clear();
    }
  }

  /**
   * Obtener dashboard con cache
   */
  getDashboardCached(filtro?: FiltroDashboardDTO): Observable<DashboardOTDTO> {
    const cacheKey = filtro ? `dashboard-${JSON.stringify(filtro)}` : 'dashboard-default';

    return this.getWithCache(cacheKey, () => this.getDashboard(filtro));
  }

  /**
   * Obtener OTs pendientes con cache
   */
  getOTsPendientesCached(): Observable<OTPendienteDTO[]> {
    return this.getWithCache('ots-pendientes', () => this.getOTsPendientes());
  }

  /**
   * Obtener estadísticas generales con cache
   */
  getEstadisticasGeneralesCached(): Observable<EstadisticasGeneralesDTO> {
    return this.getWithCache('estadisticas-generales', () => this.getEstadisticasGenerales());
  }
}
