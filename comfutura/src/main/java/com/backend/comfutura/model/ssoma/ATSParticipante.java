package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ats_participante")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATSParticipante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private ATS ats;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @ManyToOne
    @JoinColumn(name = "id_rol", nullable = false)
    private RolTrabajo rol;
}