package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "ots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ots")
    private Integer idOts;

    @Column(name = "ot", nullable = false, unique = true)
    private Integer ot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots_anterior")
    private Ots otsAnterior;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_area")
    private Area area;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_fase")
    private Fase fase;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_site")
    private Site site;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_region")
    private Region region;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    // ── Relaciones uno-a-uno/muchos-a-uno (solo una persona por rol) ──
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefatura_cliente_solicitante")
    private JefaturaClienteSolicitante jefaturaClienteSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analista_cliente_solicitante")
    private AnalistaClienteSolicitante analistaClienteSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coordinador_ti_cw")   // ← corrige aquí
    private CoordinadorTiCwPextEnergia coordinadorTiCw;     // ← también cambia el nombre del campo y el tipo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefatura_responsable")
    private JefaturaResponsable jefaturaResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_liquidador")
    private Liquidador liquidador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejecutante")
    private Ejecutante ejecutante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analista_contable")
    private AnalistaContable analistaContable;

    @Column(name = "dias_asignados")
    private Integer diasAsignados = 0;

    @Column(name = "activo")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}