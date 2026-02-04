package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inspeccion_epp_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionEPPDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_inspeccion", nullable = false)
    private InspeccionEPP inspeccion;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "id_epp", nullable = false)
    private EPP epp;

    @Column(name = "cumple")
    private Boolean cumple;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
}