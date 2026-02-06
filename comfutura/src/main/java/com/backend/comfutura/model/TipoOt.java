package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tipo_ot")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoOt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoOt;

    @Column(unique = true, length = 20)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String descripcion;

    @Builder.Default
    private Boolean activo = true;

    // Relación inversa
    @OneToMany(mappedBy = "tipoOt", fetch = FetchType.LAZY)
    private List<Ots> otsList;
}
