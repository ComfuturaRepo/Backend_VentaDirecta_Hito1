package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.otDTO.OtCreateRequest;
import com.backend.comfutura.dto.response.otDTO.OtDetailResponse;
import com.backend.comfutura.dto.response.otDTO.OtFullResponse;
import com.backend.comfutura.dto.response.otDTO.OtListDto;
import com.backend.comfutura.service.OtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ots")
@RequiredArgsConstructor
public class OtController {

    private final OtService otService;

    @GetMapping
    public ResponseEntity<PageResponseDTO<OtListDto>> listar(
            @RequestParam(required = false) String search,
            Pageable pageable) {
        PageResponseDTO<OtListDto> response = otService.listarOts(search, pageable);
        return ResponseEntity.ok(response);
    }


    @PostMapping
    public ResponseEntity<OtDetailResponse> guardar(@RequestBody OtCreateRequest request) {
        OtDetailResponse response = otService.saveOt(request);
        HttpStatus status = (request.getIdOts() == null) ? HttpStatus.CREATED : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }

    /**
     * Alternar activo/inactivo
     * Endpoint: PATCH /api/ots/{id}/toggle-activo
     */
    @PatchMapping("/{id}/toggle-activo")
    public ResponseEntity<Void> toggleActivo(@PathVariable Integer id) {
        otService.toggleActivo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Obtener datos para formulario de edición (solo IDs + básicos)
     * Endpoint: GET /api/ots/{id}/edit
     */
    @GetMapping("/{id}/edit")
    public ResponseEntity<OtFullResponse> obtenerParaEdicion(@PathVariable Integer id) {
        return ResponseEntity.ok(otService.obtenerParaEdicion(id));
    }

    /**
     * Obtener detalle completo de una OT por ID
     * Endpoint: GET /api/ots/{id}/detail
     */
    @GetMapping("/{id}/detail")
    public ResponseEntity<OtDetailResponse> obtenerDetallePorId(@PathVariable Integer id) {
        return ResponseEntity.ok(otService.obtenerDetallePorId(id));
    }
}