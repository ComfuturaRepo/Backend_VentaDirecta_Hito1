package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "riesgo")
@Data
public class Riesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riesgo")
    private Integer idRiesgo;

    @ManyToOne
    @JoinColumn(name = "id_peligro")
    private Peligro peligro;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;
}