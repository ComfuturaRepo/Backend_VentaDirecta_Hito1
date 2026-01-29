package com.backend.comfutura.model;
import com.backend.comfutura.model.emuns.TipoViatico;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "viatico_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViaticoOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_viatico")
    private Integer idViatico;

    // 🔹 OT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    // 🔹 Tipo
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoViatico tipo;

    @Column(length = 150)
    private String concepto;

    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;

    private BigDecimal cantidad;
    private BigDecimal precio;

    @Column(name = "costo_dia")
    private BigDecimal costoDia;

    @Column(name = "cant_dias")
    private BigDecimal cantDias;

    private BigDecimal total;

    // 🔹 Relaciones
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    private LocalDate fecha;

    @Column(name = "tipo_doc", length = 20)
    private String tipoDoc;

    @Column(name = "ruc_dni", length = 20)
    private String rucDni;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_banco")
    private Banco banco;

    private String moneda;
    private String cuenta;
    private String cci;

    @Builder.Default
    private Boolean activo = true;
}

