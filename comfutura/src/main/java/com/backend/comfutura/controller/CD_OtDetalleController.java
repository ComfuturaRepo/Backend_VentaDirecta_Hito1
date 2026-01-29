package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.CD_OtDetalleResponse;
import com.backend.comfutura.service.CD_OtDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compra-directa")
@RequiredArgsConstructor
public class CD_OtDetalleController {

    private final CD_OtDetalleService detalleService;

    @GetMapping("/ot/{idOts}")
    public CD_OtDetalleResponse obtenerDetalle(@PathVariable Integer idOts) {
        return detalleService.obtenerDetalleOt(idOts);
    }
}
