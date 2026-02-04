package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "trabajo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajo")
    private Integer idTrabajo;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @OneToMany(mappedBy = "trabajo")
    private List<Tarea> tareas;

    @OneToMany(mappedBy = "trabajo")
    private List<ATS> atsList;
}