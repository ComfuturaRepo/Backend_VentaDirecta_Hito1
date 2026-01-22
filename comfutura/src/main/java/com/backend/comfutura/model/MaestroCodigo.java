package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maestro_codigo")
@Data
public class MaestroCodigo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maestro")
    private Integer id;

    @Column(unique = true)
    private String codigo;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_unidad_medida")
    private UnidadMedida unidadMedida;

    private BigDecimal precioBase;
    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;
}
