// com.backend.comfutura.service.serviceImpl/PerfilServiceImpl.java
package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.request.usuarioDTO.ChangePasswordRequestDTO;
import com.backend.comfutura.dto.request.usuarioDTO.UpdatePerfilRequestDTO;
import com.backend.comfutura.dto.response.usuarioDTO.PerfilResponseDTO;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.repository.TrabajadorRepository;
import com.backend.comfutura.repository.UsuarioRepository;
import com.backend.comfutura.service.PerfilService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PerfilServiceImpl implements PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public PerfilResponseDTO getPerfil(Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        return buildPerfilResponse(usuario);
    }

    @Override
    @Transactional
    public PerfilResponseDTO updatePerfil(Integer usuarioId, UpdatePerfilRequestDTO requestDTO) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        Trabajador trabajador = usuario.getTrabajador();

        if (trabajador == null) {
            throw new RuntimeException("El usuario no tiene un trabajador asociado");
        }

        // Actualizar datos del trabajador
        trabajador.setNombres(requestDTO.getNombres());
        trabajador.setApellidos(requestDTO.getApellidos());
        trabajador.setCorreoCorporativo(requestDTO.getCorreoCorporativo());
        trabajador.setCelular(requestDTO.getCelular());

        trabajadorRepository.save(trabajador);

        return buildPerfilResponse(usuario);
    }

    @Override
    @Transactional
    public MessageResponseDTO changePassword(Integer usuarioId, ChangePasswordRequestDTO requestDTO) {
        // Validar que las contraseñas coincidan
        if (!requestDTO.getNewPassword().equals(requestDTO.getConfirmPassword())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        // Verificar contraseña actual
        if (!passwordEncoder.matches(requestDTO.getCurrentPassword(), usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // Validar que la nueva contraseña sea diferente a la actual
        if (passwordEncoder.matches(requestDTO.getNewPassword(), usuario.getPassword())) {
            throw new RuntimeException("La nueva contraseña debe ser diferente a la actual");
        }

        // Validar fortaleza de contraseña (opcional)
        validatePasswordStrength(requestDTO.getNewPassword());

        // Encriptar y guardar nueva contraseña
        usuario.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        usuarioRepository.save(usuario);

        return new MessageResponseDTO("Contraseña actualizada exitosamente", null);
    }

    @Override
    @Transactional
    public PerfilResponseDTO updateUltimaConexion(Integer usuarioId) {
        // Si tienes un campo ultimaConexion en Usuario, actualízalo aquí
        // Por ahora solo devolvemos el perfil
        return getPerfil(usuarioId);
    }

    private PerfilResponseDTO buildPerfilResponse(Usuario usuario) {
        PerfilResponseDTO response = new PerfilResponseDTO();

        // Datos del usuario
        response.setIdUsuario(usuario.getIdUsuario());
        response.setUsername(usuario.getUsername());
        response.setActivo(usuario.isActivo());
        response.setFechaCreacion(usuario.getFechaCreacion());

        // Datos del trabajador (si existe)
        if (usuario.getTrabajador() != null) {
            Trabajador trabajador = usuario.getTrabajador();
            response.setIdTrabajador(trabajador.getIdTrabajador());
            response.setNombres(trabajador.getNombres());
            response.setApellidos(trabajador.getApellidos());
            response.setDni(trabajador.getDni());
            response.setCorreoCorporativo(trabajador.getCorreoCorporativo());
            response.setCelular(trabajador.getCelular());

            // Información de la empresa, área y cargo
            if (trabajador.getEmpresa() != null) {
                response.setEmpresaNombre(trabajador.getEmpresa().getNombre());
            }

            if (trabajador.getArea() != null) {
                response.setAreaNombre(trabajador.getArea().getNombre());
            }

            if (trabajador.getCargo() != null) {
                response.setCargoNombre(trabajador.getCargo().getNombre());
            }
        }

        // Datos del nivel/rol
        if (usuario.getNivel() != null) {
            response.setIdNivel(usuario.getNivel().getIdNivel());
            response.setNivelNombre(usuario.getNivel().getNombre());
            response.setNivelCodigo(usuario.getNivel().getCodigo());
        }

        // Estadísticas (puedes implementar esto después)
        response.setTotalProyectos(0L);
        response.setTareasPendientes(0L);
        response.setTareasCompletadas(0L);

        return response;
    }

    private void validatePasswordStrength(String password) {
        if (password.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }

        // Puedes agregar más validaciones:
        // - Al menos una mayúscula
        // - Al menos un número
        // - Al menos un carácter especial
    }
}