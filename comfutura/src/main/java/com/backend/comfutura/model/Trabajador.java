package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "trabajador")
@Data
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabajador")
    private Integer idTrabajador;

    private String nombres;
    private String apellidos;

    @Column(columnDefinition = "CHAR(8)", unique = true)
    private String dni;

    private String celular;
    private String correoCorporativo;

    @ManyToOne
    @JoinColumn(name = "id_empresa")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;

    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    // Campos nuevos
    @Column(name = "puede_ser_liquidador")
    private Boolean puedeSerLiquidador = false;

    @Column(name = "puede_ser_ejecutante")
    private Boolean puedeSerEjecutante = false;

    @Column(name = "puede_ser_analista_contable")
    private Boolean puedeSerAnalistaContable = false;

    @Column(name = "puede_ser_jefatura_responsable")
    private Boolean puedeSerJefaturaResponsable = false;

    @Column(name = "puede_ser_coordinador_ti_cw")
    private Boolean puedeSerCoordinadorTiCw = false;
}