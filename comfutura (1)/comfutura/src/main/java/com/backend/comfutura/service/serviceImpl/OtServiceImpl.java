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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Override
    @Transactional
    public OtResponse saveOtCompleta(CrearOtCompletaRequest request) {
        Ots ots;

        if (request.getOt().getIdOts() != null) {
            // Edición
            ots = otsRepository.findById(request.getOt().getIdOts())
                    .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada con id: " + request.getOt().getIdOts()));
            updateOtFields(ots, request.getOt());
        } else {
            // Creación
            ots = createOtBase(request.getOt());
        }

        updateTrabajadores(ots, request.getTrabajadores());
        ots = otsRepository.save(ots);

        return mapToResponse(ots);
    }

    private Ots createOtBase(OtCreateRequest request) {
        // Generar número OT secuencial
        Integer ultimaOt = otsRepository.findTopByOrderByOtDesc()
                .map(Ots::getOt)
                .orElse(999);
        Integer nuevaOt = ultimaOt + 1;

        Cliente cliente = clienteRepository.findById(request.getIdCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
        Area area = areaRepository.findById(request.getIdArea())
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada"));
        Proyecto proyecto = proyectoRepository.findById(request.getIdProyecto())
                .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado"));
        Fase fase = faseRepository.findById(request.getIdFase())
                .orElseThrow(() -> new ResourceNotFoundException("Fase no encontrada"));
        Site site = siteRepository.findById(request.getIdSite())
                .orElseThrow(() -> new ResourceNotFoundException("Site no encontrado"));
        Region region = regionRepository.findById(request.getIdRegion())
                .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada"));

        Ots ots = Ots.builder()
                .ot(nuevaOt)
                .otsAnterior(request.getIdOtsAnterior())
                .cliente(cliente)
                .area(area)
                .proyecto(proyecto)
                .fase(fase)
                .site(site)
                .region(region)
                .descripcion(request.getDescripcion())
                .fechaApertura(request.getFechaApertura())
                .activo(true)
                .build();

        // Asignar responsables (solo IDs)
        setResponsable(ots::setJefaturaClienteSolicitante, request.getIdJefaturaClienteSolicitante(), JefaturaClienteSolicitante.class);
        setResponsable(ots::setAnalistaClienteSolicitante, request.getIdAnalistaClienteSolicitante(), AnalistaClienteSolicitante.class);
        setResponsable(ots::setCoordinadorTiCw, request.getIdCoordinadorTiCw(), CoordinadorTiCwPextEnergia.class); // ← corrige nombre si es diferente
        setResponsable(ots::setJefaturaResponsable, request.getIdJefaturaResponsable(), JefaturaResponsable.class);
        setResponsable(ots::setLiquidador, request.getIdLiquidador(), Liquidador.class);
        setResponsable(ots::setEjecutante, request.getIdEjecutante(), Ejecutante.class);
        setResponsable(ots::setAnalistaContable, request.getIdAnalistaContable(), AnalistaContable.class);

        return ots;
    }

    private <T> void setResponsable(java.util.function.Consumer<T> setter, Integer id, Class<T> clazz) {
        if (id != null) {
            try {
                T entity = clazz.getDeclaredConstructor().newInstance();
                clazz.getMethod("setId", Integer.class).invoke(entity, id);
                setter.accept(entity);
            } catch (Exception e) {
                throw new RuntimeException("Error creando stub de responsable", e);
            }
        }
    }

    private void updateOtFields(Ots ots, OtCreateRequest req) {
        if (req.getIdCliente() != null) {
            ots.setCliente(clienteRepository.findById(req.getIdCliente())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado")));
        }
        if (req.getIdArea() != null) {
            ots.setArea(areaRepository.findById(req.getIdArea())
                    .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada")));
        }
        if (req.getIdProyecto() != null) {
            ots.setProyecto(proyectoRepository.findById(req.getIdProyecto())
                    .orElseThrow(() -> new ResourceNotFoundException("Proyecto no encontrado")));
        }
        if (req.getIdFase() != null) {
            ots.setFase(faseRepository.findById(req.getIdFase())
                    .orElseThrow(() -> new ResourceNotFoundException("Fase no encontrada")));
        }
        if (req.getIdSite() != null) {
            ots.setSite(siteRepository.findById(req.getIdSite())
                    .orElseThrow(() -> new ResourceNotFoundException("Site no encontrado")));
        }
        if (req.getIdRegion() != null) {
            ots.setRegion(regionRepository.findById(req.getIdRegion())
                    .orElseThrow(() -> new ResourceNotFoundException("Región no encontrada")));
        }

        if (req.getDescripcion() != null) ots.setDescripcion(req.getDescripcion());
        if (req.getFechaApertura() != null) ots.setFechaApertura(req.getFechaApertura());
        if (req.getIdOtsAnterior() != null) ots.setOtsAnterior(req.getIdOtsAnterior());

        setResponsable(ots::setJefaturaClienteSolicitante, req.getIdJefaturaClienteSolicitante(), JefaturaClienteSolicitante.class);
        setResponsable(ots::setAnalistaClienteSolicitante, req.getIdAnalistaClienteSolicitante(), AnalistaClienteSolicitante.class);
        setResponsable(ots::setCoordinadorTiCw, req.getIdCoordinadorTiCw(), CoordinadorTiCwPextEnergia.class);
        setResponsable(ots::setJefaturaResponsable, req.getIdJefaturaResponsable(), JefaturaResponsable.class);
        setResponsable(ots::setLiquidador, req.getIdLiquidador(), Liquidador.class);
        setResponsable(ots::setEjecutante, req.getIdEjecutante(), Ejecutante.class);
        setResponsable(ots::setAnalistaContable, req.getIdAnalistaContable(), AnalistaContable.class);
    }

    private void updateTrabajadores(Ots ots, List<OtTrabajadorRequest> trabajadores) {
        otTrabajadorRepository.deleteByOts(ots);

        if (trabajadores != null) {
            for (OtTrabajadorRequest req : trabajadores) {
                Trabajador trabajador = trabajadorRepository.findById(req.getIdTrabajador())
                        .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado: " + req.getIdTrabajador()));

                OtTrabajador otTrabajador = OtTrabajador.builder()
                        .ots(ots)
                        .trabajador(trabajador)
                        .rolEnOt(req.getRolEnOt())
                        .activo(true)
                        .build();

                otTrabajadorRepository.save(otTrabajador);
            }
        }
    }

    private OtResponse mapToResponse(Ots ots) {
        return OtResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .descripcion(ots.getDescripcion())
                .activo(ots.getActivo())
                .fechaCreacion(ots.getFechaCreacion())
                .build();
    }

    @Override
    public Page<OtResponse> listarOts(Integer ot, Boolean activo, Pageable pageable) {
        if (ot != null) {
            Optional<Ots> otsOptional = otsRepository.findByOt(ot);
            if (otsOptional.isEmpty()) {
                return Page.empty(pageable);
            }
            OtResponse response = mapToResponse(otsOptional.get());
            return new PageImpl<>(List.of(response), pageable, 1);
        }
        else if (activo == null) {
            return otsRepository.findAll(pageable).map(this::mapToResponse);
        }
        else {
            return otsRepository.findByActivo(activo, pageable).map(this::mapToResponse);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OtFullResponse getOtForEdit(Integer id) {
        Ots ots = otsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada"));

        return OtFullResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .idOtsAnterior(ots.getOtsAnterior())

                // Solo IDs para los selects/combos del formulario
                .idCliente(Optional.ofNullable(ots.getCliente()).map(Cliente::getId).orElse(null))
                .idArea(Optional.ofNullable(ots.getArea()).map(Area::getId).orElse(null))
                .idProyecto(Optional.ofNullable(ots.getProyecto()).map(Proyecto::getIdProyecto).orElse(null))
                .idFase(Optional.ofNullable(ots.getFase()).map(Fase::getIdFase).orElse(null))
                .idSite(Optional.ofNullable(ots.getSite()).map(Site::getIdSite).orElse(null))
                .idRegion(Optional.ofNullable(ots.getRegion()).map(Region::getIdRegion).orElse(null))

                .descripcion(ots.getDescripcion())
                .fechaApertura(ots.getFechaApertura())

                // Solo IDs de responsables (para selects)
                .idJefaturaClienteSolicitante(Optional.ofNullable(ots.getJefaturaClienteSolicitante()).map(JefaturaClienteSolicitante::getId).orElse(null))
                .idAnalistaClienteSolicitante(Optional.ofNullable(ots.getAnalistaClienteSolicitante()).map(AnalistaClienteSolicitante::getId).orElse(null))
                .idCoordinadorTiCw(Optional.ofNullable(ots.getCoordinadorTiCw()).map(CoordinadorTiCwPextEnergia::getId).orElse(null))
                .idJefaturaResponsable(Optional.ofNullable(ots.getJefaturaResponsable()).map(JefaturaResponsable::getId).orElse(null))
                .idLiquidador(Optional.ofNullable(ots.getLiquidador()).map(Liquidador::getId).orElse(null))
                .idEjecutante(Optional.ofNullable(ots.getEjecutante()).map(Ejecutante::getId).orElse(null))
                .idAnalistaContable(Optional.ofNullable(ots.getAnalistaContable()).map(AnalistaContable::getId).orElse(null))

                .activo(ots.getActivo())
                .fechaCreacion(ots.getFechaCreacion())


                .build();
    }
    @Override
    @Transactional(readOnly = true)
    public OtResponse obtenerPorId(Integer id) {
        Ots ots = otsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada"));
        return mapToResponse(ots);
    }

    @Override
    @Transactional
    public void toggleEstado(Integer id) {
        Ots ots = otsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OT no encontrada"));
        ots.setActivo(!ots.getActivo());
        otsRepository.save(ots);
    }
}