package com.backend.comfutura.service;

import com.backend.comfutura.record.DropdownDTO;

import java.util.List;

public interface DropdownService {

    // Métodos existentes
    List<DropdownDTO> getClientes();
    List<DropdownDTO> getAreasByCliente(Integer idCliente);
    List<DropdownDTO> getProyectos();
    List<DropdownDTO> getFases();
    List<DropdownDTO> getSites();
    List<DropdownDTO> getRegiones();
    List<DropdownDTO> getOtsActivas();

    // Nuevos métodos para responsables
    List<DropdownDTO> getJefaturasClienteSolicitante();
    List<DropdownDTO> getAnalistasClienteSolicitante();
    List<DropdownDTO> getCoordinadoresTiCw();
    List<DropdownDTO> getJefaturasResponsable();
    List<DropdownDTO> getLiquidador();
    List<DropdownDTO> getEjecutantes();
    List<DropdownDTO> getAnalistasContable();

    List<DropdownDTO> getMaestroCodigos();

    List<DropdownDTO> getProveedores();
}