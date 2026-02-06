package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaDTO;
import com.backend.comfutura.dto.response.permisos.*;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.PermisoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermisoServiceImpl implements PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Autowired
    private NivelRepository nivelRepository;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private TrabajadorRepository trabajadorRepository; // NUEVO: Necesitas este repositorio

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public PermisoResponseDTO crearPermiso(PermisoDTO permisoDTO) {
        // Validar que el código no exista
        if (permisoRepository.existsByCodigo(permisoDTO.getCodigo())) {
            throw new RuntimeException("Ya existe un permiso con el código: " + permisoDTO.getCodigo());
        }

        Permiso permiso = new Permiso();
        permiso.setCodigo(permisoDTO.getCodigo());
        permiso.setNombre(permisoDTO.getNombre());
        permiso.setDescripcion(permisoDTO.getDescripcion());

        // Asignar niveles
        if (permisoDTO.getNivelesIds() != null) {
            List<Nivel> niveles = nivelRepository.findAllById(permisoDTO.getNivelesIds());
            permiso.setNiveles(new HashSet<>(niveles));
        }

        // Asignar áreas
        if (permisoDTO.getAreasIds() != null) {
            List<Area> areas = areaRepository.findAllById(permisoDTO.getAreasIds());
            permiso.setAreas(new HashSet<>(areas));
        }

        // Asignar cargos
        if (permisoDTO.getCargosIds() != null) {
            List<Cargo> cargos = cargoRepository.findAllById(permisoDTO.getCargosIds());
            permiso.setCargos(new HashSet<>(cargos));
        }

        // NUEVO: Asignar trabajadores específicos
        if (permisoDTO.getTrabajadoresIds() != null) {
            List<Trabajador> trabajadores = trabajadorRepository.findAllById(permisoDTO.getTrabajadoresIds());
            permiso.setTrabajadores(new HashSet<>(trabajadores));
        }

        Permiso saved = permisoRepository.save(permiso);
        return convertirAResponseDTO(saved);
    }

    @Override
    @Transactional
    public PermisoResponseDTO actualizarPermiso(Integer id, PermisoDTO permisoDTO) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));

        // Validar que el código no exista en otro permiso
        if (permisoRepository.existsByCodigoAndIdPermisoNot(permisoDTO.getCodigo(), id)) {
            throw new RuntimeException("Ya existe otro permiso con el código: " + permisoDTO.getCodigo());
        }

        permiso.setCodigo(permisoDTO.getCodigo());
        permiso.setNombre(permisoDTO.getNombre());
        permiso.setDescripcion(permisoDTO.getDescripcion());

        // Actualizar niveles
        if (permisoDTO.getNivelesIds() != null) {
            List<Nivel> niveles = nivelRepository.findAllById(permisoDTO.getNivelesIds());
            permiso.setNiveles(new HashSet<>(niveles));
        } else {
            permiso.getNiveles().clear();
        }

        // Actualizar áreas
        if (permisoDTO.getAreasIds() != null) {
            List<Area> areas = areaRepository.findAllById(permisoDTO.getAreasIds());
            permiso.setAreas(new HashSet<>(areas));
        } else {
            permiso.getAreas().clear();
        }

        // Actualizar cargos
        if (permisoDTO.getCargosIds() != null) {
            List<Cargo> cargos = cargoRepository.findAllById(permisoDTO.getCargosIds());
            permiso.setCargos(new HashSet<>(cargos));
        } else {
            permiso.getCargos().clear();
        }

        // NUEVO: Actualizar trabajadores
        if (permisoDTO.getTrabajadoresIds() != null) {
            List<Trabajador> trabajadores = trabajadorRepository.findAllById(permisoDTO.getTrabajadoresIds());
            permiso.setTrabajadores(new HashSet<>(trabajadores));
        } else {
            permiso.getTrabajadores().clear();
        }

        Permiso updated = permisoRepository.save(permiso);
        return convertirAResponseDTO(updated);
    }

    @Override
    @Transactional
    public void eliminarPermiso(Integer id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));

        // Limpiar relaciones
        permiso.getNiveles().clear();
        permiso.getAreas().clear();
        permiso.getCargos().clear();
        permiso.getTrabajadores().clear(); // NUEVO: Limpiar relación con trabajadores

        permisoRepository.save(permiso); // Actualizar sin relaciones
        permisoRepository.delete(permiso);
    }
    @Override
    public PermisoResponseDTO obtenerPermisoPorId(Integer id) {
        Permiso permiso = permisoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con id: " + id));
        return convertirAResponseDTO(permiso);
    }

    @Override
    public PermisoResponseDTO obtenerPermisoPorCodigo(String codigo) {
        Permiso permiso = permisoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Permiso no encontrado con código: " + codigo));
        return convertirAResponseDTO(permiso);
    }

    @Override
    public List<PermisoResponseDTO> listarTodosPermisos() {
        return permisoRepository.findAll().stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    // En el método listarTodosPermisosPaginados del servicio
    // En PermisoServiceImpl.java
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<PermisoTablaDTO> listarTodosPermisosPaginados(
            int page,
            int size,
            String sortBy,
            String sortDirection) {

        // Validar parámetros
        page = Math.max(page, 0);
        size = Math.max(1, size);

        // Configurar dirección de ordenamiento
        Sort.Direction direction = Sort.Direction.ASC;
        if (sortDirection != null && sortDirection.equalsIgnoreCase("desc")) {
            direction = Sort.Direction.DESC;
        }

        // Configurar campo de ordenamiento (con valores por defecto seguros)
        String sortField = "idPermiso"; // Campo por defecto
        if (sortBy != null && !sortBy.trim().isEmpty()) {
            // Mapear nombres de campos de frontend a nombres de entidad
            Map<String, String> fieldMapping = new HashMap<>();
            fieldMapping.put("codigo", "codigo");
            fieldMapping.put("nombre", "nombre");
            fieldMapping.put("idPermiso", "idPermiso");

            sortField = fieldMapping.getOrDefault(sortBy, "idPermiso");
        }

        // Crear objeto Sort
        Sort sort = Sort.by(direction, sortField);

        // Crear objeto Pageable
        Pageable pageable = PageRequest.of(page, size, sort);

        // Obtener página desde el repositorio
        Page<Permiso> permisoPage = permisoRepository.findAll(pageable);

        // Convertir a DTOs optimizados para tabla
        List<PermisoTablaDTO> content = permisoPage.getContent().stream()
                .map(this::convertirATablaDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(
                content,
                permisoPage.getNumber(),
                permisoPage.getTotalElements(),
                permisoPage.getTotalPages(),
                permisoPage.isFirst(),
                permisoPage.isLast(),
                permisoPage.getSize()
        );
    }


    // En el método listarTodosPermisosPaginados, actualiza el método convertirATablaDTO:
    private PermisoTablaDTO convertirATablaDTO(Permiso permiso) {
        PermisoTablaDTO dto = new PermisoTablaDTO();
        dto.setIdPermiso(permiso.getIdPermiso());
        dto.setCodigo(permiso.getCodigo());
        dto.setNombre(permiso.getNombre());
        dto.setDescripcion(permiso.getDescripcion());
        dto.setActivo(permiso.getActivo());

        // Convertir solo campos necesarios para niveles
        dto.setNiveles(permiso.getNiveles().stream()
                .map(nivel -> new PermisoNivelTablaDTO(
                        nivel.getIdNivel(),
                        nivel.getCodigo(),
                        nivel.getNombre()
                ))
                .collect(Collectors.toList()));

        // Convertir solo campos necesarios para áreas
        dto.setAreas(permiso.getAreas().stream()
                .map(area -> new PermisoAreaTablaDTO(
                        area.getIdArea(),
                        area.getNombre(),
                        area.getActivo()
                ))
                .collect(Collectors.toList()));

        // Convertir solo campos necesarios para cargos
        dto.setCargos(permiso.getCargos().stream()
                .map(cargo -> new PermisoCargoTablaDTO(
                        cargo.getIdCargo(),
                        cargo.getNombre(),
                        cargo.getActivo()
                ))
                .collect(Collectors.toList()));

        // NUEVO: Convertir solo campos necesarios para trabajadores
        dto.setTrabajadores(permiso.getTrabajadores().stream()
                .map(trabajador -> new PermisoTrabajadorTablaDTO(
                        trabajador.getIdTrabajador(),
                        trabajador.getNombres(),
                        trabajador.getApellidos(),
                        trabajador.getDni(),
                        trabajador.getActivo()
                ))
                .collect(Collectors.toList()));

        return dto;
    }

    @Override
    public boolean verificarPermisoUsuario(VerificarPermisoDTO verificarPermisoDTO) {
        // Obtener usuario
        Usuario usuario = usuarioRepository.findById(verificarPermisoDTO.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Obtener nivel, área, cargo y trabajador del usuario
        Integer idNivel = usuario.getNivel() != null ? usuario.getNivel().getIdNivel() : null;
        Integer idArea = usuario.getArea() != null ? usuario.getArea().getIdArea() : null;
        Integer idCargo = usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null;
        Integer idTrabajador = usuario.getTrabajador() != null ? usuario.getTrabajador().getIdTrabajador() : null;

        // Si el usuario no tiene ninguna relación, no tiene permisos
        if (idNivel == null && idArea == null && idCargo == null && idTrabajador == null) {
            return false;
        }

        // Buscar permiso que aplique al usuario (incluyendo trabajador específico)
        // Necesitarás actualizar tu repositorio para incluir esta búsqueda
        Optional<Permiso> permiso = permisoRepository.findPermisoByUsuario(
                verificarPermisoDTO.getCodigoPermiso(),
                idNivel,
                idArea,
                idCargo,
                idTrabajador
        );

        return permiso.isPresent();
    }

    @Override
    public List<PermisoResponseDTO> obtenerPermisosPorNivel(Integer idNivel) {
        return permisoRepository.findByNivelId(idNivel).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermisoResponseDTO> obtenerPermisosPorArea(Integer idArea) {
        return permisoRepository.findByAreaId(idArea).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PermisoResponseDTO> obtenerPermisosPorCargo(Integer idCargo) {
        return permisoRepository.findByCargoId(idCargo).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }
    @Override
    public List<PermisoResponseDTO> obtenerPermisosPorTrabajador(Integer idTrabajador) {
        // NUEVO método: Obtener permisos asignados directamente a un trabajador
        return permisoRepository.findByTrabajadorId(idTrabajador).stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> obtenerPermisosUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer idNivel = usuario.getNivel() != null ? usuario.getNivel().getIdNivel() : null;
        Integer idArea = usuario.getArea() != null ? usuario.getArea().getIdArea() : null;
        Integer idCargo = usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null;
        Integer idTrabajador = usuario.getTrabajador() != null ? usuario.getTrabajador().getIdTrabajador() : null;

        // Obtener todos los permisos y filtrar aquellos que aplican al usuario
        return permisoRepository.findAll().stream()
                .filter(permiso -> {
                    boolean tieneNivel = idNivel != null && permiso.getNiveles().stream()
                            .anyMatch(nivel -> nivel.getIdNivel().equals(idNivel));

                    boolean tieneArea = idArea != null && permiso.getAreas().stream()
                            .anyMatch(area -> area.getIdArea().equals(idArea));

                    boolean tieneCargo = idCargo != null && permiso.getCargos().stream()
                            .anyMatch(cargo -> cargo.getIdCargo().equals(idCargo));

                    // NUEVO: Verificar por trabajador específico
                    boolean tieneTrabajador = idTrabajador != null && permiso.getTrabajadores().stream()
                            .anyMatch(trabajador -> trabajador.getIdTrabajador().equals(idTrabajador));

                    return tieneNivel || tieneArea || tieneCargo || tieneTrabajador;
                })
                .map(Permiso::getCodigo)
                .collect(Collectors.toList());
    }

    private PermisoResponseDTO convertirAResponseDTO(Permiso permiso) {
        PermisoResponseDTO dto = new PermisoResponseDTO();
        dto.setIdPermiso(permiso.getIdPermiso());
        dto.setCodigo(permiso.getCodigo());
        dto.setNombre(permiso.getNombre());
        dto.setDescripcion(permiso.getDescripcion());

        // Convertir niveles
        dto.setNiveles(permiso.getNiveles().stream()
                .map(nivel -> {
                    NivelDTO nivelDTO = new NivelDTO();
                    nivelDTO.setIdNivel(nivel.getIdNivel());
                    nivelDTO.setCodigo(nivel.getCodigo());
                    nivelDTO.setNombre(nivel.getNombre());
                    return nivelDTO;
                })
                .collect(Collectors.toList()));

        // Convertir áreas
        dto.setAreas(permiso.getAreas().stream()
                .map(area -> {
                    AreaDTO areaDTO = new AreaDTO();
                    areaDTO.setIdArea(area.getIdArea());
                    areaDTO.setNombre(area.getNombre());
                    return areaDTO;
                })
                .collect(Collectors.toList()));

        // Convertir cargos
        dto.setCargos(permiso.getCargos().stream()
                .map(cargo -> {
                    CargoDTO cargoDTO = new CargoDTO();
                    cargoDTO.setIdCargo(cargo.getIdCargo());
                    cargoDTO.setNombre(cargo.getNombre());
                    return cargoDTO;
                })
                .collect(Collectors.toList()));

        // NUEVO: Convertir trabajadores
        dto.setTrabajadores(permiso.getTrabajadores().stream()
                .map(trabajador -> {
                    TrabajadorDTO trabajadorDTO = new TrabajadorDTO();
                    trabajadorDTO.setIdTrabajador(trabajador.getIdTrabajador());
                    trabajadorDTO.setNombres(trabajador.getNombres());
                    trabajadorDTO.setApellidos(trabajador.getApellidos());
                    trabajadorDTO.setDni(trabajador.getDni());
                    trabajadorDTO.setActivo(trabajador.getActivo());
                    return trabajadorDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
}