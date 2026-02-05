package com.backend.comfutura.model.ssoma.ats;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rol_trabajo")
@Data
public class RolTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre", length = 50)
    private String nombre;
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;
}