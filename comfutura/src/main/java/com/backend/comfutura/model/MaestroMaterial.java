

package com.backend.comfutura.model;


import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;


@Entity
@Table(name = "maestro_material")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaestroMaterial {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maestro_material")
    private Integer idMaestroMaterial;


    @Column(nullable = false, unique = true, length = 20)
    private String codigo;


    @Column(nullable = false, length = 255)
    private String descripcion;


    //  SOLO ID (como en la BD)
    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;


    @Column(name = "costo_base", precision = 12, scale = 2)
    private BigDecimal costoBase;


    @Builder.Default
    private Boolean activo = true;
}