package com.backend.comfutura.model.ssoma.petar;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "petar_evaluacion_ambiente")
@Data
public class PetarEvaluacionAmbiente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private Petar petar;

    @Column(name = "numero_evaluacion")
    private Integer numeroEvaluacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_ventilacion")
    private TipoVentilacion tipoVentilacion;

    @Column(name = "velocidad_aire")
    private BigDecimal velocidadAire;

    @Column(name = "contenido_oxigeno")
    private BigDecimal contenidoOxigeno;

    @Column(name = "limite_explosividad")
    private BigDecimal limiteExplosividad;

    @Column(name = "menor_tlb")
    private Boolean menorTlb;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_evaluacion")
    private LocalDateTime fechaEvaluacion = LocalDateTime.now();

    public enum TipoVentilacion {
        NATURAL, FORZADA
    }
}