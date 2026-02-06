package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.request.usuarioDTO.ChangePasswordRequestDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UpdatePerfilRequestDTO;
import com.backend.comfutura.dto.response.usuarioDTO.PerfilResponseDTO;

public interface PerfilService {

    PerfilResponseDTO getPerfil(Integer usuarioId);

    PerfilResponseDTO updatePerfil(Integer usuarioId, UpdatePerfilRequestDTO requestDTO);

    MessageResponseDTO changePassword(Integer usuarioId, ChangePasswordRequestDTO requestDTO);

    PerfilResponseDTO updateUltimaConexion(Integer usuarioId);
}