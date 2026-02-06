package com.backend.comfutura.model.ssoma.ats;


import com.backend.comfutura.model.ssoma.catalogo.Epp;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "ats_epp")
@Data
public class AtsEpp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats", nullable = false)
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_epp", nullable = false)
    private Epp epp;

    @Column(name = "cantidad")
    private Integer cantidad = 1;

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}