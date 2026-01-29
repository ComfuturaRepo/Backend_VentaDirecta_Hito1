package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.GastoLogisticoRequest;
import com.backend.comfutura.dto.response.GastoLogisticoResponse;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.GastoLogisticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoLogisticoServiceImpl implements GastoLogisticoService {

    private final GastoLogisticoOtRepository gastoRepository;
    private final OtsRepository otsRepository;
    private final ProveedorRepository proveedorRepository;

    // ➕ CREAR
    @Override
    public GastoLogisticoResponse crear(GastoLogisticoRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        Proveedor proveedor = null;
        if (request.getIdProveedor() != null) {
            proveedor = proveedorRepository.findById(request.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        }

        BigDecimal total =
                request.getCantidad().multiply(request.getPrecio());

        GastoLogisticoOt gasto = GastoLogisticoOt.builder()
                .ots(ots)
                .concepto(request.getConcepto())
                .idUnidadMedida(request.getIdUnidadMedida())
                .cantidad(request.getCantidad())
                .precio(request.getPrecio())
                .total(total)
                .proveedor(proveedor)
                .build();

        gastoRepository.save(gasto);

        return mapToResponse(gasto);
    }

    // ✏️ EDITAR
    @Override
    public GastoLogisticoResponse editar(Integer idGastoLog, GastoLogisticoRequest request) {

        GastoLogisticoOt gasto = gastoRepository.findById(idGastoLog)
                .orElseThrow(() -> new RuntimeException("Gasto no encontrado"));

        Proveedor proveedor = null;
        if (request.getIdProveedor() != null) {
            proveedor = proveedorRepository.findById(request.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        }

        BigDecimal total =
                request.getCantidad().multiply(request.getPrecio());

        gasto.setConcepto(request.getConcepto());
        gasto.setIdUnidadMedida(request.getIdUnidadMedida());
        gasto.setCantidad(request.getCantidad());
        gasto.setPrecio(request.getPrecio());
        gasto.setTotal(total);
        gasto.setProveedor(proveedor);

        gastoRepository.save(gasto);

        return mapToResponse(gasto);
    }

    // 📄 LISTAR POR OT
    @Override
    public List<GastoLogisticoResponse> listarPorOt(Integer idOts) {

        return gastoRepository.findByOts_IdOts(idOts)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 MAPPER
    private GastoLogisticoResponse mapToResponse(GastoLogisticoOt g) {
        return GastoLogisticoResponse.builder()
                .idGastoLog(g.getIdGastoLog())
                .concepto(g.getConcepto())
                .unidadMedida(
                        g.getIdUnidadMedida() != null
                                ? String.valueOf(g.getIdUnidadMedida())
                                : null
                )
                .proveedor(
                        g.getProveedor() != null
                                ? g.getProveedor().getRazonSocial()
                                : null
                )
                .cantidad(g.getCantidad())
                .precioUnitario(g.getPrecio())
                .subtotal(g.getTotal())
                .build();
    }
}



