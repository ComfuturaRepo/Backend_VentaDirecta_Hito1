package com.backend.comfutura.repository;

import com.backend.comfutura.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Page<Usuario> findByActivoTrue(Pageable pageable);

    @Query("SELECT u FROM Usuario u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.trabajador.nombres) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.trabajador.apellidos) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Usuario> searchUsuarios(@Param("search") String search, Pageable pageable);

    Page<Usuario> findAll(Specification<Usuario> spec, Pageable pageable);

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    // Métodos con ID - usar idUsuario
    boolean existsByUsernameAndIdUsuarioNot(String username, Integer idUsuario);
}