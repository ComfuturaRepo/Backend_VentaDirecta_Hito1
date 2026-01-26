package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.OcDetalleRequestDTO;
import com.backend.comfutura.dto.response.OcDetalleResponseDTO;
import com.backend.comfutura.service.OcDetalleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/oc-detalles")
@RequiredArgsConstructor
public class OcDetalleController {

    private final OcDetalleService ocDetalleService;

    /* ==================================================
       CREAR DETALLE
       ================================================== */
    @PostMapping("/orden-compra/{idOc}")
    public ResponseEntity<OcDetalleResponseDTO> crear(
            @PathVariable Integer idOc,
            @RequestBody OcDetalleRequestDTO dto
    ) {
        return ResponseEntity.ok(
                ocDetalleService.guardar(idOc, null, dto)
        );
    }

    /* ==================================================
       EDITAR DETALLE (PATCH)
       ================================================== */
    @PatchMapping("/{idDetalle}")
    public ResponseEntity<OcDetalleResponseDTO> editar(
            @PathVariable Integer idDetalle,
            @RequestBody OcDetalleRequestDTO dto
    ) {
        return ResponseEntity.ok(
                ocDetalleService.guardar(null, idDetalle, dto)
        );
    }

    /* ==================================================
       LISTAR DETALLES POR OC (PAGINADO)
       ================================================== */
    @GetMapping("/orden-compra/{idOc}")
    public ResponseEntity<Page<OcDetalleResponseDTO>> listarPorOc(
            @PathVariable Integer idOc,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ocDetalleService.listarPorOc(idOc, page, size)
        );
    }

    /* ==================================================
       GUARDAR VARIOS DETALLES (BULK)
       ================================================== */
    @PutMapping("/orden-compra/{idOc}")
    public ResponseEntity<Void> guardarVarios(
            @PathVariable Integer idOc,
            @RequestBody List<OcDetalleRequestDTO> detalles
    ) {
        ocDetalleService.guardarDetalles(idOc, detalles);
        return ResponseEntity.ok().build();
    }
}

