package com.backend.comfutura.model.ssoma.petar;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "petar_pregunta_apertura")
@Data
public class PetarPreguntaApertura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "pregunta", nullable = false)
    private String pregunta;

    @Column(name = "orden")
    private Integer orden = 0;

    @Column(name = "activo")
    private Boolean activo = true;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}