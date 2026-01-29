package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "planilla_trabajo_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanillaTrabajoOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_planilla")
    private Integer idPlanilla;

    // 🔹 OT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    // 🔹 Trabajador
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    // 🔹 Cargo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;

    private LocalDate fecha;

    @Column(name = "costo_dia")
    private BigDecimal costoDia;

    @Column(name = "cant_dias")
    private BigDecimal cantDias;

    private BigDecimal total;

    // 🔹 Pago
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_banco")
    private Banco banco;

    private String moneda;
    private String cuenta;
    private String cci;

    @Builder.Default
    private Boolean activo = true;
}
