package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.GastoLogisticoRequest;
import com.backend.comfutura.dto.response.GastoLogisticoResponse;
import com.backend.comfutura.service.GastoLogisticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gastos-logisticos")
@RequiredArgsConstructor
public class GastoLogisticoController {

    private final GastoLogisticoService service;

    @PostMapping
    public GastoLogisticoResponse crear(@RequestBody GastoLogisticoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public GastoLogisticoResponse editar(
            @PathVariable Integer id,
            @RequestBody GastoLogisticoRequest request
    ) {
        return service.editar(id, request);
    }

    @GetMapping("/ot/{idOts}")
    public List<GastoLogisticoResponse> listarPorOt(@PathVariable Integer idOts) {
        return service.listarPorOt(idOts);
    }
}

