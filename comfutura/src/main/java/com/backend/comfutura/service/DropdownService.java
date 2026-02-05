package com.backend.comfutura.service;

import com.backend.comfutura.record.DropdownDTO;
import java.util.List;

public interface DropdownService {

    // Métodos existentes
    List<DropdownDTO> getSiteCompuesto();
    List<DropdownDTO> getClientes();
    List<DropdownDTO> getAreas(); // <-- AGREGAR este método (sin filtro por cliente)
    List<DropdownDTO> getAreasByCliente(Integer idCliente);
    List<DropdownDTO> getProyectos();
    List<DropdownDTO> getFases();
    List<DropdownDTO> getSites();
    List<DropdownDTO> getSiteDescriptions();
    List<DropdownDTO> getRegiones();
    List<DropdownDTO> getEstadosOt(); // <-- AGREGAR este método
    List<DropdownDTO> getOtsActivas();
    List<DropdownDTO> getCargos();
    List<DropdownDTO> getEmpresas();
    List<DropdownDTO> getTrabajadores();
    List<DropdownDTO> getNivelesAll();
    List<DropdownDTO> getTrabajadoresSinUsuarioActivo();
    // Nuevos métodos para Site
    List<DropdownDTO> getDescripcionesBySiteCodigo(String codigoSite);
    List<DropdownDTO> getSitesConDescripciones();
    // Nuevos métodos para responsables
    List<DropdownDTO> getJefaturasClienteSolicitante();
    List<DropdownDTO> getAnalistasClienteSolicitante();
    List<DropdownDTO> getCoordinadoresTiCw();
    List<DropdownDTO> getJefaturasResponsable();
    List<DropdownDTO> getLiquidador();
    List<DropdownDTO> getEjecutantes();
    List<DropdownDTO> getAnalistasContable();
    List<DropdownDTO> getOtTipo();
    List<DropdownDTO> getMaestroCodigos();
    List<DropdownDTO> getProveedores();
    List<DropdownDTO> getTrabajos();
    List<DropdownDTO> getRolesTrabajo();
    List<DropdownDTO> getTareas();
    List<DropdownDTO> getTareasByTrabajo(Integer idTrabajo);
    List<DropdownDTO> getPeligros();
    List<DropdownDTO> getRiesgosByPeligro(Integer idPeligro);
    List<DropdownDTO> getMedidasByRiesgo(Integer idRiesgo);
    List<DropdownDTO> getEpps();
    List<DropdownDTO> getTiposRiesgoTrabajo();
    List<DropdownDTO> getHerramientas();
    List<DropdownDTO> getPreguntasPetar();
    List<DropdownDTO> getSupervisoresSST();
    List<DropdownDTO> getCapacitadores();
    List<DropdownDTO> getInspectores();
    List<DropdownDTO> getSupervisoresOperativos();
    List<DropdownDTO> getTrabajadoresByCargo(String cargo);
}