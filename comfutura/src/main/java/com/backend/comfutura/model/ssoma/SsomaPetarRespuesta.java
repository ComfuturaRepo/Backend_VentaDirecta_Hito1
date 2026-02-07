package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_petar_respuestas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaPetarRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_respuesta")
    private Integer idRespuesta;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private SsomaPetar petar;

    @ManyToOne
    @JoinColumn(name = "id_pregunta")
    private SsomaPetarPreguntaMaestra pregunta;

    @Column(name = "respuesta")
    private Boolean respuesta;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}