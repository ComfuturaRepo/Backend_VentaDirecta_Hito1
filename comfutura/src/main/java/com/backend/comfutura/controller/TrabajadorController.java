package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.TrabajadorRequestDTO;
import com.backend.comfutura.dto.response.TrabajadorResponseDTO;
import com.backend.comfutura.service.TrabajadorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trabajadores")
@RequiredArgsConstructor
public class TrabajadorController {

    private final TrabajadorService trabajadorService;

    // ===== LISTAR CON PAGINADO =====
    @GetMapping
    public Page<TrabajadorResponseDTO> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "apellidos") String sortBy
    ) {
        return trabajadorService.getAll(page, size, sortBy);
    }

    // ===== CREAR / EDITAR =====
    @PostMapping
    public TrabajadorResponseDTO crearEditar(@RequestBody TrabajadorRequestDTO dto) {
        return trabajadorService.saveOrUpdate(dto);
    }
    // ===== EDITAR TRABAJADOR =====
    @PutMapping("/{id}")
    public TrabajadorResponseDTO editar(
            @PathVariable Integer id,
            @RequestBody TrabajadorRequestDTO dto
    ) {
        // Asegurarse de que el ID del DTO coincida con el path
        dto.setIdTrabajador(id);
        return trabajadorService.saveOrUpdate(dto);
    }

    // ===== TOGGLE ACTIVO =====
    @PatchMapping("/{id}/toggle")
    public void toggleActivo(@PathVariable Integer id) {
        trabajadorService.toggleActivo(id);
    }
}