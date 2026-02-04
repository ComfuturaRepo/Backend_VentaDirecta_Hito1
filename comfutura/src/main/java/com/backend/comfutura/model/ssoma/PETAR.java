package com.backend.comfutura.model.ssoma;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "petar")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PETAR {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero_registro", length = 50)
    private String numeroRegistro;

    @Column(name = "fecha")
    private LocalDate fecha;

    @OneToMany(mappedBy = "petar", cascade = CascadeType.ALL)
    private List<PETARRespuesta> respuestas = new ArrayList<>();

    @OneToMany(mappedBy = "petar", cascade = CascadeType.ALL)
    private List<PETARAutorizado> autorizados = new ArrayList<>();
}