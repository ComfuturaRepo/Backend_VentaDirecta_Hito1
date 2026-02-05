package com.backend.comfutura.model.ssoma.capacitacion;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "capacitacion")
public class Capacitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_capacitacion")
    private Integer idCapacitacion;

    @Column(name = "numero_registro", length = 50)
    private String numeroRegistro;

    @Column(name = "tema", length = 200)
    private String tema;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @ManyToOne
    @JoinColumn(name = "id_capacitador")
    private Trabajador capacitador;
}