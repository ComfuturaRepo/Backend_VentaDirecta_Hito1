package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "peligro")
@Data
public class Peligro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peligro")
    private Integer idPeligro;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;
}