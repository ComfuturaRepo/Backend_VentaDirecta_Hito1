package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.CargoRequestDTO;
import com.backend.comfutura.dto.response.CargoResponseDTO;
import com.backend.comfutura.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cargo")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService service;

    // CREAR + EDITAR
    @PostMapping
    public ResponseEntity<CargoResponseDTO> guardar(
            @RequestBody CargoRequestDTO dto
    ) {
        return ResponseEntity.ok(service.guardar(dto));
    }

    // LISTAR PAGINADO
    @GetMapping
    public ResponseEntity<Page<CargoResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    // TOGGLE
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        service.toggle(id);
        return ResponseEntity.ok().build();
    }
}

