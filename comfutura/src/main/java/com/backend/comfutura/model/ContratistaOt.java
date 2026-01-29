package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;


@Entity
@Table(name = "contratista_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratistaOt {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_contratista_ot")
    private Integer idContratistaOt;


    // 🔹 OT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;


    // 🔹 Maestro Servicio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maestro_servicio")
    private MaestroServicio maestroServicio;


    // 🔹 Unidad de medida (ID directo)
    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;


    // 🔹 Proveedor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;


    // 🔹 Cantidad
    @Column(nullable = false)
    private BigDecimal cantidad;


    // 🔹 Costo unitario (BD REAL)
    @Column(name = "costo_unitario", nullable = false)
    private BigDecimal costoUnitario;


    // 🔹 Total (BD REAL)
    @Column(nullable = false)
    private BigDecimal total;


    @Builder.Default
    private Boolean activo = true;
}


