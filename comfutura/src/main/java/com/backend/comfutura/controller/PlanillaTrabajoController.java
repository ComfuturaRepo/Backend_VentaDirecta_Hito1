package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.PlanillaTrabajoRequest;
import com.backend.comfutura.dto.response.PlanillaTrabajoResponse;
import com.backend.comfutura.service.PlanillaTrabajoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/planilla")
@RequiredArgsConstructor
public class PlanillaTrabajoController {

    private final PlanillaTrabajoService planillaService;

    @PostMapping
    public PlanillaTrabajoResponse crear(@RequestBody PlanillaTrabajoRequest request) {
        return planillaService.crear(request);
    }

    @GetMapping("/ot/{idOts}")
    public List<PlanillaTrabajoResponse> listar(@PathVariable Integer idOts) {
        return planillaService.listarPorOt(idOts);
    }
}

