package com.backend.comfutura.service;


import com.backend.comfutura.dto.response.DashboardDTO.DashboardOTDTO;
import com.backend.comfutura.dto.response.DashboardDTO.FiltroDashboardDTO;

public interface DashboardOTService {

    DashboardOTDTO getDashboardData(FiltroDashboardDTO filtro);

    DashboardOTDTO getDashboardDataByCliente(Integer clienteId);

    DashboardOTDTO getDashboardDataByArea(Integer areaId);

    DashboardOTDTO getDashboardDataByProyecto(Integer proyectoId);

    DashboardOTDTO getDashboardDataByEstado(Integer estadoId);

    DashboardOTDTO getDashboardDataByFecha(String rango);
}