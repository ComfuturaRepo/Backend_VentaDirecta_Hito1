package com.backend.comfutura.model;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "proveedor")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proveedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer id;

    @Column(length = 11, columnDefinition = "CHAR(11)", unique = true)
    private String ruc;

    private String razonSocial;
    private String direccion;
    private String distrito;
    private String provincia;
    private String departamento;

    private String contacto;
    private String telefono;
    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_banco")
    private Banco banco;

    private String numeroCuenta;
    private String moneda;

    private Boolean activo = true;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;
}

