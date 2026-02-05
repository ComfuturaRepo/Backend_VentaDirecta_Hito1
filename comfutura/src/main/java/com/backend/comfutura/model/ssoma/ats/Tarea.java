package com.backend.comfutura.model.ssoma.ats;


import com.backend.comfutura.model.ssoma.catalogo.TipoTrabajo;
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
    private TipoTrabajo trabajo;

    @Column(name = "descripcion", length = 200)
    private String descripcion;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}