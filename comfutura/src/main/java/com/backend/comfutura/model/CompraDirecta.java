package com.backend.comfutura.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "compra_directa")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDirecta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra_directa")
    private Integer idCompraDirecta;

    @Column(name = "nro_requerimiento", nullable = false, unique = true, length = 50)
    private String nroRequerimiento;

    @Column(name = "fecha_costo", nullable = false)
    private LocalDate fechaCosto;

    // 🔗 Relación con OT
    @ManyToOne
    @JoinColumn(name = "id_ots", nullable = false)
    private Ots ots;

    // 🔗 Estado de compra directa (NO estado_ot)
    @ManyToOne
    @JoinColumn(name = "id_estado_cd", nullable = false)
    private EstadoCompraDirecta estado;

    @Column(name = "tiempo_ejecucion")
    private Integer tiempoEjecucion;

    @Column(name = "observacion")
    private String observacion;

    // 🔗 Usuario que creó la compra directa
    @ManyToOne
    @JoinColumn(name = "id_usuario_creacion", nullable = false)
    private Usuario usuarioCreacion;

    @Column(nullable = false)
    private Boolean activo = true;
}

