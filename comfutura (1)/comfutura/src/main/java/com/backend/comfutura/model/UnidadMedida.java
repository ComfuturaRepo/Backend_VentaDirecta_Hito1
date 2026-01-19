package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "unidad_medida")
@Data
public class UnidadMedida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_unidad_medida")
    private Integer id;

    @Column(unique = true)
    private String codigo;

    private String descripcion;
    private Boolean activo = true;
}
