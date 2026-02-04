package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "capacitacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Capacitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_capacitacion")
    private Integer idCapacitacion;

    @Column(name = "numero_registro", length = 50)
    private String numeroRegistro;

    @Column(name = "tema", length = 200)
    private String tema;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "hora")
    private LocalTime hora;

    @ManyToOne
    @JoinColumn(name = "id_capacitador")
    private Trabajador capacitador;

    @OneToMany(mappedBy = "capacitacion", cascade = CascadeType.ALL)
    private List<CapacitacionAsistente> asistentes = new ArrayList<>();
}