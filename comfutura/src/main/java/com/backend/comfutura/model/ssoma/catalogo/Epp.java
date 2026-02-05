package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "epp")
public class Epp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_epp")
    private Integer idEpp;

    @Column(name = "nombre", length = 100)
    private String nombre;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}