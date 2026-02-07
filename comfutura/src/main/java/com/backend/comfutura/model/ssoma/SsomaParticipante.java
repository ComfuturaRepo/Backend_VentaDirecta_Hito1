package com.backend.comfutura.model.ssoma;


import com.backend.comfutura.model.Trabajador;
import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ssoma_participantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaParticipante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_participante")
    private Integer idParticipante;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @Column(name = "cargo", length = 100)
    private String cargo;

    @OneToOne(mappedBy = "participante", cascade = CascadeType.ALL)
    private SsomaParticipanteFirma firma;

    @OneToMany(mappedBy = "participante", cascade = CascadeType.ALL)
    private List<SsomaParticipanteFoto> fotos;
}