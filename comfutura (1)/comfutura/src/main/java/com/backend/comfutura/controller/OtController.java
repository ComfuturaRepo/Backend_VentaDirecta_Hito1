package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.CrearOtCompletaRequest;
import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtResponse;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.service.OtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ots")
@RequiredArgsConstructor
public class OtController {

    private final OtService otService;

    // ==============================
    // CREAR OT COMPLETA
    // ==============================
    @PostMapping("/completa")
    public ResponseEntity<OtResponse> createOtCompleta(
            @Valid @RequestBody CrearOtCompletaRequest request
    ) {
        OtResponse response = otService.createOtCompleta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ==============================
    // Listado OT COMPLETO
    // ==============================
    @GetMapping
    public ResponseEntity<Page<OtResponse>> listar(
            @RequestParam Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "idOts")
        );

        return ResponseEntity.ok(
                otService.listarPorEstado(activo, pageable)
        );
    }



    // ==============================
    // Listado POR ID  OT COMPLETO
    // ==============================
    @GetMapping("/{id}")
    public ResponseEntity<OtResponse> obtenerPorId(@PathVariable Integer id) {

        return ResponseEntity.ok(
                otService.obtenerPorId(id)
        );
    }


    //
    // ==============================
    // TOGGLE ACTIVO / INACTIVO
    // ==============================
    @PostMapping("/{id}/toggle")
    public ResponseEntity<Void> toggleEstado(@PathVariable Integer id) {

        otService.toggleEstado(id);
        return ResponseEntity.ok().build();
    }
}
