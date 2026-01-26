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

    // Repositorios existentes
    private final ClienteRepository clienteRepository;
    private final AreaRepository areaRepository;
    private final ProyectoRepository proyectoRepository;
    private final FaseRepository faseRepository;
    private final SiteRepository siteRepository;
    private final RegionRepository regionRepository;
    private final OtsRepository otsRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final MaestroCodigoRepository maestroCodigoRepository;
    private final ProveedorRepository proveedorRepository;

    // Nuevos repositorios para los responsables (agrega estos en tu proyecto)
    private final JefaturaClienteSolicitanteRepository jefaturaClienteSolicitanteRepository;
    private final AnalistaClienteSolicitanteRepository analistaClienteSolicitanteRepository;
    private final EstadoOtRepository estadoOtRepository; // <-- AGREGAR este repositorio

    // ────────────────────────────────────────────────────────
    // Métodos existentes (sin cambios)
    // ────────────────────────────────────────────────────────

    @Override
    public List<DropdownDTO> getClientes() {
        return clienteRepository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(c -> new DropdownDTO(c.getIdCliente(), c.getRazonSocial()))
                .collect(Collectors.toList());
    }
    @Override
    public List<DropdownDTO> getAreas() {
        return areaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(a -> new DropdownDTO(a.getIdArea(), a.getNombre()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getEstadosOt() {
        return estadoOtRepository.findByActivoTrueOrderByDescripcionAsc()
                .stream()
                .map(e -> new DropdownDTO(e.getIdEstadoOt(), e.getDescripcion()))
                .collect(Collectors.toList());
    }
    @Override
    public List<DropdownDTO> getAreasByCliente(Integer idCliente) {
        // Asumiendo que tienes un método en AreaRepository que filtra por cliente
        return areaRepository.findByClienteIdAndActivoTrue(idCliente)
                .stream()
                .map(a -> new DropdownDTO(a.getIdArea(), a.getNombre()))
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
    public List<DropdownDTO> getMaestroCodigos() {
        return maestroCodigoRepository.findByActivoTrueOrderByCodigoAsc()
                .stream()
                .map(mc -> new DropdownDTO(
                        mc.getId(),
                        mc.getCodigo() + " - " + mc.getDescripcion()  // formato recomendado
                        // Alternativa más simple: solo mc.getCodigo()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getProveedores() {
        return proveedorRepository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(p -> new DropdownDTO(
                        p.getId(),
                        p.getRazonSocial() + " (" + p.getRuc() + ")"   // formato útil
                        // Alternativa más simple: solo p.getRazonSocial()
                ))
                .collect(Collectors.toList());
    }
    @Override
    public List<DropdownDTO> getSites() {
        return siteRepository.findByActivoTrueOrderByCodigoSitioAsc()
                .stream()
                .map(s -> new DropdownDTO(
                        s.getIdSite(),
                        s.getCodigoSitio() ,
                        s.getDescripcion()
                ))
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
        return trabajadorRepository
                .findByActivoTrueAndCargo_NombreOrderByApellidosAsc("COORDINADOR TI CW")
                .stream()
                .map(t -> new DropdownDTO(
                        t.getIdTrabajador(),
                        t.getApellidos() + " " + t.getNombres()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getJefaturasResponsable() {
        return trabajadorRepository
                .findByActivoTrueAndCargo_NombreOrderByApellidosAsc("JEFATURA RESPONSABLE")
                .stream()
                .map(t -> new DropdownDTO(
                        t.getIdTrabajador(),
                        t.getApellidos() + " " + t.getNombres()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getLiquidador() {
        return trabajadorRepository
                .findByActivoTrueAndCargo_NombreOrderByApellidosAsc("LIQUIDADOR")
                .stream()
                .map(t -> new DropdownDTO(
                        t.getIdTrabajador(),
                        t.getApellidos() + " " + t.getNombres()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getEjecutantes() {
        return trabajadorRepository
                .findByActivoTrueAndCargo_NombreOrderByApellidosAsc("EJECUTANTE")
                .stream()
                .map(t -> new DropdownDTO(
                        t.getIdTrabajador(),
                        t.getApellidos() + " " + t.getNombres()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getAnalistasContable() {
        return trabajadorRepository
                .findByActivoTrueAndCargo_NombreOrderByApellidosAsc("ANALISTA CONTABLE")
                .stream()
                .map(t -> new DropdownDTO(
                        t.getIdTrabajador(),
                        t.getApellidos() + " " + t.getNombres()
                ))
                .collect(Collectors.toList());
    }

}