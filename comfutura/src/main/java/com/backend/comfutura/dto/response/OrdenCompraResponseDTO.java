package com.backend.comfutura.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompraResponseDTO {

    private Integer idOc;

    // Estado OC
    private Integer idEstadoOc;
    private String estadoNombre;

    // OTS
    private Integer idOts;
    private String otsDescripcion;
    private Integer ot;          // número de OT


    // Cliente
    private String clienteNombre;
    private String clienteRuc;

    // Proveedor
    private Integer idProveedor;
    private String proveedorNombre;
    private String proveedorRuc;
    private String proveedorDireccion;
    private String proveedorContacto;
    private String proveedorBanco;

    // Forma de pago y montos
    private String formaPago;
    private BigDecimal subtotal;
    private BigDecimal igvPorcentaje;
    private BigDecimal igvTotal;
    private BigDecimal total;

    private LocalDateTime fechaOc;
    private String observacion;

    // Detalles
    private List<OcDetalleResponseDTO> detalles;

}
