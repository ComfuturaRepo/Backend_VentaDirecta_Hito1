package com.backend.comfutura.model.ssoma;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_epp_fotos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaEppFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer idFoto;

    @ManyToOne
    @JoinColumn(name = "id_epp", nullable = false)
    private SsomaEppCheck eppCheck;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;
}
