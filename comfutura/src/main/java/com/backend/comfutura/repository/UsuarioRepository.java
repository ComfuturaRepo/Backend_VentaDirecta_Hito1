package com.backend.comfutura.repository;

import com.backend.comfutura.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);

    Page<Usuario> findByActivoTrue(Pageable pageable);

    // Fixed version – assuming property is "nombres"
    @Query("SELECT u FROM Usuario u " +
            "WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) " +
            "   OR LOWER(u.trabajador.nombres) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Usuario> searchUsuarios(@Param("search") String search, Pageable pageable);

    boolean existsByUsername(String username);
}