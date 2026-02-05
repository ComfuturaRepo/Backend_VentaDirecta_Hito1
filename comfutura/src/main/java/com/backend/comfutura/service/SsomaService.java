package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ssomaDTO.SsomaRequest;
import com.backend.comfutura.dto.response.ssomaDTO.SsomaResponse;

import java.util.List;

public interface SsomaService {

    // Método para crear todas las 5 hojas en una transacción
    SsomaResponse crearCompletoSsoma(SsomaRequest request);

    // Métodos para listar sin paginación
    List<Object> listarTodosAts();
    List<Object> listarTodasCapacitaciones();
    List<Object> listarTodasInspeccionesEpp();
    List<Object> listarTodasInspeccionesHerramientas();
    List<Object> listarTodosPetar();

    // Método para obtener todo el SSOMA por fecha
    List<Object> obtenerSsomaPorFecha(String fecha);
}