package com.backend.comfutura.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estado_oc")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoOc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado_oc")
    private Integer idEstadoOc;

    @Column(nullable = false, unique = true, length = 50)
    private String nombre;
}
