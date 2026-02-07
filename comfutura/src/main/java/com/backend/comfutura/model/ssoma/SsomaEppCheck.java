package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ssoma_epp_checks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaEppCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_epp")
    private Integer idEpp;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @Column(name = "epp_nombre", length = 100)
    private String eppNombre;

    @Column(name = "usado")
    private Boolean usado;

    @OneToMany(mappedBy = "eppCheck", cascade = CascadeType.ALL)
    private List<SsomaEppFoto> fotos;
}