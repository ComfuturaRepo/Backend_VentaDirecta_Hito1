package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ats")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ATS {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ats")
    private Integer idATS;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @Column(name = "empresa", length = 150)
    private String empresa;

    @Column(name = "lugar_trabajo", length = 150)
    private String lugarTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_trabajo")
    private Trabajo trabajo;

    @OneToMany(mappedBy = "ats", cascade = CascadeType.ALL)
    private List<ATSParticipante> participantes = new ArrayList<>();

    @OneToMany(mappedBy = "ats", cascade = CascadeType.ALL)
    private List<ATSRiesgo> riesgos = new ArrayList<>();

    @OneToMany(mappedBy = "ats", cascade = CascadeType.ALL)
    private List<ATSEPP> epps = new ArrayList<>();

    @OneToMany(mappedBy = "ats", cascade = CascadeType.ALL)
    private List<ATSTipoRiesgo> tiposRiesgo = new ArrayList<>();
}