package com.backend.comfutura.model.ssoma.ats;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "trabajo")
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajo")
    private Integer idTrabajo;

    @Column(name = "nombre", length = 150, nullable = false)
    private String nombre;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}