package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Mapper.UsuarioMapper;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.usuarioDTO.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.usuarioDTO.UsuarioSimpleDTO;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.Nivel;
import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.repository.TrabajadorRepository;
import com.backend.comfutura.repository.NivelRepository;
import com.backend.comfutura.repository.UsuarioRepository;
import com.backend.comfutura.service.UsuarioService;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final NivelRepository nivelRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UsuarioSimpleDTO> findAllUsuarios(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        return toPageResponseDTO(page.map(usuarioMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UsuarioSimpleDTO> searchUsuariosWithFilters(
            String search,
            Boolean activo,
            Integer nivelId,
            Pageable pageable) {

        Specification<Usuario> spec = buildSpecification(search, activo, nivelId);
        Page<Usuario> page = usuarioRepository.findAll(spec, pageable);

        return toPageResponseDTO(page.map(usuarioMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDetailDTO findUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDetailDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional
    public UsuarioDetailDTO createUsuario(UsuarioRequestDTO usuarioDTO) {
        // Validar username único
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        // Obtener trabajador
        Trabajador trabajador = trabajadorRepository.findById(usuarioDTO.getTrabajadorId())
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        // Obtener nivel
        Nivel nivel = nivelRepository.findById(usuarioDTO.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        // Crear usuario
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario.setTrabajador(trabajador);
        usuario.setNivel(nivel);
        usuario.setActivo(true);
        usuario.setFechaCreacion(LocalDateTime.now());

        Usuario savedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDetailDTO(savedUsuario);
    }

    @Override
    @Transactional
    public UsuarioDetailDTO updateUsuario(Integer id, UsuarioUpdateDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar username único (si cambia)
        if (usuarioDTO.getUsername() != null && !usuario.getUsername().equals(usuarioDTO.getUsername())) {
            if (usuarioRepository.existsByUsernameAndIdUsuarioNot(usuarioDTO.getUsername(), id)) {
                throw new RuntimeException("El nombre de usuario ya está en uso por otro usuario");
            }
            usuario.setUsername(usuarioDTO.getUsername());
        }

        // Actualizar otros campos
        if (usuarioDTO.getNivelId() != null) {
            Nivel nivel = nivelRepository.findById(usuarioDTO.getNivelId())
                    .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));
            usuario.setNivel(nivel);
        }

        if (usuarioDTO.getTrabajadorId() != null) {
            Trabajador trabajador = trabajadorRepository.findById(usuarioDTO.getTrabajadorId())
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
            usuario.setTrabajador(trabajador);
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDetailDTO(updatedUsuario);
    }

    @Override
    @Transactional
    public UsuarioDetailDTO updateUsuarioCompleto(Integer id, UsuarioRequestDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Validar username único (si cambia)
        if (!usuario.getUsername().equals(usuarioDTO.getUsername())) {
            if (usuarioRepository.existsByUsernameAndIdUsuarioNot(usuarioDTO.getUsername(), id)) {
                throw new RuntimeException("El nombre de usuario ya está en uso por otro usuario");
            }
            usuario.setUsername(usuarioDTO.getUsername());
        }

        // Obtener trabajador
        Trabajador trabajador = trabajadorRepository.findById(usuarioDTO.getTrabajadorId())
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        // Obtener nivel
        Nivel nivel = nivelRepository.findById(usuarioDTO.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        // Actualizar todos los campos
        usuario.setTrabajador(trabajador);
        usuario.setNivel(nivel);
        usuario.setActivo(usuarioDTO.getActivo());

        // Actualizar contraseña solo si se proporciona una nueva
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().trim().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDetailDTO(updatedUsuario);
    }

    @Override
    @Transactional
    public UsuarioDetailDTO toggleUsuarioActivo(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuario.setActivo(!usuario.isActivo());
        Usuario updatedUsuario = usuarioRepository.save(usuario);

        return usuarioMapper.toDetailDTO(updatedUsuario);
    }

    private Specification<Usuario> buildSpecification(String search, Boolean activo, Integer nivelId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por búsqueda
            if (search != null && !search.trim().isEmpty()) {
                String pattern = "%" + search.toLowerCase().trim() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("username")), pattern),
                        cb.like(cb.lower(root.join("trabajador").get("nombres")), pattern),
                        cb.like(cb.lower(root.join("trabajador").get("apellidos")), pattern)
                ));
            }

            // Filtro por estado activo
            if (activo != null) {
                predicates.add(cb.equal(root.get("activo"), activo));
            }

            // Filtro por nivel
            if (nivelId != null) {
                predicates.add(cb.equal(root.get("nivel").get("idNivel"), nivelId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    // Helper para convertir Page a PageResponseDTO
    private <T> PageResponseDTO<T> toPageResponseDTO(Page<T> page) {
        PageResponseDTO<T> response = new PageResponseDTO<>();
        response.setContent(page.getContent());
        response.setCurrentPage(page.getNumber());
        response.setTotalItems(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setFirst(page.isFirst());
        response.setLast(page.isLast());
        response.setPageSize(page.getSize());

        return response;
    }
}