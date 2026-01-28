package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.CargoRequestDTO;
import com.backend.comfutura.dto.response.CargoResponseDTO;
import com.backend.comfutura.service.CargoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cargos")
@RequiredArgsConstructor
public class CargoController {

    private final CargoService cargoService;

    @GetMapping
    public ResponseEntity<PageResponseDTO<CargoResponseDTO>> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nombre") String sort,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(required = false) Boolean activos) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));

        PageResponseDTO<CargoResponseDTO> response;
        if (activos != null && activos) {
            response = cargoService.listarActivos(pageable);
        } else {
            response = cargoService.listar(pageable);
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar")
    public ResponseEntity<PageResponseDTO<CargoResponseDTO>> buscar(
            @RequestParam String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        PageResponseDTO<CargoResponseDTO> response = cargoService.buscar(search, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<CargoResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<CargoResponseDTO> response = cargoService.listarPaginado(pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CargoResponseDTO> guardar(@RequestBody CargoRequestDTO dto) {
        CargoResponseDTO response = cargoService.guardar(dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(@PathVariable Integer id) {
        cargoService.toggle(id);
        return ResponseEntity.ok().build();
    }
}