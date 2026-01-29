package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.OrdenCompraService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final EstadoOcRepository estadoOCRepository;
    private final ProveedorRepository proveedorRepository;
    private final OtsRepository otsRepository;
    private final MaestroCodigoRepository maestroCodigoRepository;
    private final EmpresaRepository empresaRepository;
    private final SpringTemplateEngine templateEngine;

    /* =====================================================
       CREAR / EDITAR ORDEN DE COMPRA
       ===================================================== */
    @Override
    @Transactional
    public OrdenCompraResponseDTO guardar(Integer idOc, OrdenCompraRequestDTO dto) {

        OrdenCompra oc;

        // 1️⃣ OC existente o nueva
        if (idOc != null) {
            oc = ordenCompraRepository.findById(idOc)
                    .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));
            if (oc.getDetalles() != null) oc.getDetalles().clear();
        } else {
            oc = new OrdenCompra();
        }

        // 2️⃣ Relaciones
        if (dto.getIdEstadoOc() != null) {
            EstadoOc estadoOC = estadoOCRepository.findById(dto.getIdEstadoOc())
                    .orElseThrow(() -> new RuntimeException("Estado OC no existe"));
            oc.setEstadoOC(estadoOC);
        }

        if (dto.getIdProveedor() != null) {
            Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                    .orElseThrow(() -> new RuntimeException("Proveedor no existe"));
            oc.setProveedor(proveedor);
        }

        if (dto.getIdOts() != null) {
            Ots ots = otsRepository.findById(dto.getIdOts())
                    .orElseThrow(() -> new RuntimeException("OT no existe"));
            oc.setOts(ots);
        }

        // 3️⃣ Campos simples
        oc.setFormaPago(dto.getFormaPago());
        oc.setFechaOc(dto.getFechaOc());
        oc.setObservacion(dto.getObservacion());

        // Guardar primero para cascade
        oc = ordenCompraRepository.save(oc);
        final OrdenCompra ocFinal = oc;

        // 4️⃣ Detalles
        AtomicReference<BigDecimal> subtotalRef = new AtomicReference<>(BigDecimal.ZERO);

        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            List<OcDetalle> detalles = dto.getDetalles().stream().map(d -> {
                OcDetalle detalle = new OcDetalle();
                detalle.setIdMaestro(d.getIdMaestro());
                detalle.setCantidad(d.getCantidad());
                detalle.setPrecioUnitario(d.getPrecioUnitario());

                BigDecimal sub = d.getPrecioUnitario().multiply(d.getCantidad());
                detalle.setSubtotal(sub);

                subtotalRef.updateAndGet(current -> current.add(sub));

                detalle.setOrdenCompra(ocFinal);
                return detalle;
            }).collect(Collectors.toList());

            oc.getDetalles().addAll(detalles);
            ordenCompraRepository.save(oc); // CascadeType.ALL guarda detalles
        }

        // 5️⃣ Totales
        BigDecimal subtotalOc = subtotalRef.get();
        BigDecimal igvPorcentaje = dto.getIgvPorcentaje() != null ? dto.getIgvPorcentaje() : BigDecimal.ZERO;
        BigDecimal igvTotal = (dto.getAplicarIgv() != null && dto.getAplicarIgv())
                ? subtotalOc.multiply(igvPorcentaje).divide(BigDecimal.valueOf(100))
                : BigDecimal.ZERO;

        oc.setSubtotal(subtotalOc);
        oc.setIgvPorcentaje(igvPorcentaje);
        oc.setIgvTotal(igvTotal);
        oc.setTotal(subtotalOc.add(igvTotal));

        OrdenCompra guardado = ordenCompraRepository.save(oc);
        return mapToResponseCompleto(guardado);
    }

    /* =====================================================
       LISTADO PAGINADO
       ===================================================== */
    @Override
    public Page<OrdenCompraResponseDTO> listarPaginado(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("DESC")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        return ordenCompraRepository.findAll(pageable) // <- EntityGraph carga relaciones
                .map(this::mapToResponseCompleto);       // usa el mapper completo
    }

    /* =====================================================
       GENERAR HTML (THYMELEAF)
       ===================================================== */
    @Override
    public String generarHtml(Integer idOc, Integer idEmpresa) {

        OrdenCompraResponseDTO oc = ordenCompraRepository.findById(idOc)
                .map(this::mapToResponseCompleto)
                .orElseThrow(() -> new RuntimeException("Orden de compra no encontrada"));

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));

        Context context = new Context();
        context.setVariable("oc", oc);
        context.setVariable("empresa", empresa);
        context.setVariable("fechaImpresion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        context.setVariable("paginaActual", 1);
        context.setVariable("totalPaginas", 1);

        return templateEngine.process("orden-compra", context);
    }

    /* =====================================================
       MAPPERS
       ===================================================== */
    private OrdenCompraResponseDTO mapToResponse(@NonNull OrdenCompra oc) {
        return OrdenCompraResponseDTO.builder()
                .idOc(oc.getIdOc())
                .fechaOc(oc.getFechaOc())
                .formaPago(oc.getFormaPago())
                .observacion(oc.getObservacion())
                .subtotal(oc.getSubtotal())
                .igvPorcentaje(oc.getIgvPorcentaje())
                .igvTotal(oc.getIgvTotal())
                .total(oc.getTotal())
                .estadoNombre(oc.getEstadoOC() != null ? oc.getEstadoOC().getNombre() : "")
                .idEstadoOc(oc.getEstadoOC() != null ? oc.getEstadoOC().getIdEstadoOc() : null)
                .build();
    }

    private OrdenCompraResponseDTO mapToResponseCompleto(OrdenCompra oc) {

        // Proveedor
        Proveedor proveedor = oc.getProveedor(); // directamente desde la relación
        // OT
        Ots ots = oc.getOts(); // directamente desde la relación

        // Detalles
        List<OcDetalleResponseDTO> detalles =
                oc.getDetalles() == null ? List.of() :
                        oc.getDetalles().stream()
                                .map(d -> {
                                    MaestroCodigo maestro = maestroCodigoRepository.findById(d.getIdMaestro()).orElse(null);

                                    return OcDetalleResponseDTO.builder()
                                            .idOcDetalle(d.getIdOcDetalle())
                                            .codigo(maestro != null ? maestro.getCodigo() : "")
                                            .descripcion(maestro != null ? maestro.getDescripcion() : "")

                                            .unidad(maestro != null && maestro.getUnidadMedida() != null ? maestro.getUnidadMedida().getCodigo() : "")
                                            .cantidad(d.getCantidad())
                                            .precioUnitario(d.getPrecioUnitario())
                                            .subtotal(d.getSubtotal())
                                            .igv(d.getIgv())
                                            .total(d.getTotal())
                                            .build();
                                })
                                .toList();

        // DTO final
        return OrdenCompraResponseDTO.builder()
                .idOc(oc.getIdOc())
                .fechaOc(oc.getFechaOc())
                .formaPago(oc.getFormaPago())
                .observacion(oc.getObservacion())
                .subtotal(oc.getSubtotal())
                .igvPorcentaje(oc.getIgvPorcentaje())
                .igvTotal(oc.getIgvTotal())
                .total(oc.getTotal())
                // Estado OC
                .idEstadoOc(oc.getEstadoOC() != null ? oc.getEstadoOC().getIdEstadoOc() : null)
                .estadoNombre(oc.getEstadoOC() != null ? oc.getEstadoOC().getNombre() : "-")
                // Proveedor
                .idProveedor(proveedor != null ? proveedor.getId() : null)
                .proveedorNombre(proveedor != null && proveedor.getRazonSocial() != null ? proveedor.getRazonSocial() : "SIN PROVEEDOR")
                .proveedorRuc(proveedor != null ? proveedor.getRuc() : "-")
                .proveedorDireccion(proveedor != null ? proveedor.getDireccion() : "-")
                .proveedorContacto(proveedor != null ? proveedor.getContacto() : "-")
                .proveedorBanco(proveedor != null && proveedor.getBanco() != null ? proveedor.getBanco().getNombre() : "-")
                // OT
                .idOts(ots != null ? ots.getIdOts() : null)
                .otsDescripcion(ots != null ? ots.getDescripcion() : "-")
                .ot(ots != null ? "OT-" + ots.getOt() : "-")
                // Detalles
                .detalles(detalles)
                .build();
    }


}
