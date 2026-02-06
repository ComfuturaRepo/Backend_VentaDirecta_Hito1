package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageRequestDTO;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.clienteDTO.ClienteCreateUpdateDTO;
import com.backend.comfutura.dto.request.clienteDTO.ClienteDTO;
import com.backend.comfutura.dto.response.clienteDTO.ClienteDetailDTO;
import com.backend.comfutura.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {

    private final ClienteService clienteService;

    @Operation(summary = "Listar todos los clientes con paginación")
    @GetMapping
    public ResponseEntity<PageResponseDTO<ClienteDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCliente") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequestDTO pageRequest = new PageRequestDTO(page, size, sortBy, sortDir);
        PageResponseDTO<ClienteDTO> response = clienteService.findAll(pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar clientes activos con paginación")
    @GetMapping("/activos")
    public ResponseEntity<PageResponseDTO<ClienteDTO>> getActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCliente") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequestDTO pageRequest = new PageRequestDTO(page, size, sortBy, sortDir);
        PageResponseDTO<ClienteDTO> response = clienteService.findAllActivos(pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar clientes por término")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<ClienteDTO>> search(
            @RequestParam String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idCliente") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        PageRequestDTO pageRequest = new PageRequestDTO(page, size, sortBy, sortDir);
        PageResponseDTO<ClienteDTO> response = clienteService.search(q, pageRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener cliente por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteDetailDTO> getById(@PathVariable Integer id) {
        ClienteDetailDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Crear nuevo cliente")
    @PostMapping
    public ResponseEntity<ClienteDTO> create(@Valid @RequestBody ClienteCreateUpdateDTO clienteDTO) {
        ClienteDTO createdCliente = clienteService.create(clienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCliente);
    }

    @Operation(summary = "Actualizar cliente existente")
    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> update(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteCreateUpdateDTO clienteDTO) {

        ClienteDTO updatedCliente = clienteService.update(id, clienteDTO);
        return ResponseEntity.ok(updatedCliente);
    }

    @Operation(summary = "Activar/desactivar cliente")
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ClienteDTO> toggleStatus(@PathVariable Integer id) {
        ClienteDTO updatedCliente = clienteService.toggleStatus(id);
        return ResponseEntity.ok(updatedCliente);
    }

    @Operation(summary = "Eliminar cliente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Verificar si existe un RUC")
    @GetMapping("/exists/ruc/{ruc}")
    public ResponseEntity<Boolean> existsByRuc(@PathVariable String ruc) {
        boolean exists = clienteService.existsByRuc(ruc);
        return ResponseEntity.ok(exists);
    }
}