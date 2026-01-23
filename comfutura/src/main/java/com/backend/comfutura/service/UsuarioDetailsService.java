package com.backend.comfutura.service;

import com.backend.comfutura.config.security.CustomUserDetails;  // ← importa tu nueva clase
import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (Boolean.FALSE.equals(usuario.isActivo())) {
            throw new UsernameNotFoundException("Usuario inactivo");
        }

        // ← Cambio clave: retorna tu CustomUserDetails
        return new CustomUserDetails(usuario);
    }

    // Método extra (queda igual)
    public Optional<Usuario> findUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}