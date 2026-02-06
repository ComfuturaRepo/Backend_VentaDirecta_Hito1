package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.ssoma.catalogo.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "ats_riesgo")
@Data
public class AtsRiesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_tarea")
    private Tarea tarea;

    @ManyToOne
    @JoinColumn(name = "id_peligro")
    private Peligro peligro;

    @ManyToOne
    @JoinColumn(name = "id_riesgo")
    private Riesgo riesgo;

    @ManyToOne
    @JoinColumn(name = "id_medida")
    private MedidaControl medida;

    @Column(name = "observacion")
    private String observacion;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}