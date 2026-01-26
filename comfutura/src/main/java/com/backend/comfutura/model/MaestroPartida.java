package com.backend.comfutura.model;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "maestro_partida")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaestroPartida {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maestro_partida")
    private Integer idMaestroPartida;


    @Column(nullable = false, length = 20, unique = true)
    private String codigo;


    @Column(nullable = false, length = 255)
    private String descripcion;


    @Column(nullable = false)
    private Boolean activo = true;
}