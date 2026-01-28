package com.backend.comfutura.service.serviceImpl;
import com.backend.comfutura.dto.*;
import com.backend.comfutura.dto.Mapper.TrabajadorMapper;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.TrabajadorSimpleDTO;
import com.backend.comfutura.dto.response.TrabajadorStatsDTO;
import com.backend.comfutura.model.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.TrabajadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
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
        return toPageResponseDTO2(page.map(trabajadorMapper::toSimpleDTO));
    }
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> findActivos(Pageable pageable) {
        Page<Trabajador> page = trabajadorRepository.findByActivoTrue(pageable);
        return toPageResponseDTO2(page.map(trabajadorMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadores(String search, Pageable pageable) {
        Specification<Trabajador> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("nombres")), pattern),
                    cb.like(cb.lower(root.get("apellidos")), pattern),
                    cb.like(cb.lower(root.get("dni")), pattern),
                    cb.like(cb.lower(root.get("email")), pattern)
            );
        };

        Page<Trabajador> page = trabajadorRepository.findAll(spec, pageable);
        return toPageResponseDTO2(page.map(trabajadorMapper::toSimpleDTO));
    }


    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> findTrabajadoresActivos(Pageable pageable) {
        Page<Trabajador> page = trabajadorRepository.findByActivoTrue(pageable);
        return toPageResponseDTO(page.map(trabajadorMapper::toSimpleDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<TrabajadorSimpleDTO> searchTrabajadores(String search, Boolean activo,
                                                                   Integer areaId, Integer cargoId, Integer empresaId, Pageable pageable) {

        Page<Trabajador> page = trabajadorRepository.searchTrabajadores(
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
    @Transactional
    public TrabajadorDetailDTO createTrabajador(TrabajadorRequestDTO trabajadorDTO) {
        // Validar que el DNI no exista
        if (trabajadorDTO.getDni() != null && trabajadorRepository.existsByDni(trabajadorDTO.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
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
        trabajador.setFechaCreacion(trabajador.getFechaCreacion()); // Mantener la original

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
    private <T> PageResponseDTO<T> toPageResponseDTO2(Page<T> page) {
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
