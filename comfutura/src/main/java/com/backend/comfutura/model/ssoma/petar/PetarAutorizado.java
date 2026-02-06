package com.backend.comfutura.model.ssoma.petar;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;


@Entity
@Table(name = "petar_autorizado")
@Data
public class PetarAutorizado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_petar", nullable = false)
    private Petar petar;

    @ManyToOne
    @JoinColumn(name = "id_trabajador", nullable = false)
    private Trabajador trabajador;

    @Column(name = "conformidad_requerida")
    private Boolean conformidadRequerida = false;

    @Column(name = "fecha_autorizacion")
    private LocalDateTime fechaAutorizacion = LocalDateTime.now();
}