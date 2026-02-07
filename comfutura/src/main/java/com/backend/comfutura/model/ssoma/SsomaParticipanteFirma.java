package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ssoma_participante_firma")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaParticipanteFirma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_firma")
    private Integer idFirma;

    @OneToOne
    @JoinColumn(name = "id_participante", nullable = false)
    private SsomaParticipante participante;

    @Column(name = "firma_url", columnDefinition = "TEXT")
    private String firmaUrl;

    @Column(name = "fecha_firma")
    private LocalDateTime fechaFirma;
}