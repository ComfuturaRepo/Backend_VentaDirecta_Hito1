package com.backend.comfutura.model.ssoma;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "tipo_riesgo_trabajo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoRiesgoTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "tipoRiesgo")
    private List<ATSTipoRiesgo> atsTiposRiesgo;
}