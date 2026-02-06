package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.usuarioDTO.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.usuarioDTO.UsuarioSimpleDTO;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    PageResponseDTO<UsuarioSimpleDTO> findAllUsuarios(Pageable pageable);

    PageResponseDTO<UsuarioSimpleDTO> searchUsuariosWithFilters(
            String search,
            Boolean activo,
            Integer nivelId,
            Pageable pageable);

    UsuarioDetailDTO findUsuarioById(Integer id);

    UsuarioDetailDTO createUsuario(UsuarioRequestDTO usuarioDTO);

    UsuarioDetailDTO updateUsuario(Integer id, UsuarioUpdateDTO usuarioDTO);

    UsuarioDetailDTO updateUsuarioCompleto(Integer id, UsuarioRequestDTO usuarioDTO);

    UsuarioDetailDTO toggleUsuarioActivo(Integer id);
}