package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.ViaticoRequest;
import com.backend.comfutura.dto.response.ViaticoResponse;
import com.backend.comfutura.service.ViaticoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/viaticos")
@RequiredArgsConstructor
public class ViaticoController {

    private final ViaticoService viaticoService;

    @PostMapping
    public ViaticoResponse crear(@RequestBody ViaticoRequest request) {
        return viaticoService.crear(request);
    }

    @PutMapping("/{id}")
    public ViaticoResponse editar(
            @PathVariable Integer id,
            @RequestBody ViaticoRequest request
    ) {
        return viaticoService.editar(id, request);
    }

    @GetMapping("/ot/{idOts}")
    public List<ViaticoResponse> listarPorOt(@PathVariable Integer idOts) {
        return viaticoService.listarPorOt(idOts);
    }
}
