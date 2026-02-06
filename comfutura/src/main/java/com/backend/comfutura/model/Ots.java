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

    @Column(name = "id_ots_anterior")
    private Integer idOtsAnterior;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_area")
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_fase")
    private Fase fase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tipo_ot", nullable = false)
    private TipoOt tipoOt;

    // =========================
    // MODIFICACIÓN AQUÍ: Site y SiteDescripcion
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_site")
    private Site site;  // Hacer nullable para compatibilidad

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_site_descripcion")
    private SiteDescripcion siteDescripcion;  // NUEVA RELACIÓN

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_region")
    private Region region;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    // =========================
    // ROLES → TODOS SON TRABAJADORES
    // =========================

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefatura_cliente_solicitante")
    private JefaturaClienteSolicitante jefaturaClienteSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analista_cliente_solicitante")
    private AnalistaClienteSolicitante analistaClienteSolicitante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_coordinador_ti_cw")
    private Trabajador coordinadorTiCw;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jefatura_responsable")
    private Trabajador jefaturaResponsable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_liquidador")
    private Trabajador liquidador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ejecutante")
    private Trabajador ejecutante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_analista_contable")
    private Trabajador analistaContable;

    // =========================
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estado_ot")
    private EstadoOt estadoOt;

    @Column(name = "activo")
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    // =========================
    // MÉTODOS HELPER PARA OBTENER DATOS DE SITE
    // =========================

    @Transient
    public String getSiteDisplayText() {
        if (siteDescripcion != null && siteDescripcion.getSite() != null) {
            String codigo = siteDescripcion.getSite().getCodigoSitio() != null
                    ? siteDescripcion.getSite().getCodigoSitio().trim()
                    : "-";
            String desc = siteDescripcion.getDescripcion() != null
                    ? siteDescripcion.getDescripcion().trim()
                    : "";
            return codigo + " " + desc;
        } else if (site != null) {
            String codigo = site.getCodigoSitio() != null
                    ? site.getCodigoSitio().trim()
                    : "-";
            return codigo;
        }
        return "-";
    }

    @Transient
    public String getSiteCodigo() {
        if (siteDescripcion != null && siteDescripcion.getSite() != null) {
            return siteDescripcion.getSite().getCodigoSitio() != null
                    ? siteDescripcion.getSite().getCodigoSitio().trim()
                    : "-";
        } else if (site != null) {
            return site.getCodigoSitio() != null ? site.getCodigoSitio().trim() : "-";
        }
        return "-";
    }

    @Transient
    public String getSiteDescripcionText() {
        if (siteDescripcion != null) {
            return siteDescripcion.getDescripcion() != null
                    ? siteDescripcion.getDescripcion().trim()
                    : "";
        }
        return "";
    }
}