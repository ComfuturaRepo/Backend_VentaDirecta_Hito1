package com.backend.comfutura.model.ssoma;


import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ssoma_checklist_seguridad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SsomaChecklistSeguridad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_checklist")
    private Integer idChecklist;

    @ManyToOne
    @JoinColumn(name = "id_ssoma", nullable = false)
    private SsomaFormulario ssomaFormulario;

    @Column(name = "item_nombre", length = 100)
    private String itemNombre;

    @Column(name = "usado")
    private Boolean usado;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "checklistSeguridad", cascade = CascadeType.ALL)
    private List<SsomaChecklistFoto> fotos;
}