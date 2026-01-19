package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ots_detalle")
@Data
public class OtsDetalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ots_detalle")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ots")
    private Ots ots;

    @ManyToOne
    @JoinColumn(name = "id_maestro")
    private MaestroCodigo maestro;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    private BigDecimal cantidad;
    private BigDecimal precioUnitario;
}
