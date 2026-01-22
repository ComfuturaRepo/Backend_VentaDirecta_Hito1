package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer idCliente;

    private String razonSocial;

    @Column(name = "ruc", columnDefinition = "char(11)")
    private String ruc;
    @ManyToMany(mappedBy = "clientes")
    private List<Area> areas = new ArrayList<>();
    private Boolean activo = true;
}

