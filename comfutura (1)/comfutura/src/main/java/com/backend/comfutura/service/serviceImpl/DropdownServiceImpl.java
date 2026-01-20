package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DropdownServiceImpl implements DropdownService {

    private final ClienteRepository clienteRepository;
    private final AreaRepository areaRepository;
    private final ProyectoRepository proyectoRepository;
    private final FaseRepository faseRepository;
    private final SiteRepository siteRepository;
    private final RegionRepository regionRepository;
    private final OtsRepository otsRepository;

    /**
     * Retorna todos los clientes activos para el dropdown
     */
    @Override
    public List<DropdownDTO> getClientes() {
        return clienteRepository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(c -> new DropdownDTO(c.getId(), c.getRazonSocial()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna las áreas activas de un cliente específico
     */
    @Override
    public List<DropdownDTO> getAreasByCliente(Integer idCliente) {
        return areaRepository.findByClienteIdAndActivoTrue(idCliente)
                .stream()
                .map(a -> new DropdownDTO(a.getId(), a.getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos los proyectos activos
     */
    @Override
    public List<DropdownDTO> getProyectos() {
        return proyectoRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(p -> new DropdownDTO(p.getIdProyecto(), p.getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las fases activas ordenadas por nombre
     */
    @Override
    public List<DropdownDTO> getFases() {
        return faseRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(f -> new DropdownDTO(f.getIdFase(), f.getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna todos los sites activos ordenados por nombre
     */
    @Override
    public List<DropdownDTO> getSites() {
        return siteRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(s -> new DropdownDTO(s.getIdSite(), s.getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * Retorna todas las regiones activas
     */
    @Override
    public List<DropdownDTO> getRegiones() {
        return regionRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getIdRegion(), r.getNombre()))
                .collect(Collectors.toList());
    }

    /**
     * Opcional: Retorna todas las OTs activas con formato "OT XXX"
     */
    @Override
    public List<DropdownDTO> getOtsActivas() {
        return otsRepository.findByActivoTrueOrderByOtAsc()
                .stream()
                .map(ot -> new DropdownDTO(ot.getIdOts(), "OT " + ot.getOt()))
                .collect(Collectors.toList());
    }
}