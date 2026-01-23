package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.CargoRequestDTO;
import com.backend.comfutura.dto.response.CargoResponseDTO;
import com.backend.comfutura.model.Cargo;
import com.backend.comfutura.model.Nivel;
import com.backend.comfutura.repository.CargoRepository;

import com.backend.comfutura.repository.NivelRepository;
import com.backend.comfutura.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CargoServiceImpl implements CargoService {

    private final CargoRepository cargoRepository;
    private final NivelRepository nivelRepository;

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
    @Transactional(readOnly = true)
    public Page<CargoResponseDTO> listar(Pageable pageable) {
        return cargoRepository.findAll(pageable)
                .map(this::mapToDTO);
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
}