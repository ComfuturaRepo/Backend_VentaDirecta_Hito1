package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ats_tipo_riesgo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSTipoRiesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private ATS ats;

    @ManyToOne
    @JoinColumn(name = "id_tipo_riesgo", nullable = false)
    private TipoRiesgoTrabajo tipoRiesgo;
}