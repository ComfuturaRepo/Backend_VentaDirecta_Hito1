package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cronograma_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cronograma")
    private Integer idCronograma;

    // 🔹 OT asociada
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    // 🔹 Maestro Partida (combo)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_maestro_partida", nullable = false)
    private MaestroPartida maestroPartida;

    // 🔹 Datos del cronograma
    @Column(name = "duracion_dias", nullable = false)
    private BigDecimal duracionDias;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
}

