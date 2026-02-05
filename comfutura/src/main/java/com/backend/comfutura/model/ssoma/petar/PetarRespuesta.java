package com.backend.comfutura.model.ssoma.petar;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "petar_respuesta")
public class PetarRespuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar")
    private Petar petar;

    @ManyToOne
    @JoinColumn(name = "id_pregunta")
    private PetarPregunta pregunta;

    @Column(name = "respuesta")
    private Boolean respuesta;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}