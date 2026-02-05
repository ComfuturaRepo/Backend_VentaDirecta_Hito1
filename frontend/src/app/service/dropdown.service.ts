// src/app/core/services/dropdown.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, forkJoin, map, switchMap } from 'rxjs';
import { environment } from '../../environment';

export interface DropdownItem {
  id: number;
  label: string;
  adicional?: string;
  estado?: boolean;
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
   getSiteCompuesto(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/sitesCompuesto`);
  }

  getSites(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/sites`);
  }

  getRegiones(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/regiones`);
  }

   getTipoOt(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/tipoOt`);
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

getDescripcionesBySiteCodigo(siteCodigo: string): Observable<DropdownItem[]> {
  const params = new HttpParams().set('siteCodigo', siteCodigo || '');
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/DescripcionesBySiteCodigo`, { params });
}


getSitesConDescripciones(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/SitesConDescripciones`);
}

  // =============================
  // ORDEN DE COMPRA
  // =============================

  getOtsActivas(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/ots`);
  }

    getTrabajadores(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/trabajador`);
  }

    getTrabajadoresSinUSuarioActivo(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/trabajadorSinUsuarioActivo`);
  }

  getNivel(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/nivel`);
  }

  getProveedores(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/proveedores`);
  }

  getEstadoOt(): Observable<DropdownItem[]> {
    return this.http.get<DropdownItem[]>(`${this.apiUrl}/estado-ot`);
  }

// src/app/core/services/dropdown.service.ts

// Agrega estos métodos a la clase DropdownService

// =============================
// SSOMA - CATÁLOGOS PRINCIPALES
// =============================

getTrabajos(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/trabajos`);
}

getRolesTrabajo(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/roles-trabajo`);
}

getTareas(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/tareas`);
}

getTareasByTrabajo(idTrabajo: number): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/tareas/trabajo/${idTrabajo}`);
}

getPeligros(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/peligros`);
}

getRiesgosByPeligro(idPeligro: number): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/riesgos/peligro/${idPeligro}`);
}

getMedidasByRiesgo(idRiesgo: number): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/medidas/riesgo/${idRiesgo}`);
}

getEpps(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/epps`);
}

getTiposRiesgoTrabajo(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/tipos-riesgo`);
}

getHerramientas(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/herramientas`);
}

getPreguntasPetar(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/preguntas-petar`);
}

// =============================
// SSOMA - TRABAJADORES ESPECIALIZADOS
// =============================

getSupervisoresSST(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/supervisores-sst`);
}

getCapacitadores(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/capacitadores`);
}

getInspectores(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/inspectores`);
}

getSupervisoresOperativos(): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/supervisores-operativos`);
}

getTrabajadoresByCargo(cargo: string): Observable<DropdownItem[]> {
  return this.http.get<DropdownItem[]>(`${this.apiUrl}/ssoma/trabajadores/cargo/${cargo}`);
}

// =============================
// SSOMA - CARGA MASIVA PARA FORMULARIO COMPLETO
// =============================

loadSsomaFormDropdowns(): Observable<{
  trabajos: DropdownItem[];
  rolesTrabajo: DropdownItem[];
  peligros: DropdownItem[];
  epps: DropdownItem[];
  tiposRiesgo: DropdownItem[];
  herramientas: DropdownItem[];
  preguntasPetar: DropdownItem[];
  trabajadores: DropdownItem[];
  supervisoresSST: DropdownItem[];
  capacitadores: DropdownItem[];
  inspectores: DropdownItem[];
  supervisoresOperativos: DropdownItem[];
}> {
  return forkJoin({
    trabajos: this.getTrabajos(),
    rolesTrabajo: this.getRolesTrabajo(),
    peligros: this.getPeligros(),
    epps: this.getEpps(),
    tiposRiesgo: this.getTiposRiesgoTrabajo(),
    herramientas: this.getHerramientas(),
    preguntasPetar: this.getPreguntasPetar(),
    trabajadores: this.getTrabajadores(),
    supervisoresSST: this.getSupervisoresSST(),
    capacitadores: this.getCapacitadores(),
    inspectores: this.getInspectores(),
    supervisoresOperativos: this.getSupervisoresOperativos()
  });
}

// =============================
// SSOMA - CARGA PARA ATS (con dependencias)
// =============================

loadAtsDropdowns(idTrabajo: number): Observable<{
  tareas: DropdownItem[];
  trabajadores: DropdownItem[];
  rolesTrabajo: DropdownItem[];
  epps: DropdownItem[];
  tiposRiesgo: DropdownItem[];
  peligros: DropdownItem[];
}> {
  return forkJoin({
    tareas: this.getTareasByTrabajo(idTrabajo),
    trabajadores: this.getTrabajadores(),
    rolesTrabajo: this.getRolesTrabajo(),
    epps: this.getEpps(),
    tiposRiesgo: this.getTiposRiesgoTrabajo(),
    peligros: this.getPeligros()
  });
}

// =============================
// SSOMA - CARGA JERÁRQUICA (Riesgos y Medidas)
// =============================

loadRiesgosMedidasDropdowns(idPeligro: number): Observable<{
  riesgos: DropdownItem[];
  medidas: DropdownItem[];
}> {
  // Primero cargar riesgos, luego para cada riesgo cargar medidas
  return this.getRiesgosByPeligro(idPeligro).pipe(
    switchMap(riesgos => {
      const medidasObservables = riesgos.map(riesgo =>
        this.getMedidasByRiesgo(riesgo.id)
      );
      return forkJoin(medidasObservables).pipe(
        map(medidasArray => ({
          riesgos,
          medidas: medidasArray.flat() // Aplanar array de arrays
        }))
      );
    })
  );
}
loadOtFormDropdowns(): Observable<{
  clientes: DropdownItem[];
  proyectos: DropdownItem[];
  fases: DropdownItem[];
  sites: DropdownItem[];
  regiones: DropdownItem[];
  tipoOt: DropdownItem[]; // ✅ AGREGAR ESTA LÍNEA
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
    tipoOt: this.getTipoOt(), // ✅ AGREGAR ESTA LÍNEA
    jefaturasClienteSolicitante: this.getJefaturasClienteSolicitante(),
    analistasClienteSolicitante: this.getAnalistasClienteSolicitante(),
    coordinadoresTiCw: this.getCoordinadoresTiCw(),
    jefaturasResponsable: this.getJefaturasResponsable(),
    liquidadores: this.getLiquidador(),
    ejecutantes: this.getEjecutantes(),
    analistasContable: this.getAnalistasContable(),
    estadoOt: this.getEstadoOt()
  });
}
loadOrdenCompraDropdowns(): Observable<{
  ots: DropdownItem[];
  maestros: DropdownItem[];     // ← agregado
  proveedores: DropdownItem[];
}> {
  return forkJoin({
    ots: this.getOtsActivas(),
    maestros: this.getEmpresas(),    // ← agregado
    proveedores: this.getProveedores()
  });
}
}
