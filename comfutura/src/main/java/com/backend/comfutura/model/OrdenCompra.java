package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estado_oc")
    private EstadoOc estadoOc;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_ots")
    private Ots ots;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_maestro")
    private MaestroCodigo maestro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    /* ================= DATOS ================= */

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal cantidad;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal costoUnitario;

    @CreationTimestamp
    @Column(name = "fecha_oc", updatable = false)
    private LocalDateTime fechaOc;

    @Column(length = 255)
    private String observacion;
}
