package com.backend.comfutura.model.ssoma;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_herramienta_fotos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaHerramientaFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer idFoto;

    @ManyToOne
    @JoinColumn(name = "id_herramienta", nullable = false)
    private SsomaHerramientaInspeccion herramientaInspeccion;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;
}