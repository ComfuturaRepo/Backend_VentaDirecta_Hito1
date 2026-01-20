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

    /* ================= PK ================= */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ots")
    private Integer idOts;

    /* ================= DATOS PRINCIPALES ================= */

    @Column(name = "ot", nullable = false, unique = true)
    private Integer ot;

    @ManyToOne
    @JoinColumn(name = "id_ots_anterior")
    private Ots otsAnterior;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_area")
    private Area area;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_fase")
    private Fase fase;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_site")
    private Site site;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_region")
    private Region region;

    /* ================= DESCRIPCIÓN ================= */

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    /* ================= RESPONSABLES ================= */

    @Column(name = "jefatura_cliente_solicitante", length = 150)
    private String jefaturaClienteSolicitante;

    @Column(name = "analista_cliente_solicitante", length = 150)
    private String analistaClienteSolicitante;

    @Column(
            name = "coordinadores_ti_cw_pext_energia",
            length = 500
    )
    private String coordinadoresTiCwPextEnergia;

    @Column(name = "jefatura_responsable", length = 150)
    private String jefaturaResponsable;

    @Column(name = "liquidador", length = 150)
    private String liquidador;

    @Column(name = "ejecutante", length = 150)
    private String ejecutante;

    @Column(name = "analista_contable", length = 150)
    private String analistaContable;

    /* ================= CONTROL ================= */

    @Column(name = "dias_asignados")
    private Integer diasAsignados = 0;

    @Column(name = "activo")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}
