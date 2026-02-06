package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.permisos.*;

import java.util.List;

public interface PermisoService {

    PermisoResponseDTO crearPermiso(PermisoDTO permisoDTO);
    PermisoResponseDTO actualizarPermiso(Integer id, PermisoDTO permisoDTO);
    void eliminarPermiso(Integer id);
    PermisoResponseDTO obtenerPermisoPorId(Integer id);
    PermisoResponseDTO obtenerPermisoPorCodigo(String codigo);
    List<PermisoResponseDTO> listarTodosPermisos();

    PageResponseDTO<PermisoTablaDTO> listarTodosPermisosPaginados(
            int page, int size, String sortBy, String sortDirection);

    boolean verificarPermisoUsuario(VerificarPermisoDTO verificarPermisoDTO);

    List<PermisoResponseDTO> obtenerPermisosPorNivel(Integer idNivel);
    List<PermisoResponseDTO> obtenerPermisosPorArea(Integer idArea);
    List<PermisoResponseDTO> obtenerPermisosPorCargo(Integer idCargo);
    List<PermisoResponseDTO> obtenerPermisosPorTrabajador(Integer idTrabajador); // NUEVO método

    List<String> obtenerPermisosUsuario(Integer idUsuario);
}