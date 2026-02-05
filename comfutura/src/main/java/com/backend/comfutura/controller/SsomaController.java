package com.backend.comfutura.controller;
import com.backend.comfutura.dto.request.ssomaDTO.SsomaRequest;
import com.backend.comfutura.dto.response.ssomaDTO.SsomaResponse;
import com.backend.comfutura.service.SsomaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/ssoma")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SsomaController {

    private final SsomaService ssomaService;

    /**
     * Crear todas las 5 hojas del SSOMA en una sola transacción
     */
    @PostMapping("/crear-completo")
    public ResponseEntity<SsomaResponse> crearSsomaCompleto(@RequestBody SsomaRequest request) {
        try {
            SsomaResponse response = ssomaService.crearCompletoSsoma(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            SsomaResponse errorResponse = new SsomaResponse();
            errorResponse.setMensaje("Error: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    /**
     * Listar todos los ATS sin paginación
     */
    @GetMapping("/ats")
    public ResponseEntity<List<Object>> listarTodosAts() {
        try {
            List<Object> atsList = ssomaService.listarTodosAts();
            return ResponseEntity.ok(atsList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Listar todas las capacitaciones sin paginación
     */
    @GetMapping("/capacitaciones")
    public ResponseEntity<List<Object>> listarTodasCapacitaciones() {
        try {
            List<Object> capacitaciones = ssomaService.listarTodasCapacitaciones();
            return ResponseEntity.ok(capacitaciones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Listar todas las inspecciones EPP sin paginación
     */
    @GetMapping("/inspecciones-epp")
    public ResponseEntity<List<Object>> listarTodasInspeccionesEpp() {
        try {
            List<Object> inspecciones = ssomaService.listarTodasInspeccionesEpp();
            return ResponseEntity.ok(inspecciones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Listar todas las inspecciones de herramientas sin paginación
     */
    @GetMapping("/inspecciones-herramientas")
    public ResponseEntity<List<Object>> listarTodasInspeccionesHerramientas() {
        try {
            List<Object> inspecciones = ssomaService.listarTodasInspeccionesHerramientas();
            return ResponseEntity.ok(inspecciones);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Listar todos los PETAR sin paginación
     */
    @GetMapping("/petar")
    public ResponseEntity<List<Object>> listarTodosPetar() {
        try {
            List<Object> petarList = ssomaService.listarTodosPetar();
            return ResponseEntity.ok(petarList);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Obtener todo el SSOMA por fecha
     */
    @GetMapping("/por-fecha/{fecha}")
    public ResponseEntity<List<Object>> obtenerSsomaPorFecha(@PathVariable String fecha) {
        try {
            List<Object> resultados = ssomaService.obtenerSsomaPorFecha(fecha);
            return ResponseEntity.ok(resultados);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint de prueba
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("SSOMA Controller funcionando correctamente");
    }
}
