package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Mapper.UsuarioMapper;
import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.ChangePasswordDTO;
import com.backend.comfutura.dto.request.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.UsuarioSimpleDTO;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.Nivel;
import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.repository.TrabajadorRepository;
import com.backend.comfutura.repository.NivelRepository;
import com.backend.comfutura.repository.UsuarioRepository;
import com.backend.comfutura.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public PageResponseDTO<UsuarioSimpleDTO> findUsuariosActivos(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findByActivoTrue(pageable);

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
    @Transactional(readOnly = true)
    public UsuarioDetailDTO findUsuarioByUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .map(usuarioMapper::toDetailDTO)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
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
        if (!usuario.getUsername().equals(usuarioDTO.getUsername())) {
            if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
                throw new RuntimeException("El nombre de usuario ya está en uso por otro usuario");
            }
        }

        // Obtener trabajador (si cambia)
        Trabajador trabajador = trabajadorRepository.findById(usuarioDTO.getTrabajadorId())
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));

        // Obtener nivel (si cambia)
        Nivel nivel = nivelRepository.findById(usuarioDTO.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        // Actualizar
        usuarioMapper.updateEntity(usuarioDTO, usuario);
        usuario.setTrabajador(trabajador);
        usuario.setNivel(nivel);

        Usuario updatedUsuario = usuarioRepository.save(usuario);
        return usuarioMapper.toDetailDTO(updatedUsuario);
    }

    @Override
    @Transactional
    public MessageResponseDTO changePassword(Integer id, ChangePasswordDTO passwordDTO) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(passwordDTO.getCurrentPassword(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Encriptar y guardar nueva contraseña
        usuario.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
        usuarioRepository.save(usuario);

        return usuarioMapper.toMessageResponse("Contraseña actualizada exitosamente");
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

    @Override
    @Transactional
    public MessageResponseDTO deleteUsuario(Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        usuarioRepository.delete(usuario);
        return usuarioMapper.toMessageResponse("Usuario eliminado exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<UsuarioSimpleDTO> searchUsuarios(String search, Boolean activo, Integer nivelId, Pageable pageable) {
        Page<Usuario> page;

        if (search != null && !search.trim().isEmpty()) {
            page = usuarioRepository.searchUsuarios(search.trim(), pageable);
        } else if (activo != null && activo) {
            page = usuarioRepository.findByActivoTrue(pageable);
        } else {
            page = usuarioRepository.findAll(pageable);
        }

        // Filtrar por nivel si se especifica (CORRECCIÓN)
        if (nivelId != null) {
            List<Usuario> filteredList = page.getContent()
                    .stream()
                    .filter(u -> u.getNivel() != null && u.getNivel().getId().equals(nivelId))
                    .collect(Collectors.toList());

            // Crear nueva página con resultados filtrados
            page = new PageImpl<>(
                    filteredList,
                    pageable,
                    filteredList.size()
            );
        }

        return toPageResponseDTO(page.map(usuarioMapper::toSimpleDTO));
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