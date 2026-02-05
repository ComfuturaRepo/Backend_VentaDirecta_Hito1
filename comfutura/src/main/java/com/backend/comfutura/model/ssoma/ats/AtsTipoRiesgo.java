package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.ssoma.catalogo.TipoRiesgoTrabajo;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ats_tipo_riesgo")
public class AtsTipoRiesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats")
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_tipo_riesgo")
    private TipoRiesgoTrabajo tipoRiesgo;
}