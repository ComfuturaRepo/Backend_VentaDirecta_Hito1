package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.Exceptions.ResourceNotFoundException;
import com.backend.comfutura.Exceptions.ValidationException;
import com.backend.comfutura.dto.response.clienteDTO.ClienteSimpleDTO;
import com.backend.comfutura.model.Area;
import com.backend.comfutura.model.Cliente;
import com.backend.comfutura.repository.AreaRepository;
import com.backend.comfutura.service.AreaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.backend.comfutura.dto.Page.PageRequestDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaCreateUpdateDTO;
import com.backend.comfutura.dto.request.areaDTO.AreaDTO;
import com.backend.comfutura.dto.response.areaDTO.AreaSimpleDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AreaServiceImpl implements AreaService {

    private final AreaRepository areaRepository;

    @Override
    public PageResponseDTO<AreaDTO> findAll(PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Area> page = areaRepository.findAll(pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public PageResponseDTO<AreaDTO> findAllActivos(PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Area> page = areaRepository.findByActivoTrue(pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public PageResponseDTO<AreaDTO> findAllInactivos(PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Area> page = areaRepository.findByActivoFalse(pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public PageResponseDTO<AreaDTO> search(String search, PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Area> page = areaRepository.search(search, pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public AreaDTO findById(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));

        return convertToDTO(area);
    }

    @Override
    @Transactional
    public AreaDTO create(AreaCreateUpdateDTO areaDTO) {
        // Validar nombre único
        if (areaRepository.existsByNombre(areaDTO.getNombre())) {
            throw new ValidationException("El área '" + areaDTO.getNombre() + "' ya existe");
        }

        // Validar nombre
        if (areaDTO.getNombre() == null || areaDTO.getNombre().trim().length() < 2) {
            throw new ValidationException("El nombre del área debe tener al menos 2 caracteres");
        }

        Area area = new Area();
        area.setNombre(areaDTO.getNombre().trim());
        area.setActivo(true);

        Area savedArea = areaRepository.save(area);
        return convertToDTO(savedArea);
    }

    @Override
    @Transactional
    public AreaDTO update(Integer id, AreaCreateUpdateDTO areaDTO) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));

        // Validar nombre único (excepto el actual)
        if (areaRepository.existsByNombreAndIdAreaNot(areaDTO.getNombre(), id)) {
            throw new ValidationException("El área '" + areaDTO.getNombre() + "' ya existe en otra área");
        }

        // Validar nombre
        if (areaDTO.getNombre() == null || areaDTO.getNombre().trim().length() < 2) {
            throw new ValidationException("El nombre del área debe tener al menos 2 caracteres");
        }

        area.setNombre(areaDTO.getNombre().trim());

        Area updatedArea = areaRepository.save(area);
        return convertToDTO(updatedArea);
    }

    @Override
    @Transactional
    public AreaDTO toggleStatus(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));

        // Verificar si el área tiene clientes activos antes de desactivar
        if (area.getActivo() && !area.getClientes().isEmpty()) {
            long clientesActivos = area.getClientes().stream()
                    .filter(Cliente::getActivo)
                    .count();

            if (clientesActivos > 0) {
                throw new ValidationException(
                        "No se puede desactivar el área porque tiene " + clientesActivos + " cliente(s) activo(s) asignado(s)"
                );
            }
        }

        area.setActivo(!area.getActivo());
        Area updatedArea = areaRepository.save(area);
        return convertToDTO(updatedArea);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Area area = areaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Área no encontrada con ID: " + id));

        // Verificar si el área tiene clientes asignados
        if (!area.getClientes().isEmpty()) {
            throw new ValidationException(
                    "No se puede eliminar el área porque tiene " + area.getClientes().size() + " cliente(s) asignado(s)"
            );
        }

        areaRepository.delete(area);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        return areaRepository.existsByNombre(nombre);
    }

    @Override
    public List<AreaSimpleDTO> findAllActivosForDropdown() {
        return areaRepository.findByActivoTrue().stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<AreaSimpleDTO> findAllForDropdown() {
        return areaRepository.findAll().stream()
                .map(this::convertToSimpleDTO)
                .collect(Collectors.toList());
    }

    // =============================
    // MÉTODOS AUXILIARES
    // =============================

    private Pageable createPageable(PageRequestDTO pageRequest) {
        Sort sort = Sort.by(
                pageRequest.getSortDir().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                pageRequest.getSortBy()
        );
        return PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
    }

    private PageResponseDTO<AreaDTO> convertToPageResponseDTO(Page<Area> page) {
        List<AreaDTO> content = page.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(
                content,
                page.getNumber(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast(),
                page.getSize()
        );
    }

    private AreaDTO convertToDTO(Area area) {
        AreaDTO dto = new AreaDTO();
        dto.setIdArea(area.getIdArea());
        dto.setNombre(area.getNombre());
        dto.setActivo(area.getActivo());

        // Convertir clientes a DTOs simples
        if (area.getClientes() != null) {
            List<ClienteSimpleDTO> clienteDTOs = area.getClientes().stream()
                    .map(cliente -> new ClienteSimpleDTO(
                            cliente.getIdCliente(),
                            cliente.getRazonSocial(),
                            cliente.getRuc(),
                            cliente.getActivo()
                    ))
                    .collect(Collectors.toList());
            dto.setClientes(clienteDTOs);
        }

        return dto;
    }

    private AreaSimpleDTO convertToSimpleDTO(Area area) {
        return new AreaSimpleDTO(
                area.getIdArea(),
                area.getNombre(),
                area.getActivo()
        );
    }
}