package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.ImportResultDTO;
import com.backend.comfutura.service.ExcelExportService;
import com.backend.comfutura.service.ExcelImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;

    // ==================== EXPORTACIÓN ====================
    @PostMapping("/export/ots")
    public ResponseEntity<byte[]> exportOts(@RequestBody List<Integer> otIds) {
        try {
            byte[] excelBytes = excelExportService.exportOtsToExcel(otIds);
            return createExcelResponse(excelBytes, "ots_export_" + getTimestamp() + ".xlsx");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al exportar OTs: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/export/all")
    public ResponseEntity<byte[]> exportAllOts() {
        try {
            byte[] excelBytes = excelExportService.exportAllOtsToExcel();
            return createExcelResponse(excelBytes, "todas_ots_" + getTimestamp() + ".xlsx");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al exportar todas las OTs: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }

    // ==================== IMPORTACIÓN ====================
    @GetMapping("/import/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            byte[] template = excelImportService.generateImportTemplate();
            return createExcelResponse(template,
                    "plantilla_importacion_ots_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".xlsx");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar plantilla: " + e.getMessage()).getBytes());
        }
    }

    @PostMapping("/import/ots")
    public ResponseEntity<ImportResultDTO> importOts(@RequestParam("file") MultipartFile file) {
        try {
            // Validaciones básicas
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo está vacío o no se proporcionó")
                );
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body(
                        crearResultadoError("Solo se permiten archivos .xlsx")
                );
            }

            if (file.getSize() > 10 * 1024 * 1024) { // 10MB límite
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo excede el tamaño máximo de 10MB")
                );
            }

            ImportResultDTO result = excelImportService.importOtsFromExcel(file);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearResultadoError("Error en importación: " + e.getMessage()));
        }
    }

    @PostMapping("/import/masivo")
    public ResponseEntity<ImportResultDTO> importMasivo(@RequestParam("file") MultipartFile file) {
        try {
            // Validaciones básicas
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo está vacío o no se proporcionó")
                );
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body(
                        crearResultadoError("Solo se permiten archivos .xlsx")
                );
            }

            if (file.getSize() > 10 * 1024 * 1024) { // 10MB límite
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo excede el tamaño máximo de 10MB")
                );
            }

            // Usar el mismo método de importación normal para masivo
            // O si quieres diferenciar la lógica, puedes modificar el mensaje
            ImportResultDTO result = excelImportService.importOtsFromExcel(file);

            // Modificar mensaje para indicar que fue procesamiento masivo
            if (result.getMensaje() != null) {
                result.setMensaje("IMPORTACIÓN MASIVA: " + result.getMensaje());
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearResultadoError("Error en importación masiva: " + e.getMessage()));
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private ResponseEntity<byte[]> createExcelResponse(byte[] excelBytes, String filename) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(excelBytes.length);

            // Headers adicionales para mejor compatibilidad
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al crear respuesta: " + e.getMessage()).getBytes());
        }
    }

    private String getTimestamp() {
        try {
            return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        } catch (Exception e) {
            return LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        }
    }

    private ImportResultDTO crearResultadoError(String mensaje) {
        ImportResultDTO resultado = new ImportResultDTO();
        resultado.setExito(false);
        resultado.setMensaje(mensaje);
        resultado.setInicio(System.currentTimeMillis());
        resultado.setFin(System.currentTimeMillis());
        resultado.setDuracionMs(0L);
        resultado.setTotalRegistros(0);
        resultado.setExitosos(0);
        resultado.setFallidos(0);
        return resultado;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Excel Controller funcionando correctamente - " + LocalDateTime.now());
    }

    // Endpoint adicional para verificar disponibilidad de servicios
    @GetMapping("/status")
    public ResponseEntity<String> checkStatus() {
        try {
            return ResponseEntity.ok("ExcelController está operativo. Servicios: "
                    + (excelExportService != null ? "ExportService OK, " : "ExportService NULL, ")
                    + (excelImportService != null ? "ImportService OK" : "ImportService NULL"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en checkStatus: " + e.getMessage());
        }
    }
}