package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.PlanillaTrabajoRequest;
import com.backend.comfutura.dto.response.PlanillaTrabajoResponse;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.PlanillaTrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanillaTrabajoServiceImpl implements PlanillaTrabajoService {

    private final PlanillaTrabajoOtRepository planillaRepository;
    private final OtsRepository otsRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final CargoRepository cargoRepository;
    private final BancoRepository bancoRepository;

    @Override
    public PlanillaTrabajoResponse crear(PlanillaTrabajoRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        Trabajador trabajador = trabajadorRepository.findById(request.getIdTrabajador())
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        Cargo cargo = cargoRepository.findById(request.getIdCargo())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        Banco banco = null;
        if (request.getIdBanco() != null) {
            banco = bancoRepository.findById(request.getIdBanco())
                    .orElseThrow(() -> new RuntimeException("Banco no encontrado"));
        }

        BigDecimal total =
                request.getCostoDia().multiply(request.getCantDias());

        PlanillaTrabajoOt planilla = PlanillaTrabajoOt.builder()
                .ots(ots)
                .trabajador(trabajador)
                .cargo(cargo)
                .fecha(request.getFecha())
                .costoDia(request.getCostoDia())
                .cantDias(request.getCantDias())
                .total(total)
                .banco(banco)
                .moneda(request.getMoneda())
                .cuenta(request.getCuenta())
                .cci(request.getCci())
                .build();

        planillaRepository.save(planilla);

        return mapToResponse(planilla);
    }

    @Override
    public PlanillaTrabajoResponse editar(Integer idPlanilla, PlanillaTrabajoRequest request) {

        PlanillaTrabajoOt planilla = planillaRepository.findById(idPlanilla)
                .orElseThrow(() -> new RuntimeException("Planilla no encontrada"));

        BigDecimal total =
                request.getCostoDia().multiply(request.getCantDias());

        planilla.setCostoDia(request.getCostoDia());
        planilla.setCantDias(request.getCantDias());
        planilla.setTotal(total);

        planillaRepository.save(planilla);

        return mapToResponse(planilla);
    }

    @Override
    public List<PlanillaTrabajoResponse> listarPorOt(Integer idOts) {

        return planillaRepository.findByOts_IdOts(idOts)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private PlanillaTrabajoResponse mapToResponse(PlanillaTrabajoOt p) {
        return PlanillaTrabajoResponse.builder()
                .idPlanilla(p.getIdPlanilla())
                .trabajador(
                        p.getTrabajador().getNombres() + " " +
                                p.getTrabajador().getApellidos()
                )
                .cargo(p.getCargo().getNombre())
                .costoDia(p.getCostoDia())
                .cantDias(p.getCantDias())
                .subtotal(p.getTotal())
                .build();
    }
}


