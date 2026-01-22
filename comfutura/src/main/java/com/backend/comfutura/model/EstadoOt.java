package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_ot")
    private Integer idEstadoOt;

    @Column(name = "descripcion", nullable = false, unique = true, length = 100)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;
}
