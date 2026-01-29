package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.CronogramaRequest;
import com.backend.comfutura.dto.response.CronogramaResponse;
import com.backend.comfutura.service.CronogramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cronograma")
@RequiredArgsConstructor
public class CronogramaController {

    private final CronogramaService cronogramaService;

    // ➕ crear
    @PostMapping
    public CronogramaResponse crear(@RequestBody CronogramaRequest request) {
        return cronogramaService.crear(request);
    }

    // ✏️ editar
    @PutMapping("/{idCronograma}")
    public CronogramaResponse editar(
            @PathVariable Integer idCronograma,
            @RequestBody CronogramaRequest request
    ) {
        return cronogramaService.editar(idCronograma, request);
    }

    // 📄 listar por OT
    @GetMapping("/ot/{idOts}")
    public List<CronogramaResponse> listarPorOt(@PathVariable Integer idOts) {
        return cronogramaService.listarPorOt(idOts);
    }
}
