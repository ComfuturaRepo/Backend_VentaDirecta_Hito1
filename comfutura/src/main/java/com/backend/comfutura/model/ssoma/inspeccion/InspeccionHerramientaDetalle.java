package com.backend.comfutura.model.ssoma.inspeccion;

import com.backend.comfutura.model.ssoma.catalogo.Herramienta;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "inspeccion_herramienta_detalle")
@Data
public class InspeccionHerramientaDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_inspeccion", nullable = false)
    private InspeccionHerramienta inspeccion;

    @ManyToOne
    @JoinColumn(name = "id_herramienta", nullable = false)
    private Herramienta herramienta;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_inspeccion")
    private LocalDateTime fechaInspeccion = LocalDateTime.now();
}