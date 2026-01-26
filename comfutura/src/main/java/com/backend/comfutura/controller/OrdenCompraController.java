
package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import com.backend.comfutura.service.OcDetalleService;
import com.backend.comfutura.service.OrdenCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final OcDetalleService ocDetalleService;

    /* ================= CREAR OC ================= */
    @PostMapping
    public ResponseEntity<OrdenCompraResponseDTO> crear(
            @RequestBody OrdenCompraRequestDTO dto
    ) {
        OrdenCompraResponseDTO oc = ordenCompraService.guardar(null, dto);

        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            ocDetalleService.guardarDetalles(oc.getIdOc(), dto.getDetalles());
        }

        return ResponseEntity.ok(oc);
    }

    /* ================= EDITAR OC ================= */
    @PutMapping("/{idOc}")
    public ResponseEntity<OrdenCompraResponseDTO> editar(
            @PathVariable Integer idOc,
            @RequestBody OrdenCompraRequestDTO dto
    ) {
        OrdenCompraResponseDTO oc = ordenCompraService.guardar(idOc, dto);

        if (dto.getDetalles() != null) {
            ocDetalleService.guardarDetalles(idOc, dto.getDetalles());
        }

        return ResponseEntity.ok(oc);
    }

    /* ================= LISTADO PAGINADO ================= */
    @GetMapping
    public ResponseEntity<Page<OrdenCompraResponseDTO>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "idOc") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction
    ) {
        return ResponseEntity.ok(
                ordenCompraService.listarPaginado(page, size, sortBy, direction)
        );
    }
}
