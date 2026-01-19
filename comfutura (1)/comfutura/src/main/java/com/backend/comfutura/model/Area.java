package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "area")
@Data
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Integer id;

    private String nombre;
    private Boolean activo = true;
}
