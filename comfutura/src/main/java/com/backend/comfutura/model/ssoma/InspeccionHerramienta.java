package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inspeccion_herramienta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InspeccionHerramienta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "numero_registro", length = 50)
    private String numeroRegistro;

    @Column(name = "fecha")
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_supervisor")
    private Trabajador supervisor;

    @OneToMany(mappedBy = "inspeccion", cascade = CascadeType.ALL)
    private List<InspeccionHerramientaDetalle> detalles = new ArrayList<>();
}