package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "ats")
@Data
public class Ats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ats")
    private Integer idAts;

    // Campos automáticos
    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @Column(name = "hora")
    private LocalTime hora = LocalTime.now();

    @Column(name = "numero_registro", unique = true)
    private String numeroRegistro;

    // Información básica
    @Column(name = "empresa")
    private String empresa;

    @Column(name = "lugar_trabajo")
    private String lugarTrabajo;

    @Column(name = "coordenadas")
    private String coordenadas;

    // Relaciones con catálogos
    @ManyToOne
    @JoinColumn(name = "id_trabajo")
    private Trabajo trabajo;

    // Personas (comunes a todas las hojas)
    @ManyToOne
    @JoinColumn(name = "id_supervisor_trabajo")
    private Trabajador supervisorTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_responsable_lugar")
    private Trabajador responsableLugar;

    @ManyToOne
    @JoinColumn(name = "id_supervisor_sst")
    private Trabajador supervisorSst;

    // Vinculación con OT
    @ManyToOne
    @JoinColumn(name = "id_ots")
    private Ots ots;

    // Fechas de auditoría
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion = LocalDateTime.now();
}
