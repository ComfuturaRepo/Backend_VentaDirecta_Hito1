package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.ChangePasswordDTO;
import com.backend.comfutura.dto.request.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.UsuarioSimpleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    PageResponseDTO<UsuarioSimpleDTO> findAllUsuarios(Pageable pageable);

    PageResponseDTO<UsuarioSimpleDTO> findUsuariosActivos(Pageable pageable);

    UsuarioDetailDTO findUsuarioById(Integer id);

    UsuarioDetailDTO findUsuarioByUsername(String username);

    UsuarioDetailDTO createUsuario(UsuarioRequestDTO usuarioDTO);

    UsuarioDetailDTO updateUsuario(Integer id, UsuarioUpdateDTO usuarioDTO);

    MessageResponseDTO changePassword(Integer id, ChangePasswordDTO passwordDTO);

    UsuarioDetailDTO toggleUsuarioActivo(Integer id);

    MessageResponseDTO deleteUsuario(Integer id);

    // Método para búsqueda con filtros
    PageResponseDTO<UsuarioSimpleDTO> searchUsuarios(
            String search,
            Boolean activo,
            Integer nivelId,
            Pageable pageable
    );
}