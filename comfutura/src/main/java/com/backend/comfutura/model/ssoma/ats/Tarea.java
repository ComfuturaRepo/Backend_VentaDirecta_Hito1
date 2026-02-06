package com.backend.comfutura.model.ssoma.ats;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tarea")
@Data
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarea")
    private Integer idTarea;

    @ManyToOne
    @JoinColumn(name = "id_trabajo")
    private Trabajo trabajo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;
}
