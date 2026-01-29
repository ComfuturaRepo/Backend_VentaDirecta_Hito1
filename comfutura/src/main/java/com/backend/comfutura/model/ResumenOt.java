
package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "resumen_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumenOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resumen")
    private Integer idResumen;

    // 🔹 OT
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    @Column(name = "tipo_gasto", nullable = false, length = 50)
    private String tipoGasto;

    private BigDecimal monto;

    @Builder.Default
    private Boolean activo = true;
}
