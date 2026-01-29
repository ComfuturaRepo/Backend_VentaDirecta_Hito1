package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.DashboardDTO.*;
import com.backend.comfutura.service.DashboardOTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard OT", description = "API para estadísticas y dashboard de OTs")
@Slf4j
public class DashboardOTController {

    private final DashboardOTService dashboardOTService;

    // ===========================================
    // ENDPOINTS PRINCIPALES
    // ===========================================

    @Operation(summary = "Obtener dashboard completo con filtros")
    @PostMapping("/ots")
    public ResponseEntity<DashboardOTDTO> getDashboard(
            @Parameter(description = "Filtros para el dashboard")
            @RequestBody(required = false) @Valid FiltroDashboardDTO filtro) {

        try {
            log.info("Solicitando dashboard completo");
            if (filtro == null) {
                filtro = new FiltroDashboardDTO();
                log.info("Filtro nulo, usando valores por defecto");
            }

            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);
            log.info("Dashboard generado exitosamente");
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener dashboard por cliente específico")
    @GetMapping("/ots/cliente/{clienteId}")
    public ResponseEntity<DashboardOTDTO> getDashboardByCliente(
            @Parameter(description = "ID del cliente", required = true)
            @PathVariable Integer clienteId) {

        try {
            log.info("Solicitando dashboard para cliente ID: {}", clienteId);
            DashboardOTDTO dashboard = dashboardOTService.getDashboardDataByCliente(clienteId);
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard por cliente {}: {}", clienteId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener dashboard por área específica")
    @GetMapping("/ots/area/{areaId}")
    public ResponseEntity<DashboardOTDTO> getDashboardByArea(
            @Parameter(description = "ID del área", required = true)
            @PathVariable Integer areaId) {

        try {
            log.info("Solicitando dashboard para área ID: {}", areaId);
            DashboardOTDTO dashboard = dashboardOTService.getDashboardDataByArea(areaId);
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard por área {}: {}", areaId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener dashboard por proyecto específico")
    @GetMapping("/ots/proyecto/{proyectoId}")
    public ResponseEntity<DashboardOTDTO> getDashboardByProyecto(
            @Parameter(description = "ID del proyecto", required = true)
            @PathVariable Integer proyectoId) {

        try {
            log.info("Solicitando dashboard para proyecto ID: {}", proyectoId);
            DashboardOTDTO dashboard = dashboardOTService.getDashboardDataByProyecto(proyectoId);
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard por proyecto {}: {}", proyectoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener dashboard por estado específico")
    @GetMapping("/ots/estado/{estadoId}")
    public ResponseEntity<DashboardOTDTO> getDashboardByEstado(
            @Parameter(description = "ID del estado de OT", required = true)
            @PathVariable Integer estadoId) {

        try {
            log.info("Solicitando dashboard para estado ID: {}", estadoId);
            DashboardOTDTO dashboard = dashboardOTService.getDashboardDataByEstado(estadoId);
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard por estado {}: {}", estadoId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===========================================
    // ENDPOINTS POR FECHAS
    // ===========================================

    @Operation(summary = "Obtener dashboard por rango de fechas personalizado")
    @GetMapping("/ots/fecha")
    public ResponseEntity<DashboardOTDTO> getDashboardByFecha(
            @Parameter(description = "Fecha de inicio (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,

            @Parameter(description = "Fecha de fin (YYYY-MM-DD)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        try {
            log.info("Solicitando dashboard por fechas - Inicio: {}, Fin: {}", fechaInicio, fechaFin);
            FiltroDashboardDTO filtro = new FiltroDashboardDTO(fechaInicio, fechaFin);
            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard por fechas: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener dashboard por rango de tiempo predefinido")
    @GetMapping("/ots/rango/{rango}")
    public ResponseEntity<DashboardOTDTO> getDashboardByRango(
            @Parameter(description = "Rango de tiempo: HOY, SEMANA, MES, TRIMESTRE, SEMESTRE, ANIO", required = true)
            @PathVariable String rango) {

        try {
            log.info("Solicitando dashboard por rango: {}", rango);
            DashboardOTDTO dashboard = dashboardOTService.getDashboardDataByFecha(rango);
            return ResponseEntity.ok(dashboard);

        } catch (Exception e) {
            log.error("Error generando dashboard por rango {}: {}", rango, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===========================================
    // ENDPOINTS ESPECÍFICOS PARA COMPONENTES
    // ===========================================

    @Operation(summary = "Obtener solo las estadísticas generales")
    @GetMapping("/ots/estadisticas-generales")
    public ResponseEntity<DashboardOTDTO> getEstadisticasGenerales() {
        try {
            log.info("Solicitando solo estadísticas generales");
            FiltroDashboardDTO filtro = new FiltroDashboardDTO();
            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);

            // Crear respuesta con solo estadísticas generales
            DashboardOTDTO respuesta = new DashboardOTDTO();
            respuesta.setGenerales(dashboard.getGenerales());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            log.error("Error obteniendo estadísticas generales: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener solo las OTs pendientes")
    @GetMapping("/ots/pendientes")
    public ResponseEntity<DashboardOTDTO> getOTsPendientes() {
        try {
            log.info("Solicitando solo OTs pendientes");
            FiltroDashboardDTO filtro = new FiltroDashboardDTO();
            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);

            // Crear respuesta con solo OTs pendientes
            DashboardOTDTO respuesta = new DashboardOTDTO();
            respuesta.setOtsPendientes(dashboard.getOtsPendientes());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            log.error("Error obteniendo OTs pendientes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener solo el resumen de costos")
    @GetMapping("/ots/resumen-costos")
    public ResponseEntity<DashboardOTDTO> getResumenCostos() {
        try {
            log.info("Solicitando solo resumen de costos");
            FiltroDashboardDTO filtro = new FiltroDashboardDTO();
            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);

            // Crear respuesta con solo resumen de costos
            DashboardOTDTO respuesta = new DashboardOTDTO();
            respuesta.setResumenCostos(dashboard.getResumenCostos());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            log.error("Error obteniendo resumen de costos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===========================================
    // ENDPOINTS ADICIONALES ÚTILES
    // ===========================================

    @Operation(summary = "Obtener solo OTs por estado (para gráficos)")
    @GetMapping("/ots/por-estado")
    public ResponseEntity<DashboardOTDTO> getOTsPorEstado() {
        try {
            log.info("Solicitando OTs por estado");
            FiltroDashboardDTO filtro = new FiltroDashboardDTO();
            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);

            DashboardOTDTO respuesta = new DashboardOTDTO();
            respuesta.setOtsPorEstado(dashboard.getOtsPorEstado());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            log.error("Error obteniendo OTs por estado: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener solo OTs por cliente (para top 10)")
    @GetMapping("/ots/por-cliente")
    public ResponseEntity<DashboardOTDTO> getOTsPorCliente() {
        try {
            log.info("Solicitando OTs por cliente");
            FiltroDashboardDTO filtro = new FiltroDashboardDTO();
            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);

            DashboardOTDTO respuesta = new DashboardOTDTO();
            respuesta.setOtsPorCliente(dashboard.getOtsPorCliente());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            log.error("Error obteniendo OTs por cliente: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Obtener evolución mensual (para gráficos de línea)")
    @GetMapping("/ots/evolucion-mensual")
    public ResponseEntity<DashboardOTDTO> getEvolucionMensual() {
        try {
            log.info("Solicitando evolución mensual");
            FiltroDashboardDTO filtro = new FiltroDashboardDTO();
            filtro.setFechaInicio(LocalDate.now().minusMonths(6));

            DashboardOTDTO dashboard = dashboardOTService.getDashboardData(filtro);

            DashboardOTDTO respuesta = new DashboardOTDTO();
            respuesta.setEvolucionMensual(dashboard.getEvolucionMensual());
            return ResponseEntity.ok(respuesta);

        } catch (Exception e) {
            log.error("Error obteniendo evolución mensual: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ===========================================
    // ENDPOINT DE SALUD
    // ===========================================

    @Operation(summary = "Verificar salud del servicio de dashboard")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        log.info("Health check solicitado");
        return ResponseEntity.ok("Dashboard service is running - " + LocalDate.now());
    }
}