package com.backend.comfutura.model.ssoma.petar;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "petar_autorizado")
public class PetarAutorizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar")
    private Petar petar;

    @ManyToOne
    @JoinColumn(name = "id_trabajador")
    private Trabajador trabajador;
}