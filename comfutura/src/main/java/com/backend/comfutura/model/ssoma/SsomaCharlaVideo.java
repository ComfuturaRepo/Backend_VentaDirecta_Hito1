package com.backend.comfutura.model.ssoma;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ssoma_charla_video")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaCharlaVideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_video")
    private Integer idVideo;

    @OneToOne
    @JoinColumn(name = "id_charla", nullable = false)
    private SsomaCharla charla;

    @Column(name = "video_url", columnDefinition = "TEXT")
    private String videoUrl;

    @Column(name = "duracion_segundos")
    private Integer duracionSegundos;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;
}