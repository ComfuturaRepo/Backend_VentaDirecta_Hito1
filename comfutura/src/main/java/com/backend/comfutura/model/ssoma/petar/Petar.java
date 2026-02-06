package com.backend.comfutura.model.ssoma.petar;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "petar")
@Data
public class Petar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Campos automáticos
    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @Column(name = "numero_registro", unique = true)
    private String numeroRegistro;

    // Evaluación de ambiente de trabajo
    @Column(name = "requiere_evaluacion_ambiente")
    private Boolean requiereEvaluacionAmbiente = false;

    @Column(name = "apertura_linea_equipos")
    private Boolean aperturaLineaEquipos = false;

    // Personas
    @ManyToOne
    @JoinColumn(name = "id_supervisor_trabajo")
    private Trabajador supervisorTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_responsable_lugar")
    private Trabajador responsableLugar;

    @ManyToOne
    @JoinColumn(name = "id_supervisor_sst")
    private Trabajador supervisorSst;

    @ManyToOne
    @JoinColumn(name = "id_brigadista")
    private Trabajador brigadista;

    @ManyToOne
    @JoinColumn(name = "id_responsable_trabajo")
    private Trabajador responsableTrabajo;

    // Vinculación con OT
    @ManyToOne
    @JoinColumn(name = "id_ots")
    private Ots ots;

    // Información adicional
    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "recursos_necesarios")
    private String recursosNecesarios;

    @Column(name = "procedimiento")
    private String procedimiento;

    // Auditoría
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}