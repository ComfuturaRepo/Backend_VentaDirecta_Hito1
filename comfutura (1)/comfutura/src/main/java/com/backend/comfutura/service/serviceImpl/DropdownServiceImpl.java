package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.*;
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

    // Repositorios existentes
    private final ClienteRepository clienteRepository;
    private final AreaRepository areaRepository;
    private final ProyectoRepository proyectoRepository;
    private final FaseRepository faseRepository;
    private final SiteRepository siteRepository;
    private final RegionRepository regionRepository;
    private final OtsRepository otsRepository;

    // Nuevos repositorios para los responsables (agrega estos en tu proyecto)
    private final JefaturaClienteSolicitanteRepository jefaturaClienteSolicitanteRepository;
    private final AnalistaClienteSolicitanteRepository analistaClienteSolicitanteRepository;
    private final CoordinadorTiCwRepository coordinadorTiCwRepository;
    private final JefaturaResponsableRepository jefaturaResponsableRepository;
    private final LiquidadorRepository liquidadorRepository;
    private final EjecutanteRepository ejecutanteRepository;
    private final AnalistaContableRepository analistaContableRepository;

    // ────────────────────────────────────────────────────────
    // Métodos existentes (sin cambios)
    // ────────────────────────────────────────────────────────

    @Override
    public List<DropdownDTO> getClientes() {
        return clienteRepository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(c -> new DropdownDTO(c.getId(), c.getRazonSocial()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getAreasByCliente(Integer idCliente) {
        // Asumiendo que tienes un método en AreaRepository que filtra por cliente
        return areaRepository.findByClienteIdAndActivoTrue(idCliente)
                .stream()
                .map(a -> new DropdownDTO(a.getId(), a.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getProyectos() {
        return proyectoRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(p -> new DropdownDTO(p.getIdProyecto(), p.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getFases() {
        return faseRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(f -> new DropdownDTO(f.getIdFase(), f.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getSites() {
        return siteRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(s -> new DropdownDTO(s.getIdSite(), s.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getRegiones() {
        return regionRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getIdRegion(), r.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getOtsActivas() {
        return otsRepository.findByActivoTrueOrderByOtAsc()
                .stream()
                .map(ot -> new DropdownDTO(ot.getIdOts(), "OT " + ot.getOt()))
                .collect(Collectors.toList());
    }

    // ────────────────────────────────────────────────────────
    // Nuevos métodos para los responsables (dropdowns)
    // ────────────────────────────────────────────────────────

    @Override
    public List<DropdownDTO> getJefaturasClienteSolicitante() {
        return jefaturaClienteSolicitanteRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getAnalistasClienteSolicitante() {
        return analistaClienteSolicitanteRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getCoordinadoresTiCw() {
        return coordinadorTiCwRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getJefaturasResponsable() {
        return jefaturaResponsableRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getLiquidador() {
        return liquidadorRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getEjecutantes() {
        return ejecutanteRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getAnalistasContable() {
        return analistaContableRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getId(), r.getDescripcion()))
                .collect(Collectors.toList());
    }
}