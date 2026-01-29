package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.*;
import com.backend.comfutura.dto.response.otDTO.OtResponse;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CD_OtDetalleServiceImpl implements CD_OtDetalleService {

    private final OtsRepository otsRepository;

    private final CronogramaService cronogramaService;
    private final MaterialService materialService;
    private final ContratistaService contratistaService;
    private final GastoLogisticoService gastoLogisticoService;
    private final ViaticoService viaticoService;
    private final PlanillaTrabajoService planillaService;
    private final ResumenOtService resumenService;

    @Override
    public CD_OtDetalleResponse obtenerDetalleOt(Integer idOts) {

        // 🔹 Validar OT
        Ots ots = otsRepository.findById(idOts)
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        return CD_OtDetalleResponse.builder()

                // Cabecera
                .ot(
                        OtResponse.builder()
                                .idOts(ots.getIdOts())
                                .ot(ots.getOt())
                                .descripcion(ots.getDescripcion())
                                .fechaApertura(ots.getFechaApertura())
                                .estadoOt(ots.getEstadoOt().getDescripcion()) // ✅ AQUÍ
                                .build()
                )

                // Cronograma
                .cronograma(
                        cronogramaService.listarPorOt(idOts)
                )

                // Módulos
                .materiales(
                        materialService.listarPorOt(idOts)
                )
                .contratistas(
                        contratistaService.listarPorOt(idOts)
                )
                .gastosLogisticos(
                        gastoLogisticoService.listarPorOt(idOts)
                )
                .viaticos(
                        viaticoService.listarPorOt(idOts)
                )
                .planilla(
                        planillaService.listarPorOt(idOts)
                )

                // Resumen
                .resumen(
                        resumenService.listarResumen(idOts)
                )

                .build();
    }
}



