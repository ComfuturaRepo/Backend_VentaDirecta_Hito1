package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.Exceptions.ResourceNotFoundException;
import com.backend.comfutura.Exceptions.ValidationException;
import com.backend.comfutura.dto.Page.PageRequestDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.clienteDTO.ClienteCreateUpdateDTO;
import com.backend.comfutura.dto.request.clienteDTO.ClienteDTO;
import com.backend.comfutura.dto.response.clienteDTO.AreaSimpleDTO;
import com.backend.comfutura.dto.response.clienteDTO.ClienteDetailDTO;
import com.backend.comfutura.model.Area;
import com.backend.comfutura.model.Cliente;
import com.backend.comfutura.repository.AreaRepository;
import com.backend.comfutura.repository.ClienteRepository;
import com.backend.comfutura.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final AreaRepository areaRepository;

    @Override
    public PageResponseDTO<ClienteDTO> findAll(PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Cliente> page = clienteRepository.findAll(pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public PageResponseDTO<ClienteDTO> findAllActivos(PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Cliente> page = clienteRepository.findByActivoTrue(pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public PageResponseDTO<ClienteDTO> findAllInactivos(PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Cliente> page = clienteRepository.findByActivoFalse(pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public PageResponseDTO<ClienteDTO> search(String search, PageRequestDTO pageRequest) {
        Pageable pageable = createPageable(pageRequest);
        Page<Cliente> page = clienteRepository.search(search, pageable);
        return convertToPageResponseDTO(page);
    }

    @Override
    public ClienteDetailDTO findById(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        return convertToDetailDTO(cliente);
    }

    @Override
    @Transactional
    public ClienteDTO create(ClienteCreateUpdateDTO clienteDTO) {
        // Validar RUC único
        if (clienteRepository.existsByRuc(clienteDTO.getRuc())) {
            throw new ValidationException("El RUC " + clienteDTO.getRuc() + " ya está registrado");
        }

        // Validar RUC (11 dígitos)
        if (clienteDTO.getRuc() == null || !clienteDTO.getRuc().matches("\\d{11}")) {
            throw new ValidationException("El RUC debe tener 11 dígitos");
        }

        Cliente cliente = new Cliente();
        cliente.setRazonSocial(clienteDTO.getRazonSocial());
        cliente.setRuc(clienteDTO.getRuc());
        cliente.setActivo(true);

        // Asociar áreas si se proporcionan
        if (clienteDTO.getAreaIds() != null && !clienteDTO.getAreaIds().isEmpty()) {
            List<Area> areas = areaRepository.findByIdAreaIn(clienteDTO.getAreaIds());
            cliente.setAreas(areas);

            // Actualizar el lado inverso de la relación
            areas.forEach(area -> {
                if (!area.getClientes().contains(cliente)) {
                    area.getClientes().add(cliente);
                }
            });
        }

        Cliente savedCliente = clienteRepository.save(cliente);
        return convertToDTO(savedCliente);
    }

    @Override
    @Transactional
    public ClienteDTO update(Integer id, ClienteCreateUpdateDTO clienteDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // Validar RUC único (excepto el actual)
        if (clienteRepository.existsByRucAndIdClienteNot(clienteDTO.getRuc(), id)) {
            throw new ValidationException("El RUC " + clienteDTO.getRuc() + " ya está registrado en otro cliente");
        }

        // Validar RUC (11 dígitos)
        if (clienteDTO.getRuc() == null || !clienteDTO.getRuc().matches("\\d{11}")) {
            throw new ValidationException("El RUC debe tener 11 dígitos");
        }

        cliente.setRazonSocial(clienteDTO.getRazonSocial());
        cliente.setRuc(clienteDTO.getRuc());

        // Actualizar áreas
        if (clienteDTO.getAreaIds() != null) {
            // Limpiar relaciones actuales
            cliente.getAreas().forEach(area -> area.getClientes().remove(cliente));
            cliente.getAreas().clear();

            // Agregar nuevas áreas
            if (!clienteDTO.getAreaIds().isEmpty()) {
                List<Area> areas = areaRepository.findByIdAreaIn(clienteDTO.getAreaIds());
                cliente.setAreas(areas);

                // Actualizar el lado inverso
                areas.forEach(area -> {
                    if (!area.getClientes().contains(cliente)) {
                        area.getClientes().add(cliente);
                    }
                });
            }
        }

        Cliente updatedCliente = clienteRepository.save(cliente);
        return convertToDTO(updatedCliente);
    }

    @Override
    @Transactional
    public ClienteDTO toggleStatus(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        cliente.setActivo(!cliente.getActivo());
        Cliente updatedCliente = clienteRepository.save(cliente);
        return convertToDTO(updatedCliente);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));

        // Desvincular de áreas antes de eliminar
        cliente.getAreas().forEach(area -> area.getClientes().remove(cliente));
        cliente.getAreas().clear();

        clienteRepository.delete(cliente);
    }

    @Override
    public boolean existsByRuc(String ruc) {
        return clienteRepository.existsByRuc(ruc);
    }

    // Métodos auxiliares
    private Pageable createPageable(PageRequestDTO pageRequest) {
        Sort sort = Sort.by(
                pageRequest.getSortDir().equalsIgnoreCase("asc")
                        ? Sort.Direction.ASC
                        : Sort.Direction.DESC,
                pageRequest.getSortBy()
        );
        return PageRequest.of(pageRequest.getPage(), pageRequest.getSize(), sort);
    }

    private PageResponseDTO<ClienteDTO> convertToPageResponseDTO(Page<Cliente> page) {
        List<ClienteDTO> content = page.getContent().stream()
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

    private ClienteDTO convertToDTO(Cliente cliente) {
        ClienteDTO dto = new ClienteDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setRazonSocial(cliente.getRazonSocial());
        dto.setRuc(cliente.getRuc());
        dto.setActivo(cliente.getActivo());

        // Convertir áreas a DTOs simples
        if (cliente.getAreas() != null) {
            List<AreaSimpleDTO> areaDTOs = cliente.getAreas().stream()
                    .map(area -> new AreaSimpleDTO(
                            area.getIdArea(),
                            area.getNombre(),
                            area.getActivo()
                    ))
                    .collect(Collectors.toList());
            dto.setAreas(areaDTOs);
        }

        return dto;
    }

    private ClienteDetailDTO convertToDetailDTO(Cliente cliente) {
        ClienteDetailDTO dto = new ClienteDetailDTO();
        dto.setIdCliente(cliente.getIdCliente());
        dto.setRazonSocial(cliente.getRazonSocial());
        dto.setRuc(cliente.getRuc());
        dto.setActivo(cliente.getActivo());

        // Convertir áreas a DTOs simples
        if (cliente.getAreas() != null) {
            List<AreaSimpleDTO> areaDTOs = cliente.getAreas().stream()
                    .map(area -> new AreaSimpleDTO(
                            area.getIdArea(),
                            area.getNombre(),
                            area.getActivo()
                    ))
                    .collect(Collectors.toList());
            dto.setAreas(areaDTOs);
        }

        return dto;
    }
}