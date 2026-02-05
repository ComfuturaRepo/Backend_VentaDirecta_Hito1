package com.backend.comfutura.model.ssoma.inspeccion;

import com.backend.comfutura.model.ssoma.catalogo.Herramienta;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inspeccion_herramienta_detalle")
public class InspeccionHerramientaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_inspeccion")
    private InspeccionHerramienta inspeccion;

    @ManyToOne
    @JoinColumn(name = "id_herramienta")
    private Herramienta herramienta;

    @Column(name = "cumple")
    private Boolean cumple;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
}