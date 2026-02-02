package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ExcelImportDTO;
import com.backend.comfutura.dto.request.ImportResultDTO;
import com.backend.comfutura.dto.request.otDTO.OtCreateRequest;
import com.backend.comfutura.model.Site;
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
            result.setExito(errores.isEmpty());
            result.generarMensajeResumen();

            log.info("=== RESUMEN IMPORTACIÓN ===");
            log.info("Exitosos: {}, Fallidos: {}", result.getExitosos(), result.getFallidos());
            log.info("Errores validación: {}, Errores persistencia: {}",
                    result.getErroresValidacion(), result.getErroresPersistencia());

            if (exitosos.isEmpty() && !registros.isEmpty()) {
                throw new IOException("No se pudo crear ninguna OT. Verifique los datos.");
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
            Sheet sheet = workbook.createSheet("Plantilla Importación OT");

            // === HOJA VISIBLE: DATOS DE COMBOS ===
            Sheet hojaCombos = workbook.createSheet("📋 DATOS_COMBOS");
            workbook.setSheetOrder("📋 DATOS_COMBOS", 1);

            // === HOJA VISIBLE: INSTRUCCIONES ===
            Sheet hojaInstrucciones = workbook.createSheet("📝 INSTRUCCIONES");
            workbook.setSheetOrder("📝 INSTRUCCIONES", 2);

            // Crear hoja de instrucciones
            crearHojaInstrucciones(hojaInstrucciones);

            // Crear encabezados hoja combos
            crearEncabezadosCombos(hojaCombos);

            // Crear estilos
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle fechaStyle = crearEstiloFecha(workbook);
            CellStyle dropdownStyle = crearEstiloDropdown(workbook);
            CellStyle opcionalStyle = crearEstiloOpcional(workbook);
            CellStyle obligatorioStyle = crearEstiloObligatorio(workbook);
            CellStyle condicionalStyle = crearEstiloCondicional(workbook);

            // Encabezados con nuevos campos
            String[] headers = {
                    "fechaApertura",                  // 0 - Obligatorio
                    "cliente",                        // 1 - Obligatorio
                    "area",                           // 2 - Obligatorio
                    "proyecto",                       // 3 - Obligatorio
                    "fase",                           // 4 - Obligatorio
                    "site",                           // 5 - Obligatorio
                    "region",                         // 6 - Obligatorio
                    "tipoOt",                         // 7 - Obligatorio (NUEVO)
                    "estado",                         // 8 - Obligatorio
                    "otAnterior",                     // 9 - Condicional
                    "JefaturaClienteSolicitante",     // 10 - Obligatorio
                    "AnalistaClienteSolicitante",     // 11 - Obligatorio
                    "CoordinadorTiCw",                // 12 - Obligatorio
                    "JefaturaResponsable",            // 13 - Obligatorio
                    "Liquidador",                     // 14 - Obligatorio
                    "Ejecutante",                     // 15 - Obligatorio
                    "AnalistaContable"                // 16 - Obligatorio
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

            // Aplicar dropdowns desde hoja visible de combos
            aplicarDropdownsCompletos(workbook, sheet, hojaCombos);

            // Aplicar estilos según tipo de campo
            aplicarEstilosPorTipo(sheet, headers.length, fechaStyle, dropdownStyle,
                    opcionalStyle, obligatorioStyle, condicionalStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.max(currentWidth, 3500));
            }

            // Ajustar hoja de combos
            hojaCombos.autoSizeColumn(0);
            hojaCombos.autoSizeColumn(1);
            hojaCombos.setColumnWidth(0, Math.max(hojaCombos.getColumnWidth(0), 5000));
            hojaCombos.setColumnWidth(1, Math.max(hojaCombos.getColumnWidth(1), 8000));

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
                "2️⃣ Los campos marcados con * son OBLIGATORIOS",
                "3️⃣ Campos CONDICIONALES:",
                "   - otAnterior: Obligatorio si fechaApertura es del año anterior",
                "   - Límite: Máximo 2,147,483,647",
                "4️⃣ Use los dropdowns (flechas ▼) para seleccionar valores válidos",
                "5️⃣ Fecha: Formato dd/mm/aaaa",
                "6️⃣ Para ver todos los valores posibles, revise la hoja '📋 DATOS_COMBOS'",
                "7️⃣ Estado OT: Siempre debe ser 'ASIGNACION'",
                "8️⃣ Máximo de filas por importación: 1,000",
                "",
                "⚠️ VALIDACIONES:",
                "- Fecha no puede ser futura",
                "- Todos los valores deben existir en el sistema",
                "- OT anterior debe ser un número entero válido",
                "- Se verificará que los responsables existan"
        };

        for (int i = 0; i < instrucciones.length; i++) {
            Row row = hojaInstrucciones.createRow(i + 2);
            row.createCell(0).setCellValue(instrucciones[i]);
        }

        hojaInstrucciones.autoSizeColumn(0);
    }

    private void crearEncabezadosCombos(Sheet hojaCombos) {
        Row header = hojaCombos.createRow(0);
        header.createCell(0).setCellValue("CATEGORÍA");
        header.createCell(1).setCellValue("VALORES VÁLIDOS");
        header.createCell(2).setCellValue("TIPO");
        header.createCell(3).setCellValue("OBSERVACIONES");
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
            case 7: mensaje = "Tipo OT (OBLIGATORIO)\nSeleccione de la lista"; break;
            case 8: mensaje = "Estado OT (OBLIGATORIO)\nSiempre: ASIGNACION"; break;
            case 9: mensaje = "OT Anterior (CONDICIONAL)\nObligatorio si fecha es del año anterior\nLímite: 2,147,483,647"; break;
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
        if (!clientes.isEmpty()) {
            ejemploRow.createCell(1).setCellValue(clientes.get(0).label());
        }

        // Área
        List<DropdownDTO> areas = dropdownService.getAreas();
        if (!areas.isEmpty()) {
            ejemploRow.createCell(2).setCellValue(areas.get(0).label());
        }

        // Proyecto
        List<DropdownDTO> proyectos = dropdownService.getProyectos();
        if (!proyectos.isEmpty()) {
            ejemploRow.createCell(3).setCellValue(proyectos.get(0).label());
        }

        // Fase
        List<DropdownDTO> fases = dropdownService.getFases();
        if (!fases.isEmpty()) {
            ejemploRow.createCell(4).setCellValue(fases.get(0).label());
        }

        // Site
        List<DropdownDTO> sites = dropdownService.getSites();
        if (!sites.isEmpty()) {
            DropdownDTO site = sites.get(0);
            String valorSite = site.label();
            if (site.adicional() != null && !site.adicional().isBlank()) {
                valorSite += " " + site.adicional();
            }
            ejemploRow.createCell(5).setCellValue(valorSite);
        }

        // Región
        List<DropdownDTO> regiones = dropdownService.getRegiones();
        if (!regiones.isEmpty()) {
            ejemploRow.createCell(6).setCellValue(regiones.get(0).label());
        }

        // Tipo OT
        List<DropdownDTO> tiposOt = dropdownService.getOtTipo();
        if (!tiposOt.isEmpty()) {
            ejemploRow.createCell(7).setCellValue(tiposOt.get(0).label());
        }

        // Estado
        ejemploRow.createCell(8).setCellValue("ASIGNACION");

        // OT Anterior (vacío)
        ejemploRow.createCell(9).setCellValue("");

        // Responsables
        llenarResponsablesEjemplo(ejemploRow);
    }

    private void llenarResponsablesEjemplo(Row row) {
        // Jefatura Cliente
        List<DropdownDTO> jefaturas = dropdownService.getJefaturasClienteSolicitante();
        if (!jefaturas.isEmpty()) row.createCell(10).setCellValue(jefaturas.get(0).label());

        // Analista Cliente
        List<DropdownDTO> analistas = dropdownService.getAnalistasClienteSolicitante();
        if (!analistas.isEmpty()) row.createCell(11).setCellValue(analistas.get(0).label());

        // Coordinador Ti CW
        List<DropdownDTO> coordinadores = dropdownService.getCoordinadoresTiCw();
        if (!coordinadores.isEmpty()) row.createCell(12).setCellValue(coordinadores.get(0).label());

        // Jefatura Responsable
        List<DropdownDTO> jefaturasResp = dropdownService.getJefaturasResponsable();
        if (!jefaturasResp.isEmpty()) row.createCell(13).setCellValue(jefaturasResp.get(0).label());

        // Liquidador
        List<DropdownDTO> liquidador = dropdownService.getLiquidador();
        if (!liquidador.isEmpty()) row.createCell(14).setCellValue(liquidador.get(0).label());

        // Ejecutante
        List<DropdownDTO> ejecutantes = dropdownService.getEjecutantes();
        if (!ejecutantes.isEmpty()) row.createCell(15).setCellValue(ejecutantes.get(0).label());

        // Analista Contable
        List<DropdownDTO> analistasCont = dropdownService.getAnalistasContable();
        if (!analistasCont.isEmpty()) row.createCell(16).setCellValue(analistasCont.get(0).label());
    }

    private void aplicarDropdownsCompletos(XSSFWorkbook workbook, Sheet sheetPrincipal, Sheet hojaCombos) {
        try {
            log.info("=== APLICANDO DROPDOWNS COMPLETOS ===");

            // Configuración de columnas
            Object[][] configs = {
                    {1, "Cliente", dropdownService.getClientes()},
                    {2, "Área", dropdownService.getAreas()},
                    {3, "Proyecto", dropdownService.getProyectos()},
                    {4, "Fase", dropdownService.getFases()},
                    {5, "Site", dropdownService.getSites()},
                    {6, "Región", dropdownService.getRegiones()},
                    {7, "Tipo OT", dropdownService.getOtTipo()}, // ✅ NUEVO
                    {8, "Estado", Arrays.asList("ASIGNACION")},
                    {10, "Jefatura Cliente", dropdownService.getJefaturasClienteSolicitante()},
                    {11, "Analista Cliente", dropdownService.getAnalistasClienteSolicitante()},
                    {12, "Coordinador Ti CW", dropdownService.getCoordinadoresTiCw()},
                    {13, "Jefatura Responsable", dropdownService.getJefaturasResponsable()},
                    {14, "Liquidador", dropdownService.getLiquidador()},
                    {15, "Ejecutante", dropdownService.getEjecutantes()},
                    {16, "Analista Contable", dropdownService.getAnalistasContable()}
            };

            int currentRow = 1; // Empezar después del encabezado en hoja combos

            for (Object[] config : configs) {
                int colIndex = (int) config[0];
                String nombreCampo = (String) config[1];
                Object datos = config[2];

                // Guardar en hoja de combos
                if (datos instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<DropdownDTO> items = (List<DropdownDTO>) datos;

                    if (items != null && !items.isEmpty()) {
                        int startRow = currentRow;

                        // Escribir categoría
                        Row categoryRow = hojaCombos.createRow(currentRow++);
                        categoryRow.createCell(0).setCellValue(nombreCampo + ":");
                        categoryRow.createCell(2).setCellValue("OBLIGATORIO");
                        categoryRow.createCell(3).setCellValue(items.size() + " opciones disponibles");

                        // Escribir valores
                        for (int i = 0; i < Math.min(items.size(), 1000); i++) {
                            DropdownDTO item = items.get(i);
                            String valor = item.label();
                            if (item.adicional() != null && !item.adicional().isBlank()) {
                                valor += " " + item.adicional();
                            }

                            Row dataRow = hojaCombos.createRow(currentRow++);
                            dataRow.createCell(0).setCellValue("");
                            dataRow.createCell(1).setCellValue(valor.trim());
                        }

                        // Crear dropdown desde rango
                        String rango = "'📋 DATOS_COMBOS'!$B$" + (startRow + 2) + ":$B$" + (startRow + 1 + Math.min(items.size(), 1000));
                        crearDropdownDesdeRango(workbook, sheetPrincipal, colIndex, rango, nombreCampo);

                        // Espacio entre categorías
                        currentRow++;
                    }
                }
            }

            log.info("✅ Todos los dropdowns aplicados exitosamente");

        } catch (Exception e) {
            log.error("❌ Error aplicando dropdowns: {}", e.getMessage(), e);
        }
    }

    private void crearDropdownDesdeRango(XSSFWorkbook workbook, Sheet sheetPrincipal,
                                         int colIndex, String rango, String nombreCampo) {
        try {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheetPrincipal);

            DataValidationConstraint constraint =
                    dvHelper.createFormulaListConstraint(rango);

            CellRangeAddressList addressList =
                    new CellRangeAddressList(1, 10000, colIndex, colIndex);

            XSSFDataValidation validation =
                    (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);

            // Mostrar flecha del dropdown
            validation.setSuppressDropDownArrow(false);

            // Configurar mensajes
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
                } else if (colNum == 9) {
                    cell.setCellStyle(condicionalStyle); // OT Anterior (condicional)
                } else if (colNum == 8) {
                    cell.setCellStyle(obligatorioStyle); // Estado (fijo)
                } else {
                    cell.setCellStyle(dropdownStyle); // Dropdowns
                }
            }
        }
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

    // ================ MÉTODOS DE IMPORTACIÓN ================
    private Map<String, Integer> mapColumnHeaders(Row headerRow) {
        Map<String, Integer> columnIndex = new HashMap<>();
        for (Cell cell : headerRow) {
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String headerName = cell.getStringCellValue().trim().toLowerCase();
                headerName = normalizeHeaderName(headerName);
                columnIndex.put(headerName, cell.getColumnIndex());
                log.debug("Mapeado encabezado: '{}' -> columna {}", headerName, cell.getColumnIndex());
            }
        }
        return columnIndex;
    }

    private String normalizeHeaderName(String headerName) {
        return headerName
                .replace("á", "a").replace("é", "e").replace("í", "i")
                .replace("ó", "o").replace("ú", "u")
                .replace("ñ", "n")
                .replaceAll("[^a-z0-9]", "");
    }

    private void verificarEncabezadosRequeridos(Map<String, Integer> columnIndex) throws IOException {
        List<String> encabezadosObligatorios = Arrays.asList(
                "fechaapertura", "cliente", "area", "proyecto",
                "fase", "site", "region", "tipoOt", "estado"  // ✅ Agregado tipoOt
        );

        List<String> faltantes = new ArrayList<>();
        for (String requerido : encabezadosObligatorios) {
            if (!columnIndex.containsKey(requerido)) {
                faltantes.add(requerido);
            }
        }

        if (!faltantes.isEmpty()) {
            String mensaje = "Faltan encabezados obligatorios: " + String.join(", ", faltantes);
            log.error("❌ {}", mensaje);
            throw new IOException(mensaje);
        }

        log.info("✅ Todos los encabezados obligatorios están presentes");
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
            dto.setSite(getStringCellValue(row, columnIndex.get("site")));
            dto.setRegion(getStringCellValue(row, columnIndex.get("region")));
            dto.setTipoOt(getStringCellValue(row, columnIndex.get("tipoOt"))); // ✅ Nuevo
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
        validarCampoExistente(dto.getTipoOt(), dropdownService.getOtTipo(), "Tipo OT", errores); // ✅ Nuevo

        // === VALIDACIÓN DE SITE ===
        if (dto.getSite() == null || dto.getSite().trim().isEmpty()) {
            errores.add("Site es obligatorio");
        } else if (!existeSite(dto.getSite())) {
            errores.add("Site '" + dto.getSite() + "' no existe en el sistema");
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
            boolean existe = listaValida.stream()
                    .anyMatch(dto -> dto.label() != null &&
                            dto.label().trim().equalsIgnoreCase(valor.trim()));

            if (!existe) {
                // Verificar también en el adicional
                boolean existeEnAdicional = listaValida.stream()
                        .anyMatch(dto -> dto.adicional() != null &&
                                dto.adicional().trim().equalsIgnoreCase(valor.trim()));

                if (!existeEnAdicional) {
                    errores.add(nombreCampo + " '" + valor.trim() + "' no existe en el sistema");
                }
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

        // === RESOLVER SITE ===
        String siteExcel = importDTO.getSite().trim();
        Site sitio = siteRepository.findAll().stream()
                .filter(s -> (s.getCodigoSitio() + " " + s.getDescripciones())
                        .equalsIgnoreCase(siteExcel))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No se encontró Site: " + siteExcel)
                );
        request.setIdSite(sitio.getIdSite());

        // === DESCRIPCIÓN AUTOMÁTICA ===
        String descripcion = String.format("%s_%s_%s",
                normalizeForDescripcion(importDTO.getProyecto()),
                normalizeForDescripcion(importDTO.getArea()),
                normalizeForDescripcion("SITE_" + sitio.getCodigoSitio())
        ).replaceAll("_+", "_").replaceAll("^_|_$", "");

        if (descripcion.isEmpty()) {
            descripcion = "OT SIN DESCRIPCION AUTOMATICA";
        }
        request.setDescripcion(descripcion);

        // === ASIGNAR IDs ===
        request.setIdCliente(buscarIdPorNombre(dropdownService.getClientes(), importDTO.getCliente()));
        request.setIdArea(buscarIdPorNombre(dropdownService.getAreas(), importDTO.getArea()));
        request.setIdProyecto(buscarIdPorNombre(dropdownService.getProyectos(), importDTO.getProyecto()));
        request.setIdFase(buscarIdPorNombre(dropdownService.getFases(), importDTO.getFase()));
        request.setIdRegion(buscarIdPorNombre(dropdownService.getRegiones(), importDTO.getRegion()));
        request.setIdTipoOt(buscarIdPorNombre(dropdownService.getOtTipo(), importDTO.getTipoOt())); // ✅ Nuevo
        request.setIdEstadoOt(buscarIdPorNombre(dropdownService.getEstadosOt(), "ASIGNACION"));

        // === OT ANTERIOR ===
        if (importDTO.getOtAnterior() != null) {
            // Validar límite antes de asignar
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

        return request;
    }

    private void validarRequestCompleto(OtCreateRequest request) {
        List<String> errores = new ArrayList<>();

        if (request.getIdCliente() == null) errores.add("Cliente no encontrado");
        if (request.getIdArea() == null) errores.add("Área no encontrada");
        if (request.getIdProyecto() == null) errores.add("Proyecto no encontrado");
        if (request.getIdFase() == null) errores.add("Fase no encontrada");
        if (request.getIdSite() == null) errores.add("Site no encontrado");
        if (request.getIdRegion() == null) errores.add("Región no encontrada");
        if (request.getIdTipoOt() == null) errores.add("Tipo OT no encontrado"); // ✅ Nuevo
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

    // ================ MÉTODOS AUXILIARES ================
    private boolean existeSite(String nombre) {
        if (nombre == null) return false;
        return siteRepository.findAll().stream()
                .anyMatch(s -> (s.getCodigoSitio() + " " + s.getDescripciones())
                        .equalsIgnoreCase(nombre.trim()));
    }

    private Integer buscarIdPorNombre(List<DropdownDTO> lista, String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || lista == null) {
            return null;
        }
        String searchName = nombre.trim();
        return lista.stream()
                .filter(dto -> {
                    if (dto.label() != null && dto.label().trim().equalsIgnoreCase(searchName)) {
                        return true;
                    }
                    if (dto.adicional() != null && dto.adicional().trim().equalsIgnoreCase(searchName)) {
                        return true;
                    }
                    return false;
                })
                .map(DropdownDTO::id)
                .findFirst()
                .orElse(null);
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
            switch (cell.getCellType()) {
                case STRING:
                    return cell.getStringCellValue().trim();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().format(formatter);
                    }
                    double numValue = cell.getNumericCellValue();
                    if (numValue == Math.floor(numValue)) {
                        return String.valueOf((int) numValue);
                    }
                    return String.valueOf(numValue);
                default:
                    return "";
            }
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