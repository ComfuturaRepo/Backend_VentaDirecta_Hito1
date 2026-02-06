package com.backend.comfutura.model.ssoma.capacitacion;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.ssoma.ats.Ats;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@Entity
@Table(name = "capacitacion")
@Data
public class Capacitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_capacitacion")
    private Integer idCapacitacion;

    // Campos automáticos
    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @Column(name = "hora")
    private LocalTime hora = LocalTime.now();

    @Column(name = "numero_registro", unique = true)
    private String numeroRegistro;

    // Información de la capacitación
    @Column(name = "tema")
    private String tema;

    @Column(name = "tipo_charla")
    private String tipoCharla = "CHARLA_5_MINUTOS";

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    // Personas
    @ManyToOne
    @JoinColumn(name = "id_capacitador")
    private Trabajador capacitador;

    @ManyToOne
    @JoinColumn(name = "id_supervisor_trabajo")
    private Trabajador supervisorTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_responsable_lugar")
    private Trabajador responsableLugar;

    @ManyToOne
    @JoinColumn(name = "id_supervisor_sst")
    private Trabajador supervisorSst;

    // Vinculación con OT y ATS
    @ManyToOne
    @JoinColumn(name = "id_ots")
    private Ots ots;

    @ManyToOne
    @JoinColumn(name = "id_ats")
    private Ats ats;

    // Auditoría
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
