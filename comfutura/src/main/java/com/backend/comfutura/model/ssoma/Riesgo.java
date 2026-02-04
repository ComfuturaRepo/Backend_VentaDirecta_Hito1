package com.backend.comfutura.model.ssoma;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "riesgo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Riesgo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_riesgo")
    private Integer idRiesgo;

    @ManyToOne
    @JoinColumn(name = "id_peligro")
    private Peligro peligro;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @OneToMany(mappedBy = "riesgo")
    private List<MedidaControl> medidasControl;
}