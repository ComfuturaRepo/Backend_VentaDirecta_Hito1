
package com.backend.comfutura.service.serviceImpl;



import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.model.EstadoOc;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.repository.EstadoOcRepository;
import com.backend.comfutura.repository.OrdenCompraRepository;
import com.backend.comfutura.service.OrdenCompraService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final EstadoOcRepository estadoOCRepository;

    @Override
    @Transactional
    public OrdenCompraResponseDTO guardar(Integer idOc, OrdenCompraRequestDTO dto) {

        OrdenCompra oc;

        // =====================
        // CREAR o EDITAR
        // =====================
        if (idOc != null) {
            oc = ordenCompraRepository.findById(idOc)
                    .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));
        } else {
            oc = new OrdenCompra();
        }

        // =====================
        // CAMPOS EDITABLES (PATCH)
        // =====================

        if (dto.getIdEstadoOc() != null) {
            EstadoOc estadoOC = estadoOCRepository.findById(dto.getIdEstadoOc())
                    .orElseThrow(() -> new RuntimeException("Estado OC no existe"));
            oc.setEstadoOC(estadoOC);
        }

        if (dto.getIdOts() != null) {
            oc.setIdOts(dto.getIdOts());
        }

        if (dto.getIdProveedor() != null) {
            oc.setIdProveedor(dto.getIdProveedor());
        }

        if (dto.getFormaPago() != null) {
            oc.setFormaPago(dto.getFormaPago());
        }

        if (dto.getSubtotal() != null) {
            oc.setSubtotal(dto.getSubtotal());
        }

        if (dto.getIgvPorcentaje() != null) {
            oc.setIgvPorcentaje(dto.getIgvPorcentaje());
        }

        if (dto.getIgvTotal() != null) {
            oc.setIgvTotal(dto.getIgvTotal());
        }

        if (dto.getTotal() != null) {
            oc.setTotal(dto.getTotal());
        }

        if (dto.getFechaOc() != null) {
            oc.setFechaOc(dto.getFechaOc());
        }

        if (dto.getObservacion() != null) {
            oc.setObservacion(dto.getObservacion());
        }

        OrdenCompra guardado = ordenCompraRepository.save(oc);
        return mapToResponse(guardado);
    }

    /* ================= LISTADO PAGINADO ================= */
    @Override
    public Page<OrdenCompraResponseDTO> listarPaginado(
            int page,
            int size,
            String sortBy,
            String direction
    ) {

        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ordenCompraRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    /* ================= MAPPER ================= */
    private OrdenCompraResponseDTO mapToResponse(OrdenCompra oc) {

        return OrdenCompraResponseDTO.builder()
                .idOc(oc.getIdOc())
                .idEstadoOc(oc.getEstadoOC().getIdEstadoOc())
                .estadoNombre(oc.getEstadoOC().getNombre())
                .idOts(oc.getIdOts())
                .idProveedor(oc.getIdProveedor())
                .formaPago(oc.getFormaPago())
                .subtotal(oc.getSubtotal())
                .igvPorcentaje(oc.getIgvPorcentaje())
                .igvTotal(oc.getIgvTotal())
                .total(oc.getTotal())
                .fechaOc(oc.getFechaOc())
                .observacion(oc.getObservacion())
                .detalles(
                        oc.getDetalles() == null ? null :
                                oc.getDetalles().stream()
                                        .map(d -> OcDetalleResponseDTO.builder()
                                                .idOcDetalle(d.getIdOcDetalle())
                                                .idMaestro(d.getIdMaestro())
                                                .cantidad(d.getCantidad())
                                                .precioUnitario(d.getPrecioUnitario())
                                                .subtotal(d.getSubtotal())
                                                .igv(d.getIgv())
                                                .total(d.getTotal())
                                                .build())
                                        .collect(Collectors.toList())
                )
                .build();
    }
}