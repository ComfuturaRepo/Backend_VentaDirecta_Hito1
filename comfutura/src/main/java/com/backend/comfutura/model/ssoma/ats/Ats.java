package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.ssoma.catalogo.TipoTrabajo;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "ats")
public class Ats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ats")
    private Integer idAts;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "empresa", length = 150)
    private String empresa;

    @Column(name = "lugar_trabajo", length = 150)
    private String lugarTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_trabajo")
    private TipoTrabajo trabajo;
}