package com.backend.comfutura.service;



import com.backend.comfutura.dto.DropTown.AreaDropdownDTO;
import com.backend.comfutura.dto.DropTown.ClienteDropdownDTO;

import java.util.List;

public interface DropdownService {
    List<ClienteDropdownDTO> getAllClientesActivos();
    List<AreaDropdownDTO> getAllAreasActivas();
}