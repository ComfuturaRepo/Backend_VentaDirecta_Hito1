package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Empresa;
import com.backend.comfutura.model.MaestroServicio;
import com.backend.comfutura.model.Ots;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ssoma_formulario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaFormulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ssoma")
    private Integer idSsoma;

    @ManyToOne
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    @ManyToOne
    @JoinColumn(name = "empresa_id")
    private Empresa empresa;

    @ManyToOne
    @JoinColumn(name = "trabajo_id")
    private MaestroServicio trabajo;

    @Column(precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitud;

    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "hora_inicio")
    private LocalTime horaInicio;

    @Column(name = "hora_fin")
    private LocalTime horaFin;

    @Column(name = "hora_inicio_trabajo")
    private LocalTime horaInicioTrabajo;

    @Column(name = "hora_fin_trabajo")
    private LocalTime horaFinTrabajo;

    @Column(name = "supervisor_trabajo", length = 150)
    private String supervisorTrabajo;

    @Column(name = "supervisor_sst", length = 150)
    private String supervisorSst;

    @Column(name = "responsable_area", length = 150)
    private String responsableArea;

    @OneToMany(mappedBy = "ssomaFormulario", cascade = CascadeType.ALL)
    private List<SsomaParticipante> participantes;

    @OneToMany(mappedBy = "ssomaFormulario", cascade = CascadeType.ALL)
    private List<SsomaSecuenciaTarea> secuenciasTarea;
}