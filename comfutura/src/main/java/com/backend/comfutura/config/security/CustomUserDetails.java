package com.backend.comfutura.config.security;

import com.backend.comfutura.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Implementación personalizada de UserDetails que encapsula toda la información
 * relevante del usuario autenticado (incluyendo id_trabajador, nivel, etc.)
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer idUsuario;
    private final String username;
    private final String password;
    private final boolean activo;
    private final Integer idTrabajador;          // ← clave para asignar el creador de la OT
    private final String nivelCodigo;            // Ej: "L1", "L2", etc.
    private final String nivelNombre;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.username = usuario.getUsername();
        this.password = usuario.getPassword();
        this.activo = usuario.isActivo();

        // Trabajador (puede ser null si el usuario no está asociado a uno)
        this.idTrabajador = usuario.getTrabajador() != null
                ? usuario.getTrabajador().getIdTrabajador()
                : null;

        // Nivel (asumiendo que Nivel no es null, pero protegido)
        this.nivelCodigo = usuario.getNivel() != null ? usuario.getNivel().getCodigo() : "UNKNOWN";
        this.nivelNombre = usuario.getNivel() != null ? usuario.getNivel().getNombre() : "Sin nivel";

        // Autoridades: usamos el código del nivel como rol (ej: ROLE_L1, ROLE_L2)
        // Puedes cambiar a SimpleGrantedAuthority("ROLE_" + nivelCodigo)
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + nivelCodigo));
    }

    // Métodos requeridos por UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Puedes implementar lógica si necesitas
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return activo;
    }

    // Métodos extras útiles (los usaremos en OtServiceImpl)
    public Integer getIdTrabajador() {
        return idTrabajador;
    }

    public String getNivelCodigo() {
        return nivelCodigo;
    }

    public String getNivelNombre() {
        return nivelNombre;
    }
}