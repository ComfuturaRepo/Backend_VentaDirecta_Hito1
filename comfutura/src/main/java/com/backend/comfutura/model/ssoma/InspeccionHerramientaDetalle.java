package com.backend.comfutura.model.ssoma;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inspeccion_herramienta_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(name = "cumple")
    private Boolean cumple;

    @Column(name = "foto_url", length = 255)
    private String fotoUrl;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
}