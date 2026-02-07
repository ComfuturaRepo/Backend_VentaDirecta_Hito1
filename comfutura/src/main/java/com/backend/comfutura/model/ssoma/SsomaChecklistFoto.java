package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "ssoma_checklist_fotos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaChecklistFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_foto")
    private Integer idFoto;

    @ManyToOne
    @JoinColumn(name = "id_checklist", nullable = false)
    private SsomaChecklistSeguridad checklistSeguridad;

    @Column(name = "foto_url", columnDefinition = "TEXT")
    private String fotoUrl;
}