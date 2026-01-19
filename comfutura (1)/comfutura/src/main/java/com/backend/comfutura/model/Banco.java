package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "banco")
@Data
public class Banco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_banco")
    private Integer id;

    private String nombre;
    private Boolean activo = true;
}
