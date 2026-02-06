package com.backend.comfutura.model.ssoma.inspeccion;

import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.ssoma.catalogo.Epp;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "inspeccion_epp_detalle")
@Data
public class InspeccionEppDetalle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_inspeccion", nullable = false)
    private InspeccionEpp inspeccion;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "id_epp", nullable = false)
    private Epp epp;

    @Column(name = "cumple")
    private Boolean cumple;

    @Column(name = "accion_correctiva")
    private String accionCorrectiva;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_inspeccion")
    private LocalDateTime fechaInspeccion = LocalDateTime.now();
}