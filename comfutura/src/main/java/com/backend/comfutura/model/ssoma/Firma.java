package com.backend.comfutura.model.ssoma;

import com.backend.comfutura.model.Trabajador;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "firma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Firma {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_firma")
    private Integer idFirma;

    @Column(name = "url", nullable = false)
    private String url;

    @OneToMany(mappedBy = "firma")
    private List<Trabajador> trabajadores;
}