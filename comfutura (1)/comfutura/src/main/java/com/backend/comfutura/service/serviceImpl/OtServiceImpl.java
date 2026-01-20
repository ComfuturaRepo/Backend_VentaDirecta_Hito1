package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.Exceptions.ResourceNotFoundException;
import com.backend.comfutura.dto.request.*;
import com.backend.comfutura.dto.response.*;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.OtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;@Service
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

    // ==============================
    // CREAR OT BASE (OT AUTOMÁTICA)
    // ==============================
    @Transactional
    public Ots createOtBase(OtCreateRequest request) {

        // 🔥 Generar OT automáticamente
        Integer nuevaOt = otsRepository.findTopByOrderByOtDesc()
                .map(o -> o.getOt() + 1)
                .orElse(1000); // OT inicial si no existe ninguna

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

        Ots otsAnterior = null;
        if (request.getIdOtsAnterior() != null) {
            otsAnterior = otsRepository.findById(request.getIdOtsAnterior())
                    .orElseThrow(() -> new ResourceNotFoundException("OT anterior no encontrada"));
        }

        Ots ots = Ots.builder()
                .ot(nuevaOt)
                .otsAnterior(otsAnterior)
                .cliente(cliente)
                .area(area)
                .proyecto(proyecto)
                .fase(fase)
                .site(site)
                .region(region)
                .descripcion(request.getDescripcion())
                .diasAsignados(
                        request.getDiasAsignados() != null ? request.getDiasAsignados() : 0
                )
                .activo(true)
                .build();

        return otsRepository.save(ots);
    }

    // ==============================
    // CREAR OT COMPLETA
    // ==============================
    @Override
    @Transactional
    public OtResponse createOtCompleta(CrearOtCompletaRequest request) {

        Ots ots = createOtBase(request.getOt());

        asignarTrabajadores(ots, request.getTrabajadores());

        return mapToResponse(ots);
    }

    // ==============================
    // TRABAJADORES
    // ==============================
    private void asignarTrabajadores(Ots ots, List<OtTrabajadorRequest> trabajadores) {

        for (OtTrabajadorRequest req : trabajadores) {

            Trabajador trabajador = trabajadorRepository.findById(req.getIdTrabajador())
                    .orElseThrow(() -> new ResourceNotFoundException("Trabajador no encontrado"));

            OtTrabajador otTrabajador = OtTrabajador.builder()
                    .ots(ots)
                    .trabajador(trabajador)
                    .rolEnOt(req.getRolEnOt())
                    .activo(true)
                    .build();

            otTrabajadorRepository.save(otTrabajador);
        }
    }

    // ==============================
    // RESPONSE
    // ==============================
    private OtResponse mapToResponse(Ots ots) {

        return OtResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .descripcion(ots.getDescripcion())
                .diasAsignados(ots.getDiasAsignados())
                .activo(ots.getActivo())
                .fechaCreacion(ots.getFechaCreacion())
                .build();
    }
}
