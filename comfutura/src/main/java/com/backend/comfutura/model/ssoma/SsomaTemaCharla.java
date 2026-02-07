package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_tema_charla")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaTemaCharla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tema")
    private Integer idTema;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "activo")
    private Boolean activo;
}