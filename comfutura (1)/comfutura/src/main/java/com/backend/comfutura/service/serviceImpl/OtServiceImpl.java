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

import java.util.List;
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

    private final MaestroCodigoRepository maestroCodigoRepository;
    private final ProveedorRepository proveedorRepository;

    // ==============================
    // CREAR OT BASE
    // ==============================
    @Transactional
    public Ots createOtBase(OtCreateRequest request) {

        if (otsRepository.existsByOt(request.getOt())) {
            throw new IllegalArgumentException("Ya existe una OT con el número: " + request.getOt());
        }

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
                .ot(request.getOt())
                .ceco(request.getCeco())
                .otsAnterior(otsAnterior)
                .cliente(cliente)
                .area(area)
                .proyecto(proyecto)
                .fase(fase)
                .site(site)
                .region(region)
                .descripcion(request.getDescripcion())
                .fechaApertura(request.getFechaApertura())
                .diasAsignados(request.getDiasAsignados() != null ? request.getDiasAsignados() : 0)
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
        agregarDetalles(ots, request.getDetalles());

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
    // DETALLES
    // ==============================
    private void agregarDetalles(Ots ots, List<OtDetalleRequest> detalles) {

        for (OtDetalleRequest req : detalles) {

            MaestroCodigo maestro = maestroCodigoRepository.findById(req.getIdMaestro())
                    .orElseThrow(() -> new ResourceNotFoundException("Código maestro no encontrado"));

            Proveedor proveedor = proveedorRepository.findById(req.getIdProveedor())
                    .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));


        }
    }

    // ==============================
    // RESPONSE
    // ==============================
    private OtResponse mapToResponse(Ots ots) {

        return OtResponse.builder()
                .idOts(ots.getIdOts())
                .ot(ots.getOt())
                .ceco(ots.getCeco())
                .descripcion(ots.getDescripcion())
                .fechaApertura(ots.getFechaApertura())
                .diasAsignados(ots.getDiasAsignados())
                .activo(ots.getActivo())
                .fechaCreacion(ots.getFechaCreacion())
                .build();
    }
}
