package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "site_descripcion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteDescripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSiteDescripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_site", nullable = false)
    private Site site;

    @Column(nullable = false, length = 255)
    private String descripcion;

    @Builder.Default
    private Boolean activo = true;

    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();
}
