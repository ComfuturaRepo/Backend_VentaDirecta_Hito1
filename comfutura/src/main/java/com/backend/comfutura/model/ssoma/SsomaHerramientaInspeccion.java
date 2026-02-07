package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ssoma_herramienta_inspeccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaHerramientaInspeccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_herramienta")
    private Integer idHerramienta;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @ManyToOne
    @JoinColumn(name = "id_herramienta_maestra")
    private SsomaHerramientaMaestra herramientaMaestra;

    @Column(name = "herramienta_nombre", length = 150)
    private String herramientaNombre;

    @Column(name = "p1")
    private Boolean p1;

    @Column(name = "p2")
    private Boolean p2;

    @Column(name = "p3")
    private Boolean p3;

    @Column(name = "p4")
    private Boolean p4;

    @Column(name = "p5")
    private Boolean p5;

    @Column(name = "p6")
    private Boolean p6;

    @Column(name = "p7")
    private Boolean p7;

    @Column(name = "p8")
    private Boolean p8;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "herramientaInspeccion", cascade = CascadeType.ALL)
    private List<SsomaHerramientaFoto> fotos;
}