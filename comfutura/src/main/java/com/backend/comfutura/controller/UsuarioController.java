package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.ChangePasswordDTO;
import com.backend.comfutura.dto.request.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.UsuarioSimpleDTO;
import com.backend.comfutura.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET: Listar usuarios con paginación y filtros
    @GetMapping
    public ResponseEntity<PageResponseDTO<UsuarioSimpleDTO>> getAllUsuarios(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idUsuario") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos,
            @RequestParam(required = false) Integer nivelId,
            @RequestParam(required = false) String search) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        PageResponseDTO<UsuarioSimpleDTO> response;

        if (search != null || nivelId != null) {
            response = usuarioService.searchUsuarios(search, activos, nivelId, pageable);
        } else if (activos != null && activos) {
            response = usuarioService.findUsuariosActivos(pageable);
        } else {
            response = usuarioService.findAllUsuarios(pageable);
        }

        return ResponseEntity.ok(response);
    }

    // GET: Obtener usuario por ID
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDetailDTO> getUsuarioById(@PathVariable Integer id) {
        try {
            UsuarioDetailDTO usuario = usuarioService.findUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // POST: Crear nuevo usuario
    @PostMapping
    public ResponseEntity<?> createUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
        try {
            UsuarioDetailDTO nuevoUsuario = usuarioService.createUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // PUT: Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUsuario(
            @PathVariable Integer id,
            @Valid @RequestBody UsuarioUpdateDTO usuarioDTO) {
        try {
            UsuarioDetailDTO usuarioActualizado = usuarioService.updateUsuario(id, usuarioDTO);
            return ResponseEntity.ok(usuarioActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // PATCH: Cambiar contraseña
    @PatchMapping("/{id}/cambiar-password")
    public ResponseEntity<?> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody ChangePasswordDTO passwordDTO) {
        try {
            MessageResponseDTO response = usuarioService.changePassword(id, passwordDTO);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // PATCH: Activar/Desactivar usuario
    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<?> toggleUsuarioActivo(@PathVariable Integer id) {
        try {
            UsuarioDetailDTO usuario = usuarioService.toggleUsuarioActivo(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // DELETE: Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUsuario(@PathVariable Integer id) {
        try {
            MessageResponseDTO response = usuarioService.deleteUsuario(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(e.getMessage(), null));
        }
    }

    // GET: Buscar usuario por username
    @GetMapping("/buscar")
    public ResponseEntity<UsuarioDetailDTO> getUsuarioByUsername(@RequestParam String username) {
        try {
            UsuarioDetailDTO usuario = usuarioService.findUsuarioByUsername(username);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}