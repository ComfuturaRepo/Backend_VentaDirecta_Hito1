package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.ContratistaRequest;
import com.backend.comfutura.dto.response.ContratistaResponse;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.ContratistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContratistaServiceImpl implements ContratistaService {

    private final ContratistaOtRepository contratistaRepository;
    private final OtsRepository otsRepository;
    private final MaestroServicioRepository maestroServicioRepository;
    private final ProveedorRepository proveedorRepository;

    // ➕ CREAR
    @Override
    public ContratistaResponse crear(ContratistaRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        MaestroServicio servicio = maestroServicioRepository
                .findById(request.getIdMaestroServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Proveedor proveedor = proveedorRepository
                .findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        BigDecimal total =
                request.getCantidad().multiply(request.getCostoUnitario());

        ContratistaOt contratista = ContratistaOt.builder()
                .ots(ots)
                .maestroServicio(servicio)
                .proveedor(proveedor)
                .idUnidadMedida(servicio.getIdUnidadMedida())
                .cantidad(request.getCantidad())
                .costoUnitario(request.getCostoUnitario())
                .total(total)
                .build();

        contratistaRepository.save(contratista);

        return mapToResponse(contratista);
    }

    // ✏️ EDITAR
    @Override
    public ContratistaResponse editar(Integer idContratistaOt, ContratistaRequest request) {

        ContratistaOt contratista = contratistaRepository.findById(idContratistaOt)
                .orElseThrow(() -> new RuntimeException("Contratista no encontrado"));

        MaestroServicio servicio = maestroServicioRepository
                .findById(request.getIdMaestroServicio())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Proveedor proveedor = proveedorRepository
                .findById(request.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        BigDecimal total =
                request.getCantidad().multiply(request.getCostoUnitario());

        contratista.setMaestroServicio(servicio);
        contratista.setProveedor(proveedor);
        contratista.setCantidad(request.getCantidad());
        contratista.setCostoUnitario(request.getCostoUnitario());
        contratista.setTotal(total);

        contratistaRepository.save(contratista);

        return mapToResponse(contratista);
    }

    // 📄 LISTAR POR OT
    @Override
    public List<ContratistaResponse> listarPorOt(Integer idOts) {

        return contratistaRepository.findByOts_IdOts(idOts)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 MAPPER
    private ContratistaResponse mapToResponse(ContratistaOt c) {
        return ContratistaResponse.builder()
                .idContratistaOt(c.getIdContratistaOt())
                .servicio(c.getMaestroServicio().getDescripcion())
                .proveedor(c.getProveedor().getRazonSocial())
                .unidadMedida(String.valueOf(c.getIdUnidadMedida()))
                .cantidad(c.getCantidad())
                // 👇 alias frontend
                .precioUnitario(c.getCostoUnitario())
                .subtotal(c.getTotal())
                .build();
    }
}



