package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.ssoma.catalogo.TipoRiesgoTrabajo;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "ats_tipo_riesgo")
@Data
public class AtsTipoRiesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_tipo_riesgo", nullable = false)
    private TipoRiesgoTrabajo tipoRiesgo;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}