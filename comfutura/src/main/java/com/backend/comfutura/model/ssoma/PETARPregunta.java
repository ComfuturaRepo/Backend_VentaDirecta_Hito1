package com.backend.comfutura.model.ssoma;


import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "petar_pregunta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PETARPregunta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "pregunta")
    private List<PETARRespuesta> respuestas;
}