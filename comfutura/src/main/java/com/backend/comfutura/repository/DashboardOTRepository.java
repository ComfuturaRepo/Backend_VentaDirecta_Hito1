package com.backend.comfutura.repository;

import com.backend.comfutura.dto.response.DashboardDTO.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface DashboardOTRepository {
    Long countTotalOTs();
    Long countOTsByEstado(Integer estadoId);
    Long countOTsEsteAnio();
    Long countOTsEsteMes();
    List<OTPorEstadoDTO> findOTsPorEstado();
    List<OTPorClienteDTO> findOTsPorCliente();
    List<OTPorAreaDTO> findOTsPorArea();
    List<OTPorProyectoDTO> findOTsPorProyecto();
    List<OTPorRegionDTO> findOTsPorRegion();
    List<Object[]> findEvolucionMensual(LocalDate fechaInicio);
    BigDecimal getCostoTotalMateriales();
    BigDecimal getCostoTotalContratistas();
    BigDecimal getCostoTotalGastosLogisticos();
    BigDecimal getCostoTotalViaticos();
    BigDecimal getCostoTotalPlanillas();
    List<OTPendienteDTO> findOTsPendientes();
    List<Object[]> findCostoPorOT();
}