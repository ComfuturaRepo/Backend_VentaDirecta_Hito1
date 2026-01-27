// src/app/core/services/dropdown.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';
import { environment } from '../../environment';

export interface DropdownItem {
  id: number;
  label: string;
  adicional?: string; // 👈 AÑADIR ESTO
}

@Injectable({
  providedIn: 'root'
})
export class DropdownService {

  private apiUrl = `${environment.baseUrl}/api/dropdowns`;

  constructor(private http: HttpClient) {}

  // =============================
  // BÁSICOS - Maestros principales
  // =============================

  getClientes(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/clientes`);
  }

  getAreasByCliente(idCliente: number): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/clientes/${idCliente}/areas`);
  }

  getProyectos(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/proyectos`);
  }

  getFases(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/fases`);
  }

  getSites(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/sites`);
  }

  getRegiones(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/regiones`);
  }
   getEmpresas(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/empresas`);
  }
  getAreas(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/areas`);
  }
  getCargos(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/cargos`);
  }

  // =============================
  // RESPONSABLES (nuevos)
  // =============================

  getJefaturasClienteSolicitante(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/jefaturas-cliente-solicitante`);
  }

  getAnalistasClienteSolicitante(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/analistas-cliente-solicitante`);
  }

  getCoordinadoresTiCw(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/coordinadores-ti-cw`);
  }

  getJefaturasResponsable(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/jefaturas-responsable`);
  }

  getLiquidador(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/liquidadores`);
  }

  getEjecutantes(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/ejecutantes`);
  }

  getAnalistasContable(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/analistas-contable`);
  }

  getMaestroCodigo(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/maestro-codigos`);
  }
  // =============================
  // ORDEN DE COMPRA
  // =============================

  getOtsActivas(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/ots`);
  }


  getProveedores(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/proveedores`);
  }

  getEstadoOt(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/estado-ot`);
  }

  // =============================
  // CARGA MASIVA - Formularios completos
  // =============================

  /**
   * Carga TODOS los dropdowns necesarios para crear/editar una OT
   */
  loadOtFormDropdowns(): Observable<{
    clientes: DropdownItem[];
    proyectos: DropdownItem[];
    fases: DropdownItem[];
    sites: DropdownItem[];
    regiones: DropdownItem[];
    jefaturasClienteSolicitante: DropdownItem[];
    analistasClienteSolicitante: DropdownItem[];
    coordinadoresTiCw: DropdownItem[];
    jefaturasResponsable: DropdownItem[];
    liquidadores: DropdownItem[];
    ejecutantes: DropdownItem[];
    analistasContable: DropdownItem[];
    estadoOt: DropdownItem[];
  }> {
    return forkJoin({
      clientes: this.getClientes(),
      proyectos: this.getProyectos(),
      fases: this.getFases(),
      sites: this.getSites(),
      regiones: this.getRegiones(),
      jefaturasClienteSolicitante: this.getJefaturasClienteSolicitante(),
      analistasClienteSolicitante: this.getAnalistasClienteSolicitante(),
      coordinadoresTiCw: this.getCoordinadoresTiCw(),
      jefaturasResponsable: this.getJefaturasResponsable(),
      liquidadores: this.getLiquidador(),
      ejecutantes: this.getEjecutantes(),
      analistasContable: this.getAnalistasContable(),
      estadoOt:this.getEstadoOt()
    });
  }

loadOrdenCompraDropdowns(): Observable<{
  ots: DropdownItem[];
  maestros: DropdownItem[];     // ← agregado
  proveedores: DropdownItem[];
}> {
  return forkJoin({
    ots: this.getOtsActivas(),
    maestros: this.getMaestroCodigo(),    // ← agregado
    proveedores: this.getProveedores()
  });
}
}
