package com.backend.comfutura.model.ssoma.ats;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ats_participante")
public class AtsParticipante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats")
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private RolTrabajo rol;
}

