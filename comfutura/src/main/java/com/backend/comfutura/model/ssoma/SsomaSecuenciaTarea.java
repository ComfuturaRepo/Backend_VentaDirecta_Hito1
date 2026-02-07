package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_secuencia_tarea")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaSecuenciaTarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_secuencia")
    private Integer idSecuencia;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @Column(name = "secuencia_tarea", length = 255)
    private String secuenciaTarea;

    @Column(name = "peligro", length = 255)
    private String peligro;

    @Column(name = "riesgo", length = 255)
    private String riesgo;

    @Column(name = "consecuencias", length = 255)
    private String consecuencias;

    @Column(name = "medidas_control", length = 255)
    private String medidasControl;

    @Column(name = "orden")
    private Integer orden;
}