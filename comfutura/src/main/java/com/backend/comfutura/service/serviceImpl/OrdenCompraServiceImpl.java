package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.service.OrdenCompraService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.thymeleaf.context.Context;


@Service
@RequiredArgsConstructor
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final EstadoOcRepository estadoOCRepository;
    private final ProveedorRepository proveedorRepository;
    private final OtsRepository otsRepository;
    private final MaestroCodigoRepository maestroCodigoRepository;
    private final SpringTemplateEngine templateEngine;
    private final EmpresaRepository empresaRepository;


    /* =====================================================
       CRUD - CREAR / EDITAR ORDEN DE COMPRA
       ===================================================== */
    @Override
    @Transactional
    public OrdenCompraResponseDTO guardar(Integer idOc, OrdenCompraRequestDTO dto) {

        OrdenCompra oc;

        if (idOc != null) {
            oc = ordenCompraRepository.findById(idOc)
                    .orElseThrow(() -> new RuntimeException("Orden de compra no existe"));
        } else {
            oc = new OrdenCompra();
        }

        if (dto.getIdEstadoOc() != null) {
            EstadoOc estadoOC = estadoOCRepository.findById(dto.getIdEstadoOc())
                    .orElseThrow(() -> new RuntimeException("Estado OC no existe"));
            oc.setEstadoOC(estadoOC);
        }

        if (dto.getIdOts() != null) oc.setIdOts(dto.getIdOts());
        if (dto.getIdProveedor() != null) oc.setIdProveedor(dto.getIdProveedor());
        if (dto.getFormaPago() != null) oc.setFormaPago(dto.getFormaPago());
        if (dto.getSubtotal() != null) oc.setSubtotal(dto.getSubtotal());
        if (dto.getIgvPorcentaje() != null) oc.setIgvPorcentaje(dto.getIgvPorcentaje());
        if (dto.getIgvTotal() != null) oc.setIgvTotal(dto.getIgvTotal());
        if (dto.getTotal() != null) oc.setTotal(dto.getTotal());
        if (dto.getFechaOc() != null) oc.setFechaOc(dto.getFechaOc());
        if (dto.getObservacion() != null) oc.setObservacion(dto.getObservacion());

        OrdenCompra guardado = ordenCompraRepository.save(oc);
        return mapToResponse(guardado);
    }

    /* =====================================================
       LISTADO PAGINADO
       ===================================================== */
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

    /* =====================================================
       MAPPER SIMPLE (API / CRUD)
       ===================================================== */
    private OrdenCompraResponseDTO mapToResponse(@NonNull OrdenCompra oc) {

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
                .build();
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

        // Crear contexto Thymeleaf
        Context context = new Context();
        context.setVariable("oc", oc);
        context.setVariable("empresa", empresa);
        context.setVariable("fechaImpresion", LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Páginas (ejemplo 1 de 1)
        context.setVariable("paginaActual", 1);
        context.setVariable("totalPaginas", 1);

        // Procesar template
        return templateEngine.process("orden-compra", context);
    }









    /* =====================================================
       MAPPER COMPLETO (HTML / PDF)
       ===================================================== */
    private OrdenCompraResponseDTO mapToResponseCompleto(OrdenCompra oc) {

        /* ========= PROVEEDOR ========= */
        Proveedor proveedor = proveedorRepository.findById(oc.getIdProveedor()).orElse(null);

        /* ========= OT / CLIENTE ========= */
        Ots ots = otsRepository.findById(oc.getIdOts()).orElse(null);

        /* ========= DETALLES ========= */
        List<OcDetalleResponseDTO> detalles =
                oc.getDetalles() == null ? List.of() :
                        oc.getDetalles().stream()
                                .map(d -> {

                                    MaestroCodigo maestro = maestroCodigoRepository
                                            .findById(d.getIdMaestro())
                                            .orElse(null);

                                    return OcDetalleResponseDTO.builder()
                                            .idOcDetalle(d.getIdOcDetalle())
                                            .codigo(maestro != null ? maestro.getCodigo() : "")
                                            .descripcion(maestro != null ? maestro.getDescripcion() : "")
                                            .unidad(
                                                    maestro != null && maestro.getUnidadMedida() != null
                                                            ? maestro.getUnidadMedida().getCodigo()
                                                            : ""
                                            )
                                            .cantidad(d.getCantidad())
                                            .precioUnitario(d.getPrecioUnitario()) // SIN IGV
                                            .subtotal(d.getSubtotal())
                                            .igv(d.getIgv())
                                            .total(d.getTotal())
                                            .build();
                                })
                                .toList();

        /* ========= DTO FINAL ========= */
        return OrdenCompraResponseDTO.builder()
                .idOc(oc.getIdOc())
                .fechaOc(oc.getFechaOc())
                .formaPago(oc.getFormaPago())
                .observacion(oc.getObservacion())

                // Totales
                .subtotal(oc.getSubtotal())
                .igvPorcentaje(oc.getIgvPorcentaje())
                .igvTotal(oc.getIgvTotal())
                .total(oc.getTotal())

                // Proveedor
                .proveedorNombre(proveedor != null ? proveedor.getRazonSocial() : "")
                .proveedorRuc(proveedor != null ? proveedor.getRuc() : "")
                .proveedorDireccion(proveedor != null ? proveedor.getDireccion() : "")
                .proveedorContacto(proveedor != null ? proveedor.getContacto() : "")
                .proveedorBanco(
                        proveedor != null && proveedor.getBanco() != null
                                ? proveedor.getBanco().getNombre()
                                : ""
                )

                // Cliente / OT
                .ot(
                        ots != null ? ots.getOt() : null
                )
                .clienteNombre(
                        ots != null && ots.getCliente() != null
                                ? ots.getCliente().getRazonSocial()
                                : ""
                )
                .clienteRuc(
                        ots != null && ots.getCliente() != null
                                ? ots.getCliente().getRuc()
                                : ""
                )
                .otsDescripcion(
                        ots != null ? ots.getDescripcion() : ""
                )



                .detalles(detalles)
                .build();
    }
}
