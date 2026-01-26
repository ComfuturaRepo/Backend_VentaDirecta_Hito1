package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.Exceptions.ResourceNotFoundException;
import com.backend.comfutura.config.security.CustomUserDetails;
import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtDetailResponse;
import com.backend.comfutura.dto.response.OtFullResponse;
import com.backend.comfutura.dto.response.OtListDto;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.OtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class OtServiceImpl implements OtService {

    private final OtsRepository otsRepository;
    private final ClienteRepository clienteRepository;
    private final AreaRepository areaRepository;
    private final ProyectoRepository proyectoRepository;
    private final FaseRepository faseRepository;
    private final SiteRepository siteRepository;
    private final RegionRepository regionRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final JefaturaClienteSolicitanteRepository jefaturaClienteRepository;
    private final AnalistaClienteSolicitanteRepository analistaClienteRepository;
    private final EstadoOtRepository estadoOtRepository;

    // LISTADO OPTIMIZADO + FILTRO DE TEXTO
    @Override
    @Transactional(readOnly = true)
    public Page<OtListDto> listarOts(String search, Pageable pageable) {
        Specification<Ots> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("ot").as(String.class)), pattern),
                    cb.like(cb.lower(root.get("descripcion")), pattern),
                    cb.like(cb.lower(root.get("fechaApertura").as(String.class)), pattern),
                    cb.like(cb.lower(root.join("estadoOt").get("descripcion")), pattern),
                    cb.like(cb.lower(root.join("region").get("nombre")), pattern),
                    cb.like(cb.lower(root.join("site").get("codigoSitio")), pattern),
                    cb.like(cb.lower(root.join("fase").get("nombre")), pattern)
            );
        };

        return otsRepository.findAll(spec, pageable).map(this::toOtListDto);
    }

    private OtListDto toOtListDto(Ots ots) {
        return OtListDto.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .fechaApertura(ots.getFechaApertura())
                .estadoOt(ofNullable(ots.getEstadoOt()).map(EstadoOt::getDescripcion).orElse("—"))
                .proyecto(ofNullable(ots.getProyecto()).map(Proyecto::getNombre).orElse("—"))
                .siteNombre(ofNullable(ots.getSite()).map(Site::getCodigoSitio).orElse("—"))
                .site_descripcion(ofNullable(ots.getSite()).map(Site::getDescripcion).orElse("—"))
                .cliente(ofNullable(ots.getCliente()).map(Cliente::getRazonSocial).orElse("—"))
                .cliente_id(ofNullable(ots.getCliente()).map(Cliente::getRuc).orElse("—"))
                .region(ofNullable(ots.getRegion()).map(Region::getNombre).orElse("—"))
                .activo(ots.getActivo())
                .build();
    }

    // DETALLE COMPLETO POR ID
    @Override
    @Transactional(readOnly = true)
    public OtDetailResponse obtenerDetallePorId(Integer idOts) {
        Ots ots = otsRepository.findById(idOts)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada con ID: " + idOts));

        return toOtDetailResponse(ots);
    }

    private OtDetailResponse toOtDetailResponse(Ots ots) {
        int diasAsignados = 0;
        if (ots.getFechaApertura() != null) {
            long dias = ChronoUnit.DAYS.between(ots.getFechaApertura(), LocalDate.now());
            diasAsignados = (int) Math.max(dias, 0);
        }

        return OtDetailResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .idOtsAnterior(ots.getIdOtsAnterior())
                .descripcion(ots.getDescripcion())
                .fechaApertura(ots.getFechaApertura())
                .diasAsignados(diasAsignados)
                .fechaCreacion(ots.getFechaCreacion())
                .activo(ots.getActivo())

                .idCliente(ofNullable(ots.getCliente()).map(Cliente::getIdCliente).orElse(null))
                .clienteRazonSocial(ofNullable(ots.getCliente()).map(Cliente::getRazonSocial).orElse(null))

                .idArea(ofNullable(ots.getArea()).map(Area::getIdArea).orElse(null))
                .areaNombre(ofNullable(ots.getArea()).map(Area::getNombre).orElse(null))

                .idProyecto(ofNullable(ots.getProyecto()).map(Proyecto::getIdProyecto).orElse(null))
                .proyectoNombre(ofNullable(ots.getProyecto()).map(Proyecto::getNombre).orElse(null))

                .idFase(ofNullable(ots.getFase()).map(Fase::getIdFase).orElse(null))
                .faseNombre(ofNullable(ots.getFase()).map(Fase::getNombre).orElse(null))

                .idSite(ofNullable(ots.getSite()).map(Site::getIdSite).orElse(null))
                .siteNombre(ofNullable(ots.getSite()).map(Site::getCodigoSitio).orElse(null))

                .idRegion(ofNullable(ots.getRegion()).map(Region::getIdRegion).orElse(null))
                .regionNombre(ofNullable(ots.getRegion()).map(Region::getNombre).orElse(null))

                .idJefaturaClienteSolicitante(ofNullable(ots.getJefaturaClienteSolicitante()).map(JefaturaClienteSolicitante::getId).orElse(null))
                .jefaturaClienteSolicitanteNombre(ofNullable(ots.getJefaturaClienteSolicitante()).map(JefaturaClienteSolicitante::getDescripcion).orElse(null))

                .idAnalistaClienteSolicitante(ofNullable(ots.getAnalistaClienteSolicitante()).map(AnalistaClienteSolicitante::getId).orElse(null))
                .analistaClienteSolicitanteNombre(ofNullable(ots.getAnalistaClienteSolicitante()).map(AnalistaClienteSolicitante::getDescripcion).orElse(null))

                .idCreador(ofNullable(ots.getTrabajador()).map(Trabajador::getIdTrabajador).orElse(null))
                .creadorNombre(nombreCompleto(ots.getTrabajador()))

                .idCoordinadorTiCw(ofNullable(ots.getCoordinadorTiCw()).map(Trabajador::getIdTrabajador).orElse(null))
                .coordinadorTiCwNombre(nombreCompleto(ots.getCoordinadorTiCw()))

                .idJefaturaResponsable(ofNullable(ots.getJefaturaResponsable()).map(Trabajador::getIdTrabajador).orElse(null))
                .jefaturaResponsableNombre(nombreCompleto(ots.getJefaturaResponsable()))

                .idLiquidador(ofNullable(ots.getLiquidador()).map(Trabajador::getIdTrabajador).orElse(null))
                .liquidadorNombre(nombreCompleto(ots.getLiquidador()))

                .idEjecutante(ofNullable(ots.getEjecutante()).map(Trabajador::getIdTrabajador).orElse(null))
                .ejecutanteNombre(nombreCompleto(ots.getEjecutante()))

                .idAnalistaContable(ofNullable(ots.getAnalistaContable()).map(Trabajador::getIdTrabajador).orElse(null))
                .analistaContableNombre(nombreCompleto(ots.getAnalistaContable()))

                .estadoOt(ofNullable(ots.getEstadoOt()).map(EstadoOt::getDescripcion).orElse(null))
                .build();
    }

    // PARA EDICIÓN (solo IDs)
    @Override
    @Transactional(readOnly = true)
    public OtFullResponse obtenerParaEdicion(Integer idOts) {
        Ots ots = otsRepository.findById(idOts)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada con ID: " + idOts));

        return OtFullResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .idOtsAnterior(ots.getIdOtsAnterior())
                .descripcion(ots.getDescripcion())
                .fechaApertura(ots.getFechaApertura())
                .idCliente(ofNullable(ots.getCliente()).map(Cliente::getIdCliente).orElse(null))
                .idArea(ofNullable(ots.getArea()).map(Area::getIdArea).orElse(null))
                .idProyecto(ofNullable(ots.getProyecto()).map(Proyecto::getIdProyecto).orElse(null))
                .idFase(ofNullable(ots.getFase()).map(Fase::getIdFase).orElse(null))
                .idSite(ofNullable(ots.getSite()).map(Site::getIdSite).orElse(null))
                .idRegion(ofNullable(ots.getRegion()).map(Region::getIdRegion).orElse(null))
                .idJefaturaClienteSolicitante(ofNullable(ots.getJefaturaClienteSolicitante()).map(JefaturaClienteSolicitante::getId).orElse(null))
                .idAnalistaClienteSolicitante(ofNullable(ots.getAnalistaClienteSolicitante()).map(AnalistaClienteSolicitante::getId).orElse(null))
                .idCreador(ofNullable(ots.getTrabajador()).map(Trabajador::getIdTrabajador).orElse(null))
                .idCoordinadorTiCw(ofNullable(ots.getCoordinadorTiCw()).map(Trabajador::getIdTrabajador).orElse(null))
                .idJefaturaResponsable(ofNullable(ots.getJefaturaResponsable()).map(Trabajador::getIdTrabajador).orElse(null))
                .idLiquidador(ofNullable(ots.getLiquidador()).map(Trabajador::getIdTrabajador).orElse(null))
                .idEjecutante(ofNullable(ots.getEjecutante()).map(Trabajador::getIdTrabajador).orElse(null))
                .idAnalistaContable(ofNullable(ots.getAnalistaContable()).map(Trabajador::getIdTrabajador).orElse(null))
                .activo(ots.getActivo())
                .fechaCreacion(ots.getFechaCreacion())
                .build();
    }

    // CREAR / ACTUALIZAR
    @Override
    @Transactional
    public OtDetailResponse saveOt(OtCreateRequest req) {
        Ots ots;

        if (req.getIdOts() != null) {
            ots = otsRepository.findById(req.getIdOts())
                    .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada con ID: " + req.getIdOts()));
            updateOt(ots, req);
        } else {
            ots = createOt(req);
        }

        otsRepository.save(ots);
        return toOtDetailResponse(ots);
    }

    private Ots createOt(OtCreateRequest req) {
        Integer ultima = otsRepository.findTopByOrderByOtDesc()
                .map(Ots::getOt)
                .orElse(20250000);
        Integer nuevoOt = ultima + 1;

        Integer userId = getCurrentTrabajadorId();
        Trabajador creador = trabajadorRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador autenticado no encontrado"));

        EstadoOt estadoPendiente = estadoOtRepository.findByDescripcion("ASIGNACION")
                .orElseThrow(() -> new ResourceNotFoundException("Estado 'ASIGNACION' no encontrado"));

        Ots ots = Ots.builder()
                .ot(nuevoOt)
                .idOtsAnterior(req.getIdOtsAnterior())
                .descripcion(req.getDescripcion())
                .fechaApertura(req.getFechaApertura())
                .activo(true)
                .trabajador(creador)
                .estadoOt(estadoPendiente)
                .build();

        setRelations(ots, req);
        return ots;
    }

    private void updateOt(Ots ots, OtCreateRequest req) {
        if (req.getDescripcion() != null) ots.setDescripcion(req.getDescripcion());
        if (req.getFechaApertura() != null) ots.setFechaApertura(req.getFechaApertura());
        if (req.getIdOtsAnterior() != null) ots.setIdOtsAnterior(req.getIdOtsAnterior());

        setRelations(ots, req);
    }

    private void setRelations(Ots ots, OtCreateRequest req) {
        if (req.getIdCliente() != null)
            ots.setCliente(find(clienteRepository, req.getIdCliente(), "Cliente"));
        if (req.getIdArea() != null)
            ots.setArea(find(areaRepository, req.getIdArea(), "Área"));
        if (req.getIdProyecto() != null)
            ots.setProyecto(find(proyectoRepository, req.getIdProyecto(), "Proyecto"));
        if (req.getIdFase() != null)
            ots.setFase(find(faseRepository, req.getIdFase(), "Fase"));
        if (req.getIdSite() != null)
            ots.setSite(find(siteRepository, req.getIdSite(), "Site"));
        if (req.getIdRegion() != null)
            ots.setRegion(find(regionRepository, req.getIdRegion(), "Región"));

        ots.setJefaturaClienteSolicitante(
                req.getIdJefaturaClienteSolicitante() != null ?
                        find(jefaturaClienteRepository, req.getIdJefaturaClienteSolicitante(), "Jefatura cliente") : null);

        ots.setAnalistaClienteSolicitante(
                req.getIdAnalistaClienteSolicitante() != null ?
                        find(analistaClienteRepository, req.getIdAnalistaClienteSolicitante(), "Analista cliente") : null);

        ots.setCoordinadorTiCw(req.getIdCoordinadorTiCw() != null ? findTrabajador(req.getIdCoordinadorTiCw()) : null);
        ots.setJefaturaResponsable(req.getIdJefaturaResponsable() != null ? findTrabajador(req.getIdJefaturaResponsable()) : null);
        ots.setLiquidador(req.getIdLiquidador() != null ? findTrabajador(req.getIdLiquidador()) : null);
        ots.setEjecutante(req.getIdEjecutante() != null ? findTrabajador(req.getIdEjecutante()) : null);
        ots.setAnalistaContable(req.getIdAnalistaContable() != null ? findTrabajador(req.getIdAnalistaContable()) : null);
    }

    private Trabajador findTrabajador(Integer id) {
        return trabajadorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + id));
    }

    private <T> T find(JpaRepository<T, Integer> repo, Integer id, String name) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(name + " no encontrado: " + id));
    }

    // TOGGLE ACTIVO
    @Override
    @Transactional
    public void toggleActivo(Integer idOts) {
        Ots ot = otsRepository.findById(idOts)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada: " + idOts));
        ot.setActivo(!ot.getActivo());
        otsRepository.save(ot);
    }
    @Transactional
    public List<OtDetailResponse> saveOtsMasivo(List<OtCreateRequest> requests) {
        List<OtDetailResponse> responses = new ArrayList<>();

        for (OtCreateRequest request : requests) {
            try {
                OtDetailResponse response = saveOt(request);
                responses.add(response);
            } catch (Exception e) {
                // Continuar con las demás OTs si una falla
                System.err.println("Error guardando OT: " + e.getMessage());
            }
        }

        return responses;
    }
    // HELPERS
    private Integer getCurrentTrabajadorId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            return user.getIdTrabajador();
        }
        throw new IllegalStateException("No se encontró trabajador autenticado");
    }

    private String nombreCompleto(Trabajador t) {
        if (t == null) return null;
        return (ofNullable(t.getNombres()).orElse("") + " " + ofNullable(t.getApellidos()).orElse("")).trim();
    }
}