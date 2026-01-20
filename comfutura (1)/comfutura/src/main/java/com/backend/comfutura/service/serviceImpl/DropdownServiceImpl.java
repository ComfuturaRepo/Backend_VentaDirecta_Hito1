package com.backend.comfutura.service.serviceImpl;
import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.DropdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DropdownServiceImpl implements DropdownService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AreaRepository areaRepository;
    @Autowired
    private ProyectoRepository proyectoRepository;
    @Autowired
    private FaseRepository faseRepository;
    @Autowired
    private SiteRepository siteRepository;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private OtsRepository otsRepository;
    @Autowired
    private TrabajadorRepository trabajadorRepository;
    @Autowired
    private MaestroCodigoRepository maestroCodigoRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;



    @Override
    public List<DropdownDTO> getTrabajadores() {
        return trabajadorRepository.findByActivoTrueOrderByApellidosAsc()
                .stream()
                .map(t -> new DropdownDTO(
                        t.getId(),
                        t.getApellidos() + ", " + t.getNombres() + " - " + t.getDni()
                ))
                .toList();
    }

    @Override
    public List<DropdownDTO> getMaestroCodigos() {
        return maestroCodigoRepository.findByActivoTrueOrderByCodigoAsc()
                .stream()
                .map(m -> new DropdownDTO(
                        m.getId(),
                        m.getCodigo() + " - " + m.getDescripcion() + " (" + m.getPrecioBase() + ")"
                ))
                .toList();
    }

    @Override
    public List<DropdownDTO> getProveedores() {
        return proveedorRepository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(p -> new DropdownDTO(p.getId(), p.getRazonSocial() + " - " + p.getRuc()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getClientes() {
        return clienteRepository.findByActivoTrueOrderByRazonSocialAsc()
                .stream()
                .map(c -> new DropdownDTO(c.getId(), c.getRazonSocial()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getAreas() {
        return areaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(a -> new DropdownDTO(a.getId(), a.getNombre()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getProyectos() {
        return proyectoRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(p -> new DropdownDTO(p.getIdProyecto(), p.getNombre()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getFases() {
        return faseRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(f -> new DropdownDTO(f.getIdFase(), f.getNombre()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getSites() {
        return siteRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(s -> new DropdownDTO(s.getIdSite(), s.getNombre()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getRegiones() {
        return regionRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(r -> new DropdownDTO(r.getIdRegion(), r.getNombre()))
                .toList();
    }

    @Override
    public List<DropdownDTO> getOtsActivas() {
        return otsRepository.findByActivoTrueOrderByOtAsc()
                .stream()
                .map(o -> new DropdownDTO(o.getIdOts(), "OT " + o.getOt()))
                .toList();
    }
}
