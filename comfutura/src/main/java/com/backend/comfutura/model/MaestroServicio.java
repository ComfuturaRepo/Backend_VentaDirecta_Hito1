package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "maestro_servicio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaestroServicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maestro_servicio")
    private Integer idMaestroServicio;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Column(name = "id_unidad_medida")
    private Integer idUnidadMedida;

    @Column(name = "costo_base", precision = 12, scale = 2)
    private BigDecimal costoBase;

    private Boolean activo = true;
}