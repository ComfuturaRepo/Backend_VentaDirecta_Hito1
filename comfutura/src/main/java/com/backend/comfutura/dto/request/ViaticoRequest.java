package com.backend.comfutura.dto.request;

import com.backend.comfutura.model.emuns.TipoViatico;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViaticoRequest {

    private Integer idOts;
    private TipoViatico tipo;
    private String concepto;

    private Integer idUnidadMedida;
    private BigDecimal cantidad;
    private BigDecimal precio;

    private BigDecimal costoDia;
    private BigDecimal cantDias;

    private Integer idTrabajador;
    private Integer idProveedor;

    private LocalDate fecha;

    private String tipoDoc;
    private String rucDni;

    private Integer idBanco;
    private String moneda;
    private String cuenta;
    private String cci;
}
