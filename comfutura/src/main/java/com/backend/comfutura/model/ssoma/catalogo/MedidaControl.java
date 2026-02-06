package com.backend.comfutura.model.ssoma.catalogo;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medida_control")
@Data
public class MedidaControl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_medida")
    private Integer idMedida;

    @ManyToOne
    @JoinColumn(name = "id_riesgo")
    private Riesgo riesgo;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;
}