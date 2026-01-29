package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.OcDetalleRequestDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.model.MaestroCodigo;
import com.backend.comfutura.model.OcDetalle;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.repository.MaestroCodigoRepository;
import com.backend.comfutura.repository.OcDetalleRepository;
import com.backend.comfutura.repository.OrdenCompraRepository;
import com.backend.comfutura.service.OcDetalleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OcDetalleServiceImpl implements OcDetalleService {
    private final MaestroCodigoRepository maestroCodigoRepository;

    private final OcDetalleRepository ocDetalleRepository;
    private final OrdenCompraRepository ordenCompraRepository;

    /* ==================================================
       GUARDAR / EDITAR UN DETALLE
       ================================================== */
    @Override
    public OcDetalleResponseDTO guardar(Integer idOc, Integer idDetalle, OcDetalleRequestDTO dto) {

        OcDetalle detalle;
        OrdenCompra oc = ordenCompraRepository.findById(idOc)
                .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));

        // ========= EDITAR =========
        if (idDetalle != null) {
            detalle = ocDetalleRepository.findById(idDetalle)
                    .orElseThrow(() -> new RuntimeException("Detalle no existe"));
        }
        // ========= CREAR =========
        else {
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

        // Calcular subtotal y total automáticamente
        BigDecimal subtotal = detalle.getCantidad().multiply(detalle.getPrecioUnitario());
        detalle.setSubtotal(subtotal);
        detalle.setIgv(BigDecimal.ZERO); // IGV = 0
        detalle.setTotal(subtotal);      // total = subtotal

        // Guardar detalle
        OcDetalle detalleGuardado = ocDetalleRepository.save(detalle);

        // Actualizar totales de la OC
        actualizarTotalesOc(oc);

        return mapToResponse(detalleGuardado);
    }

    /* ==================================================
       GUARDAR LISTA (BULK)
       ================================================== */
    @Override
    public void guardarDetalles(Integer idOc, List<OcDetalleRequestDTO> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            return;
        }

        // Obtener la OC
        OrdenCompra oc = ordenCompraRepository.findById(idOc)
                .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));

        // Calcular subtotal
        BigDecimal subtotalOc = BigDecimal.ZERO;

        List<OcDetalle> nuevos = new ArrayList<>();
        for (OcDetalleRequestDTO d : detalles) {
            BigDecimal subtotal = d.getPrecioUnitario().multiply(d.getCantidad());
            subtotalOc = subtotalOc.add(subtotal);

            OcDetalle detalle = OcDetalle.builder()
                    .ordenCompra(oc)
                    .idMaestro(d.getIdMaestro())
                    .cantidad(d.getCantidad())
                    .precioUnitario(d.getPrecioUnitario())
                    .subtotal(subtotal)
                    .igv(BigDecimal.ZERO)  // IGV = 0
                    .total(subtotal)       // Total = subtotal
                    .build();

            nuevos.add(detalle);
        }

        // Reemplazar detalles en la OC de manera mutable
        oc.getDetalles().clear();
        oc.getDetalles().addAll(nuevos);

        // Calcular totales de la OC
        BigDecimal igvTotal = BigDecimal.ZERO; // ya no se aplica IGV
        BigDecimal totalOc = subtotalOc.add(igvTotal);

        oc.setSubtotal(subtotalOc);
        oc.setIgvTotal(igvTotal);
        oc.setTotal(totalOc);

        // Guardar OC (y los detalles por cascade)
        ordenCompraRepository.save(oc);
    }


    /* ==================================================
       LISTAR POR OC (PAGINADO)
       ================================================== */
    @Override
    public Page<OcDetalleResponseDTO> listarPorOc(Integer idOc, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ocDetalleRepository.findByOrdenCompra_IdOc(idOc, pageable)
                .map(this::mapToResponse);
    }

    /* ==================================================
       MAPPER
       ================================================== */
    private OcDetalleResponseDTO mapToResponse(OcDetalle d) {

        // 🔥 BUSCAR EL MAESTRO POR ID
        MaestroCodigo m = null;

        if (d.getIdMaestro() != null) {
            m = maestroCodigoRepository
                    .findById(d.getIdMaestro())
                    .orElse(null);
        }

        return OcDetalleResponseDTO.builder()
                .idOcDetalle(d.getIdOcDetalle())
                .idOc(d.getOrdenCompra().getIdOc())

                .idMaestro(d.getIdMaestro())

                // 🔥 DATOS DEL MAESTRO
                .codigo(m != null ? m.getCodigo() : null)
                .descripcion(m != null ? m.getDescripcion() : null)


                .cantidad(d.getCantidad())
                .precioUnitario(d.getPrecioUnitario())
                .subtotal(d.getSubtotal())
                .igv(d.getIgv())
                .total(d.getTotal())
                .build();
    }


    /* ==================================================
       MÉTODO AUXILIAR: actualizar totales de OC
       ================================================== */
    private void actualizarTotalesOc(OrdenCompra oc) {
        BigDecimal subtotalOc = oc.getDetalles().stream()
                .map(OcDetalle::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        oc.setSubtotal(subtotalOc);
        oc.setIgvTotal(BigDecimal.ZERO); // IGV siempre 0
        oc.setTotal(subtotalOc);          // total = subtotal

        ordenCompraRepository.save(oc);
    }
}
