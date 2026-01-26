
package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "oc_detalle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oc_detalle")
    private Integer idOcDetalle;

    /* ================= RELACIONES ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oc", nullable = false)
    private OrdenCompra ordenCompra;

    /* ================= CAMPOS ================= */

    @Column(name = "id_maestro", nullable = false)
    private Integer idMaestro;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private BigDecimal precioUnitario;

    private BigDecimal subtotal;
    private BigDecimal igv;
    private BigDecimal total;
}