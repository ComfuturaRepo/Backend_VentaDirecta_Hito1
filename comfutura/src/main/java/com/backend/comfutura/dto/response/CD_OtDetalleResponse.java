package com.backend.comfutura.dto.response; // esta trae todasss

import com.backend.comfutura.dto.response.otDTO.OtResponse;
import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CD_OtDetalleResponse {

    // 🔹 Cabecera OT / Compra Directa
    private OtResponse ot;

    // 🔹 Cronograma (NO económico)
    private List<CronogramaResponse> cronograma;

    // 🔹 Módulos económicos
    private List<MaterialResponse> materiales;
    private List<ContratistaResponse> contratistas;
    private List<GastoLogisticoResponse> gastosLogisticos;
    private List<ViaticoResponse> viaticos;
    private List<PlanillaTrabajoResponse> planilla;

    // 🔹 Resumen final
    private List<ResumenOtResponse> resumen;
}
