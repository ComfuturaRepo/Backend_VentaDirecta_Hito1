package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ots")
@Data
public class Ots {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ots")
    private Integer id;

    private Long ot;
    private String ceco;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_area")
    private Area area;

    private String descripcion;
    private LocalDate fechaApertura;

    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;
}
