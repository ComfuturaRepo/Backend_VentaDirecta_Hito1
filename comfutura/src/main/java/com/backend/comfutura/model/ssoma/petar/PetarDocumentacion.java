package com.backend.comfutura.model.ssoma.petar;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "petar_documentacion")
@Data
public class PetarDocumentacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private Petar petar;

    @Column(name = "pregunta")
    private String pregunta;

    @Column(name = "respuesta")
    private Boolean respuesta;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}