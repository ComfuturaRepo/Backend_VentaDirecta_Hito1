package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "herramienta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Herramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 150)
    private String nombre;

    @OneToMany(mappedBy = "herramienta")
    private List<InspeccionHerramientaDetalle> inspecciones;
}