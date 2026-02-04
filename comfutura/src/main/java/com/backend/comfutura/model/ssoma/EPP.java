package com.backend.comfutura.model.ssoma;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "epp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EPP {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_epp")
    private Integer idEPP;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @OneToMany(mappedBy = "epp")
    private List<ATSEPP> atsEpps;

    @OneToMany(mappedBy = "epp")
    private List<InspeccionEPPDetalle> inspecciones;
}