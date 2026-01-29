package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.MaterialRequest;
import com.backend.comfutura.dto.response.MaterialResponse;
import com.backend.comfutura.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/material")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    // ➕ crear
    @PostMapping
    public MaterialResponse crear(@RequestBody MaterialRequest request) {
        return materialService.crear(request);
    }

    // ✏️ editar
    @PutMapping("/{idMaterialOt}")
    public MaterialResponse editar(
            @PathVariable Integer idMaterialOt,
            @RequestBody MaterialRequest request
    ) {
        return materialService.editar(idMaterialOt, request);
    }

    // 📄 listar por OT
    @GetMapping("/ot/{idOts}")
    public List<MaterialResponse> listarPorOt(@PathVariable Integer idOts) {
        return materialService.listarPorOt(idOts);
    }
}

