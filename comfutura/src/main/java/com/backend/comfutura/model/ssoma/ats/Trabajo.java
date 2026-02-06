package com.backend.comfutura.model.ssoma.ats;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "trabajo")
@Data
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajo")
    private Integer idTrabajo;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo = true;
}