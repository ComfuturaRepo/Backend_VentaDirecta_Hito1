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
public class OrdenCompraHtmlDTO {

    // CABECERA OC
    private Integer idOc;
    private String formaPago;
    private BigDecimal subtotal;
    private BigDecimal igvPorcentaje;
    private BigDecimal igvTotal;
    private BigDecimal total;
    private LocalDateTime fechaOc;
    private String observacion;

    // PROVEEDOR
    private Integer idProveedor;
    private String proveedorRazonSocial;
    private String proveedorRuc;
    private String proveedorDireccion;
    private String proveedorContacto;
    private String proveedorBancoNombre; // <-- trae nombre del banco

    // OTS
    private Integer idCliente;
    private String clienteNombre;
    private String clienteRuc;
    private String otsDescripcion;

    // DETALLES
    private List<OcDetalleHtmlDTO> detalles;

}
