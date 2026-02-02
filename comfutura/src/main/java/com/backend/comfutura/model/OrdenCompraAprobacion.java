package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "orden_compra_aprobacion")
@Data
public class OrdenCompraAprobacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAprobacion;

    @ManyToOne
    @JoinColumn(name = "id_oc")
    private OrdenCompra ordenCompra;

    private Integer nivel;
    private String estado;
    private String aprobadoPor;





    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
