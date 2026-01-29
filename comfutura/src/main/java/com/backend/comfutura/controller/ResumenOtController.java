package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.ResumenOtResponse;
import com.backend.comfutura.service.ResumenOtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resumen")
@RequiredArgsConstructor
public class ResumenOtController {

    private final ResumenOtService resumenService;

    @PostMapping("/recalcular/{idOts}")
    public void recalcular(@PathVariable Integer idOts) {
        resumenService.recalcularResumen(idOts);
    }

    @GetMapping("/ot/{idOts}")
    public List<ResumenOtResponse> listar(@PathVariable Integer idOts) {
        return resumenService.listarResumen(idOts);
    }
}
