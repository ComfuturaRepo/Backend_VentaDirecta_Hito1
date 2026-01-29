
package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "material_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material_ot")
    private Integer idMaterialOt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maestro_material")
    private MaestroMaterial maestroMaterial;

    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Column(name = "costo_unitario", nullable = false)
    private BigDecimal costoUnitario;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(length = 10)
    private String moneda;

    @Builder.Default
    private Boolean activo = true;
}


