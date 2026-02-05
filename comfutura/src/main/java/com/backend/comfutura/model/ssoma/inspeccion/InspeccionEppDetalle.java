package com.backend.comfutura.model.ssoma.inspeccion;

import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.ssoma.catalogo.Epp;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "inspeccion_epp_detalle")
public class InspeccionEppDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_inspeccion")
    private InspeccionEpp inspeccion;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "id_epp")
    private Epp epp;

    @Column(name = "cumple")
    private Boolean cumple;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
}