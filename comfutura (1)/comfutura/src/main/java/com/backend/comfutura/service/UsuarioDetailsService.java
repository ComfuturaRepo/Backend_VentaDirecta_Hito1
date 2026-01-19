package com.backend.comfutura.service;

import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
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

        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                getAuthorities(usuario)
        );
    }

    private List<GrantedAuthority> getAuthorities(Usuario usuario) {
        // Aquí puedes mejorar según tus necesidades
        // Por ahora devolvemos un rol básico
        // Ejemplo real: podrías tener relación con roles o usar el cargo
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        // return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTrabajador().getCargo().getNombre().toUpperCase()));
    }


    // Método extra que usas en el controlador (muy útil)
    public Optional<Usuario> findUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }
}