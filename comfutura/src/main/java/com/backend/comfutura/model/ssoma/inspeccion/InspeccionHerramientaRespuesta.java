package com.backend.comfutura.model.ssoma.inspeccion;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "inspeccion_herramienta_respuesta")
@Data
public class InspeccionHerramientaRespuesta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_inspeccion_detalle", nullable = false)
    private InspeccionHerramientaDetalle inspeccionDetalle;

    @ManyToOne
    @JoinColumn(name = "id_pregunta", nullable = false)
    private PreguntaHerramienta pregunta;

    @Column(name = "respuesta")
    private Boolean respuesta;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}