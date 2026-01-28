package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.CargoRequestDTO;
import com.backend.comfutura.dto.response.CargoResponseDTO;
import com.backend.comfutura.model.Cargo;
import com.backend.comfutura.model.Nivel;
import com.backend.comfutura.repository.CargoRepository;
import com.backend.comfutura.repository.NivelRepository;
import com.backend.comfutura.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {

    private final CargoRepository cargoRepository;
    private final NivelRepository nivelRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<CargoResponseDTO> listarPaginado(Pageable pageable) {
        return cargoRepository.findAll(pageable).map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CargoResponseDTO> listar(Pageable pageable) {
        Page<Cargo> page = cargoRepository.findAll(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("nombre").ascending()
                )
        );
        return toPageResponseDTO(page.map(this::mapToDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CargoResponseDTO> listarActivos(Pageable pageable) {
        Page<Cargo> page = cargoRepository.findByActivoTrue(
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by("nombre").ascending()
                )
        );
        return toPageResponseDTO(page.map(this::mapToDTO));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<CargoResponseDTO> buscar(String search, Pageable pageable) {
        Specification<Cargo> spec = (root, query, cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String pattern = "%" + search.toLowerCase().trim() + "%";
            return cb.like(cb.lower(root.get("nombre")), pattern);
        };

        Page<Cargo> page = cargoRepository.findAll(spec, pageable);
        return toPageResponseDTO(page.map(this::mapToDTO));
    }

    @Override
    @Transactional
    public CargoResponseDTO guardar(CargoRequestDTO dto) {
        Cargo cargo = dto.getId() == null
                ? new Cargo()
                : cargoRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        cargo.setNombre(dto.getNombre());

        Nivel nivel = nivelRepository.findById(dto.getIdNivel())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        cargo.setNivel(nivel);

        if (dto.getId() == null) {
            cargo.setActivo(true);
        }

        Cargo saved = cargoRepository.save(cargo);
        return mapToDTO(saved);
    }

    @Override
    @Transactional
    public void toggle(Integer id) {
        Cargo cargo = cargoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

        cargo.setActivo(!cargo.getActivo());
        cargoRepository.save(cargo);
    }

    private CargoResponseDTO mapToDTO(Cargo cargo) {
        return CargoResponseDTO.builder()
                .id(cargo.getId())
                .nombre(cargo.getNombre())
                .activo(cargo.getActivo())
                .idNivel(cargo.getNivel().getId())
                .nombreNivel(cargo.getNivel().getNombre())
                .build();
    }

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