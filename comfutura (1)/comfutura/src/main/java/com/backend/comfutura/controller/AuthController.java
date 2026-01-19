package com.backend.comfutura.controller;

import com.backend.comfutura.config.JwtService;
import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.record.AuthResponse;
import com.backend.comfutura.record.LoginRequest;
import com.backend.comfutura.record.UserJwtDto;
import com.backend.comfutura.service.UsuarioDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            Usuario usuario = usuarioDetailsService.findUsuarioByUsername(request.username())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String jwt = jwtService.generateToken(usuario);

            return ResponseEntity.ok(new AuthResponse(jwt));

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales inválidas"));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserJwtDto> getCurrentUser() {
        // La forma más limpia y recomendada en 2025
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = auth.getName();
        Usuario usuario = usuarioDetailsService.findUsuarioByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        UserJwtDto dto = new UserJwtDto(
                usuario.getId(),
                usuario.getUsername(),
                usuario.isActivo()
        );

        return ResponseEntity.ok(dto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Con JWT puro → el logout real lo hace el frontend eliminando el token
        SecurityContextHolder.clearContext();

        return ResponseEntity.ok(Map.of(
                "message", "Logout exitoso",
                "status", "success"
        ));
    }
}