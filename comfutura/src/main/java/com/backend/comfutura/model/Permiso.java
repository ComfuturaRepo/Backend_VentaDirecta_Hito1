package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permiso")
@Getter
@Setter
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Column(name = "codigo", unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(name = "nombre", length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 200)
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;

    @ManyToMany
    @JoinTable(
            name = "permiso_nivel",
            joinColumns = @JoinColumn(name = "id_permiso"),
            inverseJoinColumns = @JoinColumn(name = "id_nivel")
    )
    private Set<Nivel> niveles = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "permiso_area",
            joinColumns = @JoinColumn(name = "id_permiso"),
            inverseJoinColumns = @JoinColumn(name = "id_area")
    )
    private Set<Area> areas = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "permiso_cargo",
            joinColumns = @JoinColumn(name = "id_permiso"),
            inverseJoinColumns = @JoinColumn(name = "id_cargo")
    )
    private Set<Cargo> cargos = new HashSet<>();

    // NUEVO: Relación con trabajadores
    @ManyToMany
    @JoinTable(
            name = "permiso_trabajador",
            joinColumns = @JoinColumn(name = "id_permiso"),
            inverseJoinColumns = @JoinColumn(name = "id_trabajador")
    )
    private Set<Trabajador> trabajadores = new HashSet<>();

    // Constructores
    public Permiso() {}

    public Permiso(String codigo, String nombre, String descripcion) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = true;
    }
}