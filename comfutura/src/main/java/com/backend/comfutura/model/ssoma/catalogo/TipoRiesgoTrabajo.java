package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tipo_riesgo_trabajo")
public class TipoRiesgoTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}