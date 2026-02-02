package com.backend.comfutura.dto.request.otDTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class OtCreateRequest {

    // Campos básicos
    private Integer idOtsAnterior;

    @Min(value = 1, message = "OT anterior debe ser mayor o igual a 1")
    @Max(value = 2147483647, message = "OT anterior excede el límite máximo")
    public Integer getIdOtsAnterior() {
        return idOtsAnterior;
    }

    private Integer idOts;

    // Campos obligatorios
    @NotNull(message = "Cliente es requerido")
    @Min(value = 1, message = "ID Cliente inválido")
    private Integer idCliente;

    @NotNull(message = "Área es requerida")
    @Min(value = 1, message = "ID Área inválido")
    private Integer idArea;

    @NotNull(message = "Proyecto es requerido")
    @Min(value = 1, message = "ID Proyecto inválido")
    private Integer idProyecto;

    @NotNull(message = "Fase es requerida")
    @Min(value = 1, message = "ID Fase inválido")
    private Integer idFase;

    @NotNull(message = "Site es requerido")
    @Min(value = 1, message = "ID Site inválido")
    private Integer idSite;

    @NotNull(message = "Región es requerida")
    @Min(value = 1, message = "ID Región inválido")
    private Integer idRegion;

    @NotNull(message = "Tipo OT es requerido")
    @Min(value = 1, message = "ID Tipo OT inválido")
    private Integer idTipoOt;

    @NotNull(message = "Estado OT es requerido")
    @Min(value = 1, message = "ID Estado OT inválido")
    private Integer idEstadoOt;

    @NotNull(message = "Fecha de apertura es requerida")
    @PastOrPresent(message = "Fecha de apertura no puede ser futura")
    private LocalDate fechaApertura;

    private Integer diasAsignados;

    @NotBlank(message = "Descripción es requerida")
    @Size(min = 10, max = 500, message = "Descripción debe tener entre 10 y 500 caracteres")
    private String descripcion;

    // Campos opcionales
    private Integer idJefaturaClienteSolicitante;
    private Integer idAnalistaClienteSolicitante;

    @NotNull(message = "Coordinador Ti CW es requerido")
    @Min(value = 1, message = "ID Coordinador inválido")
    private Integer idCoordinadorTiCw;

    @NotNull(message = "Jefatura Responsable es requerida")
    @Min(value = 1, message = "ID Jefatura Responsable inválido")
    private Integer idJefaturaResponsable;

    @NotNull(message = "Liquidador es requerido")
    @Min(value = 1, message = "ID Liquidador inválido")
    private Integer idLiquidador;

    @NotNull(message = "Ejecutante es requerido")
    @Min(value = 1, message = "ID Ejecutante inválido")
    private Integer idEjecutante;

    @NotNull(message = "Analista Contable es requerido")
    @Min(value = 1, message = "ID Analista Contable inválido")
    private Integer idAnalistaContable;

    private boolean activo = true;

    // Validación personalizada para OT anterior cuando fecha es del año pasado
    @JsonIgnore
    public boolean esOtAnteriorRequerida() {
        if (fechaApertura == null) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        return fechaApertura.getYear() < hoy.getYear();
    }

    @JsonIgnore
    public String getValidacionOtAnterior() {
        if (esOtAnteriorRequerida() && (idOtsAnterior == null || idOtsAnterior < 1)) {
            return "OT anterior es obligatoria para fechas del año anterior";
        }
        if (idOtsAnterior != null && idOtsAnterior > 2147483647) {
            return "OT anterior excede el límite máximo (2,147,483,647)";
        }
        return null;
    }
}