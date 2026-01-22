package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.OrdenCompraRequestDTO;
import com.backend.comfutura.dto.response.OrdenCompraResponseDTO;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.service.OrdenCompraService;
import com.backend.comfutura.repository.EstadoOcRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.repository.MaestroCodigoRepository;
import com.backend.comfutura.repository.ProveedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordenes-compra")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final EstadoOcRepository estadoOcRepository;
    private final OtsRepository otsRepository;
    private final MaestroCodigoRepository maestroRepository;
    private final ProveedorRepository proveedorRepository;

    // ===== Crear orden de compra =====
    @PostMapping
    public ResponseEntity<OrdenCompraResponseDTO> crear(@RequestBody OrdenCompraRequestDTO dto) {
        OrdenCompra oc = OrdenCompra.builder()
                .estadoOc(estadoOcRepository.findById(dto.getEstadoOcId()).orElseThrow(() -> new RuntimeException("Estado OC no encontrado")))
                .ots(otsRepository.findById(dto.getOtsId()).orElseThrow(() -> new RuntimeException("OTS no encontrado")))
                .maestro(maestroRepository.findById(dto.getMaestroId()).orElseThrow(() -> new RuntimeException("Maestro no encontrado")))
                .proveedor(proveedorRepository.findById(dto.getProveedorId()).orElseThrow(() -> new RuntimeException("Proveedor no encontrado")))
                .cantidad(dto.getCantidad())
                .costoUnitario(dto.getCostoUnitario())
                .observacion(dto.getObservacion())
                .build();

        OrdenCompra creada = ordenCompraService.crear(oc);
        return ResponseEntity.ok(mapToResponse(creada));
    }

    // ===== Actualizar orden de compra =====
    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> actualizar(@PathVariable Integer id,
                                                             @RequestBody OrdenCompraRequestDTO dto) {
        OrdenCompra oc = OrdenCompra.builder()
                .estadoOc(estadoOcRepository.findById(dto.getEstadoOcId()).orElseThrow(() -> new RuntimeException("Estado OC no encontrado")))
                .ots(otsRepository.findById(dto.getOtsId()).orElseThrow(() -> new RuntimeException("OTS no encontrado")))
                .maestro(maestroRepository.findById(dto.getMaestroId()).orElseThrow(() -> new RuntimeException("Maestro no encontrado")))
                .proveedor(proveedorRepository.findById(dto.getProveedorId()).orElseThrow(() -> new RuntimeException("Proveedor no encontrado")))
                .cantidad(dto.getCantidad())
                .costoUnitario(dto.getCostoUnitario())
                .observacion(dto.getObservacion())
                .build();

        OrdenCompra actualizada = ordenCompraService.actualizar(id, oc);
        return ResponseEntity.ok(mapToResponse(actualizada));
    }

    // ===== Obtener orden por ID =====
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ordenCompraService.obtenerPorId(id)
                .map(oc -> ResponseEntity.ok(mapToResponse(oc)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ===== Listar ordenes con paginación =====
    @GetMapping
    public ResponseEntity<Page<OrdenCompraResponseDTO>> listar(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<OrdenCompra> lista = ordenCompraService.listar(pageable);
        Page<OrdenCompraResponseDTO> dtoPage = lista.map(this::mapToResponse);
        return ResponseEntity.ok(dtoPage);
    }

    // ===== Eliminar orden de compra =====
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        ordenCompraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private OrdenCompraResponseDTO mapToResponse(OrdenCompra oc) {
        return OrdenCompraResponseDTO.builder()
                .idOc(oc.getIdOc())

                // Estado
                .estadoOcId(oc.getEstadoOc() != null ? oc.getEstadoOc().getIdEstadoOc() : null)
                .estadoOcNombre(oc.getEstadoOc() != null ? oc.getEstadoOc().getNombre() : null)

                // OTS
                .otsId(oc.getOts() != null ? oc.getOts().getIdOts() : null)
                .otsNombre(oc.getOts() != null
                        ? "OT: " + oc.getOts().getOt() + " - " + oc.getOts().getDescripcion()
                        : null)

                // Maestro / Material
                .maestroId(oc.getMaestro() != null ? oc.getMaestro().getId() : null)
                .maestroCodigo(oc.getMaestro() != null ? oc.getMaestro().getCodigo() : null)

                // Proveedor
                .proveedorId(oc.getProveedor() != null ? oc.getProveedor().getId() : null)
                .proveedorNombre(oc.getProveedor() != null ? oc.getProveedor().getRazonSocial() : null)

                .cantidad(oc.getCantidad())
                .costoUnitario(oc.getCostoUnitario())
                .fechaOc(oc.getFechaOc())
                .observacion(oc.getObservacion())
                .build();
    }

}
