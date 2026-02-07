package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_petar_pregunta_maestra")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaPetarPreguntaMaestra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "pregunta", length = 255, nullable = false)
    private String pregunta;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "activo")
    private Boolean activo;
}