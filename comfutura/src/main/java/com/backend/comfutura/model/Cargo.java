package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "cargo")
@Data
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer idCargo; // Cambié de "id" a "idCargo" para consistencia

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private Nivel nivel;

    private Boolean activo = true;

    @ManyToMany(mappedBy = "cargos")
    private Set<Permiso> permisos = new HashSet<>();
}