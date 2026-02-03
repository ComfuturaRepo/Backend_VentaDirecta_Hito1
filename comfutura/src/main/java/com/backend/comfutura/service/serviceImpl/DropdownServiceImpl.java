package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.Site;
import com.backend.comfutura.model.SiteDescripcion;
import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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
    private final CargoRepository cargoRepository;
    private final EmpresaRepository empresaRepository;
    private final NivelRepository nivelRepository;
    private final EstadoOtRepository estadoOtRepository;
    private final SiteDescripcionRepository siteDescripcionRepository;
    private final TipoOtRepository tipoOtRepository;

    // Nuevos repositorios para los responsables (agrega estos en tu proyecto)
    private final JefaturaClienteSolicitanteRepository jefaturaClienteSolicitanteRepository;
    private final AnalistaClienteSolicitanteRepository analistaClienteSolicitanteRepository;

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
                .map(a -> new DropdownDTO(a.getIdArea(), a.getNombre() ,null,a.getActivo()))
                .collect(Collectors.toList());
    }
    @Override
    public List<DropdownDTO> getCargos() {
        return cargoRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(c -> new DropdownDTO(
                        c.getIdCargo(),
                        c.getNombre()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getEmpresas() {
        return empresaRepository.findByActivoTrueOrderByNombreAsc()
                .stream()
                .map(e -> new DropdownDTO(
                        e.getId(),
                        e.getNombre()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getTrabajadores() {
        return trabajadorRepository.findByActivoTrueOrderByNombresAsc()
                .stream()
                .map(e -> new DropdownDTO(
                        e.getIdTrabajador(),
                        e.getNombres() +" "+ e.getApellidos()
                ))
                .collect(Collectors.toList());    }

    @Override
    public List<DropdownDTO> getTrabajadoresSinUsuarioActivo() {
        return trabajadorRepository.findTrabajadoresActivosSinUsuario()
                .stream()
                .map(e -> new DropdownDTO(
                        e.getIdTrabajador(),
                        e.getNombres() +" "+ e.getApellidos()
                ))
                .collect(Collectors.toList());    }

    @Override
    public List<DropdownDTO> getNivelesAll() {
        return nivelRepository.findAll()
                .stream()
                .map(e -> new DropdownDTO(
                        e.getIdNivel(),
                        e.getCodigo(),
                        e.getNombre(),
                        null
                ))
                .collect(Collectors.toList());    }

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
                        s.getCodigoSitio()
                ))
                .toList();
    }
    @Override
    public List<DropdownDTO> getDescripcionesBySiteCodigo(String codigoSite) {

        boolean sinCodigo =
                codigoSite == null ||
                        codigoSite.trim().isEmpty() ||
                        codigoSite.trim().equals("-") ||
                        codigoSite.trim().equalsIgnoreCase("SIN CÓDIGO");

        List<SiteDescripcion> lista;

        if (sinCodigo) {
            lista = siteDescripcionRepository
                    .findBySiteCodigoSitioIsNullOrSiteCodigoSitioEmpty();
        } else {
            lista = siteDescripcionRepository
                    .findBySiteCodigoSitioIgnoreCaseAndActivoTrue(codigoSite.trim());
        }

        return lista.stream()
                .map(desc -> new DropdownDTO(
                        desc.getIdSiteDescripcion(),
                        Optional.ofNullable(desc.getDescripcion()).orElse("").trim(),
                        sinCodigo ? "SIN CÓDIGO" : codigoSite.trim(),
                        true
                ))
                .collect(Collectors.toList());
    }


    @Override
    public List<DropdownDTO> getSitesConDescripciones() {
        return siteRepository.findByActivoTrueOrderByCodigoSitioAsc()
                .stream()
                .map(site -> {
                    String codigo = (site.getCodigoSitio() != null && !site.getCodigoSitio().trim().isEmpty())
                            ? site.getCodigoSitio().trim()
                            : "SIN CÓDIGO";

                    return new DropdownDTO(
                            site.getIdSite(),  // ID del site, no de la descripción
                            codigo,
                            codigo,  // Código como adicional también
                            site.getActivo()
                    );
                })
                .distinct()  // Para evitar duplicados si hay sites con el mismo código
                .collect(Collectors.toList());
    }@Override
    public List<DropdownDTO> getSiteCompuesto() {
        return siteRepository.findByActivoTrueOrderByCodigoSitioAsc().stream()
                .map(site -> {
                    String codigo = (site.getCodigoSitio() != null && !site.getCodigoSitio().trim().isEmpty())
                            ? site.getCodigoSitio().trim()
                            : "SIN CÓDIGO";

                    // Obtener todas las descripciones activas
                    List<SiteDescripcion> descripcionesActivas = site.getDescripciones().stream()
                            .filter(desc -> desc.getActivo() != null && desc.getActivo())
                            .collect(Collectors.toList());

                    List<DropdownDTO> resultados = new ArrayList<>();

                    if (!descripcionesActivas.isEmpty()) {
                        // Crear un DTO por cada descripción
                        for (SiteDescripcion desc : descripcionesActivas) {
                            String descripcionTexto = desc.getDescripcion() != null
                                    ? desc.getDescripcion().trim()
                                    : "";

                            if (!descripcionTexto.isEmpty()) {
                                // Formato: "CÓDIGO - DESCRIPCIÓN"
                                String label = codigo + " - " + descripcionTexto;

                                resultados.add(new DropdownDTO(
                                        desc.getIdSiteDescripcion(),  // Usar ID de descripción
                                        label,
                                        codigo,                       // Código como adicional
                                        true
                                ));
                            }
                        }
                    } else {
                        // Si no hay descripciones, crear solo el código
                        resultados.add(new DropdownDTO(
                                site.getIdSite(),                    // Usar ID del site
                                codigo,
                                codigo,
                                site.getActivo()
                        ));
                    }

                    return resultados;
                })
                .flatMap(List::stream)  // Aplanar la lista de listas
                .sorted(Comparator.comparing(DropdownDTO::label, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    @Override
    public List<DropdownDTO> getSiteDescriptions() {

        return siteDescripcionRepository
                .findByActivoTrueOrderBySite_CodigoSitioAscDescripcionAsc()
                .stream()
                .map(sd -> new DropdownDTO(
                        sd.getIdSiteDescripcion(),                 // id real
                        sd.getDescripcion(),                       // label
                        sd.getSite().getCodigoSitio(),             // adicional
                        null                                      // estado opcional
                ))
                .toList();
    }

    @Override
    public List<DropdownDTO> getOtTipo() {

        return tipoOtRepository
                .findByActivoTrueOrderByCodigoAsc()
                .stream()
                .map(sd -> new DropdownDTO(
                        sd.getIdTipoOt(),                 // id real
                        sd.getDescripcion(),                       // label
                        null,             // adicional
                        null                                      // estado opcional
                ))
                .toList();
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
                .findAllByActivoTrueAndPuedeSerCoordinadorTiCwTrueOrderByApellidosAsc()
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
                .findAllByActivoTrueAndPuedeSerJefaturaResponsableTrueOrderByApellidosAsc()
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
                .findAllByActivoTrueAndPuedeSerLiquidadorTrueOrderByApellidosAsc()
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
                .findAllByActivoTrueAndPuedeSerEjecutanteTrueOrderByApellidosAsc()
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
                .findAllByActivoTrueAndPuedeSerAnalistaContableTrueOrderByApellidosAsc()
                .stream()
                .map(t -> new DropdownDTO(
                        t.getIdTrabajador(),
                        t.getApellidos() + " " + t.getNombres()
                ))
                .collect(Collectors.toList());
    }

}