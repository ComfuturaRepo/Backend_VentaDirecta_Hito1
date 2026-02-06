package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "epp")
@Data
public class Epp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_epp")
    private Integer idEpp;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "activo")
    private Boolean activo = true;
}
