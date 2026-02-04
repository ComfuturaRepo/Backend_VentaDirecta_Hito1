package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "petar_autorizado")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PETARAutorizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private PETAR petar;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;
}