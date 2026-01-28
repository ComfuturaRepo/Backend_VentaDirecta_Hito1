package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ExcelImportDTO;
import com.backend.comfutura.dto.request.ImportResultDTO;
import com.backend.comfutura.dto.request.OtCreateRequest;
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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExcelImportService {

    private final OtService otService;
    private final DropdownService dropdownService;
    private final SiteRepository siteRepository;

    // ================ IMPORTACIÓN DE EXCEL ================
    @Transactional(rollbackFor = Exception.class)
    public ImportResultDTO importOtsFromExcel(MultipartFile file) throws IOException {
        ImportResultDTO result = new ImportResultDTO();
        result.setInicio(System.currentTimeMillis());

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
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null || isRowEmpty(row)) continue;

                ExcelImportDTO registro = parseRowToDTO(row, columnIndex, i + 1);
                validarRegistro(registro);

                if (registro.isValido()) {
                    registro.setOt(siguienteOt++);

                    // Validar y convertir a request ANTES de guardar
                    try {
                        OtCreateRequest request = convertirARequest(registro);
                        registro.setTempRequest(request);
                    } catch (Exception e) {
                        registro.setValido(false);
                        registro.setMensajeError("Error en validación: " + e.getMessage());
                        log.warn("Validación fallida fila {}: {}", i + 1, e.getMessage());
                    }
                }
                registros.add(registro);
            }

            result.setTotalRegistros(registros.size());

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
                    } catch (Exception e) {
                        log.error("Error al guardar OT {}: {}", registro.getOt(), e.getMessage(), e);
                        registro.setValido(false);
                        registro.setMensajeError("Error al guardar OT " + registro.getOt() + ": " +
                                getErrorMessage(e));
                        errores.add(registro);
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
            result.setMensaje(result.getExitosos() + " OTs importadas exitosamente");

            // Si no se creó ninguna OT, lanzar excepción para rollback
            if (exitosos.isEmpty() && !registros.isEmpty()) {
                throw new IOException("No se pudo crear ninguna OT. Verifique los datos.");
            }

        } catch (Exception e) {
            log.error("Error al procesar archivo Excel", e);
            throw new IOException("Error al procesar archivo Excel: " + getErrorMessage(e), e);
        } finally {
            result.setFin(System.currentTimeMillis());
            result.setDuracionMs(result.getFin() - result.getInicio());
        }

        return result;
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

    // ================ GENERACIÓN DE TEMPLATE ================
    public byte[] generateImportTemplate() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Crear hoja principal
            Sheet sheet = workbook.createSheet("Plantilla Importación OT");

            // Crear hoja visible para todos los datos de combos
            Sheet hojaCombos = workbook.createSheet("DATOS_COMBOS");
            workbook.setSheetOrder("DATOS_COMBOS", 1); // Ponerla como segunda hoja

            // Crear encabezado para la hoja de combos
            Row headerCombos = hojaCombos.createRow(0);
            headerCombos.createCell(0).setCellValue("CATEGORIA");
            headerCombos.createCell(1).setCellValue("VALOR");

            // Crear estilos
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle fechaStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            fechaStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

            String[] headers = {
                    "fechaApertura",                  // 0
                    "cliente",                        // 1
                    "area",                           // 2
                    "proyecto",                       // 3
                    "fase",                           // 4
                    "site",                           // 5
                    "region",                         // 6
                    "estado",                         // 7
                    "otAnterior",                     // 8
                    "JefaturaClienteSolicitante",     // 9
                    "AnalistaClienteSolicitante",     // 10
                    "CoordinadorTiCw",                // 11
                    "JefaturaResponsable",            // 12
                    "Liquidador",                     // 13
                    "Ejecutante",                     // 14
                    "AnalistaContable"                // 15
            };

            // Crear fila de encabezados
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Fila de ejemplo
            Row ejemploRow = sheet.createRow(1);

            // Fecha apertura (HOY)
            Cell fechaCell = ejemploRow.createCell(0);
            fechaCell.setCellValue(new java.util.Date());
            fechaCell.setCellStyle(fechaStyle);

            // Llenar valores de ejemplo
            llenarValoresEjemplo(ejemploRow);

            // Aplicar dropdowns VISIBLES usando la nueva hoja de combos
            aplicarDropdownsConHojaVisible(workbook, sheet, hojaCombos);

            // Aplicar estilos a las filas
            aplicarEstilosFilas(workbook, sheet, headers.length, fechaStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.max(currentWidth, 4000));
            }

            // Ajustar ancho de columnas en hoja de combos
            hojaCombos.autoSizeColumn(0);
            hojaCombos.autoSizeColumn(1);
            hojaCombos.setColumnWidth(0, Math.max(hojaCombos.getColumnWidth(0), 5000));
            hojaCombos.setColumnWidth(1, Math.max(hojaCombos.getColumnWidth(1), 8000));

            // Congelar paneles (fila de encabezado)
            sheet.createFreezePane(0, 1);

            workbook.write(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            log.error("Error al generar template Excel: {}", e.getMessage(), e);
            throw new IOException("Error al generar plantilla Excel: " + e.getMessage(), e);
        }
    }

    // ================ MÉTODOS PRIVADOS PRINCIPALES ================

    // Método para aplicar dropdowns usando la hoja visible de combos
    private void aplicarDropdownsConHojaVisible(XSSFWorkbook workbook, Sheet sheetPrincipal, Sheet hojaCombos) {
        try {
            log.info("=== APLICANDO DROPDOWNS CON HOJA VISIBLE DE COMBOS ===");

            // Contador para filas en la hoja de combos
            int currentRow = 1; // Empezar después del encabezado

            // Mapeo de columnas con sus datos
            Object[][] configs = {
                    {1, "Cliente", dropdownService.getClientes()},
                    {2, "Área", dropdownService.getAreas()},
                    {3, "Proyecto", dropdownService.getProyectos()},
                    {4, "Fase", dropdownService.getFases()},
                    {5, "Site", dropdownService.getSites()},
                    {6, "Región", dropdownService.getRegiones()},
                    {7, "Estado", null},
                    {9, "Jefatura Cliente", dropdownService.getJefaturasClienteSolicitante()},
                    {10, "Analista Cliente", dropdownService.getAnalistasClienteSolicitante()},
                    {11, "Coordinador Ti CW", dropdownService.getCoordinadoresTiCw()},
                    {12, "Jefatura Responsable", dropdownService.getJefaturasResponsable()},
                    {13, "Liquidador", dropdownService.getLiquidador()},
                    {14, "Ejecutante", dropdownService.getEjecutantes()},
                    {15, "Analista Contable", dropdownService.getAnalistasContable()}
            };

            for (Object[] config : configs) {
                int colIndex = (int) config[0];
                String nombreCampo = (String) config[1];
                Object datos = config[2];

                if (colIndex == 7) {
                    // Estado especial - solo "ASIGNACION"
                    crearDropdownConFlecha(sheetPrincipal, colIndex, Arrays.asList("ASIGNACION"), nombreCampo);
                } else if (datos instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<DropdownDTO> items = (List<DropdownDTO>) datos;

                    if (items != null && !items.isEmpty()) {
                        // Guardar en hoja visible de combos
                        int startRow = currentRow;

                        // Escribir nombre del campo como categoría
                        Row categoryRow = hojaCombos.createRow(currentRow++);
                        categoryRow.createCell(0).setCellValue(nombreCampo + ":");
                        categoryRow.createCell(1).setCellValue("LISTA DE VALORES");

                        // Escribir todos los valores
                        int itemCount = 0;
                        int maxItems = Math.min(items.size(), 1000); // Límite amplio

                        for (int i = 0; i < maxItems; i++) {
                            DropdownDTO item = items.get(i);
                            String valor = item.label();
                            if (item.adicional() != null && !item.adicional().isBlank()) {
                                valor += " " + item.adicional();
                            }

                            Row dataRow = hojaCombos.createRow(currentRow++);
                            dataRow.createCell(0).setCellValue("");
                            dataRow.createCell(1).setCellValue(valor.trim());
                            itemCount++;
                        }

                        // Crear rango para la validación
                        String rango = "'DATOS_COMBOS'!$B$" + (startRow + 2) + ":$B$" + (startRow + 1 + itemCount);

                        // Crear dropdown en hoja principal usando el rango
                        crearDropdownDesdeRango(workbook, sheetPrincipal, colIndex, rango, nombreCampo);

                        // Dejar una fila en blanco entre categorías
                        currentRow++;
                    }
                }
            }

            log.info("✅ Todos los dropdowns aplicados exitosamente desde hoja visible");

        } catch (Exception e) {
            log.error("❌ Error aplicando dropdowns con hoja visible: {}", e.getMessage(), e);
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

            // Configurar para mostrar flecha
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

            log.info("✅ Dropdown '{}' creado desde rango: {}", nombreCampo, rango);

        } catch (Exception e) {
            log.error("❌ Error creando dropdown desde rango para '{}': {}", nombreCampo, e.getMessage());

            // Fallback: crear dropdown simple
            List<String> valoresBasicos = Arrays.asList("SELECCIONE...", "OPCIÓN 1", "OPCIÓN 2");
            crearDropdownConFlecha(sheetPrincipal, colIndex, valoresBasicos, nombreCampo);
        }
    }

    // Método principal para crear dropdown con flecha visible (para listas pequeñas)
    private void crearDropdownConFlecha(Sheet sheet, int colIndex, List<String> valores, String nombreCampo) {
        if (valores == null || valores.isEmpty()) {
            log.warn("Lista vacía para '{}' (columna {})", nombreCampo, colIndex);
            return;
        }

        try {
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

            // Crear constraint con los valores
            DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(
                    valores.toArray(new String[0])
            );

            // Definir rango de celdas
            CellRangeAddressList addressList = new CellRangeAddressList(1, 10000, colIndex, colIndex);

            // Crear validación
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);

            // 🔥 Mostrar flecha del dropdown
            validation.setSuppressDropDownArrow(false);

            // Configurar mensajes
            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.createErrorBox("Valor no permitido",
                    "Debe seleccionar un valor de la lista para: " + nombreCampo);

            // Mensaje emergente al seleccionar celda
            validation.setShowPromptBox(true);
            validation.createPromptBox("Seleccione " + nombreCampo,
                    "Haga clic aquí o presione ALT+Flecha Abajo para ver opciones");

            // Aplicar a la hoja
            sheet.addValidationData(validation);

            log.info("✅ Dropdown VISIBLE para '{}' en columna {} ({} opciones)",
                    nombreCampo, colIndex, valores.size());

        } catch (Exception e) {
            log.error("❌ Error en dropdown para '{}': {}", nombreCampo, e.getMessage());
        }
    }

    // Método para llenar valores de ejemplo
    private void llenarValoresEjemplo(Row ejemploRow) {
        try {
            // Cliente
            List<DropdownDTO> clientes = dropdownService.getClientes();
            if (!clientes.isEmpty()) {
                ejemploRow.createCell(1).setCellValue(clientes.get(0).label());
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

            // Región
            List<DropdownDTO> regiones = dropdownService.getRegiones();
            if (!regiones.isEmpty()) {
                ejemploRow.createCell(6).setCellValue(regiones.get(0).label());
            }

            // Estado - siempre ASIGNACION
            ejemploRow.createCell(7).setCellValue("ASIGNACION");

            // otAnterior - vacío (opcional)
            ejemploRow.createCell(8).setCellValue("");

            // Responsables
            List<DropdownDTO> jefaturasCliente = dropdownService.getJefaturasClienteSolicitante();
            if (!jefaturasCliente.isEmpty()) {
                ejemploRow.createCell(9).setCellValue(jefaturasCliente.get(0).label());
            }

            List<DropdownDTO> analistasCliente = dropdownService.getAnalistasClienteSolicitante();
            if (!analistasCliente.isEmpty()) {
                ejemploRow.createCell(10).setCellValue(analistasCliente.get(0).label());
            }

            List<DropdownDTO> coordinadores = dropdownService.getCoordinadoresTiCw();
            if (!coordinadores.isEmpty()) {
                ejemploRow.createCell(11).setCellValue(coordinadores.get(0).label());
            }

            List<DropdownDTO> jefaturasResponsable = dropdownService.getJefaturasResponsable();
            if (!jefaturasResponsable.isEmpty()) {
                ejemploRow.createCell(12).setCellValue(jefaturasResponsable.get(0).label());
            }

            List<DropdownDTO> liquidador = dropdownService.getLiquidador();
            if (!liquidador.isEmpty()) {
                ejemploRow.createCell(13).setCellValue(liquidador.get(0).label());
            }

            List<DropdownDTO> ejecutantes = dropdownService.getEjecutantes();
            if (!ejecutantes.isEmpty()) {
                ejemploRow.createCell(14).setCellValue(ejecutantes.get(0).label());
            }

            List<DropdownDTO> analistasContable = dropdownService.getAnalistasContable();
            if (!analistasContable.isEmpty()) {
                ejemploRow.createCell(15).setCellValue(analistasContable.get(0).label());
            }
        } catch (Exception e) {
            log.warn("Error al llenar valores de ejemplo: {}", e.getMessage());
        }
    }

    // Método para aplicar estilos a las filas
    private void aplicarEstilosFilas(Workbook workbook, Sheet sheet, int numColumnas, CellStyle fechaStyle) {
        // Crear estilos
        CellStyle dropdownStyle = crearEstiloDropdown(workbook);
        CellStyle opcionalStyle = crearEstiloOpcional(workbook);

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

                // Aplicar estilo según el tipo de columna
                if (colNum == 0) {
                    // Columna fecha
                    cell.setCellStyle(fechaStyle);
                } else if (colNum == 8) { // otAnterior es la columna 8
                    // Columna opcional
                    cell.setCellStyle(opcionalStyle);
                } else {
                    // Columnas con dropdown
                    cell.setCellStyle(dropdownStyle);
                }
            }
        }
    }

    // ================ MÉTODOS AUXILIARES DE ESTILO ================
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

    // ================ MÉTODOS DE IMPORTACIÓN ================
    private Map<String, Integer> mapColumnHeaders(Row headerRow) {
        Map<String, Integer> columnIndex = new HashMap<>();
        for (Cell cell : headerRow) {
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String headerName = cell.getStringCellValue().trim().toLowerCase();
                headerName = headerName
                        .replace("á", "a").replace("é", "e").replace("í", "i")
                        .replace("ó", "o").replace("ú", "u")
                        .replace("ñ", "n")
                        .replaceAll("[^a-z0-9]", "");
                columnIndex.put(headerName, cell.getColumnIndex());
            }
        }
        return columnIndex;
    }

    private void verificarEncabezadosRequeridos(Map<String, Integer> columnIndex) throws IOException {
        List<String> encabezadosObligatorios = Arrays.asList(
                "fechaapertura", "cliente", "area", "proyecto",
                "fase", "site", "region", "estado"
        );
        List<String> faltantes = new ArrayList<>();
        for (String requerido : encabezadosObligatorios) {
            if (!columnIndex.containsKey(requerido)) {
                faltantes.add(requerido);
            }
        }
        if (!faltantes.isEmpty()) {
            throw new IOException("Faltan encabezados obligatorios: " + String.join(", ", faltantes));
        }
    }

    private ExcelImportDTO parseRowToDTO(Row row,
                                         Map<String, Integer> columnIndex,
                                         int fila) {

        ExcelImportDTO dto = new ExcelImportDTO();
        dto.setFilaExcel(fila);
        dto.setValido(true);

        try {
            // =========================
            // FECHA APERTURA
            // =========================
            if (columnIndex.containsKey("fechaapertura")) {
                dto.setFechaApertura(
                        parseFecha(row.getCell(columnIndex.get("fechaapertura")))
                );
            }

            // =========================
            // CLIENTE
            // =========================
            if (columnIndex.containsKey("cliente")) {
                dto.setCliente(getStringCellValue(row, columnIndex.get("cliente")));
            }

            // =========================
            // ÁREA
            // =========================
            if (columnIndex.containsKey("area")) {
                dto.setArea(getStringCellValue(row, columnIndex.get("area")));
            }

            // =========================
            // PROYECTO
            // =========================
            if (columnIndex.containsKey("proyecto")) {
                dto.setProyecto(getStringCellValue(row, columnIndex.get("proyecto")));
            }

            // =========================
            // FASE
            // =========================
            if (columnIndex.containsKey("fase")) {
                dto.setFase(getStringCellValue(row, columnIndex.get("fase")));
            }

            // =========================
            // SITE (texto completo: CODIGO + DESCRIPCIÓN)
            // =========================
            if (columnIndex.containsKey("site")) {
                dto.setSite(getStringCellValue(row, columnIndex.get("site")));
            }

            // =========================
            // REGIÓN
            // =========================
            if (columnIndex.containsKey("region")) {
                dto.setRegion(getStringCellValue(row, columnIndex.get("region")));
            }

            // =========================
            // ESTADO
            // =========================
            if (columnIndex.containsKey("estado")) {
                dto.setEstado(getStringCellValue(row, columnIndex.get("estado")));
            }

            // =========================
            // OT ANTERIOR
            // =========================
            if (columnIndex.containsKey("otanterior")) {
                dto.setOtAnterior(
                        getNumericCellValue(row, columnIndex.get("otanterior"))
                );
            }

            // =========================
            // JEFATURA CLIENTE
            // =========================
            if (columnIndex.containsKey("jefaturaclientesolicitante")) {
                dto.setJefaturaClienteSolicitante(
                        getStringCellValue(row, columnIndex.get("jefaturaclientesolicitante"))
                );
            } else if (columnIndex.containsKey("jefatura")) {
                dto.setJefaturaClienteSolicitante(
                        getStringCellValue(row, columnIndex.get("jefatura"))
                );
            }

            // =========================
            // ANALISTA CLIENTE
            // =========================
            if (columnIndex.containsKey("analistaclientesolicitante")) {
                dto.setAnalistaClienteSolicitante(
                        getStringCellValue(row, columnIndex.get("analistaclientesolicitante"))
                );
            } else if (columnIndex.containsKey("analista")) {
                dto.setAnalistaClienteSolicitante(
                        getStringCellValue(row, columnIndex.get("analista"))
                );
            }

            // =========================
            // COORDINADOR TI CW
            // =========================
            if (columnIndex.containsKey("coordinadorticw")) {
                dto.setCoordinadorTiCw(
                        getStringCellValue(row, columnIndex.get("coordinadorticw"))
                );
            }

            // =========================
            // JEFATURA RESPONSABLE
            // =========================
            if (columnIndex.containsKey("jefaturaresponsable")) {
                dto.setJefaturaResponsable(
                        getStringCellValue(row, columnIndex.get("jefaturaresponsable"))
                );
            }

            // =========================
            // LIQUIDADOR
            // =========================
            if (columnIndex.containsKey("liquidador")) {
                dto.setLiquidador(
                        getStringCellValue(row, columnIndex.get("liquidador"))
                );
            }

            // =========================
            // EJECUTANTE
            // =========================
            if (columnIndex.containsKey("ejecutante")) {
                dto.setEjecutante(
                        getStringCellValue(row, columnIndex.get("ejecutante"))
                );
            }

            // =========================
            // ANALISTA CONTABLE
            // =========================
            if (columnIndex.containsKey("analistacontable")) {
                dto.setAnalistaContable(
                        getStringCellValue(row, columnIndex.get("analistacontable"))
                );
            }

        } catch (Exception e) {
            dto.setValido(false);
            dto.setMensajeError("Error al leer fila Excel: " + e.getMessage());
            log.warn("Error parse fila {}: {}", fila, e.getMessage());
        }

        return dto;
    }

    private void validarCampoObligatorioYExistente(
            String valor,
            List<DropdownDTO> listaValida,
            String nombreCampo,
            List<String> errores) {

        if (valor == null || valor.trim().isEmpty()) {
            errores.add(nombreCampo + " es obligatorio");
            return;
        }

        boolean existe = listaValida.stream()
                .anyMatch(dto -> dto.label() != null &&
                        dto.label().trim().equalsIgnoreCase(valor.trim()));

        if (!existe) {
            errores.add(nombreCampo + " '" + valor.trim() + "' no existe en el sistema");
        }
    }

    private void validarRegistro(ExcelImportDTO dto) {
        List<String> errores = new ArrayList<>();

        // =========================
        // Fecha de apertura
        // =========================
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
        }

        // =========================
        // Dropdowns simples
        // =========================
        if (dto.getCliente() == null || dto.getCliente().trim().isEmpty()) {
            errores.add("Cliente es obligatorio");
        } else if (!existeCliente(dto.getCliente())) {
            errores.add("Cliente '" + dto.getCliente() + "' no existe en el sistema");
        }

        if (dto.getArea() == null || dto.getArea().trim().isEmpty()) {
            errores.add("Área es obligatoria");
        } else if (!existeArea(dto.getArea())) {
            errores.add("Área '" + dto.getArea() + "' no existe en el sistema");
        }

        if (dto.getProyecto() == null || dto.getProyecto().trim().isEmpty()) {
            errores.add("Proyecto es obligatorio");
        } else if (!existeProyecto(dto.getProyecto())) {
            errores.add("Proyecto '" + dto.getProyecto() + "' no existe en el sistema");
        }

        if (dto.getFase() == null || dto.getFase().trim().isEmpty()) {
            errores.add("Fase es obligatoria");
        } else if (!existeFase(dto.getFase())) {
            errores.add("Fase '" + dto.getFase() + "' no existe en el sistema");
        }

        // =========================
        // Site (ÚNICO CAMPO)
        // =========================
        if (dto.getSite() == null || dto.getSite().trim().isEmpty()) {
            errores.add("Site es obligatorio");
        } else {
            boolean existe = siteRepository.findAll().stream()
                    .anyMatch(site ->
                            (site.getCodigoSitio() + " " + site.getDescripcion())
                                    .equalsIgnoreCase(dto.getSite().trim())
                    );

            if (!existe) {
                errores.add("Site '" + dto.getSite() + "' no existe en el sistema");
            }
        }

        if (dto.getRegion() == null || dto.getRegion().trim().isEmpty()) {
            errores.add("Región es obligatoria");
        } else if (!existeRegion(dto.getRegion())) {
            errores.add("Región '" + dto.getRegion() + "' no existe en el sistema");
        }

        // =========================
        // Estado fijo
        // =========================
        if (dto.getEstado() == null || !"ASIGNACION".equalsIgnoreCase(dto.getEstado().trim())) {
            errores.add("Estado debe ser siempre 'ASIGNACION'");
        }

        // =========================
        // Responsables obligatorios
        // =========================
        validarCampoObligatorioYExistente(
                dto.getCoordinadorTiCw(),
                dropdownService.getCoordinadoresTiCw(),
                "Coordinador Ti CW",
                errores
        );

        validarCampoObligatorioYExistente(
                dto.getJefaturaResponsable(),
                dropdownService.getJefaturasResponsable(),
                "Jefatura Responsable",
                errores
        );

        validarCampoObligatorioYExistente(
                dto.getLiquidador(),
                dropdownService.getLiquidador(),
                "Liquidador",
                errores
        );

        validarCampoObligatorioYExistente(
                dto.getEjecutante(),
                dropdownService.getEjecutantes(),
                "Ejecutante",
                errores
        );

        validarCampoObligatorioYExistente(
                dto.getAnalistaContable(),
                dropdownService.getAnalistasContable(),
                "Analista Contable",
                errores
        );

        // =========================
        // Resultado
        // =========================
        if (!errores.isEmpty()) {
            dto.setValido(false);
            dto.setMensajeError(String.join("; ", errores));
        }
    }

    private OtCreateRequest convertirARequest(ExcelImportDTO importDTO) {
        OtCreateRequest request = new OtCreateRequest();
        request.setFechaApertura(importDTO.getFechaApertura());
        request.setActivo(true);

        // =========================
        // Resolver SITE desde Excel
        // =========================
        String siteExcel = importDTO.getSite().trim();

        Site sitio = siteRepository.findAll().stream()
                .filter(s ->
                        (s.getCodigoSitio() + " " + s.getDescripcion())
                                .equalsIgnoreCase(siteExcel)
                )
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("No se encontró Site: " + siteExcel)
                );

        request.setIdSite(sitio.getIdSite());

        // =========================
        // Descripción automática
        // =========================
        String descripcion = String.format("%s_%s_%s",
                normalizeForDescripcion(importDTO.getProyecto()),
                normalizeForDescripcion(importDTO.getArea()),
                normalizeForDescripcion(sitio.getDescripcion())
        ).replaceAll("_+", "_").replaceAll("^_|_$", "");

        if (descripcion.isEmpty()) {
            descripcion = "OT SIN DESCRIPCION AUTOMATICA";
        }

        request.setDescripcion(descripcion);

        // =========================
        // IDs por dropdown - con validaciones
        // =========================
        Integer idCliente = buscarIdPorNombre(dropdownService.getClientes(), importDTO.getCliente());
        if (idCliente == null) {
            throw new RuntimeException("Cliente no encontrado: " + importDTO.getCliente());
        }
        request.setIdCliente(idCliente);

        Integer idArea = buscarIdPorNombre(dropdownService.getAreas(), importDTO.getArea());
        if (idArea == null) {
            throw new RuntimeException("Área no encontrada: " + importDTO.getArea());
        }
        request.setIdArea(idArea);

        Integer idProyecto = buscarIdPorNombre(dropdownService.getProyectos(), importDTO.getProyecto());
        if (idProyecto == null) {
            throw new RuntimeException("Proyecto no encontrado: " + importDTO.getProyecto());
        }
        request.setIdProyecto(idProyecto);

        Integer idFase = buscarIdPorNombre(dropdownService.getFases(), importDTO.getFase());
        if (idFase == null) {
            throw new RuntimeException("Fase no encontrada: " + importDTO.getFase());
        }
        request.setIdFase(idFase);

        Integer idRegion = buscarIdPorNombre(dropdownService.getRegiones(), importDTO.getRegion());
        if (idRegion == null) {
            throw new RuntimeException("Región no encontrada: " + importDTO.getRegion());
        }
        request.setIdRegion(idRegion);

        Integer idEstado = buscarIdPorNombre(dropdownService.getEstadosOt(), "ASIGNACION");
        if (idEstado == null) {
            throw new RuntimeException("Estado 'ASIGNACION' no encontrado");
        }
        request.setIdEstadoOt(idEstado);

        if (importDTO.getOtAnterior() != null) {
            Integer idOtsAnterior = otService.buscarIdPorOt(importDTO.getOtAnterior());
            request.setIdOtsAnterior(idOtsAnterior);
        }

        // Campos opcionales
        Integer idJefaturaCliente = buscarIdPorNombre(
                dropdownService.getJefaturasClienteSolicitante(),
                importDTO.getJefaturaClienteSolicitante()
        );
        request.setIdJefaturaClienteSolicitante(idJefaturaCliente);

        Integer idAnalistaCliente = buscarIdPorNombre(
                dropdownService.getAnalistasClienteSolicitante(),
                importDTO.getAnalistaClienteSolicitante()
        );
        request.setIdAnalistaClienteSolicitante(idAnalistaCliente);

        // Campos obligatorios
        Integer idCoordinador = buscarIdPorNombre(
                dropdownService.getCoordinadoresTiCw(),
                importDTO.getCoordinadorTiCw()
        );
        if (idCoordinador == null) {
            throw new RuntimeException("Coordinador Ti CW no encontrado: " + importDTO.getCoordinadorTiCw());
        }
        request.setIdCoordinadorTiCw(idCoordinador);

        Integer idJefaturaResponsable = buscarIdPorNombre(
                dropdownService.getJefaturasResponsable(),
                importDTO.getJefaturaResponsable()
        );
        if (idJefaturaResponsable == null) {
            throw new RuntimeException("Jefatura Responsable no encontrada: " + importDTO.getJefaturaResponsable());
        }
        request.setIdJefaturaResponsable(idJefaturaResponsable);

        Integer idLiquidador = buscarIdPorNombre(
                dropdownService.getLiquidador(),
                importDTO.getLiquidador()
        );
        if (idLiquidador == null) {
            throw new RuntimeException("Liquidador no encontrado: " + importDTO.getLiquidador());
        }
        request.setIdLiquidador(idLiquidador);

        Integer idEjecutante = buscarIdPorNombre(
                dropdownService.getEjecutantes(),
                importDTO.getEjecutante()
        );
        if (idEjecutante == null) {
            throw new RuntimeException("Ejecutante no encontrado: " + importDTO.getEjecutante());
        }
        request.setIdEjecutante(idEjecutante);

        Integer idAnalistaContable = buscarIdPorNombre(
                dropdownService.getAnalistasContable(),
                importDTO.getAnalistaContable()
        );
        if (idAnalistaContable == null) {
            throw new RuntimeException("Analista Contable no encontrado: " + importDTO.getAnalistaContable());
        }
        request.setIdAnalistaContable(idAnalistaContable);

        return request;
    }

    // ================ MÉTODOS AUXILIARES DE VALIDACIÓN ================
    private boolean existeCliente(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getClientes().stream()
                .anyMatch(c -> c.label() != null && c.label().trim().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeArea(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getAreas().stream()
                .anyMatch(a -> a.label() != null && a.label().trim().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeProyecto(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getProyectos().stream()
                .anyMatch(p -> p.label() != null && p.label().trim().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeFase(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getFases().stream()
                .anyMatch(f -> f.label() != null && f.label().trim().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeRegion(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getRegiones().stream()
                .anyMatch(r -> r.label() != null && r.label().trim().equalsIgnoreCase(nombre.trim()));
    }

    private Integer buscarIdPorNombre(List<DropdownDTO> lista, String nombre) {
        if (nombre == null || nombre.trim().isEmpty() || lista == null) {
            return null;
        }
        String searchName = nombre.trim();
        return lista.stream()
                .filter(dto -> dto.label() != null && dto.label().trim().equalsIgnoreCase(searchName))
                .map(DropdownDTO::id)
                .findFirst()
                .orElse(null);
    }

    private String normalizeForDescripcion(String str) {
        if (str == null) return "";
        return str.trim()
                .replaceAll("[^\\w\\s]", "")   // elimina caracteres especiales
                .replaceAll("\\s+", " ");      // normaliza espacios
    }

    // ================ MÉTODOS AUXILIARES DE CELDAS ================
    private String getStringCellValue(Row row, int columnIndex) {
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
        Cell cell = row.getCell(columnIndex);
        if (cell == null) return null;

        try {
            if (cell.getCellType() == CellType.NUMERIC) {
                double value = cell.getNumericCellValue();
                return (int) Math.round(value);
            } else if (cell.getCellType() == CellType.STRING) {
                String value = cell.getStringCellValue().trim();
                if (value.isEmpty()) return null;
                value = value.replaceAll("[^0-9]", "");
                if (value.isEmpty()) return null;
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
            return null;
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
}