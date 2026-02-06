package com.backend.comfutura.model.ssoma.inspeccion;

import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "inspeccion_epp")
@Data
public class InspeccionEpp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Campos automáticos
    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @Column(name = "numero_registro", unique = true)
    private String numeroRegistro;

    // Información de inspección
    @Column(name = "tipo_inspeccion")
    private String tipoInspeccion = "PLANIFICADA";

    @Column(name = "area_trabajo")
    private String areaTrabajo;

    // Personas
    @ManyToOne
    @JoinColumn(name = "id_inspector")
    private Trabajador inspector;

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

    // Auditoría
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
