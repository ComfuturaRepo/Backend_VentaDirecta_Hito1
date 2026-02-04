package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "petar_respuesta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PETARRespuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private PETAR petar;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private PETARPregunta pregunta;

    @Column(name = "respuesta")
    private Boolean respuesta;
}