package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "riesgo")
public class Riesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riesgo")
    private Integer idRiesgo;

    @ManyToOne
    @JoinColumn(name = "id_peligro")
    private Peligro peligro;

    @Column(name = "descripcion", length = 200)
    private String descripcion;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}