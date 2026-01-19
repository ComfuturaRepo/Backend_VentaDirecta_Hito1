package com.backend.comfutura.service.serviceImpl;
import com.backend.comfutura.dto.DropTown.AreaDropdownDTO;
import com.backend.comfutura.dto.DropTown.ClienteDropdownDTO;
import com.backend.comfutura.repository.AreaRepository;
import com.backend.comfutura.repository.ClienteRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.service.DropdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DropdownServiceImpl implements DropdownService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private OtsRepository otsRepository; // Asumiendo que tienes un repo para OTS para la consulta dependiente

    @Override
    public List<ClienteDropdownDTO> getAllClientesActivos() {
        return clienteRepository.findByActivoTrueOrderByRazonSocialAsc().stream()
                .map(c -> new ClienteDropdownDTO(c.getId(), c.getRazonSocial(), c.getRuc()))
                .collect(Collectors.toList());
    }

    @Override
    public List<AreaDropdownDTO> getAllAreasActivas() {
        return areaRepository.findByActivoTrueOrderByNombreAsc().stream()
                .map(a -> new AreaDropdownDTO(a.getId(), a.getNombre()))
                .collect(Collectors.toList());
    }

}