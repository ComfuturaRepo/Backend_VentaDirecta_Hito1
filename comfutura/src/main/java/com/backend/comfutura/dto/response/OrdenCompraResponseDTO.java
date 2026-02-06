package com.backend.comfutura.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrdenCompraResponseDTO {

    private Integer idOc;

    private LocalDateTime fechaOc;
    private String formaPago;
    private String observacion;

    private BigDecimal subtotal;
    private BigDecimal igvPorcentaje;
    private BigDecimal igvTotal;
    private BigDecimal total;

    // Estado
    private Integer idEstadoOc;
    private String estadoNombre;

    // Proveedor
    private Integer idProveedor;
    private String proveedorNombre;
    private String proveedorRuc;
    private String proveedorDireccion;
    private String proveedorContacto;
    private String proveedorBanco;

    // OT
    private Integer idOts;
    private String ot;
    private String otsDescripcion;

    // Detalles
    private List<OcDetalleResponseDTO> detalles;
}
