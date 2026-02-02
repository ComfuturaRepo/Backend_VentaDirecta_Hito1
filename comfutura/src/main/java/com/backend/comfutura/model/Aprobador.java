package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "aprobador",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"id_cliente", "id_area", "nivel", "id_trabajador"}
        ))
@Data
public class Aprobador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAprobador;

    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_area", nullable = false)
    private Area area;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    private Integer nivel; // 1,2,3

    private Boolean activo = true;
}
