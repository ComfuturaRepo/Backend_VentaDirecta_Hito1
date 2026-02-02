package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.ImportResultDTO;
import com.backend.comfutura.service.ExcelExportService;
import com.backend.comfutura.service.ExcelImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/api/excel")
@RequiredArgsConstructor
@Tag(name = "Importación/Exportación Excel", description = "Endpoints para importar y exportar datos desde/hacia Excel")
public class ExcelController {

    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;

    // ==================== EXPORTACIÓN ====================
    @PostMapping("/export/ots")
    @Operation(summary = "Exportar OTs seleccionadas", description = "Exporta las OTs especificadas por sus IDs a un archivo Excel")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Exportación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<byte[]> exportOts(@RequestBody List<Integer> otIds) {
        try {
            byte[] excelBytes = excelExportService.exportOtsToExcel(otIds);
            return createExcelResponse(excelBytes, "ots_export_" + getTimestamp() + ".xlsx");
        } catch (IOException e) {
            log.error("Error al exportar OTs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al exportar OTs: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            log.error("Error inesperado al exportar OTs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/export/all")
    @Operation(summary = "Exportar todas las OTs", description = "Exporta todas las OTs del sistema a un archivo Excel")
    public ResponseEntity<byte[]> exportAllOts() {
        try {
            byte[] excelBytes = excelExportService.exportAllOtsToExcel();
            return createExcelResponse(excelBytes, "todas_ots_" + getTimestamp() + ".xlsx");
        } catch (IOException e) {
            log.error("Error al exportar todas las OTs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al exportar todas las OTs: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            log.error("Error inesperado al exportar todas las OTs: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error inesperado: " + e.getMessage()).getBytes());
        }
    }

    // ==================== IMPORTACIÓN ====================
    @GetMapping("/import/template")
    @Operation(summary = "Descargar plantilla de importación",
            description = "Descarga una plantilla Excel con validaciones y dropdowns para importar OTs")
    public ResponseEntity<byte[]> downloadTemplate() {
        try {
            log.info("Iniciando generación de plantilla de importación");
            byte[] template = excelImportService.generateImportTemplate();

            String filename = "plantilla_importacion_ots_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";

            log.info("Plantilla generada exitosamente: {} bytes", template.length);
            return createExcelResponse(template, filename);

        } catch (Exception e) {
            log.error("Error al generar plantilla: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar plantilla: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/import/template-simple")
    @Operation(summary = "Descargar plantilla básica",
            description = "Descarga una plantilla Excel sin datos de referencia")
    public ResponseEntity<byte[]> downloadTemplateSimple() {
        try {
            // Misma plantilla pero con nombre diferente
            byte[] template = excelImportService.generateImportTemplate();
            return createExcelResponse(template, "plantilla_ots_basica.xlsx");
        } catch (Exception e) {
            log.error("Error al generar plantilla básica: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar plantilla básica: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/import/modelo")
    @Operation(summary = "Descargar modelo de datos",
            description = "Descarga un archivo con solo los datos de referencia para los dropdowns")
    public ResponseEntity<byte[]> downloadModeloDatos() {
        try {
            // Podrías crear un método específico para solo los datos de combos
            byte[] template = excelImportService.generateImportTemplate();
            return createExcelResponse(template, "modelo_datos_combos.xlsx");
        } catch (Exception e) {
            log.error("Error al generar modelo de datos: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error al generar modelo de datos: " + e.getMessage()).getBytes());
        }
    }

    @PostMapping("/import/ots")
    @Operation(summary = "Importar OTs desde Excel",
            description = "Importa OTs desde un archivo Excel con validaciones completas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Importación exitosa o parcial"),
            @ApiResponse(responseCode = "400", description = "Archivo inválido o errores de validación"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ImportResultDTO> importOts(
            @Parameter(description = "Archivo Excel a importar", required = true)
            @RequestParam("file") MultipartFile file) {

        long startTime = System.currentTimeMillis();
        log.info("=== INICIANDO IMPORTACIÓN DE OTs ===");
        log.info("Archivo: {}, Tamaño: {} bytes, Tipo: {}",
                file.getOriginalFilename(), file.getSize(), file.getContentType());

        try {
            // ============ VALIDACIONES INICIALES ============
            if (file == null || file.isEmpty()) {
                log.warn("Archivo vacío o nulo");
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo está vacío o no se proporcionó", startTime)
                );
            }

            String filename = file.getOriginalFilename();
            if (filename == null) {
                log.warn("Nombre de archivo nulo");
                return ResponseEntity.badRequest().body(
                        crearResultadoError("Nombre de archivo inválido", startTime)
                );
            }

            // Validar extensión
            String filenameLower = filename.toLowerCase();
            if (!filenameLower.endsWith(".xlsx") && !filenameLower.endsWith(".xls")) {
                log.warn("Extensión no permitida: {}", filename);
                return ResponseEntity.badRequest().body(
                        crearResultadoError("Solo se permiten archivos Excel (.xlsx, .xls)", startTime)
                );
            }

            // Validar tamaño máximo (20MB)
            if (file.getSize() > 20 * 1024 * 1024) {
                log.warn("Archivo excede tamaño máximo: {} bytes", file.getSize());
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo excede el tamaño máximo de 20MB", startTime)
                );
            }

            // ============ PROCESAR IMPORTACIÓN ============
            ImportResultDTO result = excelImportService.importOtsFromExcel(file);

            long endTime = System.currentTimeMillis();
            result.setDuracionMs(endTime - startTime);

            log.info("=== IMPORTACIÓN FINALIZADA ===");
            log.info("Resultado: {} exitosas, {} fallidas, Tiempo: {} ms",
                    result.getExitosos(), result.getFallidos(), result.getDuracionMs());

            if (!result.getRegistrosConError().isEmpty()) {
                log.warn("Errores encontrados: {} registros con error", result.getRegistrosConError().size());
                result.getRegistrosConError().forEach(error ->
                        log.debug("Fila {}: {}", error.getFilaExcel(), error.getMensajeError()));
            }

            return ResponseEntity.ok(result);

        } catch (IOException e) {
            log.error("Error de E/S en importación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearResultadoError("Error de E/S: " + e.getMessage(), startTime));
        } catch (Exception e) {
            log.error("Error inesperado en importación: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearResultadoError("Error inesperado: " + e.getMessage(), startTime));
        }
    }

    @PostMapping("/import/masivo")
    @Operation(summary = "Importación masiva de OTs",
            description = "Importa gran cantidad de OTs con procesamiento optimizado")
    public ResponseEntity<ImportResultDTO> importMasivo(
            @Parameter(description = "Archivo Excel para importación masiva", required = true)
            @RequestParam("file") MultipartFile file) {

        long startTime = System.currentTimeMillis();
        log.info("=== INICIANDO IMPORTACIÓN MASIVA ===");

        try {
            // ============ VALIDACIONES ============
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo está vacío o no se proporcionó", startTime)
                );
            }

            String filename = file.getOriginalFilename();
            if (filename == null || !filename.toLowerCase().endsWith(".xlsx")) {
                return ResponseEntity.badRequest().body(
                        crearResultadoError("Solo se permiten archivos .xlsx para importación masiva", startTime)
                );
            }

            if (file.getSize() > 50 * 1024 * 1024) { // 50MB límite para masivo
                return ResponseEntity.badRequest().body(
                        crearResultadoError("El archivo excede el tamaño máximo de 50MB para importación masiva", startTime)
                );
            }

            // ============ PROCESAR IMPORTACIÓN MASIVA ============
            ImportResultDTO result = excelImportService.importOtsFromExcel(file);

            long endTime = System.currentTimeMillis();
            result.setDuracionMs(endTime - startTime);

            // Modificar mensaje para indicar que fue procesamiento masivo
            String mensajeOriginal = result.getMensaje() != null ? result.getMensaje() : "";
            result.setMensaje("IMPORTACIÓN MASIVA COMPLETADA - " + mensajeOriginal);

            log.info("=== IMPORTACIÓN MASIVA FINALIZADA ===");
            log.info("Resultado: {} registros procesados, {} exitosos, {} fallidos, Tiempo: {} ms",
                    result.getTotalRegistros(), result.getExitosos(), result.getFallidos(), result.getDuracionMs());

            if (result.getFallidos() > 0) {
                log.warn("Se encontraron {} errores en importación masiva", result.getFallidos());
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            log.error("Error en importación masiva: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(crearResultadoError("Error en importación masiva: " + e.getMessage(), startTime));
        }
    }

    @GetMapping("/import/instrucciones")
    @Operation(summary = "Obtener instrucciones de importación",
            description = "Devuelve un archivo PDF/Texto con instrucciones detalladas para la importación")
    public ResponseEntity<byte[]> getInstrucciones() {
        try {
            String instrucciones = crearContenidoInstrucciones();
            byte[] contenido = instrucciones.getBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentDispositionFormData("attachment", "instrucciones_importacion_ots.txt");
            headers.setContentLength(contenido.length);

            return new ResponseEntity<>(contenido, headers, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Error generando instrucciones: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // ==================== MÉTODOS AUXILIARES ====================
    private ResponseEntity<byte[]> createExcelResponse(byte[] excelBytes, String filename) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", filename);
            headers.setContentLength(excelBytes.length);

            // Headers para mejor compatibilidad y cache
            headers.setCacheControl("no-cache, no-store, must-revalidate");
            headers.setPragma("no-cache");
            headers.setExpires(0);

            // Headers adicionales de seguridad
            headers.set("X-Content-Type-Options", "nosniff");
            headers.set("X-Frame-Options", "DENY");

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al crear respuesta Excel: {}", e.getMessage());
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

    private ImportResultDTO crearResultadoError(String mensaje, long startTime) {
        ImportResultDTO resultado = new ImportResultDTO();
        resultado.setExito(false);
        resultado.setMensaje(mensaje);
        resultado.setInicio(startTime);
        resultado.setFin(System.currentTimeMillis());
        resultado.setDuracionMs(resultado.getFin() - resultado.getInicio());
        resultado.setTotalRegistros(0);
        resultado.setExitosos(0);
        resultado.setFallidos(0);
        resultado.setErroresValidacion(0);
        resultado.setErroresPersistencia(0);
        resultado.setWarnings(0);
        return resultado;
    }

    private String crearContenidoInstrucciones() {
        return """
                ==========================================
                INSTRUCCIONES PARA IMPORTACIÓN DE OTs
                ==========================================
                
                1. FORMATO DEL ARCHIVO
                   - Solo archivos Excel (.xlsx, .xls)
                   - Tamaño máximo: 20MB (normal) / 50MB (masivo)
                   - Use la plantilla oficial para evitar errores
                
                2. COLUMNAS OBLIGATORIAS
                   - fechaApertura (Formato: dd/mm/aaaa)
                   - cliente (Seleccionar de lista)
                   - area (Seleccionar de lista)
                   - proyecto (Seleccionar de lista)
                   - fase (Seleccionar de lista)
                   - site (Seleccionar de lista)
                   - region (Seleccionar de lista)
                   - tipoOt (Nuevo - Seleccionar de lista)
                   - estado (Siempre: ASIGNACION)
                
                3. COLUMNAS CONDICIONALES
                   - otAnterior: Obligatorio si fecha es del año anterior
                   - Límite: Máximo 2,147,483,647
                
                4. RESPONSABLES (Obligatorios)
                   - CoordinadorTiCw
                   - JefaturaResponsable
                   - Liquidador
                   - Ejecutante
                   - AnalistaContable
                
                5. VALIDACIONES
                   - Fecha no puede ser futura
                   - Todos los valores deben existir en sistema
                   - Máximo 1,000 registros por importación
                
                6. RECOMENDACIONES
                   - Descargue la plantilla con datos de referencia
                   - Verifique los valores en hoja '📋 DATOS_COMBOS'
                   - Revise errores detallados en respuesta
                
                7. CONTACTO
                   - Soporte Técnico: soporte@comfutura.com
                   - Teléfono: +51 123 456 789
                
                ==========================================
                Última actualización: %s
                ==========================================
                """.formatted(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
    }

    @GetMapping("/test")
    @Operation(summary = "Test de conexión", description = "Verifica que el controlador esté funcionando")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Excel Controller funcionando correctamente - " + LocalDateTime.now());
    }

    @GetMapping("/status")
    @Operation(summary = "Estado del servicio", description = "Verifica el estado de los servicios de importación/exportación")
    public ResponseEntity<String> checkStatus() {
        try {
            String status = String.format("""
                    ExcelController Status Report
                    =============================
                    Timestamp: %s
                    ExportService: %s
                    ImportService: %s
                    Estado: OPERATIVO
                    """,
                    LocalDateTime.now(),
                    excelExportService != null ? "ACTIVO" : "INACTIVO",
                    excelImportService != null ? "ACTIVO" : "INACTIVO");

            return ResponseEntity.ok(status);
        } catch (Exception e) {
            log.error("Error en checkStatus: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error en checkStatus: " + e.getMessage());
        }
    }

    @GetMapping("/import/limites")
    @Operation(summary = "Obtener límites de importación", description = "Devuelve los límites y restricciones de importación")
    public ResponseEntity<String> getLimitesImportacion() {
        String limites = """
                LÍMITES DE IMPORTACIÓN
                ======================
                
                1. TAMAÑO DE ARCHIVO
                   - Normal: 20 MB máximo
                   - Masivo: 50 MB máximo
                
                2. REGISTROS
                   - Máximo por importación: 1,000 registros
                   - Si necesita más, use importación masiva
                
                3. VALORES NUMÉRICOS
                   - OT anterior: 1 - 2,147,483,647
                   - Solo números enteros
                
                4. FECHAS
                   - No pueden ser futuras
                   - No anteriores a 5 años
                   - Formato: dd/mm/aaaa
                
                5. DROPDOWNS
                   - Todos los valores deben existir en sistema
                   - Use los valores de la hoja '📋 DATOS_COMBOS'
                
                6. VALIDACIONES ESPECIALES
                   - OT anterior obligatoria si fecha es del año anterior
                   - Estado siempre debe ser 'ASIGNACION'
                   - Todos los responsables deben existir
                """;

        return ResponseEntity.ok(limites);
    }
}