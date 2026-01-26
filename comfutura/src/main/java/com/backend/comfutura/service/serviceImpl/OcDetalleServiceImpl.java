
package com.backend.comfutura.service.serviceImpl;


import com.backend.comfutura.dto.request.OcDetalleRequestDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.model.OcDetalle;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.repository.OcDetalleRepository;
import com.backend.comfutura.repository.OrdenCompraRepository;
import com.backend.comfutura.service.OcDetalleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;


import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class OcDetalleServiceImpl implements OcDetalleService {

    private final OcDetalleRepository ocDetalleRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    /* ==================================================
       CREAR / EDITAR (UNO)
       ================================================== */
    @Override
    public OcDetalleResponseDTO guardar(
            Integer idOc,
            Integer idDetalle,
            OcDetalleRequestDTO dto
    ) {

        OcDetalle detalle;

        // ========= EDITAR =========
        if (idDetalle != null) {
            detalle = ocDetalleRepository.findById(idDetalle)
                    .orElseThrow(() -> new RuntimeException("Detalle no existe"));
        }
        // ========= CREAR =========
        else {
            OrdenCompra oc = ordenCompraRepository.findById(idOc)
                    .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));

            detalle = new OcDetalle();
            detalle.setOrdenCompra(oc);
        }

        // ========= PATCH =========
        if (dto.getIdMaestro() != null)
            detalle.setIdMaestro(dto.getIdMaestro());

        if (dto.getCantidad() != null)
            detalle.setCantidad(dto.getCantidad());

        if (dto.getPrecioUnitario() != null)
            detalle.setPrecioUnitario(dto.getPrecioUnitario());

        if (dto.getSubtotal() != null)
            detalle.setSubtotal(dto.getSubtotal());

        if (dto.getIgv() != null)
            detalle.setIgv(dto.getIgv());

        if (dto.getTotal() != null)
            detalle.setTotal(dto.getTotal());

        return mapToResponse(ocDetalleRepository.save(detalle));
    }

    /* ==================================================
       LISTAR POR OC (PAGINADO)
       ================================================== */
    @Override
    public Page<OcDetalleResponseDTO> listarPorOc(
            Integer idOc,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ocDetalleRepository
                .findByOrdenCompra_IdOc(idOc, pageable)
                .map(this::mapToResponse);
    }

    /* ==================================================
       GUARDAR LISTA (BULK) — SIN BORRAR
       ================================================== */
    @Override
    public void guardarDetalles(
            Integer idOc,
            List<OcDetalleRequestDTO> detalles
    ) {

        if (detalles == null || detalles.isEmpty()) {
            return;
        }

        OrdenCompra oc = ordenCompraRepository.findById(idOc)
                .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));

        List<OcDetalle> nuevos = detalles.stream()
                .map(d -> OcDetalle.builder()
                        .ordenCompra(oc)
                        .idMaestro(d.getIdMaestro())
                        .cantidad(d.getCantidad())
                        .precioUnitario(d.getPrecioUnitario())
                        .subtotal(d.getSubtotal())
                        .igv(d.getIgv())
                        .total(d.getTotal())
                        .build())
                .toList();

        ocDetalleRepository.saveAll(nuevos);
    }

    /* ==================================================
       MAPPER
       ================================================== */
    private OcDetalleResponseDTO mapToResponse(OcDetalle d) {

        return OcDetalleResponseDTO.builder()
                .idOcDetalle(d.getIdOcDetalle())
                .idOc(d.getOrdenCompra().getIdOc())
                .idMaestro(d.getIdMaestro())
                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .igv(d.getIgv())
                .total(d.getTotal())
                .build();
    }
}
