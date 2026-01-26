package com.backend.comfutura.dto.Mapper;
import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.request.UsuarioRequestDTO;
import com.backend.comfutura.dto.request.UsuarioUpdateDTO;
import com.backend.comfutura.dto.response.UsuarioDetailDTO;
import com.backend.comfutura.dto.response.UsuarioSimpleDTO;
import com.backend.comfutura.model.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class UsuarioMapper {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Entity → SimpleDTO
    public UsuarioSimpleDTO toSimpleDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioSimpleDTO dto = new UsuarioSimpleDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());

        // Trabajador
        if (usuario.getTrabajador() != null) {
            dto.setNombreTrabajador(usuario.getTrabajador().getNombres() + " " +
                    usuario.getTrabajador().getApellidos());
        }

        // Nivel
        if (usuario.getNivel() != null) {
            dto.setNivelNombre(usuario.getNivel().getNombre());
        }

        dto.setActivo(usuario.isActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());

        return dto;
    }

    // Entity → DetailDTO
    public UsuarioDetailDTO toDetailDTO(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioDetailDTO dto = new UsuarioDetailDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setUsername(usuario.getUsername());

        // Trabajador
        if (usuario.getTrabajador() != null) {
            dto.setTrabajadorId(usuario.getTrabajador().getIdTrabajador());
            dto.setTrabajadorNombre(usuario.getTrabajador().getNombres());
            dto.setTrabajadorApellidos(usuario.getTrabajador().getApellidos());
            dto.setTrabajadorDNI(usuario.getTrabajador().getDni());
        }

        // Nivel
        if (usuario.getNivel() != null) {
            dto.setNivelId(usuario.getNivel().getId());
            dto.setNivelNombre(usuario.getNivel().getNombre());
            dto.setNivelCodigo(usuario.getNivel().getCodigo());
        }

        dto.setActivo(usuario.isActivo());
        dto.setFechaCreacion(usuario.getFechaCreacion());

        return dto;
    }

    // RequestDTO → Entity (para creación)
    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null;

        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword()); // Se encriptará en el servicio

        // NOTA: trabajador y nivel se setean por ID en el servicio

        return usuario;
    }

    // UpdateDTO → Entity (para actualización)
    public void updateEntity(UsuarioUpdateDTO dto, Usuario usuario) {
        if (dto == null || usuario == null) return;

        usuario.setUsername(dto.getUsername());
        // NOTA: trabajador y nivel se actualizan por ID en el servicio
    }

    // Helper para mensaje de respuesta
    public MessageResponseDTO toMessageResponse(String message) {
        return new MessageResponseDTO(
                message,
                LocalDateTime.now().format(formatter)
        );
    }
}