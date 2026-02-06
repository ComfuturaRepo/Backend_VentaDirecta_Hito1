package com.backend.comfutura.model.ssoma.inspeccion;

import com.backend.comfutura.model.Cliente;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "inspeccion_herramienta")
@Data
public class InspeccionHerramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Campos automáticos
    @Column(name = "fecha")
    private LocalDate fecha = LocalDate.now();

    @Column(name = "numero_registro", unique = true)
    private String numeroRegistro;

    // Información del cliente/proyecto
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @Column(name = "ubicacion_sede")
    private String ubicacionSede;

    // Personas
    @ManyToOne
    @JoinColumn(name = "id_supervisor")
    private Trabajador supervisor;

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
