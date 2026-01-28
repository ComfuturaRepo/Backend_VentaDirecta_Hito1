package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.ChangePasswordDTO;
import com.backend.comfutura.dto.request.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.UsuarioSimpleDTO;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    PageResponseDTO<UsuarioSimpleDTO> findAllUsuarios(Pageable pageable);

    PageResponseDTO<UsuarioSimpleDTO> findUsuariosActivos(Pageable pageable);

    UsuarioDetailDTO findUsuarioById(Integer id);

    UsuarioDetailDTO findUsuarioByUsername(String username);

    PageResponseDTO<UsuarioSimpleDTO> searchUsuarios(String search, Pageable pageable);

    UsuarioDetailDTO createUsuario(UsuarioRequestDTO usuarioDTO);

    UsuarioDetailDTO updateUsuario(Integer id, UsuarioUpdateDTO usuarioDTO);

    // NUEVO: Método para actualizar todos los datos del usuario
    UsuarioDetailDTO updateUsuarioCompleto(Integer id, UsuarioRequestDTO usuarioDTO);

    MessageResponseDTO changePassword(Integer id, ChangePasswordDTO passwordDTO);

    UsuarioDetailDTO toggleUsuarioActivo(Integer id);

    MessageResponseDTO deleteUsuario(Integer id);

    PageResponseDTO<UsuarioSimpleDTO> findActivos(Pageable pageable);

    // Método para búsqueda con filtros
    PageResponseDTO<UsuarioSimpleDTO> searchUsuariosWithFilters(
            String search,
            Boolean activo,
            Integer nivelId,
            Pageable pageable
    );
}