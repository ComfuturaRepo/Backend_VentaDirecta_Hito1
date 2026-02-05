package com.backend.comfutura.model.ssoma.petar;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "petar_pregunta")
public class PetarPregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "descripcion", length = 200)
    private String descripcion;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}