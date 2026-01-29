package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.ContratistaRequest;
import com.backend.comfutura.dto.response.ContratistaResponse;
import com.backend.comfutura.service.ContratistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contratistas")
@RequiredArgsConstructor
public class ContratistaController {

    private final ContratistaService contratistaService;

    @PostMapping
    public ResponseEntity<ContratistaResponse> crear(
            @RequestBody ContratistaRequest request
    ) {
        return ResponseEntity.ok(contratistaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContratistaResponse> editar(
            @PathVariable Integer id,
            @RequestBody ContratistaRequest request
    ) {
        return ResponseEntity.ok(contratistaService.editar(id, request));
    }

    @GetMapping("/ot/{idOts}")
    public ResponseEntity<List<ContratistaResponse>> listarPorOt(
            @PathVariable Integer idOts
    ) {
        return ResponseEntity.ok(contratistaService.listarPorOt(idOts));
    }
}
