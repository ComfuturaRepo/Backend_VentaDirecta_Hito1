package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    private String razonSocial;

    @Column(unique = true, length = 11)
    private String ruc;

    private Boolean activo = true;
}

