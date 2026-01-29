package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.emuns.TipoViatico;
import com.backend.comfutura.dto.request.ViaticoRequest;
import com.backend.comfutura.dto.response.ViaticoResponse;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.ViaticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ViaticoServiceImpl implements ViaticoService {

    private final ViaticoOtRepository viaticoRepository;
    private final OtsRepository otsRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final ProveedorRepository proveedorRepository;
    private final BancoRepository bancoRepository;

    @Override
    public ViaticoResponse crear(ViaticoRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        BigDecimal total =
                request.getTipo() == TipoViatico.VIATICO
                        ? request.getCostoDia().multiply(request.getCantDias())
                        : request.getCantidad().multiply(request.getPrecio());

        ViaticoOt viatico = ViaticoOt.builder()
                .ots(ots)
                .tipo(request.getTipo())
                .concepto(request.getConcepto())
                .cantidad(request.getCantidad())
                .precio(request.getPrecio())
                .costoDia(request.getCostoDia())
                .cantDias(request.getCantDias())
                .total(total)
                .moneda(request.getMoneda())
                .build();

        viaticoRepository.save(viatico);

        return mapToResponse(viatico);
    }

    @Override
    public ViaticoResponse editar(Integer idViatico, ViaticoRequest request) {

        ViaticoOt viatico = viaticoRepository.findById(idViatico)
                .orElseThrow(() -> new RuntimeException("Viático no encontrado"));

        BigDecimal total =
                request.getTipo() == TipoViatico.VIATICO
                        ? request.getCostoDia().multiply(request.getCantDias())
                        : request.getCantidad().multiply(request.getPrecio());

        viatico.setConcepto(request.getConcepto());
        viatico.setCantidad(request.getCantidad());
        viatico.setPrecio(request.getPrecio());
        viatico.setCostoDia(request.getCostoDia());
        viatico.setCantDias(request.getCantDias());
        viatico.setTotal(total);

        viaticoRepository.save(viatico);

        return mapToResponse(viatico);
    }

    @Override
    public List<ViaticoResponse> listarPorOt(Integer idOts) {

        return viaticoRepository.findByOts_IdOts(idOts)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ViaticoResponse mapToResponse(ViaticoOt v) {
        return ViaticoResponse.builder()
                .idViatico(v.getIdViatico())
                .tipo(v.getTipo().name())
                .concepto(v.getConcepto())
                .precioUnitario(
                        v.getPrecio() != null ? v.getPrecio() : v.getCostoDia()
                )
                .subtotal(v.getTotal())
                .build();
    }
}
