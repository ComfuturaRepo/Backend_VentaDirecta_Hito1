package com.backend.comfutura.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "empresa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empresa")
    private Integer id;

    private String nombre;

    private Boolean activo = true;
}
