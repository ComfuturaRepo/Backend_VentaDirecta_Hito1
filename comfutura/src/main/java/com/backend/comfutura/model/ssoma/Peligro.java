package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "peligro")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Peligro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_peligro")
    private Integer idPeligro;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "peligro")
    private List<Riesgo> riesgos;
}