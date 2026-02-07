package com.backend.comfutura.model.ssoma;


import com.backend.comfutura.model.Trabajador;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_inspeccion_trabajador")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaInspeccionTrabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_inspeccion")
    private Integer idInspeccion;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_inspeccion", length = 20)
    private TipoInspeccion tipoInspeccion;

    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    @Column(name = "trabajador_nombre", length = 150)
    private String trabajadorNombre;

    @Column(name = "casco")
    private Boolean casco;

    @Column(name = "lentes")
    private Boolean lentes;

    @Column(name = "orejeras")
    private Boolean orejeras;

    @Column(name = "tapones")
    private Boolean tapones;

    @Column(name = "guantes")
    private Boolean guantes;

    @Column(name = "botas")
    private Boolean botas;

    @Column(name = "arnes")
    private Boolean arnes;

    @Column(name = "chaleco")
    private Boolean chaleco;

    @Column(name = "mascarilla")
    private Boolean mascarilla;

    @Column(name = "gafas")
    private Boolean gafas;

    @Column(name = "otros", columnDefinition = "TEXT")
    private String otros;

    @Column(name = "accion_correctiva", columnDefinition = "TEXT")
    private String accionCorrectiva;

    @Column(name = "seguimiento", columnDefinition = "TEXT")
    private String seguimiento;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private Trabajador responsable;

    public enum TipoInspeccion {
        PLANIFICADA, NO_PLANIFICADA
    }
}