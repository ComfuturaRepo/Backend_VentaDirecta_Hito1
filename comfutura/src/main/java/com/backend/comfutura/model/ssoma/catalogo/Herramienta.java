package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "herramienta")
@Data
public class Herramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "activo")
    private Boolean activo = true;
}