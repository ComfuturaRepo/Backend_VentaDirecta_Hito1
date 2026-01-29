
package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_compra_directa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoCompraDirecta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_cd")
    private Integer idEstadoCd;

    @Column(nullable = false, unique = true, length = 50)
    private String descripcion;

    @Column(nullable = false)
    private Boolean activo = true;
}

