
package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "gasto_logistico_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GastoLogisticoOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gasto_log")
    private Integer idGastoLog;

    // 🔹 OT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    // 🔹 Concepto libre
    @Column(nullable = false, length = 150)
    private String concepto;

    // 🔹 Unidad de medida (ID directo)
    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;

    // 🔹 Cantidad
    @Column(nullable = false)
    private BigDecimal cantidad;

    // 🔹 Precio unitario (BD REAL)
    @Column(nullable = false)
    private BigDecimal precio;

    // 🔹 Total (BD REAL)
    @Column(nullable = false)
    private BigDecimal total;

    // 🔹 Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @Builder.Default
    private Boolean activo = true;
}

