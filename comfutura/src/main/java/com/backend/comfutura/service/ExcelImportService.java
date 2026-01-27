package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.ExcelImportDTO;
import com.backend.comfutura.dto.request.ImportResultDTO;
import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.record.DropdownDTO;
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

    // ================ IMPORTACIÓN DE EXCEL ================
    @Transactional
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
                }
                registros.add(registro);
            }

            result.setTotalRegistros(registros.size());

            List<ExcelImportDTO> exitosos = new ArrayList<>();
            List<ExcelImportDTO> errores = new ArrayList<>();

            for (ExcelImportDTO registro : registros) {
                if (registro.isValido()) {
                    try {
                        OtCreateRequest request = convertirARequest(registro);
                        otService.saveOt(request);
                        registro.setMensajeError("CREADA EXITOSAMENTE - OT: " + registro.getOt());
                        exitosos.add(registro);
                        result.incrementarExitosos();
                    } catch (Exception e) {
                        registro.setValido(false);
                        registro.setMensajeError("Error al guardar OT " + registro.getOt() + ": " + e.getMessage());
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

        } catch (Exception e) {
            log.error("Error al procesar archivo Excel", e);
            throw new IOException("Error al procesar archivo Excel: " + e.getMessage(), e);
        } finally {
            result.setFin(System.currentTimeMillis());
            result.setDuracionMs(result.getFin() - result.getInicio());
        }

        return result;
    }

    // ================ GENERACIÓN DE TEMPLATE ================
    public byte[] generateImportTemplate() throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Plantilla Importación OT");

            // Crear estilos
            CellStyle headerStyle = crearEstiloEncabezado(workbook);
            CellStyle fechaStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            fechaStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

            // ENCABEZADOS
            String[] headers = {
                    "fechaApertura",           // Obligatorio
                    "cliente",                 // Obligatorio - Dropdown
                    "area",                    // Obligatorio - Dropdown
                    "proyecto",                // Obligatorio - Dropdown
                    "fase",                    // Obligatorio - Dropdown
                    "site",                    // Obligatorio - Dropdown
                    "region",                  // Obligatorio - Dropdown
                    "estado",                  // Obligatorio - Dropdown (siempre ASIGNACION)
                    "otAnterior",              // Opcional - NO dropdown
                    "JefaturaClienteSolicitante", // Obligatorio - Dropdown
                    "AnalistaClienteSolicitante", // Obligatorio - Dropdown
                    "CoordinadorTiCw",         // Obligatorio - Dropdown
                    "JefaturaResponsable",     // Obligatorio - Dropdown
                    "Liquidador",              // Obligatorio - Dropdown
                    "Ejecutante",              // Obligatorio - Dropdown
                    "AnalistaContable"         // Obligatorio - Dropdown
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

            // Aplicar dropdowns VISIBLES
            aplicarDropdownsFuncionales(workbook, sheet);

            // Aplicar estilos a las filas
            aplicarEstilosFilas(workbook, sheet, headers.length, fechaStyle);

            // Ajustar ancho de columnas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                sheet.setColumnWidth(i, Math.max(currentWidth, 4000));
            }

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

    // Método para aplicar dropdowns funcionales con flecha visible
    private void aplicarDropdownsFuncionales(XSSFWorkbook workbook, Sheet sheet) {
        try {
            log.info("=== APLICANDO DROPDOWNS VISIBLES ===");

            // Solo las primeras 30 opciones para evitar límites
            int limite = 30;

            // Mapeo de columnas con sus datos - CORREGIDO
            Object[][] configs = {
                    {1, "Cliente", dropdownService.getClientes()},
                    {2, "Área", dropdownService.getAreas()},
                    {3, "Proyecto", dropdownService.getProyectos()},
                    {4, "Fase", dropdownService.getFases()},
                    {5, "Site", dropdownService.getSites()},
                    {6, "Región", dropdownService.getRegiones()},
                    {7, "Estado", null}, // Especial - manejado separadamente
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
                Object datos = config[2]; // Ahora es Object, no List<DropdownDTO>

                if (colIndex == 7) {
                    // Estado especial - solo "ASIGNACION"
                    List<String> estados = Arrays.asList("ASIGNACION");
                    crearDropdownConFlecha(sheet, colIndex, estados, nombreCampo);
                } else if (datos instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<DropdownDTO> items = (List<DropdownDTO>) datos;

                    if (items != null && !items.isEmpty()) {
                        // PARA CLIENTE (1) y SITE (5): Usar método para listas largas
                        if (colIndex == 1 || colIndex == 5) {
                            crearDropdownParaListaLarga(workbook, sheet, colIndex, items, nombreCampo);
                        } else {
                            // Para las demás columnas: método normal con límite
                            List<String> valores = procesarValoresParaDropdown(items, limite, nombreCampo);

                            if (!valores.isEmpty()) {
                                crearDropdownConFlecha(sheet, colIndex, valores, nombreCampo);
                            }
                        }
                    }
                }
            }

            log.info("✅ Todos los dropdowns aplicados exitosamente");

        } catch (Exception e) {
            log.error("❌ Error aplicando dropdowns: {}", e.getMessage(), e);
        }
    }    // Método para listas MUY largas (hoja oculta)
    private void crearDropdownParaListaLarga(XSSFWorkbook workbook, Sheet sheetPrincipal,
                                             int colIndex, List<DropdownDTO> items,
                                             String nombreCampo) {
        if (items == null || items.isEmpty()) return;

        try {
            // Crear hoja oculta para la lista
            String nombreHojaOculta = "Lista_" + nombreCampo.replace(" ", "");
            Sheet hojaLista = workbook.createSheet(nombreHojaOculta);
            workbook.setSheetHidden(workbook.getSheetIndex(hojaLista), true);

            // Escribir valores en columna A
            for (int i = 0; i < Math.min(items.size(), 100); i++) { // Máximo 100 items
                DropdownDTO item = items.get(i);
                if (item != null && item.label() != null) {
                    Row row = hojaLista.createRow(i);
                    row.createCell(0).setCellValue(item.label().trim());
                }
            }

            // Crear fórmula
            String formula = "'" + nombreHojaOculta + "'!$A$1:$A$" + Math.min(items.size(), 100);

            // Aplicar validación con fórmula
            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheetPrincipal);
            DataValidationConstraint constraint = dvHelper.createFormulaListConstraint(formula);

            CellRangeAddressList addressList = new CellRangeAddressList(1, 100, colIndex, colIndex);
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);

            // Mostrar flecha
            validation.setSuppressDropDownArrow(false);

            // Configurar mensajes
            validation.setShowErrorBox(true);
            validation.setErrorStyle(DataValidation.ErrorStyle.STOP);
            validation.setShowPromptBox(true);

            sheetPrincipal.addValidationData(validation);

            log.info("✅ Dropdown LARGO para '{}' en columna {} ({} items via hoja oculta)",
                    nombreCampo, colIndex, Math.min(items.size(), 100));

        } catch (Exception e) {
            log.error("❌ Error en dropdown largo para '{}': {}", nombreCampo, e.getMessage());
            // Fallback a método simple con menos items
            crearDropdownConFlecha(sheetPrincipal, colIndex,
                    items.stream().limit(10).map(d -> d.label()).collect(Collectors.toList()),
                    nombreCampo);
        }
    }
    // NUEVO MÉTODO: Procesar valores con límite de 255 caracteres
    private List<String> procesarValoresParaDropdown(List<DropdownDTO> items, int limiteItems, String nombreCampo) {
        List<String> valores = new ArrayList<>();
        int longitudTotal = 0;

        for (DropdownDTO item : items) {
            if (valores.size() >= limiteItems) {
                break; // Limitar número de items
            }

            if (item != null && item.label() != null) {
                String label = item.label().trim();
                if (!label.isEmpty()) {
                    // Calcular si agregar este item excede 255 caracteres
                    // +1 por la coma separadora (excepto el último)
                    int longitudItem = label.length() + (valores.isEmpty() ? 0 : 1);

                    if (longitudTotal + longitudItem <= 250) { // Margen de seguridad
                        valores.add(label);
                        longitudTotal += longitudItem;
                    } else {
                        log.warn("❌ Lista '{}' truncada por límite de 255 chars (actual: {})",
                                nombreCampo, longitudTotal);
                        break;
                    }
                }
            }
        }

        log.info("📊 Lista '{}': {} items, {} caracteres total",
                nombreCampo, valores.size(), longitudTotal);

        return valores;
    }
    // Método principal para crear dropdown con flecha visible
    private void crearDropdownConFlecha(Sheet sheet, int colIndex, List<String> valores, String nombreCampo) {
        if (valores == null || valores.isEmpty()) {
            log.warn("Lista vacía para '{}' (columna {})", nombreCampo, colIndex);
            return;
        }

        try {
            // Verificar longitud total
            int longitudTotal = valores.stream()
                    .mapToInt(String::length)
                    .sum() + (valores.size() - 1); // + separadores

            if (longitudTotal > 250) {
                log.warn("❌ Lista '{}' excede límite ({} chars). Reduciendo...",
                        nombreCampo, longitudTotal);

                // Reducir dinámicamente
                List<String> reducidos = new ArrayList<>();
                int acumulado = 0;

                for (String valor : valores) {
                    if (acumulado + valor.length() + 1 > 250) break;
                    reducidos.add(valor);
                    acumulado += valor.length() + 1;
                }

                valores = reducidos;
                log.info("📉 Lista '{}' reducida a {} items", nombreCampo, valores.size());
            }

            XSSFDataValidationHelper dvHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

            // Crear constraint con los valores
            DataValidationConstraint constraint = dvHelper.createExplicitListConstraint(
                    valores.toArray(new String[0])
            );

            // Definir rango de celdas
            CellRangeAddressList addressList = new CellRangeAddressList(1, 100, colIndex, colIndex);

            // Crear validación
            XSSFDataValidation validation = (XSSFDataValidation) dvHelper.createValidation(constraint, addressList);

            // 🔥 Mostrar flecha del dropdown
            validation.setSuppressDropDownArrow(false);

            // Intentar acceder al XML para forzar mostrar dropdown
            try {
                java.lang.reflect.Field ctField = XSSFDataValidation.class.getDeclaredField("_ctDataValidation");
                ctField.setAccessible(true);
                Object ctDataValidation = ctField.get(validation);

                if (ctDataValidation != null) {
                    Class<?> ctClass = ctDataValidation.getClass();
                    java.lang.reflect.Method setShowDropDown = ctClass.getMethod("setShowDropDown", boolean.class);
                    // false = mostrar flecha, true = ocultar flecha
                    setShowDropDown.invoke(ctDataValidation, false);
                    log.debug("✅ XML modificado: showDropDown=false para {}", nombreCampo);
                }
            } catch (Exception e) {
                log.warn("⚠️ No se pudo modificar XML para {}, pero validation está configurada", nombreCampo);
            }

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

            log.info("✅ Dropdown VISIBLE para '{}' en columna {} ({} opciones, {} chars)",
                    nombreCampo, colIndex, valores.size(), longitudTotal);

        } catch (Exception e) {
            log.error("❌ Error en dropdown para '{}': {}", nombreCampo, e.getMessage());
        }
    }


    // Método para llenar valores de ejemplo
    private void llenarValoresEjemplo(Row ejemploRow) {
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
            ejemploRow.createCell(5).setCellValue(sites.get(0).label());
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
                } else if (colNum == 8) {
                    // Columna opcional (otAnterior)
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

    // ================ MÉTODOS DE IMPORTACIÓN (sin cambios) ================
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

    private ExcelImportDTO parseRowToDTO(Row row, Map<String, Integer> columnIndex, int fila) {
        ExcelImportDTO dto = new ExcelImportDTO();
        dto.setFilaExcel(fila);
        dto.setValido(true);

        try {
            // FECHA APERTURA
            if (columnIndex.containsKey("fechaapertura")) {
                LocalDate fecha = parseFecha(row.getCell(columnIndex.get("fechaapertura")));
                dto.setFechaApertura(fecha);
            }

            // CLIENTE
            if (columnIndex.containsKey("cliente")) {
                dto.setCliente(getStringCellValue(row, columnIndex.get("cliente")));
            }

            // ÁREA
            if (columnIndex.containsKey("area")) {
                dto.setArea(getStringCellValue(row, columnIndex.get("area")));
            }

            // PROYECTO
            if (columnIndex.containsKey("proyecto")) {
                dto.setProyecto(getStringCellValue(row, columnIndex.get("proyecto")));
            }

            // FASE
            if (columnIndex.containsKey("fase")) {
                dto.setFase(getStringCellValue(row, columnIndex.get("fase")));
            }

            // SITE
            if (columnIndex.containsKey("site")) {
                dto.setSite(getStringCellValue(row, columnIndex.get("site")));
            }

            // REGIÓN
            if (columnIndex.containsKey("region")) {
                dto.setRegion(getStringCellValue(row, columnIndex.get("region")));
            }

            // ESTADO
            if (columnIndex.containsKey("estado")) {
                dto.setEstado(getStringCellValue(row, columnIndex.get("estado")));
            }

            // OT ANTERIOR
            if (columnIndex.containsKey("otanterior")) {
                Integer otAnterior = getNumericCellValue(row, columnIndex.get("otanterior"));
                dto.setOtAnterior(otAnterior);
            }

            // JEFATURA CLIENTE
            if (columnIndex.containsKey("jefaturaclientesolicitante") || columnIndex.containsKey("jefatura")) {
                String key = columnIndex.containsKey("jefaturaclientesolicitante") ? "jefaturaclientesolicitante" : "jefatura";
                dto.setJefaturaClienteSolicitante(getStringCellValue(row, columnIndex.get(key)));
            }

            // ANALISTA CLIENTE
            if (columnIndex.containsKey("analistaclientesolicitante") || columnIndex.containsKey("analista")) {
                String key = columnIndex.containsKey("analistaclientesolicitante") ? "analistaclientesolicitante" : "analista";
                dto.setAnalistaClienteSolicitante(getStringCellValue(row, columnIndex.get(key)));
            }

            // COORDINADOR TI CW
            if (columnIndex.containsKey("coordinadorticw")) {
                dto.setCoordinadorTiCw(getStringCellValue(row, columnIndex.get("coordinadorticw")));
            }

            // JEFATURA RESPONSABLE
            if (columnIndex.containsKey("jefaturaresponsable")) {
                dto.setJefaturaResponsable(getStringCellValue(row, columnIndex.get("jefaturaresponsable")));
            }

            // LIQUIDADOR
            if (columnIndex.containsKey("liquidador")) {
                dto.setLiquidador(getStringCellValue(row, columnIndex.get("liquidador")));
            }

            // EJECUTANTE
            if (columnIndex.containsKey("ejecutante")) {
                dto.setEjecutante(getStringCellValue(row, columnIndex.get("ejecutante")));
            }

            // ANALISTA CONTABLE
            if (columnIndex.containsKey("analistacontable")) {
                dto.setAnalistaContable(getStringCellValue(row, columnIndex.get("analistacontable")));
            }

        } catch (Exception e) {
            dto.setValido(false);
            dto.setMensajeError("Error al leer datos: " + e.getMessage());
        }

        return dto;
    }

    private void validarRegistro(ExcelImportDTO dto) {
        List<String> errores = new ArrayList<>();

        // Validaciones básicas
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

        // Validar que existan los valores en los dropdowns
        if (dto.getCliente() == null || dto.getCliente().trim().isEmpty() || !existeCliente(dto.getCliente())) {
            errores.add("Cliente es obligatorio y debe existir en el sistema");
        }

        if (dto.getArea() == null || dto.getArea().trim().isEmpty() || !existeArea(dto.getArea())) {
            errores.add("Área es obligatoria y debe existir en el sistema");
        }

        if (dto.getProyecto() == null || dto.getProyecto().trim().isEmpty() || !existeProyecto(dto.getProyecto())) {
            errores.add("Proyecto es obligatorio y debe existir en el sistema");
        }

        if (dto.getFase() == null || dto.getFase().trim().isEmpty() || !existeFase(dto.getFase())) {
            errores.add("Fase es obligatoria y debe existir en el sistema");
        }

        if (dto.getSite() == null || dto.getSite().trim().isEmpty() || !existeSite(dto.getSite())) {
            errores.add("Site es obligatorio y debe existir en el sistema");
        }

        if (dto.getRegion() == null || dto.getRegion().trim().isEmpty() || !existeRegion(dto.getRegion())) {
            errores.add("Región es obligatoria y debe existir en el sistema");
        }

        if (dto.getEstado() == null || !"ASIGNACION".equalsIgnoreCase(dto.getEstado().trim())) {
            errores.add("Estado debe ser siempre 'ASIGNACION'");
        }

        if (dto.getOtAnterior() != null && !existeOtAnterior(dto.getOtAnterior())) {
            errores.add("OT anterior no existe");
        }

        if (!errores.isEmpty()) {
            dto.setValido(false);
            dto.setMensajeError(String.join("; ", errores));
        }
    }

    private OtCreateRequest convertirARequest(ExcelImportDTO importDTO) {
        OtCreateRequest request = new OtCreateRequest();
        request.setFechaApertura(importDTO.getFechaApertura());
        request.setActivo(true);

        // Generar descripción automáticamente
        String descripcion = String.format("%s_%s_%s",
                importDTO.getProyecto() != null ? normalizeForDescripcion(importDTO.getProyecto()) : "",
                importDTO.getArea() != null ? normalizeForDescripcion(importDTO.getArea()) : "",
                importDTO.getSite() != null ? normalizeForDescripcion(importDTO.getSite()) : ""
        ).replace("__", "_").replace("__", "_");

        if (descripcion.endsWith("_")) {
            descripcion = descripcion.substring(0, descripcion.length() - 1);
        }

        if (descripcion.isEmpty()) {
            descripcion = "OT SIN DESCRIPCION AUTOMATICA";
        }

        request.setDescripcion(descripcion);

        // Mapear IDs por nombres
        request.setIdCliente(buscarIdPorNombre(dropdownService.getClientes(), importDTO.getCliente()));
        request.setIdArea(buscarIdPorNombre(dropdownService.getAreas(), importDTO.getArea()));
        request.setIdProyecto(buscarIdPorNombre(dropdownService.getProyectos(), importDTO.getProyecto()));
        request.setIdFase(buscarIdPorNombre(dropdownService.getFases(), importDTO.getFase()));
        request.setIdSite(buscarIdPorNombre(dropdownService.getSites(), importDTO.getSite()));
        request.setIdRegion(buscarIdPorNombre(dropdownService.getRegiones(), importDTO.getRegion()));
        request.setIdEstadoOt(buscarIdPorNombre(dropdownService.getEstadosOt(), "ASIGNACION"));

        if (importDTO.getOtAnterior() != null) {
            Integer idOtsAnterior = otService.buscarIdPorOt(importDTO.getOtAnterior());
            request.setIdOtsAnterior(idOtsAnterior);
        }

        request.setIdJefaturaClienteSolicitante(
                buscarIdPorNombre(dropdownService.getJefaturasClienteSolicitante(), importDTO.getJefaturaClienteSolicitante())
        );

        request.setIdAnalistaClienteSolicitante(
                buscarIdPorNombre(dropdownService.getAnalistasClienteSolicitante(), importDTO.getAnalistaClienteSolicitante())
        );

        request.setIdCoordinadorTiCw(
                buscarIdPorNombre(dropdownService.getCoordinadoresTiCw(), importDTO.getCoordinadorTiCw())
        );

        request.setIdJefaturaResponsable(
                buscarIdPorNombre(dropdownService.getJefaturasResponsable(), importDTO.getJefaturaResponsable())
        );

        request.setIdLiquidador(
                buscarIdPorNombre(dropdownService.getLiquidador(), importDTO.getLiquidador())
        );

        request.setIdEjecutante(
                buscarIdPorNombre(dropdownService.getEjecutantes(), importDTO.getEjecutante())
        );

        request.setIdAnalistaContable(
                buscarIdPorNombre(dropdownService.getAnalistasContable(), importDTO.getAnalistaContable())
        );

        return request;
    }

    // ================ MÉTODOS AUXILIARES DE VALIDACIÓN ================
    private boolean existeCliente(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getClientes().stream()
                .anyMatch(c -> c.label().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeArea(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getAreas().stream()
                .anyMatch(a -> a.label().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeProyecto(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getProyectos().stream()
                .anyMatch(p -> p.label().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeFase(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getFases().stream()
                .anyMatch(f -> f.label().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeSite(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getSites().stream()
                .anyMatch(s -> s.label().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeRegion(String nombre) {
        if (nombre == null) return false;
        return dropdownService.getRegiones().stream()
                .anyMatch(r -> r.label().equalsIgnoreCase(nombre.trim()));
    }

    private boolean existeOtAnterior(Integer ot) {
        if (ot == null) return false;
        return otService.existeOt(ot);
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
                return Integer.parseInt(value);
            }
        } catch (NumberFormatException e) {
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