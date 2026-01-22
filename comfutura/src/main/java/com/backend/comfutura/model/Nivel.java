package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "nivel")
@Data
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_nivel")
    private Integer id;

    @Column(unique = true)
    private String codigo;

    private String nombre;
    private String descripcion;
}
