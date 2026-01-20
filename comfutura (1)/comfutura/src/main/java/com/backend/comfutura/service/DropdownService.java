package com.backend.comfutura.service;

import com.backend.comfutura.record.DropdownDTO;

import java.util.List;

public interface DropdownService {

    List<DropdownDTO> getClientes();
    List<DropdownDTO> getAreas();
    List<DropdownDTO> getProyectos();
    List<DropdownDTO> getFases();
    List<DropdownDTO> getSites();
    List<DropdownDTO> getRegiones();
    List<DropdownDTO> getOtsActivas();
     List<DropdownDTO> getTrabajadores();
     List<DropdownDTO> getMaestroCodigos();
     List<DropdownDTO> getProveedores();
}
