// com.backend.comfutura.controller/PerfilController.java
package com.backend.comfutura.controller;

import com.backend.comfutura.config.security.CustomUserDetails;
import com.backend.comfutura.dto.Page.MessageResponseDTO;

import com.backend.comfutura.dto.request.usuarioDTO.ChangePasswordRequestDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UpdatePerfilRequestDTO;
import com.backend.comfutura.dto.response.usuarioDTO.PerfilResponseDTO;
import com.backend.comfutura.service.PerfilService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/perfil")
@RequiredArgsConstructor
public class PerfilController {

    private final PerfilService perfilService;

    // Obtener usuario autenticado
    private CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof CustomUserDetails) {
                return (CustomUserDetails) principal;
            }
        }

        throw new RuntimeException("Usuario no autenticado");
    }

    // Obtener ID del usuario autenticado
    private Integer getCurrentUserId() {
        return getCurrentUser().getIdUsuario();
    }

    // 1. Obtener perfil del usuario autenticado
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PerfilResponseDTO> getMiPerfil() {
        try {
            Integer usuarioId = getCurrentUserId();
            PerfilResponseDTO perfil = perfilService.getPerfil(usuarioId);
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 2. Actualizar datos personales
    @PutMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateMiPerfil(@Valid @RequestBody UpdatePerfilRequestDTO requestDTO) {
        try {
            Integer usuarioId = getCurrentUserId();
            PerfilResponseDTO perfilActualizado = perfilService.updatePerfil(usuarioId, requestDTO);
            return ResponseEntity.ok(perfilActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // 3. Cambiar contraseña
    @PatchMapping("/me/cambiar-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> cambiarMiPassword(@Valid @RequestBody ChangePasswordRequestDTO requestDTO) {
        try {
            Integer usuarioId = getCurrentUserId();
            MessageResponseDTO response = perfilService.changePassword(usuarioId, requestDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // 4. Actualizar última conexión (llamar cuando el usuario inicie sesión)
    @PostMapping("/me/actualizar-conexion")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PerfilResponseDTO> updateUltimaConexion() {
        try {
            Integer usuarioId = getCurrentUserId();
            PerfilResponseDTO perfil = perfilService.updateUltimaConexion(usuarioId);
            return ResponseEntity.ok(perfil);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 5. Obtener información básica del usuario autenticado
    @GetMapping("/me/info")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getInfoUsuario() {
        try {
            CustomUserDetails user = getCurrentUser();

            // Puedes crear un DTO más simple
            var userInfo = new UserInfoResponse(
                    user.getIdUsuario(),
                    user.getUsername(),
                    user.getNivelCodigo(),
                    user.getNivelNombre(),
                    user.getIdTrabajador()
            );

            return ResponseEntity.ok(userInfo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // Clase interna para respuesta simple
    @lombok.Data
    @lombok.AllArgsConstructor
    private static class UserInfoResponse {
        private Integer idUsuario;
        private String username;
        private String nivelCodigo;
        private String nivelNombre;
        private Integer idTrabajador;
    }
}