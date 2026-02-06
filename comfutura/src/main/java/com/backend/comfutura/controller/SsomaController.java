package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.ssomaDTO.*;
import com.backend.comfutura.dto.response.ssomaDTO.*;
import com.backend.comfutura.service.SsomaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ssoma")
@RequiredArgsConstructor
public class SsomaController {

    private final SsomaService ssomaService;

    // =====================================================
    // ATS - Análisis de Trabajo Seguro
    // =====================================================

    @PostMapping("/ats")
    public ResponseEntity<AtsResponse> crearAts(@Valid @RequestBody AtsRequest request) {
        AtsResponse response = ssomaService.crearAts(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/ats/{id}")
    public ResponseEntity<AtsResponse> obtenerAts(@PathVariable Integer id) {
        AtsResponse response = ssomaService.obtenerAtsPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/ats")
    public ResponseEntity<List<AtsResponse>> listarAts() {
        List<AtsResponse> response = ssomaService.listarTodosAts();
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // CAPACITACIÓN - Charla de 5 minutos
    // =====================================================

    @PostMapping("/capacitacion")
    public ResponseEntity<CapacitacionResponse> crearCapacitacion(@Valid @RequestBody CapacitacionRequest request) {
        CapacitacionResponse response = ssomaService.crearCapacitacion(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/capacitacion/{id}")
    public ResponseEntity<CapacitacionResponse> obtenerCapacitacion(@PathVariable Integer id) {
        CapacitacionResponse response = ssomaService.obtenerCapacitacionPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/capacitacion")
    public ResponseEntity<List<CapacitacionResponse>> listarCapacitaciones() {
        List<CapacitacionResponse> response = ssomaService.listarTodasCapacitaciones();
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // INSPECCIÓN EPP
    // =====================================================

    @PostMapping("/inspeccion-epp")
    public ResponseEntity<InspeccionEppResponse> crearInspeccionEpp(@Valid @RequestBody InspeccionEppRequest request) {
        InspeccionEppResponse response = ssomaService.crearInspeccionEpp(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/inspeccion-epp/{id}")
    public ResponseEntity<InspeccionEppResponse> obtenerInspeccionEpp(@PathVariable Integer id) {
        InspeccionEppResponse response = ssomaService.obtenerInspeccionEppPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inspeccion-epp")
    public ResponseEntity<List<InspeccionEppResponse>> listarInspeccionesEpp() {
        List<InspeccionEppResponse> response = ssomaService.listarTodasInspeccionesEpp();
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // INSPECCIÓN HERRAMIENTAS
    // =====================================================

    @PostMapping("/inspeccion-herramienta")
    public ResponseEntity<InspeccionHerramientaResponse> crearInspeccionHerramienta(@Valid @RequestBody InspeccionHerramientaRequest request) {
        InspeccionHerramientaResponse response = ssomaService.crearInspeccionHerramienta(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/inspeccion-herramienta/{id}")
    public ResponseEntity<InspeccionHerramientaResponse> obtenerInspeccionHerramienta(@PathVariable Integer id) {
        InspeccionHerramientaResponse response = ssomaService.obtenerInspeccionHerramientaPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inspeccion-herramienta")
    public ResponseEntity<List<InspeccionHerramientaResponse>> listarInspeccionesHerramientas() {
        List<InspeccionHerramientaResponse> response = ssomaService.listarTodasInspeccionesHerramientas();
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // PETAR - Permisos para Trabajos de Alto Riesgo
    // =====================================================

    @PostMapping("/petar")
    public ResponseEntity<PetarResponse> crearPetar(@Valid @RequestBody PetarRequest request) {
        PetarResponse response = ssomaService.crearPetar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/petar/{id}")
    public ResponseEntity<PetarResponse> obtenerPetar(@PathVariable Integer id) {
        PetarResponse response = ssomaService.obtenerPetarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/petar")
    public ResponseEntity<List<PetarResponse>> listarPetar() {
        List<PetarResponse> response = ssomaService.listarTodosPetar();
        return ResponseEntity.ok(response);
    }

    // =====================================================
    // ENDPOINTS COMBINADOS (Opcionales)
    // =====================================================

    @PostMapping("/completo")
    public ResponseEntity<SsomaCompletoResponse> crearSsomaCompleto(@Valid @RequestBody SsomaCompletoRequest request) {
        try {
            SsomaCompletoResponse response = ssomaService.crearSsomaCompleto(request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al crear SSOMA completo", e.getMessage()));
        }
    }

    // =====================================================
    // 2. OBTENER TODO EL SSOMA POR ID DE OT
    // =====================================================

    @GetMapping("/por-ots/{idOts}")
    public ResponseEntity<?> obtenerSsomaPorOts(@PathVariable Integer idOts) {
        try {
            if (idOts == null || idOts <= 0) {
                return ResponseEntity.badRequest()
                        .body(crearErrorResponse("ID inválido", "El ID de OT debe ser un número positivo"));
            }

            SsomaCompletoResponse response = ssomaService.obtenerSsomaCompletoPorOts(idOts);

            if (response == null ||
                    (response.getAts() == null &&
                            response.getCapacitacion() == null &&
                            response.getInspeccionEpp() == null &&
                            response.getInspeccionHerramienta() == null &&
                            response.getPetar() == null)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(crearErrorResponse("No encontrado", "No se encontró SSOMA para la OT ID: " + idOts));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearErrorResponse("Error al obtener SSOMA", e.getMessage()));
        }
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    private SsomaCompletoResponse crearErrorResponse(String mensaje, String error) {
        SsomaCompletoResponse response = new SsomaCompletoResponse();
        response.setMensaje(mensaje + ": " + error);
        return response;
    }

}