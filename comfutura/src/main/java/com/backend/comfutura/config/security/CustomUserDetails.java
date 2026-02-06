package com.backend.comfutura.config.security;

import com.backend.comfutura.model.Usuario;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final Integer idUsuario;
    private final String username;
    private final String password;
    private final boolean activo;
    private final Integer idTrabajador;
    private final String nivelCodigo;
    private final String nivelNombre;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(Usuario usuario) {
        this.idUsuario = usuario.getIdUsuario();
        this.username = usuario.getUsername();
        this.password = usuario.getPassword();
        this.activo = usuario.isActivo();

        this.idTrabajador = usuario.getTrabajador() != null
                ? usuario.getTrabajador().getIdTrabajador()
                : null;

        this.nivelCodigo = usuario.getNivel() != null ? usuario.getNivel().getCodigo() : "UNKNOWN";
        this.nivelNombre = usuario.getNivel() != null ? usuario.getNivel().getNombre() : "Sin nivel";

        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_" + nivelCodigo));
    }

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
        return true;
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
}