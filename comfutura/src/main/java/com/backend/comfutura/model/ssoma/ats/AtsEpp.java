package com.backend.comfutura.model.ssoma.ats;


import com.backend.comfutura.model.ssoma.catalogo.Epp;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "ats_epp")
public class AtsEpp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_ats")
    private Ats ats;

    @ManyToOne
    @JoinColumn(name = "id_epp")
    private Epp epp;
}