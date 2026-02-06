package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.MaestroCodigoComboDTO;
import com.backend.comfutura.service.MaestroCodigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/maestro-codigo")
@RequiredArgsConstructor
public class MaestroCodigoController {

    private final MaestroCodigoService service;

    @GetMapping("/combo")
    public List<MaestroCodigoComboDTO> listarParaCombo() {
        return service.listarParaCombo();
    }
}

