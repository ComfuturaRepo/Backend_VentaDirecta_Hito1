package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ats_riesgo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSRiesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private ATS ats;

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
}