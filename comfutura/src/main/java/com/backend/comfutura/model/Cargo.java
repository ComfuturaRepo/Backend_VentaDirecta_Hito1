package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "cargo")
@Data
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_nivel")
    private Nivel nivel;

    private Boolean activo = true;
}
