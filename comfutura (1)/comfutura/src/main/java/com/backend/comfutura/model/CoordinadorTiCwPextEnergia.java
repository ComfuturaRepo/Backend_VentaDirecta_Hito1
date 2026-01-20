package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "coordinador_ti_cw")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CoordinadorTiCwPextEnergia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;
}