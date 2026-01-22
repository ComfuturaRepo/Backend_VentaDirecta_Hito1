package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "fase")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idFase;

    @Column(nullable = false, length = 100)
    private String nombre;

    private Integer orden;

    private Boolean activo = true;
}
