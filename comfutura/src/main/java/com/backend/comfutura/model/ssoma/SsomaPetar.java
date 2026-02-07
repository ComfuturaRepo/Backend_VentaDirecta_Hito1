package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ssoma_petar")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaPetar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_petar")
    private Integer idPetar;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @Column(name = "energia_peligrosa")
    private Boolean energiaPeligrosa;

    @Column(name = "trabajo_altura")
    private Boolean trabajoAltura;

    @Column(name = "izaje")
    private Boolean izaje;

    @Column(name = "excavacion")
    private Boolean excavacion;

    @Column(name = "espacios_confinados")
    private Boolean espaciosConfinados;

    @Column(name = "trabajo_caliente")
    private Boolean trabajoCaliente;

    @Column(name = "otros")
    private Boolean otros;

    @Column(name = "otros_descripcion", columnDefinition = "TEXT")
    private String otrosDescripcion;

    @Column(name = "velocidad_aire", length = 50)
    private String velocidadAire;

    @Column(name = "contenido_oxigeno", length = 50)
    private String contenidoOxigeno;

    @Column(name = "hora_inicio_petar")
    private LocalTime horaInicioPetar;

    @Column(name = "hora_fin_petar")
    private LocalTime horaFinPetar;

    @OneToMany(mappedBy = "petar", cascade = CascadeType.ALL)
    private List<SsomaPetarRespuesta> respuestas;

    @OneToMany(mappedBy = "petar", cascade = CascadeType.ALL)
    private List<SsomaEquipoProteccion> equiposProteccion;
}