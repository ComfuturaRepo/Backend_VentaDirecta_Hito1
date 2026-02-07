package com.backend.comfutura.model.ssoma;


import com.backend.comfutura.model.Trabajador;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "ssoma_charla")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaCharla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_charla")
    private Integer idCharla;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @ManyToOne
    @JoinColumn(name = "id_tema")
    private SsomaTemaCharla tema;


    @Column(name = "fecha_charla")
    private LocalDateTime fechaCharla;

    @Column(name = "duracion_horas", precision = 4, scale = 2)
    private BigDecimal duracionHoras;

    @ManyToOne
    @JoinColumn(name = "capacitador_id")
    private Trabajador capacitador;

    @OneToOne(mappedBy = "charla", cascade = CascadeType.ALL)
    private SsomaCharlaVideo video;
}