package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.ResumenOtResponse;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.ResumenOt;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.ResumenOtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumenOtServiceImpl implements ResumenOtService {

    private final ResumenOtRepository resumenRepository;
    private final OtsRepository otsRepository;

    private final MaterialOtRepository materialRepository;
    private final ContratistaOtRepository contratistaRepository;
    private final GastoLogisticoOtRepository gastoRepository;
    private final ViaticoOtRepository viaticoRepository;
    private final PlanillaTrabajoOtRepository planillaRepository;

    @Override
    public void recalcularResumen(Integer idOts) {

        Ots ots = otsRepository.findById(idOts)
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        guardar(ots, "MATERIAL",
                materialRepository.findByOts_IdOts(idOts)
                        .stream()
                        .map(m -> m.getTotal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        guardar(ots, "CONTRATISTA",
                contratistaRepository.findByOts_IdOts(idOts)
                        .stream()
                        .map(c -> c.getTotal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        guardar(ots, "GASTO_LOGISTICO",
                gastoRepository.findByOts_IdOts(idOts)
                        .stream()
                        .map(g -> g.getTotal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        guardar(ots, "VIATICO",
                viaticoRepository.findByOts_IdOts(idOts)
                        .stream()
                        .map(v -> v.getTotal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );

        guardar(ots, "PLANILLA",
                planillaRepository.findByOts_IdOts(idOts)
                        .stream()
                        .map(p -> p.getTotal())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    private void guardar(Ots ots, String tipo, BigDecimal monto) {

        ResumenOt resumen = resumenRepository
                .findByOts_IdOtsAndTipoGasto(ots.getIdOts(), tipo)
                .orElse(
                        ResumenOt.builder()
                                .ots(ots)
                                .tipoGasto(tipo)
                                .build()
                );

        resumen.setMonto(monto);
        resumenRepository.save(resumen);
    }

    @Override
    public List<ResumenOtResponse> listarResumen(Integer idOts) {

        return resumenRepository.findByOts_IdOts(idOts)
                .stream()
                .map(r -> ResumenOtResponse.builder()
                        .tipoGasto(r.getTipoGasto())
                        .monto(r.getMonto())
                        .build())
                .toList();
    }
}


