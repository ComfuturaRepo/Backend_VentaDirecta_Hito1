package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.Exceptions.ResourceNotFoundException;
import com.backend.comfutura.dto.request.*;
import com.backend.comfutura.dto.response.*;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.OtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
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
    private final OtTrabajadorRepository otTrabajadorRepository;

    // ───────────────────────────────────────────────
    // LISTADO PAGINADO
    // ───────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public Page<OtResponse> listarOts(Boolean activo, Pageable pageable) {
        Page<Ots> page = (activo == null)
                ? otsRepository.findAll(pageable)
                : otsRepository.findByActivo(activo, pageable);

        return page.map(this::mapToBasicResponse);
    }

    private OtResponse mapToBasicResponse(Ots ots) {
        return OtResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .descripcion(ots.getDescripcion())
                .activo(ots.getActivo())
                .fechaCreacion(ots.getFechaCreacion())
                .build();
    }

    // ───────────────────────────────────────────────
    // DETALLE COMPLETO (por ID interno)
    // ───────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public OtFullDetailResponse obtenerDetalleCompleto(Integer idOts) {
        Ots ots = otsRepository.findById(idOts)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada con id: " + idOts));

        return mapToFullDetailResponse(ots);
    }

    // ───────────────────────────────────────────────
    // DETALLE COMPLETO (por número OT legible)
    // ───────────────────────────────────────────────
    @Override
    @Transactional(readOnly = true)
    public OtFullDetailResponse obtenerPorNumeroOt(Integer numeroOt) {
        Ots ots = otsRepository.findByOt(numeroOt)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró OT con número: " + numeroOt));

        return mapToFullDetailResponse(ots);
    }


    // Helpers genéricos
    private <T> Integer getId(T entity) {
        if (entity == null) return null;
        try {
            return (Integer) entity.getClass().getMethod("getId").invoke(entity);
        } catch (Exception e) {
            return null;
        }
    }

    private <T> String getNombre(T entity) {
        if (entity == null) return null;
        try {
            return (String) entity.getClass().getMethod("getNombre").invoke(entity);
        } catch (Exception ignored) {
            try {
                return (String) entity.getClass().getMethod("getNombreCompleto").invoke(entity);
            } catch (Exception e2) {
                try {
                    return (String) entity.getClass().getMethod("getRazonSocial").invoke(entity);
                } catch (Exception e3) {
                    return null;
                }
            }
        }
    }

    // ───────────────────────────────────────────────
    // CREATE + UPDATE
    // ───────────────────────────────────────────────
    @Override
    @Transactional
    public OtResponse saveOtCompleta(CrearOtCompletaRequest request) {
        Ots ots;

        if (request.getOt().getIdOts() != null) {
            // UPDATE
            ots = otsRepository.findById(request.getOt().getIdOts())
                    .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada: " + request.getOt().getIdOts()));
            updateOtFields(ots, request.getOt());
        } else {
            // CREATE
            ots = createNewOt(request.getOt());
        }

        // Siempre actualizamos trabajadores (borra y recrea)
        updateTrabajadores(ots, request.getTrabajadores());

        ots = otsRepository.saveAndFlush(ots);

        return mapToBasicResponse(ots);
    }

    private Ots createNewOt(OtCreateRequest req) {
        Integer ultimaOt = otsRepository.findTopByOrderByOtDesc()
                .map(Ots::getOt)
                .orElse(999);
        Integer nuevaOt = ultimaOt + 1;

        Ots ots = Ots.builder()
                .ot(nuevaOt)
                .otsAnterior(req.getIdOtsAnterior())
                .descripcion(req.getDescripcion())
                .fechaApertura(req.getFechaApertura())
                .activo(true)
                .build();

        // Relaciones obligatorias / requeridas
        ots.setCliente(findByIdOrThrow(clienteRepository, req.getIdCliente(), "Cliente"));
        ots.setArea(findByIdOrThrow(areaRepository, req.getIdArea(), "Área"));
        ots.setProyecto(findByIdOrThrow(proyectoRepository, req.getIdProyecto(), "Proyecto"));
        ots.setFase(findByIdOrThrow(faseRepository, req.getIdFase(), "Fase"));
        ots.setSite(findByIdOrThrow(siteRepository, req.getIdSite(), "Site"));
        ots.setRegion(findByIdOrThrow(regionRepository, req.getIdRegion(), "Región"));

        // Responsables (opcionales)
        setResponsableIfPresent(ots::setJefaturaClienteSolicitante, req.getIdJefaturaClienteSolicitante(), JefaturaClienteSolicitante.class);
        setResponsableIfPresent(ots::setAnalistaClienteSolicitante, req.getIdAnalistaClienteSolicitante(), AnalistaClienteSolicitante.class);
        setResponsableIfPresent(ots::setCoordinadorTiCw, req.getIdCoordinadorTiCw(), CoordinadorTiCwPextEnergia.class);
        setResponsableIfPresent(ots::setJefaturaResponsable, req.getIdJefaturaResponsable(), JefaturaResponsable.class);
        setResponsableIfPresent(ots::setLiquidador, req.getIdLiquidador(), Liquidador.class);
        setResponsableIfPresent(ots::setEjecutante, req.getIdEjecutante(), Ejecutante.class);
        setResponsableIfPresent(ots::setAnalistaContable, req.getIdAnalistaContable(), AnalistaContable.class);

        return ots;
    }

    private <T> T findByIdOrThrow(JpaRepository<T, Integer> repo, Integer id, String entityName) {
        if (id == null) {
            throw new IllegalArgumentException(entityName + " es requerido");
        }
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(entityName + " no encontrado: " + id));
    }

    private void updateOtFields(Ots ots, OtCreateRequest req) {
        if (req.getIdCliente() != null) ots.setCliente(findByIdOrThrow(clienteRepository, req.getIdCliente(), "Cliente"));
        if (req.getIdArea() != null) ots.setArea(findByIdOrThrow(areaRepository, req.getIdArea(), "Área"));
        if (req.getIdProyecto() != null) ots.setProyecto(findByIdOrThrow(proyectoRepository, req.getIdProyecto(), "Proyecto"));
        if (req.getIdFase() != null) ots.setFase(findByIdOrThrow(faseRepository, req.getIdFase(), "Fase"));
        if (req.getIdSite() != null) ots.setSite(findByIdOrThrow(siteRepository, req.getIdSite(), "Site"));
        if (req.getIdRegion() != null) ots.setRegion(findByIdOrThrow(regionRepository, req.getIdRegion(), "Región"));

        if (req.getDescripcion() != null) ots.setDescripcion(req.getDescripcion());
        if (req.getFechaApertura() != null) ots.setFechaApertura(req.getFechaApertura());
        if (req.getIdOtsAnterior() != null) ots.setOtsAnterior(req.getIdOtsAnterior());

        // Responsables
        setResponsableIfPresent(ots::setJefaturaClienteSolicitante, req.getIdJefaturaClienteSolicitante(), JefaturaClienteSolicitante.class);
        setResponsableIfPresent(ots::setAnalistaClienteSolicitante, req.getIdAnalistaClienteSolicitante(), AnalistaClienteSolicitante.class);
        setResponsableIfPresent(ots::setCoordinadorTiCw, req.getIdCoordinadorTiCw(), CoordinadorTiCwPextEnergia.class);
        setResponsableIfPresent(ots::setJefaturaResponsable, req.getIdJefaturaResponsable(), JefaturaResponsable.class);
        setResponsableIfPresent(ots::setLiquidador, req.getIdLiquidador(), Liquidador.class);
        setResponsableIfPresent(ots::setEjecutante, req.getIdEjecutante(), Ejecutante.class);
        setResponsableIfPresent(ots::setAnalistaContable, req.getIdAnalistaContable(), AnalistaContable.class);
    }

    private <T> void setResponsableIfPresent(Consumer<T> setter, Integer id, Class<T> clazz) {
        if (id == null) {
            setter.accept(null);
            return;
        }
        try {
            T instance = clazz.getDeclaredConstructor().newInstance();
            clazz.getMethod("setId", Integer.class).invoke(instance, id);
            setter.accept(instance);
        } catch (Exception e) {
            throw new RuntimeException("Error al instanciar responsable: " + clazz.getSimpleName(), e);
        }
    }

    private void updateTrabajadores(Ots ots, List<OtTrabajadorRequest> trabajadores) {
        otTrabajadorRepository.deleteByOts(ots);

        if (trabajadores == null || trabajadores.isEmpty()) {
            return;
        }

        for (OtTrabajadorRequest req : trabajadores) {
            Trabajador trabajador = trabajadorRepository.findById(req.getIdTrabajador())
                    .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + req.getIdTrabajador()));

            OtTrabajador otTrab = OtTrabajador.builder()
                    .ots(ots)
                    .trabajador(trabajador)
                    .rolEnOt(req.getRolEnOt())
                    .activo(true)
                    .build();

            otTrabajadorRepository.save(otTrab);
        }
    }

    // ───────────────────────────────────────────────
    // TOGGLE ACTIVO
    // ───────────────────────────────────────────────
    @Override
    @Transactional
    public void toggleEstado(Integer idOts) {
        Ots ots = otsRepository.findById(idOts)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada: " + idOts));
        ots.setActivo(!ots.getActivo());
        otsRepository.save(ots);
    }
    private OtFullDetailResponse mapToFullDetailResponse(Ots ots) {
        OtFullDetailResponse dto = OtFullDetailResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .idOtsAnterior(ots.getOtsAnterior())
                .descripcion(ots.getDescripcion())
                .fechaApertura(ots.getFechaApertura())
                .fechaCreacion(ots.getFechaCreacion())
                .activo(ots.getActivo())

                // Entidades relacionadas (maestras)
                .idCliente(ots.getCliente() != null ? ots.getCliente().getId() : null)
                .clienteRazonSocial(ots.getCliente() != null ? ots.getCliente().getRazonSocial() : null)

                .idArea(ots.getArea() != null ? ots.getArea().getId() : null)
                .areaNombre(ots.getArea() != null ? ots.getArea().getNombre() : null)

                .idProyecto(ots.getProyecto() != null ? ots.getProyecto().getIdProyecto() : null)
                .proyectoNombre(ots.getProyecto() != null ? ots.getProyecto().getNombre() : null)

                .idFase(ots.getFase() != null ? ots.getFase().getIdFase() : null)
                .faseNombre(ots.getFase() != null ? ots.getFase().getNombre() : null)

                .idSite(ots.getSite() != null ? ots.getSite().getIdSite() : null)
                .siteNombre(ots.getSite() != null ? ots.getSite().getNombre() : null)

                .idRegion(ots.getRegion() != null ? ots.getRegion().getIdRegion() : null)
                .regionNombre(ots.getRegion() != null ? ots.getRegion().getNombre() : null)

                // Responsables
                .idJefaturaClienteSolicitante(
                        ots.getJefaturaClienteSolicitante() != null ?
                                ots.getJefaturaClienteSolicitante().getId() : null)
                .jefaturaClienteSolicitanteNombre(
                        ots.getJefaturaClienteSolicitante() != null ?
                                ots.getJefaturaClienteSolicitante().getDescripcion() : null)

                .idAnalistaClienteSolicitante(
                        ots.getAnalistaClienteSolicitante() != null ?
                                ots.getAnalistaClienteSolicitante().getId() : null)
                .analistaClienteSolicitanteNombre(
                        ots.getAnalistaClienteSolicitante() != null ?
                                ots.getAnalistaClienteSolicitante().getDescripcion() : null)

                .idCoordinadorTiCw(
                        ots.getCoordinadorTiCw() != null ?
                                ots.getCoordinadorTiCw().getId() : null)
                .coordinadorTiCwNombre(
                        ots.getCoordinadorTiCw() != null ?
                                ots.getCoordinadorTiCw().getDescripcion() : null)

                .idJefaturaResponsable(
                        ots.getJefaturaResponsable() != null ?
                                ots.getJefaturaResponsable().getId() : null)
                .jefaturaResponsableNombre(
                        ots.getJefaturaResponsable() != null ?
                                ots.getJefaturaResponsable().getDescripcion() : null)

                .idLiquidador(
                        ots.getLiquidador() != null ?
                                ots.getLiquidador().getId() : null)
                .liquidadorNombre(
                        ots.getLiquidador() != null ?
                                ots.getLiquidador().getDescripcion() : null)

                .idEjecutante(
                        ots.getEjecutante() != null ?
                                ots.getEjecutante().getId() : null)
                .ejecutanteNombre(
                        ots.getEjecutante() != null ?
                                ots.getEjecutante().getDescripcion() : null)

                .idAnalistaContable(
                        ots.getAnalistaContable() != null ?
                                ots.getAnalistaContable().getId() : null)
                .analistaContableNombre(
                        ots.getAnalistaContable() != null ?
                                ots.getAnalistaContable().getDescripcion() : null)

                // Trabajadores asignados (solo activos)
                .trabajadoresAsignados(
                        otTrabajadorRepository.findByOtsAndActivoTrue(ots).stream()
                                .map(otTrab -> OtFullDetailResponse.TrabajadorEnOtDto.builder()
                                        .idTrabajador(otTrab.getTrabajador().getId())
                                        .nombresCompletos(
                                                otTrab.getTrabajador().getNombres() + " " +
                                                        otTrab.getTrabajador().getApellidos())
                                        .cargoNombre(
                                                otTrab.getTrabajador().getCargo() != null ?
                                                        otTrab.getTrabajador().getCargo().getNombre() : null)
                                        .areaTrabajadorNombre(
                                                otTrab.getTrabajador().getArea() != null ?
                                                        otTrab.getTrabajador().getArea().getNombre() : null)
                                        .rolEnOt(otTrab.getRolEnOt())
                                        .activo(otTrab.getActivo())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();

        return dto;
    }
    // Compatibilidad opcional
    @Override
    public OtResponse obtenerPorId(Integer id) {
        return mapToBasicResponse(
                otsRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada"))
        );
    }
}