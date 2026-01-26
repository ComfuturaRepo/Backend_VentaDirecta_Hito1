package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orden_compra")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oc")
    private Integer idOc;

    /* ================= RELACIONES ================= */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estado_oc", nullable = false)
    private EstadoOc estadoOC;

    /* ================= CAMPOS ================= */

    @Column(name = "id_ots", nullable = false)
    private Integer idOts;

    @Column(name = "id_proveedor", nullable = false)
    private Integer idProveedor;

    @Column(name = "forma_pago", length = 50)
    private String formaPago;

    private BigDecimal subtotal;

    @Column(name = "igv_porcentaje")
    private BigDecimal igvPorcentaje;

    @Column(name = "igv_total")
    private BigDecimal igvTotal;

    private BigDecimal total;

    @Column(name = "fecha_oc")
    private LocalDateTime fechaOc;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    /* ================= DETALLE ================= */

    @OneToMany(
            mappedBy = "ordenCompra",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OcDetalle> detalles;
}
