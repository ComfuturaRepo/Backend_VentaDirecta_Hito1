package com.backend.comfutura.config;

import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.record.UserJwtDto;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-minutes:60}")
    private long expirationMinutes;

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(Usuario usuario) {
        // Ahora nivel es un objeto único, no una lista
        String nivelCodigo = usuario.getNivel() != null ? usuario.getNivel().getCodigo() : "UNKNOWN";
        String nivelNombre = usuario.getNivel() != null ? usuario.getNivel().getNombre() : "Sin nivel";

        // Puedes enviar solo el código (L1, L2, etc.) o el nombre, o ambos
        List<String> roles = List.of(nivelCodigo);  // o List.of(nivelNombre) o List.of(nivelCodigo + " - " + nivelNombre)

        UserJwtDto userData = new UserJwtDto(
                usuario.getIdUsuario(),
                usuario.getTrabajador() != null ? usuario.getTrabajador().getIdTrabajador() : null,
                usuario.getUsername(),
                usuario.isActivo(),
                roles
        );

        Map<String, Object> claims = new HashMap<>();
        claims.put("data", userData);
        claims.put("roles", roles);           // Lista con el código o nombre del nivel
        claims.put("nivelCodigo", nivelCodigo);  // Opcional: para validaciones rápidas
        claims.put("nivelNombre", nivelNombre);

        return Jwts.builder()
                .claims(claims)
                .subject(usuario.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationMinutes * 60 * 1000))
                .signWith(getSignInKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}