package com.backend.comfutura.model.ssoma;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_equipo_proteccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaEquipoProteccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Integer idEquipo;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private SsomaPetar petar;

    @Column(name = "equipo_nombre", length = 100)
    private String equipoNombre;

    @Column(name = "usado")
    private Boolean usado;
}