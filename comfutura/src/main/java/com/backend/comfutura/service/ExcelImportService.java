package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ExcelImportDTO;
import com.backend.comfutura.dto.request.ImportResultDTO;
import com.backend.comfutura.dto.request.otDTO.OtCreateRequest;
import com.backend.comfutura.model.Site;
import com.backend.comfutura.model.SiteDescripcion;
import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.repository.SiteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final OtService otService;
    private final DropdownService dropdownService;
    private final SiteRepository siteRepository;

    // Constantes para límites
    private static final int MAX_OT_ANTERIOR = 2147483647;
    private static final int MAX_ROWS_IMPORT = 1000;
    private static final int MAX_COLUMNAS_COMBOS = 5; // Máximo 5 columnas en hoja combos

    // ================ IMPORTACIÓN DE EXCEL ================
    @Transactional(rollbackFor = Exception.class)
    public ImportResultDTO importOtsFromExcel(MultipartFile file) throws IOException {
        ImportResultDTO result = new ImportResultDTO();
        result.setInicio(System.currentTimeMillis());

        log.info("=== INICIANDO IMPORTACIÓN DE EXCEL ===");
        log.info("Archivo: {}, Tamaño: {} bytes", file.getOriginalFilename(), file.getSize());

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getLastRowNum() < 0) {
                throw new IOException("El archivo Excel está vacío");
            }

            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndex = mapColumnHeaders(headerRow);

            verificarEncabezadosRequeridos(columnIndex);

            Integer ultimoOt = otService.getUltimoOtCorrelativo();
            int siguienteOt = (ultimoOt != null ? ultimoOt + 1 : 20250001);

            List<ExcelImportDTO> registros = new ArrayList<>();
            int filasProcesadas = 0;

            for (int i = 1; i <= sheet.getLastRowNum() && filasProcesadas < MAX_ROWS_IMPORT; i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                filasProcesadas++;

                ExcelImportDTO registro = parseRowToDTO(row, columnIndex, i + 1);
                validarRegistroCompleto(registro);

                if (registro.isValido()) {
                    registro.setOt(siguienteOt++);

                    try {
                        OtCreateRequest request = convertirARequest(registro);
                        String validacion = request.getValidacionOtAnterior();
                        if (validacion != null) {
                            registro.setValido(false);
                            registro.setMensajeError(validacion);
                            result.incrementarErroresValidacion();
                            result.agregarResumenError(validacion);
                        } else {
                            registro.setTempRequest(request);
                        }
                    } catch (Exception e) {
                        registro.setValido(false);
                        registro.setMensajeError("Error en validación: " + e.getMessage());
                        log.warn("Validación fallida fila {}: {}", i + 1, e.getMessage());
                        result.incrementarErroresValidacion();
                    }
                }
                registros.add(registro);
            }

            result.setTotalRegistros(registros.size());
            log.info("Total registros encontrados: {}", registros.size());

            List<ExcelImportDTO> exitosos = new ArrayList<>();
            List<ExcelImportDTO> errores = new ArrayList<>();

            // Procesar registros válidos
            for (ExcelImportDTO registro : registros) {
                if (registro.isValido() && registro.getTempRequest() != null) {
                    try {
                        otService.saveOt(registro.getTempRequest());
                        registro.setMensajeError("CREADA EXITOSAMENTE - OT: " + registro.getOt());
                        exitosos.add(registro);
                        result.incrementarExitosos();
                        log.info("✓ OT {} creada exitosamente", registro.getOt());
                    } catch (Exception e) {
                        log.error("✗ Error al guardar OT {}: {}", registro.getOt(), e.getMessage(), e);
                        registro.setValido(false);
                        registro.setMensajeError("Error al guardar OT " + registro.getOt() + ": " +
                                getErrorMessage(e));
                        errores.add(registro);
                        result.incrementarErroresPersistencia();
                        result.incrementarFallidos();
                    }
                } else {
                    errores.add(registro);
                    result.incrementarFallidos();
                }
            }

            result.setRegistrosProcesados(exitosos);
            result.setRegistrosConError(errores);
            result.setExito(!errores.isEmpty() ? false : exitosos.size() > 0);
            result.generarMensajeResumen();

            log.info("=== RESUMEN IMPORTACIÓN ===");
            log.info("Exitosos: {}, Fallidos: {}", result.getExitosos(), result.getFallidos());
            log.info("Errores validación: {}, Errores persistencia: {}",
                    result.getErroresValidacion(), result.getErroresPersistencia());

            if (exitosos.isEmpty() && !registros.isEmpty()) {
                log.warn("No se pudo crear ninguna OT. Verifique los datos.");
            }

        } catch (Exception e) {
            log.error("❌ ERROR CRÍTICO al procesar archivo Excel", e);
            throw new IOException("Error al procesar archivo Excel: " + getErrorMessage(e), e);
        } finally {
            result.setFin(System.currentTimeMillis());
            result.setDuracionMs(result.getFin() - result.getInicio());
            log.info("Tiempo total de procesamiento: {} ms", result.getDuracionMs());
        }

        return result;
    }

    // ================ GENERACIÓN DE TEMPLATE MEJORADO ================
    public byte[] generateImportTemplate() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // === HOJA PRINCIPAL: PLANTILLA IMPORTACIÓN ===
            Sheet sheet = workbook.createSheet("📋 PLANTILLA OT");

            // === HOJA VISIBLE: DATOS DE COMBOS (ORGANIZADA POR COLUMNAS) ===
            Sheet hojaCombos = workbook.createSheet("📊 VALORES_COMBOS");
            workbook.setSheetOrder("📊 VALORES_COMBOS", 1);

            // === HOJA VISIBLE: INSTRUCCIONES ===
            Sheet hojaInstrucciones = workbook.createSheet("📝 INSTRUCCIONES");
            workbook.setSheetOrder("📝 INSTRUCCIONES", 2);

            // Crear hoja de instrucciones
            crearHojaInstrucciones(hojaInstrucciones);

            // Crear encabezados hoja combos (una categoría por columna)
            crearEncabezadosCombos(hojaCombos);

            // Crear estilos
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle fechaStyle = crearEstiloFecha(workbook);
            CellStyle dropdownStyle = crearEstiloDropdown(workbook);
            CellStyle opcionalStyle = crearEstiloOpcional(workbook);
            CellStyle obligatorioStyle = crearEstiloObligatorio(workbook);
            CellStyle condicionalStyle = crearEstiloCondicional(workbook);

            // Encabezados con NUEVO CAMPO para descripción de site
            String[] headers = {
                    "fechaApertura*",                  // 0 - Obligatorio
                    "cliente*",                        // 1 - Obligatorio
                    "area*",                           // 2 - Obligatorio
                    "proyecto*",                       // 3 - Obligatorio
                    "fase*",                           // 4 - Obligatorio
                    "codigoSite*",                     // 5 - Obligatorio (NUEVO: solo código)
                    "descripcionSite*",                // 6 - Obligatorio (NUEVO: descripción específica)
                    "region*",                         // 7 - Obligatorio
                    "tipoOt*",                         // 8 - Obligatorio
                    "estado*",                         // 9 - Obligatorio (SIEMPRE: ASIGNACION)
                    "otAnterior",                      // 10 - Condicional
                    "JefaturaClienteSolicitante*",     // 11 - Obligatorio
                    "AnalistaClienteSolicitante*",     // 12 - Obligatorio
                    "CoordinadorTiCw*",                // 13 - Obligatorio
                    "JefaturaResponsable*",            // 14 - Obligatorio
                    "Liquidador*",                     // 15 - Obligatorio
                    "Ejecutante*",                     // 16 - Obligatorio
                    "AnalistaContable*"                // 17 - Obligatorio
            };

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);

                // Añadir comentario según tipo de campo
                agregarComentarioCampo(cell, i);
            }

            // Fila de ejemplo
            Row ejemploRow = sheet.createRow(1);
            llenarValoresEjemplo(ejemploRow, fechaStyle);

            // Aplicar dropdowns organizados en columnas
            aplicarDropdownsPorColumnas(workbook, sheet, hojaCombos);

            // Aplicar estilos según tipo de campo
            aplicarEstilosPorTipo(sheet, headers.length, fechaStyle, dropdownStyle,
                    opcionalStyle, obligatorioStyle, condicionalStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.max(currentWidth, 3000));

                // Ajustes específicos para algunas columnas
                if (i == 5 || i == 6) { // codigoSite y descripcionSite
                    sheet.setColumnWidth(i, Math.max(currentWidth, 3500));
                } else if (i == 0) { // fechaApertura
                    sheet.setColumnWidth(i, Math.max(currentWidth, 2500));
                }
            }

            // Ajustar hoja de combos (cada columna separada)
            for (int i = 0; i < MAX_COLUMNAS_COMBOS * 2; i++) {
                hojaCombos.autoSizeColumn(i);
                if (i % 2 == 0) { // Columnas de categoría
                    hojaCombos.setColumnWidth(i, Math.max(hojaCombos.getColumnWidth(i), 4000));
                } else { // Columnas de valores
                    hojaCombos.setColumnWidth(i, Math.max(hojaCombos.getColumnWidth(i), 6000));
                }
            }

            // Congelar paneles
            sheet.createFreezePane(0, 1);

            workbook.write(outputStream);
            log.info("✅ Plantilla Excel generada exitosamente con {} columnas", headers.length);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("❌ Error al generar template Excel: {}", e.getMessage(), e);
            throw new IOException("Error al generar plantilla Excel: " + e.getMessage(), e);
        }
    }

    // ================ MÉTODOS PARA GENERAR TEMPLATE ================

    private void crearHojaInstrucciones(Sheet hojaInstrucciones) {
        Row titleRow = hojaInstrucciones.createRow(0);
        titleRow.createCell(0).setCellValue("📋 INSTRUCCIONES PARA IMPORTACIÓN DE OTs");

        String[] instrucciones = {
                "1️⃣ IMPORTANTE: No modifique los nombres de las columnas",
                "2️⃣ Campos con * son OBLIGATORIOS",
                "3️⃣ Campos CONDICIONALES:",
                "   - otAnterior: Obligatorio si fechaApertura es del año anterior",
                "   - Límite máximo: 2,147,483,647",
                "4️⃣ Use los dropdowns (flechas ▼) para seleccionar valores válidos",
                "5️⃣ Fecha: Formato dd/mm/aaaa",
                "6️⃣ Para ver todos los valores posibles, revise la hoja '📊 VALORES_COMBOS'",
                "7️⃣ Estado OT: Siempre debe ser 'ASIGNACION'",
                "8️⃣ Máximo de filas por importación: 1,000",
                "",
                "📌 FORMATO ESPECIAL PARA SITE:",
                "- Paso 1: Seleccione el CÓDIGO del site (columna F)",
                "- Paso 2: Seleccione la DESCRIPCIÓN específica (columna G)",
                "- El sistema mostrará solo las descripciones del código seleccionado",
                "- Ejemplo correcto:",
                "   • codigoSite: 0130493",
                "   • descripcionSite: LM_Bertello",
                "- Ejemplo incorrecto:",
                "   • codigoSite: 0130493 LM_Bertello (NO mezclar)",
                "   • descripcionSite: (vacío)",
                "",
                "⚠️ VALIDACIONES AUTOMÁTICAS:",
                "- Fecha no puede ser futura",
                "- Todos los valores deben existir en el sistema",
                "- OT anterior debe ser un número entero válido",
                "- Se verificará que los responsables existan",
                "- Las descripciones deben corresponder al código de site"
        };

        for (int i = 0; i < instrucciones.length; i++) {
            Row row = hojaInstrucciones.createRow(i + 2);
            row.createCell(0).setCellValue(instrucciones[i]);
        }

        hojaInstrucciones.autoSizeColumn(0);
    }

    private void crearEncabezadosCombos(Sheet hojaCombos) {
        Row header = hojaCombos.createRow(0);
        // Crear 5 columnas independientes (cada una con su categoría y valores)
        for (int col = 0; col < MAX_COLUMNAS_COMBOS; col++) {
            int baseCol = col * 2; // Cada categoría ocupa 2 columnas
            header.createCell(baseCol).setCellValue("CATEGORÍA " + (col + 1));
            header.createCell(baseCol + 1).setCellValue("VALORES " + (col + 1));
        }
    }

    private void agregarComentarioCampo(Cell cell, int colIndex) {
        XSSFComment comment = ((XSSFCell) cell).getCellComment();
        if (comment == null) {
            comment = ((XSSFSheet) cell.getSheet()).createDrawingPatriarch().createCellComment(
                    new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6)
            );
        }

        String mensaje = "";
        switch (colIndex) {
            case 0: mensaje = "Fecha de apertura (OBLIGATORIO)\nFormato: dd/mm/aaaa"; break;
            case 5: mensaje = "Código del Site (OBLIGATORIO)\nSeleccione solo el código\nEjemplo: '0130493'"; break;
            case 6: mensaje = "Descripción del Site (OBLIGATORIO)\nSeleccione la descripción específica\nSe mostrarán solo las opciones del código seleccionado\nEjemplo: 'LM_Bertello'"; break;
            case 7: mensaje = "Tipo OT (OBLIGATORIO)\nSeleccione de la lista"; break;
            case 9: mensaje = "Estado OT (OBLIGATORIO)\nSiempre: ASIGNACION"; break;
            case 10: mensaje = "OT Anterior (CONDICIONAL)\nObligatorio si fecha es del año anterior\nLímite: 2,147,483,647"; break;
            default: mensaje = "Campo obligatorio\nSeleccione de la lista";
        }

        comment.setString(new XSSFRichTextString(mensaje));
        cell.setCellComment(comment);
    }

    private void llenarValoresEjemplo(Row ejemploRow, CellStyle fechaStyle) {
        // Fecha actual
        Cell fechaCell = ejemploRow.createCell(0);
        fechaCell.setCellValue(new java.util.Date());
        fechaCell.setCellStyle(fechaStyle);

        // Cliente
        List<DropdownDTO> clientes = dropdownService.getClientes();
        if (!clientes.isEmpty() && clientes.get(0) != null && clientes.get(0).label() != null) {
            ejemploRow.createCell(1).setCellValue(clientes.get(0).label());
        }

        // Área
        List<DropdownDTO> areas = dropdownService.getAreas();
        if (!areas.isEmpty() && areas.get(0) != null && areas.get(0).label() != null) {
            ejemploRow.createCell(2).setCellValue(areas.get(0).label());
        }

        // Proyecto
        List<DropdownDTO> proyectos = dropdownService.getProyectos();
        if (!proyectos.isEmpty() && proyectos.get(0) != null && proyectos.get(0).label() != null) {
            ejemploRow.createCell(3).setCellValue(proyectos.get(0).label());
        }

        // Fase
        List<DropdownDTO> fases = dropdownService.getFases();
        if (!fases.isEmpty() && fases.get(0) != null && fases.get(0).label() != null) {
            ejemploRow.createCell(4).setCellValue(fases.get(0).label());
        }

        // Código Site (NUEVO: solo el código)
        List<DropdownDTO> sites = dropdownService.getSitesConDescripciones();
        if (!sites.isEmpty() && sites.get(0) != null) {
            ejemploRow.createCell(5).setCellValue(sites.get(0).adicional());
        }

        // Descripción Site (NUEVO: se llenará dinámicamente según el código)
        // Dejar vacío por ahora, se llenará con dropdown condicional
        ejemploRow.createCell(6).setCellValue("");

        // Región
        List<DropdownDTO> regiones = dropdownService.getRegiones();
        if (!regiones.isEmpty() && regiones.get(0) != null && regiones.get(0).label() != null) {
            ejemploRow.createCell(7).setCellValue(regiones.get(0).label());
        }

        // Tipo OT
        List<DropdownDTO> tiposOt = dropdownService.getOtTipo();
        if (!tiposOt.isEmpty() && tiposOt.get(0) != null && tiposOt.get(0).label() != null) {
            ejemploRow.createCell(8).setCellValue(tiposOt.get(0).label());
        } else {
            ejemploRow.createCell(8).setCellValue("CORRECTIVO");
        }

        // Estado (¡DEBE SER "ASIGNACION"!)
        ejemploRow.createCell(9).setCellValue("ASIGNACION");

        // OT Anterior (vacío)
        ejemploRow.createCell(10).setCellValue("");

        // Responsables
        llenarResponsablesEjemplo(ejemploRow);
    }

    private void llenarResponsablesEjemplo(Row row) {
        // Jefatura Cliente
        List<DropdownDTO> jefaturas = dropdownService.getJefaturasClienteSolicitante();
        if (!jefaturas.isEmpty() && jefaturas.get(0) != null && jefaturas.get(0).label() != null) {
            row.createCell(11).setCellValue(jefaturas.get(0).label());
        } else {
            row.createCell(11).setCellValue("JEFATURA EJEMPLO");
        }

        // Analista Cliente
        List<DropdownDTO> analistas = dropdownService.getAnalistasClienteSolicitante();
        if (!analistas.isEmpty() && analistas.get(0) != null && analistas.get(0).label() != null) {
            row.createCell(12).setCellValue(analistas.get(0).label());
        } else {
            row.createCell(12).setCellValue("ANALISTA EJEMPLO");
        }

        // Coordinador Ti CW
        List<DropdownDTO> coordinadores = dropdownService.getCoordinadoresTiCw();
        if (!coordinadores.isEmpty() && coordinadores.get(0) != null && coordinadores.get(0).label() != null) {
            row.createCell(13).setCellValue(coordinadores.get(0).label());
        } else {
            row.createCell(13).setCellValue("COORDINADOR EJEMPLO");
        }

        // Jefatura Responsable
        List<DropdownDTO> jefaturasResp = dropdownService.getJefaturasResponsable();
        if (!jefaturasResp.isEmpty() && jefaturasResp.get(0) != null && jefaturasResp.get(0).label() != null) {
            row.createCell(14).setCellValue(jefaturasResp.get(0).label());
        } else {
            row.createCell(14).setCellValue("JEFATURA RESPONSABLE EJEMPLO");
        }

        // Liquidador
        List<DropdownDTO> liquidador = dropdownService.getLiquidador();
        if (!liquidador.isEmpty() && liquidador.get(0) != null && liquidador.get(0).label() != null) {
            row.createCell(15).setCellValue(liquidador.get(0).label());
        } else {
            row.createCell(15).setCellValue("LIQUIDADOR EJEMPLO");
        }

        // Ejecutante
        List<DropdownDTO> ejecutantes = dropdownService.getEjecutantes();
        if (!ejecutantes.isEmpty() && ejecutantes.get(0) != null && ejecutantes.get(0).label() != null) {
            row.createCell(16).setCellValue(ejecutantes.get(0).label());
        } else {
            row.createCell(16).setCellValue("EJECUTANTE EJEMPLO");
        }

        // Analista Contable
        List<DropdownDTO> analistasCont = dropdownService.getAnalistasContable();
        if (!analistasCont.isEmpty() && analistasCont.get(0) != null && analistasCont.get(0).label() != null) {
            row.createCell(17).setCellValue(analistasCont.get(0).label());
        } else {
            row.createCell(17).setCellValue("ANALISTA CONTABLE EJEMPLO");
        }
    }

    private void aplicarDropdownsPorColumnas(XSSFWorkbook workbook, Sheet sheetPrincipal, Sheet hojaCombos) {
        try {
            log.info("=== APLICANDO DROPDOWNS POR COLUMNAS ===");

            // Configuración de dropdowns - ORGANIZAR EN 5 COLUMNAS INDEPENDIENTES
            Object[][][] configsPorColumna = {
                    // COLUMNA 0 (columnas A-B en hoja combos)
                    {
                            {1, "Cliente", dropdownService.getClientes()},
                            {2, "Área", dropdownService.getAreas()},
                            {3, "Proyecto", dropdownService.getProyectos()},
                            {4, "Fase", dropdownService.getFases()}
                    },
                    // COLUMNA 1 (columnas C-D en hoja combos)
                    {
                            {5, "Código Site", dropdownService.getSitesConDescripciones()},
                            // La descripción site (columna 6) se manejará con validación condicional
                            {7, "Región", dropdownService.getRegiones()},
                            {8, "Tipo OT", dropdownService.getOtTipo()}
                    },
                    // COLUMNA 2 (columnas E-F en hoja combos)
                    {
                            {9, "Estado", Arrays.asList("ASIGNACION")},
                            {11, "Jefatura Cliente", dropdownService.getJefaturasClienteSolicitante()},
                            {12, "Analista Cliente", dropdownService.getAnalistasClienteSolicitante()}
                    },
                    // COLUMNA 3 (columnas G-H en hoja combos)
                    {
                            {13, "Coordinador Ti CW", dropdownService.getCoordinadoresTiCw()},
                            {14, "Jefatura Responsable", dropdownService.getJefaturasResponsable()},
                            {15, "Liquidador", dropdownService.getLiquidador()}
                    },
                    // COLUMNA 4 (columnas I-J en hoja combos)
                    {
                            {16, "Ejecutante", dropdownService.getEjecutantes()},
                            {17, "Analista Contable", dropdownService.getAnalistasContable()}
                    }
            };

            // Procesar cada columna
            for (int columnaIndex = 0; columnaIndex < configsPorColumna.length; columnaIndex++) {
                Object[][] configs = configsPorColumna[columnaIndex];
                int startRow = 1; // Empezar después del encabezado

                // Calcular desplazamiento de columnas en hoja combos
                int colBaseHojaCombos = columnaIndex * 2; // Cada columna ocupa 2 columnas

                for (Object[] config : configs) {
                    int colIndex = (int) config[0];
                    String nombreCampo = (String) config[1];
                    Object datos = config[2];

                    if (datos instanceof List) {
                        @SuppressWarnings("unchecked")
                        List<DropdownDTO> items = (List<DropdownDTO>) datos;

                        if (items != null && !items.isEmpty()) {
                            // Escribir categoría en hoja combos
                            Row categoriaRow = hojaCombos.getRow(startRow);
                            if (categoriaRow == null) {
                                categoriaRow = hojaCombos.createRow(startRow);
                            }
                            categoriaRow.createCell(colBaseHojaCombos).setCellValue(nombreCampo + ":");

                            // Escribir valores
                            int dataRow = startRow + 1;
                            for (int i = 0; i < Math.min(items.size(), 100); i++) {
                                DropdownDTO item = items.get(i);
                                if (item == null) continue;

                                String valor = obtenerValorDropdown(item);
                                if (valor != null && !valor.trim().isEmpty()) {
                                    Row valorRow = hojaCombos.getRow(dataRow);
                                    if (valorRow == null) {
                                        valorRow = hojaCombos.createRow(dataRow);
                                    }
                                    valorRow.createCell(colBaseHojaCombos + 1).setCellValue(valor);
                                    dataRow++;
                                }
                            }

                            // Crear dropdown si hay valores válidos
                            if (dataRow > startRow + 1) {
                                String rango = "'📊 VALORES_COMBOS'!$" +
                                        getColumnaExcel(colBaseHojaCombos + 1) + "$" + (startRow + 1) +
                                        ":$" + getColumnaExcel(colBaseHojaCombos + 1) + "$" + (dataRow - 1);

                                // Crear validación de datos
                                crearDropdownDesdeRango(workbook, sheetPrincipal, colIndex, rango, nombreCampo);
                            }

                            // Avanzar startRow para la siguiente categoría en esta columna
                            startRow = Math.max(startRow, dataRow) + 2; // +2 para espacio entre categorías
                        }
                    }
                }
            }

            // Crear validación condicional para descripción site (columna 6)
            crearValidacionCondicionalSite(workbook, sheetPrincipal, hojaCombos);

            log.info("✅ Todos los dropdowns aplicados exitosamente");

        } catch (Exception e) {
            log.error("❌ Error aplicando dropdowns: {}", e.getMessage(), e);
        }
    }

    private void crearValidacionCondicionalSite(XSSFWorkbook workbook, Sheet sheetPrincipal, Sheet hojaCombos) {
        try {
            log.info("=== CREANDO VALIDACIÓN CONDICIONAL PARA SITE ===");

            // Obtener todos los sites con sus descripciones
            List<DropdownDTO> sitesCodigos = dropdownService.getSitesConDescripciones();

            // Para cada código de site, obtener sus descripciones
            int currentRow = 1;
            int colBase = 10; // Empezar en columna K (columna 10) para las descripciones

            for (DropdownDTO siteCodigo : sitesCodigos) {
                if (siteCodigo.adicional() == null || siteCodigo.adicional().trim().isEmpty()) {
                    continue;
                }

                String codigo = siteCodigo.adicional();

                // Obtener descripciones para este código
                List<DropdownDTO> descripciones = dropdownService.getDescripcionesBySiteCodigo(codigo);

                if (descripciones != null && !descripciones.isEmpty()) {
                    // Escribir código como categoría
                    Row categoriaRow = hojaCombos.getRow(currentRow);
                    if (categoriaRow == null) {
                        categoriaRow = hojaCombos.createRow(currentRow);
                    }
                    categoriaRow.createCell(colBase).setCellValue("Descripciones para: " + codigo);

                    // Escribir descripciones
                    int dataRow = currentRow + 1;
                    for (DropdownDTO desc : descripciones) {
                        String descripcionTexto = obtenerValorDropdown(desc);
                        if (descripcionTexto != null && !descripcionTexto.trim().isEmpty()) {
                            Row valorRow = hojaCombos.getRow(dataRow);
                            if (valorRow == null) {
                                valorRow = hojaCombos.createRow(dataRow);
                            }
                            valorRow.createCell(colBase + 1).setCellValue(descripcionTexto);
                            dataRow++;
                        }
                    }

                    // Crear rango para validación condicional
                    if (dataRow > currentRow + 1) {
                        String rangoDescripciones = "'📊 VALORES_COMBOS'!$" +
                                getColumnaExcel(colBase + 1) + "$" + (currentRow + 1) +
                                ":$" + getColumnaExcel(colBase + 1) + "$" + (dataRow - 1);

                        // Crear nombre definido para este rango
                        String nombreDefinido = "Descripciones_" + codigo.replaceAll("[^A-Za-z0-9]", "");

                        // Agregar nombre definido al workbook
                        XSSFName definedName = ((XSSFWorkbook) workbook).createName();
                        definedName.setNameName(nombreDefinido);
                        definedName.setRefersToFormula(rangoDescripciones);

                        // Crear validación de datos dependiente
                        crearDropdownDependiente(workbook, sheetPrincipal, 6, nombreDefinido, codigo);
                    }

                    currentRow = Math.max(currentRow, dataRow) + 2;
                }
            }

            log.info("✅ Validación condicional creada para {} códigos de site", sitesCodigos.size());

        } catch (Exception e) {
            log.error("❌ Error creando validación condicional: {}", e.getMessage(), e);
        }
    }

    private void crearDropdownDependiente(XSSFWorkbook workbook, Sheet sheet, int colIndex,
                                          String nombreRango, String codigoSite) {
        try {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

            // Crear fórmula que dependa del código seleccionado
            String formula = "IF($F2=\"" + codigoSite + "\",INDIRECT(\"" + nombreRango + "\"),\"\")";

            DataValidationConstraint constraint = dvHelper.createFormulaListConstraint(formula);

            CellRangeAddressList addressList = new CellRangeAddressList(1, 10000, colIndex, colIndex);

            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);

            validation.setSuppressDropDownArrow(true);
            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox("Descripción no válida",
                    "Seleccione una descripción válida para el código de site " + codigoSite);

            validation.setShowPromptBox(true);
            validation.createPromptBox("Seleccione descripción",
                    "Seleccione una descripción para el código " + codigoSite);

            sheet.addValidationData(validation);

        } catch (Exception e) {
            log.warn("Error creando dropdown dependiente para {}: {}", codigoSite, e.getMessage());
        }
    }

    private void crearDropdownDesdeRango(XSSFWorkbook workbook, Sheet sheetPrincipal,
                                         int colIndex, String rango, String nombreCampo) {
        try {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheetPrincipal);

            DataValidationConstraint constraint = dvHelper.createFormulaListConstraint(rango);

            CellRangeAddressList addressList = new CellRangeAddressList(1, 10000, colIndex, colIndex);

            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);

            validation.setSuppressDropDownArrow(false);
            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox("Valor no permitido",
                    "Debe seleccionar un valor de la lista para: " + nombreCampo);

            validation.setShowPromptBox(true);
            validation.createPromptBox("Seleccione " + nombreCampo,
                    "Haga clic aquí o presione ALT+Flecha Abajo para ver opciones");

            sheetPrincipal.addValidationData(validation);

        } catch (Exception e) {
            log.error("❌ Error creando dropdown para '{}': {}", nombreCampo, e.getMessage());
        }
    }

    private void aplicarEstilosPorTipo(Sheet sheet, int numColumnas,
                                       CellStyle fechaStyle, CellStyle dropdownStyle,
                                       CellStyle opcionalStyle, CellStyle obligatorioStyle,
                                       CellStyle condicionalStyle) {
        for (int rowNum = 1; rowNum <= 100; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) {
                row = sheet.createRow(rowNum);
            }

            for (int colNum = 0; colNum < numColumnas; colNum++) {
                Cell cell = row.getCell(colNum);
                if (cell == null) {
                    cell = row.createCell(colNum);
                }

                // Aplicar estilo según tipo de campo
                if (colNum == 0) {
                    cell.setCellStyle(fechaStyle); // Fecha
                } else if (colNum == 10) {
                    cell.setCellStyle(condicionalStyle); // OT Anterior (condicional)
                } else if (colNum == 9) {
                    cell.setCellStyle(obligatorioStyle); // Estado (fijo)
                } else if (colNum == 6) {
                    // Columna de descripción site - estilo diferente
                    cell.setCellStyle(crearEstiloSiteDescripcion(sheet.getWorkbook()));
                } else {
                    cell.setCellStyle(dropdownStyle); // Dropdowns
                }
            }
        }
    }

    private CellStyle crearEstiloSiteDescripcion(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        Font font = workbook.createFont();
        font.setItalic(true);
        style.setFont(font);
        return style;
    }

    // ================ ESTILOS ================
    private CellStyle crearEstiloEncabezado(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloFecha(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloDropdown(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloOpcional(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloObligatorio(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private CellStyle crearEstiloCondicional(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // ================ MÉTODOS AUXILIARES ================

    private String obtenerValorDropdown(DropdownDTO dto) {
        if (dto == null) return null;

        if (dto.label() != null && !dto.label().trim().isEmpty()) {
            return dto.label().trim();
        }

        if (dto.adicional() != null && !dto.adicional().trim().isEmpty()) {
            return dto.adicional().trim();
        }

        return null;
    }

    private String getColumnaExcel(int colIndex) {
        StringBuilder column = new StringBuilder();
        while (colIndex >= 0) {
            column.insert(0, (char) ('A' + (colIndex % 26)));
            colIndex = (colIndex / 26) - 1;
        }
        return column.toString();
    }

    // ================ MÉTODOS PARA IMPORTACIÓN ================

    private Map<String, Integer> mapColumnHeaders(Row headerRow) {
        Map<String, Integer> columnIndex = new HashMap<>();
        for (Cell cell : headerRow) {
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String headerName = cell.getStringCellValue().trim();
                String headerOriginal = headerName;
                String headerNormalized = normalizeHeaderName(headerName);

                columnIndex.put(headerNormalized, cell.getColumnIndex());
                log.info("🚀 Mapeado encabezado: Original='{}', Normalizado='{}' -> columna {}",
                        headerOriginal, headerNormalized, cell.getColumnIndex());
            }
        }

        log.info("📊 Encabezados encontrados en Excel: {}", columnIndex.keySet());
        return columnIndex;
    }

    private void verificarEncabezadosRequeridos(Map<String, Integer> columnIndex) throws IOException {
        // Lista de encabezados con todas las posibles variaciones
        Map<String, List<String>> encabezadosConVariaciones = Map.of(
                "fechaapertura", Arrays.asList("fechaapertura", "fecha", "fechaapertura*"),
                "cliente", Arrays.asList("cliente", "cliente*"),
                "area", Arrays.asList("area", "area*"),
                "proyecto", Arrays.asList("proyecto", "proyecto*"),
                "fase", Arrays.asList("fase", "fase*"),
                "codigosite", Arrays.asList("codigosite", "codigosite*", "site", "site*"),
                "descripcionsite", Arrays.asList("descripcionsite", "descripcionsite*", "descripcion", "descripcion*"),
                "region", Arrays.asList("region", "region*"),
                "tipoot", Arrays.asList("tipoot", "tipoot*", "tipo", "tipo*", "tipot"),
                "estado", Arrays.asList("estado", "estado*")
        );

        List<String> faltantes = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : encabezadosConVariaciones.entrySet()) {
            String nombreCampo = entry.getKey();
            List<String> variaciones = entry.getValue();

            boolean encontrado = false;
            for (String variacion : variaciones) {
                if (columnIndex.containsKey(variacion)) {
                    encontrado = true;
                    log.info("✅ Encontrado {} como '{}'", nombreCampo, variacion);
                    break;
                }
            }

            if (!encontrado) {
                faltantes.add(nombreCampo);
                log.warn("❌ No encontrado: {} (variaciones probadas: {})",
                        nombreCampo, variaciones);
            }
        }

        if (!faltantes.isEmpty()) {
            String mensaje = "Faltan encabezados obligatorios: " + String.join(", ", faltantes);
            log.error("❌ {}", mensaje);
            throw new IOException(mensaje);
        }

        log.info("✅ Todos los encabezados obligatorios están presentes");
    }

    private String normalizeHeaderName(String headerName) {
        if (headerName == null) return "";

        String normalized = headerName
                .toLowerCase()
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u")
                .replace("ñ", "n")
                .replace("*", ""); // Remover asterisco si existe

        normalized = normalized
                .replace(" ", "")
                .replace("_", "")
                .replace("-", "")
                .replace(".", "")
                .replace(",", "")
                .replace(";", "")
                .replace(":", "");

        log.debug("Normalizado: '{}' -> '{}'", headerName, normalized);
        return normalized;
    }

    private ExcelImportDTO parseRowToDTO(Row row, Map<String, Integer> columnIndex, int fila) {
        ExcelImportDTO dto = new ExcelImportDTO();
        dto.setFilaExcel(fila);
        dto.setValido(true);

        try {
            // Campos obligatorios
            dto.setFechaApertura(parseFecha(row.getCell(columnIndex.get("fechaapertura"))));
            dto.setCliente(getStringCellValue(row, columnIndex.get("cliente")));
            dto.setArea(getStringCellValue(row, columnIndex.get("area")));
            dto.setProyecto(getStringCellValue(row, columnIndex.get("proyecto")));
            dto.setFase(getStringCellValue(row, columnIndex.get("fase")));

            // NUEVO: Código y descripción de site separados
            dto.setSite(getStringCellValue(row, columnIndex.get("codigosite")));
            dto.setSiteDescripcion(getStringCellValue(row, columnIndex.get("descripcionsite")));

            dto.setRegion(getStringCellValue(row, columnIndex.get("region")));

            // Verificar y buscar tipoOt con diferentes variaciones
            String tipoOt = null;
            if (columnIndex.containsKey("tipoot")) {
                tipoOt = getStringCellValue(row, columnIndex.get("tipoot"));
            } else if (columnIndex.containsKey("tipot")) {
                tipoOt = getStringCellValue(row, columnIndex.get("tipot"));
            } else if (columnIndex.containsKey("tipo")) {
                tipoOt = getStringCellValue(row, columnIndex.get("tipo"));
            }
            dto.setTipoOt(tipoOt);

            dto.setEstado(getStringCellValue(row, columnIndex.get("estado")));

            // Campo condicional
            if (columnIndex.containsKey("otanterior")) {
                dto.setOtAnterior(getNumericCellValue(row, columnIndex.get("otanterior")));
            }

            // Responsables
            dto.setJefaturaClienteSolicitante(getStringCellValue(row, columnIndex.getOrDefault("jefaturaclientesolicitante",
                    columnIndex.getOrDefault("jefatura", -1))));
            dto.setAnalistaClienteSolicitante(getStringCellValue(row, columnIndex.getOrDefault("analistaclientesolicitante",
                    columnIndex.getOrDefault("analista", -1))));
            dto.setCoordinadorTiCw(getStringCellValue(row, columnIndex.get("coordinadorticw")));
            dto.setJefaturaResponsable(getStringCellValue(row, columnIndex.get("jefaturaresponsable")));
            dto.setLiquidador(getStringCellValue(row, columnIndex.get("liquidador")));
            dto.setEjecutante(getStringCellValue(row, columnIndex.get("ejecutante")));
            dto.setAnalistaContable(getStringCellValue(row, columnIndex.get("analistacontable")));

        } catch (Exception e) {
            dto.setValido(false);
            dto.setMensajeError("Error al leer fila Excel: " + e.getMessage());
            log.warn("Error parse fila {}: {}", fila, e.getMessage());
        }

        return dto;
    }

    private void validarRegistroCompleto(ExcelImportDTO dto) {
        List<String> errores = new ArrayList<>();

        // === VALIDACIÓN DE FECHA ===
        if (dto.getFechaApertura() == null) {
            errores.add("Fecha de apertura es obligatoria");
        } else {
            LocalDate hoy = LocalDate.now();
            if (dto.getFechaApertura().isAfter(hoy)) {
                errores.add("Fecha de apertura no puede ser futura");
            }
            if (dto.getFechaApertura().isBefore(hoy.minusYears(5))) {
                errores.add("Fecha de apertura no puede ser anterior a 5 años");
            }

            // Validación condicional de OT anterior
            if (dto.getFechaApertura().getYear() < hoy.getYear()) {
                if (dto.getOtAnterior() == null || dto.getOtAnterior() < 1) {
                    errores.add("OT anterior es obligatoria para fechas del año anterior");
                }
            }
        }

        // === VALIDACIÓN DE OT ANTERIOR ===
        if (dto.getOtAnterior() != null) {
            if (dto.getOtAnterior() < 1) {
                errores.add("OT anterior debe ser mayor o igual a 1");
            }
            if (dto.getOtAnterior() > MAX_OT_ANTERIOR) {
                errores.add("OT anterior excede el límite máximo (" + MAX_OT_ANTERIOR + ")");
            }
        }

        // === VALIDACIÓN DE CAMPOS OBLIGATORIOS ===
        validarCampoExistente(dto.getCliente(), dropdownService.getClientes(), "Cliente", errores);
        validarCampoExistente(dto.getArea(), dropdownService.getAreas(), "Área", errores);
        validarCampoExistente(dto.getProyecto(), dropdownService.getProyectos(), "Proyecto", errores);
        validarCampoExistente(dto.getFase(), dropdownService.getFases(), "Fase", errores);
        validarCampoExistente(dto.getRegion(), dropdownService.getRegiones(), "Región", errores);

        // === VALIDACIÓN DE TIPO OT ===
        if (dto.getTipoOt() == null || dto.getTipoOt().trim().isEmpty()) {
            errores.add("Tipo OT es obligatorio");
        } else {
            validarCampoExistente(dto.getTipoOt(), dropdownService.getOtTipo(), "Tipo OT", errores);
        }

        // === VALIDACIÓN DE SITE (CÓDIGO Y DESCRIPCIÓN) ===
        if (dto.getSite() == null || dto.getSiteDescripcion().trim().isEmpty()) {
            errores.add("Código Site es obligatorio");
        } else {
            // Validar que el código existe
            boolean codigoExiste = false;
            for (DropdownDTO site : dropdownService.getSitesConDescripciones()) {
                if (site.adicional() != null && site.adicional().trim().equalsIgnoreCase(dto.getSite().trim())) {
                    codigoExiste = true;
                    break;
                }
            }

            if (!codigoExiste) {
                errores.add("Código Site '" + dto.getSite() + "' no existe en el sistema");
            } else {
                // Validar que la descripción existe para este código
                if (dto.getSiteDescripcion() == null || dto.getSiteDescripcion().trim().isEmpty()) {
                    errores.add("Descripción Site es obligatoria para el código " + dto.getSite());
                } else {
                    // Obtener descripciones para este código
                    List<DropdownDTO> descripciones = dropdownService.getDescripcionesBySiteCodigo(dto.getSite());

                    // === LOGGING DE DEBUG - AGREGADO ===
                    log.debug("\n=== DEBUG VALIDACIÓN SITE - Fila {} ===", dto.getFilaExcel());
                    log.debug("Código Site desde Excel: '{}'", dto.getSite());
                    log.debug("Descripción desde Excel: '{}'", dto.getSiteDescripcion());
                    log.debug("Longitud descripción Excel: {} caracteres", dto.getSiteDescripcion().length());
                    log.debug("Número de descripciones encontradas en BD: {}", descripciones.size());
                    log.debug("Descripciones disponibles en BD para código '{}':", dto.getSite());

                    // Mostrar cada descripción con detalles
                    for (int i = 0; i < descripciones.size(); i++) {
                        DropdownDTO desc = descripciones.get(i);
                        String label = desc.label() != null ? desc.label() : "null";
                        String adicional = desc.adicional() != null ? desc.adicional() : "null";
                        log.debug("  [{}/{}] ID: {}, Label: '{}' ({} chars), Adicional: '{}'",
                                i+1, descripciones.size(), desc.id(), label, label.length(), adicional);
                    }
                    // === FIN LOGGING ===

                    boolean descripcionValida = false;

                    for (DropdownDTO desc : descripciones) {
                        if (desc.label() != null) {
                            // === LOGGING DETALLADO DE COMPARACIÓN ===
                            String labelBD = desc.label();
                            String labelExcel = dto.getSiteDescripcion();

                            log.debug("Comparando descripción:");
                            log.debug("  - BD:      '{}' ({} chars)", labelBD, labelBD.length());
                            log.debug("  - Excel:   '{}' ({} chars)", labelExcel, labelExcel.length());
                            log.debug("  - Trim BD: '{}'", labelBD.trim());
                            log.debug("  - Trim Excel: '{}'", labelExcel.trim());
                            log.debug("  - equalsIgnoreCase?: {}",
                                    labelBD.trim().equalsIgnoreCase(labelExcel.trim()));

                            // Verificar caracteres especiales
                            if (!labelBD.equals(labelBD.trim()) || !labelExcel.equals(labelExcel.trim())) {
                                log.debug("  ¡ADVERTENCIA! Hay espacios extras:");
                                log.debug("  - BD tiene espacios?: {}", !labelBD.equals(labelBD.trim()));
                                log.debug("  - Excel tiene espacios?: {}", !labelExcel.equals(labelExcel.trim()));
                            }
                            // === FIN LOGGING COMPARACIÓN ===

                            if (desc.label().trim().equalsIgnoreCase(dto.getSiteDescripcion().trim())) {
                                descripcionValida = true;
                                log.debug("✓ ¡COINCIDENCIA ENCONTRADA para código {}!", dto.getSite());
                                break;
                            }
                        }
                    }

                    if (!descripcionValida) {
                        // Construir mensaje de error más informativo
                        StringBuilder opciones = new StringBuilder();
                        for (DropdownDTO desc : descripciones) {
                            if (desc.label() != null) {
                                opciones.append("'").append(desc.label()).append("', ");
                            }
                        }

                        String opcionesStr = opciones.length() > 0 ?
                                opciones.substring(0, opciones.length() - 2) : "Ninguna descripción disponible";

                        // Mensaje con más detalles para debugging
                        String mensajeError = String.format(
                                "Descripción '%s' no es válida para el código %s. " +
                                        "Longitud Excel: %d chars. Opciones disponibles en BD: %s",
                                dto.getSiteDescripcion(),
                                dto.getSite(),
                                dto.getSiteDescripcion().length(),
                                opcionesStr
                        );

                        errores.add(mensajeError);
                        log.warn("❌ Validación fallida - {}", mensajeError);
                    }
                }
            }
        }

        // === VALIDACIÓN DE ESTADO ===
        if (dto.getEstado() == null || !"ASIGNACION".equalsIgnoreCase(dto.getEstado().trim())) {
            errores.add("Estado debe ser siempre 'ASIGNACION'");
        }

        // === VALIDACIÓN DE RESPONSABLES ===
        validarResponsableObligatorio(dto.getCoordinadorTiCw(), dropdownService.getCoordinadoresTiCw(),
                "Coordinador Ti CW", errores);
        validarResponsableObligatorio(dto.getJefaturaResponsable(), dropdownService.getJefaturasResponsable(),
                "Jefatura Responsable", errores);
        validarResponsableObligatorio(dto.getLiquidador(), dropdownService.getLiquidador(),
                "Liquidador", errores);
        validarResponsableObligatorio(dto.getEjecutante(), dropdownService.getEjecutantes(),
                "Ejecutante", errores);
        validarResponsableObligatorio(dto.getAnalistaContable(), dropdownService.getAnalistasContable(),
                "Analista Contable", errores);

        // Validar opcionales
        validarCampoExistente(dto.getJefaturaClienteSolicitante(), dropdownService.getJefaturasClienteSolicitante(),
                "Jefatura Cliente", errores, false);
        validarCampoExistente(dto.getAnalistaClienteSolicitante(), dropdownService.getAnalistasClienteSolicitante(),
                "Analista Cliente", errores, false);

        // === RESULTADO ===
        if (!errores.isEmpty()) {
            dto.setValido(false);
            dto.setMensajeError(String.join("; ", errores));
            log.warn("Validación fallida fila {}: {}", dto.getFilaExcel(), dto.getMensajeError());
        }
    }
    private void validarCampoExistente(String valor, List<DropdownDTO> listaValida,
                                       String nombreCampo, List<String> errores) {
        validarCampoExistente(valor, listaValida, nombreCampo, errores, true);
    }

    private void validarCampoExistente(String valor, List<DropdownDTO> listaValida,
                                       String nombreCampo, List<String> errores, boolean obligatorio) {
        if (obligatorio && (valor == null || valor.trim().isEmpty())) {
            errores.add(nombreCampo + " es obligatorio");
            return;
        }

        if (valor != null && !valor.trim().isEmpty()) {
            boolean existe = false;
            for (DropdownDTO dto : listaValida) {
                if (dto.label() != null && dto.label().trim().equalsIgnoreCase(valor.trim())) {
                    existe = true;
                    break;
                }
                if (dto.adicional() != null && dto.adicional().trim().equalsIgnoreCase(valor.trim())) {
                    existe = true;
                    break;
                }
            }

            if (!existe) {
                errores.add(nombreCampo + " '" + valor.trim() + "' no existe en el sistema");
            }
        }
    }

    private void validarResponsableObligatorio(String valor, List<DropdownDTO> listaValida,
                                               String nombreCampo, List<String> errores) {
        if (valor == null || valor.trim().isEmpty()) {
            errores.add(nombreCampo + " es obligatorio");
            return;
        }

        validarCampoExistente(valor, listaValida, nombreCampo, errores, true);
    }

    private OtCreateRequest convertirARequest(ExcelImportDTO importDTO) {
        OtCreateRequest request = new OtCreateRequest();
        request.setFechaApertura(importDTO.getFechaApertura());
        request.setActivo(true);

        // === RESOLVER SITE (CÓDIGO Y DESCRIPCIÓN) ===
        String codigoSite = importDTO.getSite().trim();
        String descripcionSite = importDTO.getSiteDescripcion().trim();

        log.info("🔍 Buscando Site: Código='{}', Descripción='{}'", codigoSite, descripcionSite);

        // Buscar el site por código
        Site site = siteRepository.findByCodigoSitioAndActivoTrue(codigoSite)
                .orElseThrow(() -> new RuntimeException("Site con código '" + codigoSite + "' no encontrado"));

        // Buscar la descripción específica
        SiteDescripcion siteDescripcion = site.getDescripciones().stream()
                .filter(desc -> desc.getDescripcion() != null &&
                        desc.getDescripcion().trim().equalsIgnoreCase(descripcionSite))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Descripción '" + descripcionSite +
                        "' no encontrada para el código " + codigoSite));

        request.setIdSite(site.getIdSite());
        request.setIdSiteDescripcion(siteDescripcion.getIdSiteDescripcion());

        // === DESCRIPCIÓN AUTOMÁTICA ===
        String descripcionParaOT = siteDescripcion.getDescripcion() != null
                ? siteDescripcion.getDescripcion().trim()
                : "";

        String descripcionOT = String.format("%s_%s_%s",
                normalizeForDescripcion(importDTO.getProyecto()),
                normalizeForDescripcion(importDTO.getArea()),
                normalizeForDescripcion(descripcionParaOT)
        ).replaceAll("_+", "_").replaceAll("^_|_$", "");

        if (descripcionOT.isEmpty()) {
            descripcionOT = "OT SIN DESCRIPCION AUTOMATICA";
        }

        request.setDescripcion(descripcionOT);
        log.info("📝 Descripción OT: {}", descripcionOT);

        // === ASIGNAR IDs ===
        Integer idCliente = buscarIdPorNombre(dropdownService.getClientes(), importDTO.getCliente());
        Integer idArea = buscarIdPorNombre(dropdownService.getAreas(), importDTO.getArea());
        Integer idProyecto = buscarIdPorNombre(dropdownService.getProyectos(), importDTO.getProyecto());
        Integer idFase = buscarIdPorNombre(dropdownService.getFases(), importDTO.getFase());
        Integer idRegion = buscarIdPorNombre(dropdownService.getRegiones(), importDTO.getRegion());
        Integer idTipoOt = buscarIdPorNombre(dropdownService.getOtTipo(), importDTO.getTipoOt());
        Integer idEstadoOt = buscarIdPorNombre(dropdownService.getEstadosOt(), "ASIGNACION");

        request.setIdCliente(idCliente);
        request.setIdArea(idArea);
        request.setIdProyecto(idProyecto);
        request.setIdFase(idFase);
        request.setIdRegion(idRegion);
        request.setIdTipoOt(idTipoOt);
        request.setIdEstadoOt(idEstadoOt);

        // === OT ANTERIOR ===
        if (importDTO.getOtAnterior() != null) {
            if (importDTO.getOtAnterior() > MAX_OT_ANTERIOR) {
                throw new RuntimeException("OT anterior excede el límite máximo: " + MAX_OT_ANTERIOR);
            }
            request.setIdOtsAnterior(importDTO.getOtAnterior());
        }

        // === RESPONSABLES ===
        request.setIdJefaturaClienteSolicitante(
                buscarIdPorNombre(dropdownService.getJefaturasClienteSolicitante(),
                        importDTO.getJefaturaClienteSolicitante()));
        request.setIdAnalistaClienteSolicitante(
                buscarIdPorNombre(dropdownService.getAnalistasClienteSolicitante(),
                        importDTO.getAnalistaClienteSolicitante()));
        request.setIdCoordinadorTiCw(
                buscarIdPorNombre(dropdownService.getCoordinadoresTiCw(),
                        importDTO.getCoordinadorTiCw()));
        request.setIdJefaturaResponsable(
                buscarIdPorNombre(dropdownService.getJefaturasResponsable(),
                        importDTO.getJefaturaResponsable()));
        request.setIdLiquidador(
                buscarIdPorNombre(dropdownService.getLiquidador(),
                        importDTO.getLiquidador()));
        request.setIdEjecutante(
                buscarIdPorNombre(dropdownService.getEjecutantes(),
                        importDTO.getEjecutante()));
        request.setIdAnalistaContable(
                buscarIdPorNombre(dropdownService.getAnalistasContable(),
                        importDTO.getAnalistaContable()));

        // === VALIDACIÓN FINAL ===
        validarRequestCompleto(request);

        log.info("✅ Request OT convertido exitosamente para importación");

        return request;
    }

    private void validarRequestCompleto(OtCreateRequest request) {
        List<String> errores = new ArrayList<>();

        if (request.getIdCliente() == null) errores.add("Cliente no encontrado");
        if (request.getIdArea() == null) errores.add("Área no encontrada");
        if (request.getIdProyecto() == null) errores.add("Proyecto no encontrado");
        if (request.getIdFase() == null) errores.add("Fase no encontrada");
        if (request.getIdSite() == null) errores.add("Site no encontrado");
        if (request.getIdSiteDescripcion() == null) errores.add("Descripción Site no encontrada");
        if (request.getIdRegion() == null) errores.add("Región no encontrada");
        if (request.getIdTipoOt() == null) errores.add("Tipo OT no encontrado");
        if (request.getIdEstadoOt() == null) errores.add("Estado OT no encontrado");
        if (request.getIdCoordinadorTiCw() == null) errores.add("Coordinador Ti CW no encontrado");
        if (request.getIdJefaturaResponsable() == null) errores.add("Jefatura Responsable no encontrada");
        if (request.getIdLiquidador() == null) errores.add("Liquidador no encontrado");
        if (request.getIdEjecutante() == null) errores.add("Ejecutante no encontrado");
        if (request.getIdAnalistaContable() == null) errores.add("Analista Contable no encontrado");

        // Validación condicional de OT anterior
        if (request.getFechaApertura() != null) {
            LocalDate hoy = LocalDate.now();
            if (request.getFechaApertura().getYear() < hoy.getYear()) {
                if (request.getIdOtsAnterior() == null || request.getIdOtsAnterior() < 1) {
                    errores.add("OT anterior es obligatoria para fechas del año anterior");
                }
            }
        }

        if (!errores.isEmpty()) {
            throw new RuntimeException(String.join("; ", errores));
        }
    }

    private Integer buscarIdPorNombre(List<DropdownDTO> lista, String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || lista == null) {
            return null;
        }
        String searchName = nombre.trim();
        for (DropdownDTO dto : lista) {
            if (dto.label() != null && dto.label().trim().equalsIgnoreCase(searchName)) {
                return dto.id();
            }
            if (dto.adicional() != null && dto.adicional().trim().equalsIgnoreCase(searchName)) {
                return dto.id();
            }
        }
        return null;
    }

    private String normalizeForDescripcion(String str) {
        if (str == null) return "";
        return str.trim()
                .replaceAll("[^\\w\\s]", "")
                .replaceAll("\\s+", " ");
    }

    private String getStringCellValue(Row row, int columnIndex) {
        if (columnIndex < 0) return null;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        try {
            String valor = "";
            switch (cell.getCellType()) {
                case STRING:
                    valor = cell.getStringCellValue();
                    break;
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        valor = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
                    } else {
                        double numValue = cell.getNumericCellValue();
                        if (numValue == Math.floor(numValue)) {
                            valor = String.valueOf((int) numValue);
                        } else {
                            valor = String.valueOf(numValue);
                        }
                    }
                    break;
                default:
                    valor = "";
            }

            // ========== CAMBIO CRÍTICO AQUÍ ==========
            // Normalización completa que soluciona TODOS los problemas de espacios
            return valor
                    .replace("\u00A0", " ")    // Espacios no-breaking Unicode
                    .replace("\u2007", " ")    // Espacio figura
                    .replace("\u202F", " ")    // Espacio angosto
                    .replace("\u3000", " ")    // Espacio ideográfico
                    .replaceAll("\\s+", " ")   // Múltiples espacios por uno solo
                    .trim();                   // Eliminar espacios inicio/fin
            // =========================================

        } catch (Exception e) {
            log.warn("Error al leer celda [{},{}]: {}", row.getRowNum(), columnIndex, e.getMessage());
            return "";
        }
    }

    private Integer getNumericCellValue(Row row, int columnIndex) {
        if (columnIndex < 0) return null;

        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                double value = cell.getNumericCellValue();
                if (value > MAX_OT_ANTERIOR) {
                    log.warn("Valor numérico excede límite: {} en fila {}", value, row.getRowNum());
                    return null;
                }
                return (int) Math.round(value);
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty()) return null;
                value = value.replaceAll("[^0-9]", "");
                if (value.isEmpty()) return null;
                try {
                    long longValue = Long.parseLong(value);
                    if (longValue > MAX_OT_ANTERIOR) {
                        log.warn("Valor string excede límite: {} en fila {}", longValue, row.getRowNum());
                        return null;
                    }
                    return (int) longValue;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        } catch (Exception e) {
            log.warn("Error al leer número celda [{},{}]: {}", row.getRowNum(), columnIndex, e.getMessage());
            return null;
        }
        return null;
    }

    private LocalDate parseFecha(Cell cell) {
        if (cell == null) {
            return null;
        }

        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date javaDate = cell.getDateCellValue();
                        return javaDate.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                    } else {
                        double numericValue = cell.getNumericCellValue();
                        Date date = DateUtil.getJavaDate(numericValue);
                        return date.toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate();
                    }
                case STRING:
                    String dateString = cell.getStringCellValue().trim();
                    if (dateString.isEmpty()) {
                        return null;
                    }

                    DateTimeFormatter[] formatters = {
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                            DateTimeFormatter.ofPattern("dd/MM/yy"),
                            DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                            DateTimeFormatter.ISO_LOCAL_DATE
                    };

                    for (DateTimeFormatter formatter : formatters) {
                        try {
                            return LocalDate.parse(dateString, formatter);
                        } catch (DateTimeParseException e) {
                            // Continuar
                        }
                    }
                    return null;
                default:
                    return null;
            }
        } catch (Exception e) {
            log.warn("Error al parsear fecha: {}", e.getMessage());
            return null;
        }
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getStringCellValue(row, i);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private String getErrorMessage(Exception e) {
        if (e.getMessage() != null) {
            return e.getMessage();
        }
        if (e.getCause() != null && e.getCause().getMessage() != null) {
            return e.getCause().getMessage();
        }
        return "Error desconocido en el servidor";
    }
}