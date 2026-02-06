package com.backend.comfutura.model.ssoma.capacitacion;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "capacitacion_asistente")
@Data
public class CapacitacionAsistente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_capacitacion", nullable = false)
    private Capacitacion capacitacion;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @Column(name = "asistio")
    private Boolean asistio = true;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}