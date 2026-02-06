package com.backend.comfutura.model.ssoma.petar;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
@Entity
@Table(name = "petar_respuesta_apertura")
@Data
public class PetarRespuestaApertura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private Petar petar;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private PetarPreguntaApertura pregunta;

    @Column(name = "respuesta")
    private Boolean respuesta;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}