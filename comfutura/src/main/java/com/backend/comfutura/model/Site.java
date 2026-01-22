package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "site")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSite;

    @Column(nullable = false, length = 150, name = "codigo_sitio")
    private String codigoSitio;  // ← camelCase, sin guión bajo

    @Column(length = 255)
    private String descripcion;

    private Boolean activo = true;
}