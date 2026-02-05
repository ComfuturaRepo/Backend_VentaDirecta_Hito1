package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.Exceptions.ValidationException;
import com.backend.comfutura.dto.Mapper.TrabajadorMapper;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.TrabajadorFilterDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorStatsDTO;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.TrabajadorService;
import com.backend.comfutura.service.TrabajadorSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrabajadorServiceImpl implements TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;
    private final AreaRepository areaRepository;
    private final CargoRepository cargoRepository;
    private final EmpresaRepository empresaRepository;
    private final TrabajadorMapper trabajadorMapper;

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> findAllTrabajadores(Pageable pageable) {
        Page<Trabajador> page = trabajadorRepository.findAll(pageable);
        return toPageResponseDTO(page.map(trabajadorMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> findActivos(Pageable pageable) {
        Page<Trabajador> page = trabajadorRepository.findByActivoTrue(pageable);
        return toPageResponseDTO(page.map(trabajadorMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadores(String search, Pageable pageable) {
        Specification<Trabajador> spec = (root, query, cb) -> {
            if (!StringUtils.hasText(search)) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("nombres")), pattern),
                    cb.like(cb.lower(root.get("apellidos")), pattern),
                    cb.like(cb.lower(root.get("dni")), pattern),
                    cb.like(cb.lower(root.get("correoCorporativo")), pattern)
            );
        };

        Page<Trabajador> page = trabajadorRepository.findAll(spec, pageable);
        return toPageResponseDTO(page.map(trabajadorMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadoresAdvanced(TrabajadorFilterDTO filterDTO) {
        // Crear Pageable desde el DTO
        Sort sort = Sort.by(
                filterDTO.getSortDirection().equalsIgnoreCase("ASC") ?
                        Sort.Direction.ASC : Sort.Direction.DESC,
                filterDTO.getSortBy()
        );

        Pageable pageable = PageRequest.of(
                filterDTO.getPage(),
                filterDTO.getSize(),
                sort
        );

        // Crear Specification dinámica
        Specification<Trabajador> spec = TrabajadorSpecification.withDynamicFilters(
                filterDTO.getSearch(),
                filterDTO.getActivo(),
                filterDTO.getAreaIds(),
                filterDTO.getCargoIds(),
                filterDTO.getEmpresaIds(),
                filterDTO.getPuedeSerLiquidador(),
                filterDTO.getPuedeSerEjecutante(),
                filterDTO.getPuedeSerAnalistaContable(),
                filterDTO.getPuedeSerJefaturaResponsable(),
                filterDTO.getPuedeSerCoordinadorTiCw()
        );

        Page<Trabajador> page = trabajadorRepository.findAll(spec, pageable);

        // Crear respuesta
        PageResponseDTO<TrabajadorSimpleDTO> response = toPageResponseDTO(page.map(trabajadorMapper::toSimpleDTO));

        Map<String, Object> filters = new HashMap<>();
        filters.put("search", filterDTO.getSearch());
        filters.put("activo", filterDTO.getActivo());
        filters.put("areaIds", filterDTO.getAreaIds());
        filters.put("cargoIds", filterDTO.getCargoIds());
        filters.put("empresaIds", filterDTO.getEmpresaIds());
        filters.put("puedeSerLiquidador", filterDTO.getPuedeSerLiquidador());
        filters.put("puedeSerEjecutante", filterDTO.getPuedeSerEjecutante());
        filters.put("puedeSerAnalistaContable", filterDTO.getPuedeSerAnalistaContable());
        filters.put("puedeSerJefaturaResponsable", filterDTO.getPuedeSerJefaturaResponsable());
        filters.put("puedeSerCoordinadorTiCw", filterDTO.getPuedeSerCoordinadorTiCw());
        filters.put("sortBy", filterDTO.getSortBy());
        filters.put("sortDirection", filterDTO.getSortDirection());

        response.setFilters(filters);

        return response;
    }

    // Método alternativo usando la consulta simple del repository
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadoresSimple(
            String search,
            Boolean activo,
            Integer areaId,
            Integer cargoId,
            Integer empresaId,
            Pageable pageable) {

        Page<Trabajador> page = trabajadorRepository.searchTrabajadoresSimple(
                search, activo, areaId, cargoId, empresaId, pageable);

        return toPageResponseDTO(page.map(trabajadorMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public TrabajadorDetailDTO findTrabajadorById(Integer id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con ID: " + id));
        return trabajadorMapper.toDetailDTO(trabajador);
    }

    @Override
    @Transactional(readOnly = true)
    public TrabajadorDetailDTO findTrabajadorByDni(String dni) {
        Trabajador trabajador = trabajadorRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con DNI: " + dni));
        return trabajadorMapper.toDetailDTO(trabajador);
    }

    @Override
    @Transactional(readOnly = true)
    public TrabajadorDetailDTO findTrabajadorByEmail(String email) {
        Trabajador trabajador = trabajadorRepository.findByCorreoCorporativo(email)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con email: " + email));
        return trabajadorMapper.toDetailDTO(trabajador);
    }

    @Override
    @Transactional
    public TrabajadorDetailDTO createTrabajador(TrabajadorRequestDTO trabajadorDTO) {
        // Validar que el DNI no exista
        if (trabajadorDTO.getDni() != null && trabajadorRepository.existsByDni(trabajadorDTO.getDni())) {
            throw new ValidationException("El DNI ya está registrado");
        }

        // Validar que el correo corporativo no exista
        if (trabajadorDTO.getCorreoCorporativo() != null &&
                trabajadorRepository.existsByCorreoCorporativo(trabajadorDTO.getCorreoCorporativo())) {
            throw new ValidationException("El correo corporativo ya está registrado");
        }


        // Obtener y validar área
        Area area = areaRepository.findById(trabajadorDTO.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        // Obtener y validar cargo
        Cargo cargo = cargoRepository.findById(trabajadorDTO.getCargoId())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        // Obtener empresa si se especifica
        Empresa empresa = null;
        if (trabajadorDTO.getEmpresaId() != null) {
            empresa = empresaRepository.findById(trabajadorDTO.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        }

        // Crear trabajador
        Trabajador trabajador = trabajadorMapper.toEntity(trabajadorDTO);
        trabajador.setArea(area);
        trabajador.setCargo(cargo);
        trabajador.setEmpresa(empresa);
        trabajador.setActivo(trabajadorDTO.getActivo() != null ? trabajadorDTO.getActivo() : true);
        trabajador.setFechaCreacion(LocalDateTime.now());

        // Establecer valores por defecto para los nuevos campos
        if (trabajador.getPuedeSerLiquidador() == null) trabajador.setPuedeSerLiquidador(false);
        if (trabajador.getPuedeSerEjecutante() == null) trabajador.setPuedeSerEjecutante(false);
        if (trabajador.getPuedeSerAnalistaContable() == null) trabajador.setPuedeSerAnalistaContable(false);
        if (trabajador.getPuedeSerJefaturaResponsable() == null) trabajador.setPuedeSerJefaturaResponsable(false);
        if (trabajador.getPuedeSerCoordinadorTiCw() == null) trabajador.setPuedeSerCoordinadorTiCw(false);

        Trabajador savedTrabajador = trabajadorRepository.save(trabajador);
        return trabajadorMapper.toDetailDTO(savedTrabajador);
    }

    @Override
    @Transactional
    public TrabajadorDetailDTO updateTrabajador(Integer id, TrabajadorUpdateDTO trabajadorDTO) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con ID: " + id));

        // Validar que el DNI no esté en uso por otro trabajador
        if (trabajadorDTO.getDni() != null && !trabajadorDTO.getDni().equals(trabajador.getDni())) {
            if (trabajadorRepository.existsByDniAndIdTrabajadorNot(trabajadorDTO.getDni(), id)) {
                throw new RuntimeException("El DNI ya está registrado por otro trabajador");
            }
        }

        // Validar que el correo corporativo no esté en uso por otro trabajador
        if (trabajadorDTO.getCorreoCorporativo() != null &&
                !trabajadorDTO.getCorreoCorporativo().equals(trabajador.getCorreoCorporativo())) {
            if (trabajadorRepository.existsByCorreoCorporativoAndIdTrabajadorNot(
                    trabajadorDTO.getCorreoCorporativo(), id)) {
                throw new ValidationException("El correo corporativo ya está registrado por otro trabajador");
            }
        }
        // Obtener y validar área
        Area area = areaRepository.findById(trabajadorDTO.getAreaId())
                .orElseThrow(() -> new RuntimeException("Área no encontrada"));

        // Obtener y validar cargo
        Cargo cargo = cargoRepository.findById(trabajadorDTO.getCargoId())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        // Obtener empresa si se especifica
        Empresa empresa = null;
        if (trabajadorDTO.getEmpresaId() != null) {
            empresa = empresaRepository.findById(trabajadorDTO.getEmpresaId())
                    .orElseThrow(() -> new RuntimeException("Empresa no encontrada"));
        }

        // Actualizar trabajador
        trabajadorMapper.updateEntity(trabajadorDTO, trabajador);
        trabajador.setArea(area);
        trabajador.setCargo(cargo);
        trabajador.setEmpresa(empresa);

        // Actualizar fecha de modificación
        trabajador.setFechaCreacion(LocalDateTime.now());

        Trabajador updatedTrabajador = trabajadorRepository.save(trabajador);
        return trabajadorMapper.toDetailDTO(updatedTrabajador);
    }

    @Override
    @Transactional
    public TrabajadorDetailDTO toggleTrabajadorActivo(Integer id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado con ID: " + id));

        trabajador.setActivo(!trabajador.getActivo());
        Trabajador updatedTrabajador = trabajadorRepository.save(trabajador);

        return trabajadorMapper.toDetailDTO(updatedTrabajador);
    }

    @Override
    @Transactional(readOnly = true)
    public TrabajadorStatsDTO getTrabajadorStats() {
        TrabajadorStatsDTO stats = new TrabajadorStatsDTO();

        // Obtener total de trabajadores
        long total = trabajadorRepository.count();
        stats.setTotalTrabajadores(total);

        // Obtener trabajadores activos
        long activos = trabajadorRepository.findAll().stream()
                .filter(Trabajador::getActivo)
                .count();
        stats.setTrabajadoresActivos(activos);

        // Calcular porcentaje de activos
        double porcentajeActivos = total > 0 ? (activos * 100.0 / total) : 0;
        stats.setPorcentajeActivos(Math.round(porcentajeActivos * 100.0) / 100.0);

        return stats;
    }

    @Override
    @Transactional(readOnly = true)
    public long countActivosByArea(Integer areaId) {
        return trabajadorRepository.countActivosByArea(areaId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countByCargo(Integer cargoId) {
        return trabajadorRepository.countByCargo(cargoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TrabajadorSimpleDTO> getTrabajadoresByFilters(
            String search,
            Boolean activo,
            List<Integer> areaIds,
            List<Integer> cargoIds,
            List<Boolean> roles) {

        // Crear especificación dinámica usando el helper
        Specification<Trabajador> spec = TrabajadorSpecification.withDynamicFilters(
                search,
                activo,
                areaIds,
                cargoIds,
                null, // empresaIds
                null, // puedeSerLiquidador
                null, // puedeSerEjecutante
                null, // puedeSerAnalistaContable
                null, // puedeSerJefaturaResponsable
                null  // puedeSerCoordinadorTiCw
        );

        List<Trabajador> trabajadores = trabajadorRepository.findAll(spec);
        return trabajadores.stream()
                .map(trabajadorMapper::toSimpleDTO)
                .toList();
    }

    // Helper para convertir Page a PageResponseDTO
    private <T> PageResponseDTO<T> toPageResponseDTO(Page<T> page) {
        return new PageResponseDTO<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getSize()
        );
    }
}