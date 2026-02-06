package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "ats_participante")
@Data
public class AtsParticipante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolTrabajo rol;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}