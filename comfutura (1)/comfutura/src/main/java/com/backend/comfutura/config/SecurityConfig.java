package com.backend.comfutura.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Permite @PreAuthorize, @Secured, etc. en los métodos
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitamos CSRF porque usamos JWT (stateless)
                .csrf(csrf -> csrf.disable())

                // Configuración de autorización de requests
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas (sin autenticación)
                        .requestMatchers(
                                "/api/auth/**",                     // login, register, refresh token, etc.
                                "/v3/api-docs/**",                  // Documentación OpenAPI (JSON/YAML)
                                "/v3/api-docs.yaml",                // En caso de que uses yaml
                                "/swagger-ui/**",                   // Recursos de la UI (js, css, etc.)
                                "/swagger-ui.html",                 // Ruta clásica/legacy
                                "/swagger-ui/index.html",           // Ruta más común en springdoc recientes
                                "/swagger-resources/**",            // Por si acaso (muy raro ya)
                                "/webjars/**"                       // Recursos webjars que usa swagger-ui
                        ).permitAll()

                        // Todo el resto requiere autenticación
                        .anyRequest().authenticated()
                )

                // Sesión STATELESS → no guarda nada en servidor
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Proveedor de autenticación (normalmente el que usa UserDetailsService + PasswordEncoder)
                .authenticationProvider(authenticationProvider)

                // Añadimos nuestro filtro JWT antes del filtro por defecto de username/password
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean necesario si quieres exponer AuthenticationManager (por ejemplo para usarlo en AuthService)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}