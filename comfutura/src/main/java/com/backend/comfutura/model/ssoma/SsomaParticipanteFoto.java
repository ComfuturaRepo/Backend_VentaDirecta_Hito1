package com.backend.comfutura.model.ssoma;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_participante_fotos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaParticipanteFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer idFoto;

    @ManyToOne
    @JoinColumn(name = "id_participante", nullable = false)
    private SsomaParticipante participante;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_foto", length = 20)
    private TipoFoto tipoFoto;

    public enum TipoFoto {
        FRONTAL, CREDENCIAL
    }
}