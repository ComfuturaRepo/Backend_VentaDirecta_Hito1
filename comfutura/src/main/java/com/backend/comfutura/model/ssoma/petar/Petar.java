package com.backend.comfutura.model.ssoma.petar;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "petar")
public class Petar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero_registro", length = 50)
    private String numeroRegistro;

    @Column(name = "fecha")
    private LocalDate fecha;
}