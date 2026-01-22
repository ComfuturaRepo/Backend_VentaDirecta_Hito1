package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "area")
@Data
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    private Integer idArea;
    @ManyToMany
    @JoinTable(
            name = "cliente_area",  // nombre exacto de tu tabla intermedia
            joinColumns = @JoinColumn(name = "id_area"),
            inverseJoinColumns = @JoinColumn(name = "id_cliente")
    )
    private List<Cliente> clientes = new ArrayList<>();
    private String nombre;
    private Boolean activo = true;
}
