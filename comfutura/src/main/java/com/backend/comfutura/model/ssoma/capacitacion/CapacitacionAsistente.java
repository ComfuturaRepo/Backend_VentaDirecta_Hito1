package com.backend.comfutura.model.ssoma.capacitacion;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "capacitacion_asistente")
public class CapacitacionAsistente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_capacitacion")
    private Capacitacion capacitacion;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}