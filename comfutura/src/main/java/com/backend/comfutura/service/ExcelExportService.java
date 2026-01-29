package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ExcelOtExportDTO;
import com.backend.comfutura.dto.response.otDTO.OtDetailResponse;
import com.backend.comfutura.dto.response.otDTO.OtListDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    private final OtService otService;

    // Método para exportar OTs específicas
    public byte[] exportOtsToExcel(List<Integer> otIds) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Órdenes de Trabajo");
            createHeaderRow(workbook, sheet);

            int rowNum = 1; // Empieza en la fila 1 (0 es el header)

            // Obtener y procesar cada OT
            for (Integer otId : otIds) {
                try {
                    OtDetailResponse otDetail = otService.obtenerDetallePorId(otId);
                    ExcelOtExportDTO exportDto = convertToExcelDto(otDetail);
                    addDataRow(workbook, sheet, rowNum++, exportDto);
                } catch (Exception e) {
                    // Si hay error con una OT específica, continuar con las demás
                    System.err.println("Error al procesar OT ID: " + otId + " - " + e.getMessage());
                }
            }

            // Si no hay datos, agregar mensaje
            if (rowNum == 1) {
                Row row = sheet.createRow(rowNum);
                Cell cell = row.createCell(0);
                cell.setCellValue("No hay datos para exportar");
            }

            // Ajustar tamaño de columnas
            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // Método para exportar todas las OTs (actualizado)
    public byte[] exportAllOtsToExcel() throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Todas las OTs");
            createHeaderRow(workbook, sheet);

            // Obtener todas las OTs (usa paginación si hay muchas)
            int page = 0;
            int pageSize = 1000;
            int rowNum = 1;

            while (true) {
                Pageable pageable = PageRequest.of(page, pageSize);
                PageResponseDTO<OtListDto> otPage = otService.listarOts(null, pageable);

                if (otPage.getContent().isEmpty()) {
                    break;
                }

                for (OtListDto ot : otPage.getContent()) {
                    try {
                        OtDetailResponse otDetail = otService.obtenerDetallePorId(ot.getIdOts());
                        ExcelOtExportDTO exportDto = convertToExcelDto(otDetail);
                        addDataRow(workbook, sheet, rowNum++, exportDto);
                    } catch (Exception e) {
                        System.err.println("Error al procesar OT ID: " + ot.getIdOts() + " - " + e.getMessage());
                    }
                }

                if (otPage.isLast()) {
                    break;
                }

                page++;
            }

            // Si no hay datos
            if (rowNum == 1) {
                Row row = sheet.createRow(rowNum);
                Cell cell = row.createCell(0);
                cell.setCellValue("No hay órdenes de trabajo para exportar");
            }

            autoSizeColumns(sheet);
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }
    // Método para exportar OTs filtradas
    public byte[] exportFilteredOts(String search, LocalDate fechaDesde,
                                    LocalDate fechaHasta, Boolean activo) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("OTs Filtradas");

            // Agregar información del filtro
            Row infoRow = sheet.createRow(0);
            infoRow.createCell(0).setCellValue("Reporte generado el: ");
            infoRow.createCell(1).setCellValue(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            Row filterRow1 = sheet.createRow(1);
            filterRow1.createCell(0).setCellValue("Filtro de búsqueda: ");
            filterRow1.createCell(1).setCellValue(search != null ? search : "Ninguno");

            // Crear header en fila 3
            createHeaderRow(workbook, sheet, 3);

            // Aquí necesitarías implementar la lógica de filtrado
            // Por ahora, exportaremos todas
            return exportAllOtsToExcel();
        }
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet) {
        createHeaderRow(workbook, sheet, 0);
    }

    private void createHeaderRow(Workbook workbook, Sheet sheet, int startRow) {
        Row headerRow = sheet.createRow(startRow);
        CellStyle headerStyle = createHeaderStyle(workbook);

        String[] headers = {
                "ID OT", "OT", "OT Anterior", "Descripción", "Fecha Apertura",
                "Días Asignados", "Fecha Creación", "Activo", "Cliente", "Área",
                "Proyecto", "Fase", "Site", "Región", "Jefatura Cliente",
                "Analista Cliente", "Creador", "Coordinador TI/CW", "Jefatura Responsable",
                "Liquidador", "Ejecutante", "Analista Contable", "Estado"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();

        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        font.setFontHeightInPoints((short) 11);

        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true); // Para texto largo en descripción
        return style;
    }

    private void addDataRow(Workbook workbook, Sheet sheet, int rowNum, ExcelOtExportDTO dto) {
        Row row = sheet.createRow(rowNum);
        CellStyle dataStyle = createDataStyle(workbook);
        CellStyle dateStyle = createDateStyle(workbook);
        CellStyle dateTimeStyle = createDateTimeStyle(workbook);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter datetimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        int col = 0;

        // ID OT
        createCell(row, col++, dto.getIdOts() != null ? dto.getIdOts().toString() : "", dataStyle);

        // OT
        createCell(row, col++, dto.getOt() != null ? dto.getOt().toString() : "", dataStyle);

        // OT Anterior
        createCell(row, col++, dto.getIdOtsAnterior() != null ? dto.getIdOtsAnterior().toString() : "", dataStyle);

        // Descripción
        createCell(row, col++, dto.getDescripcion() != null ? dto.getDescripcion() : "", dataStyle);

        // Fecha Apertura
        if (dto.getFechaApertura() != null) {
            createCell(row, col++, dto.getFechaApertura().format(dateFormatter), dateStyle);
        } else {
            createCell(row, col++, "", dataStyle);
        }

        // Días Asignados
        createCell(row, col++, dto.getDiasAsignados() != null ? dto.getDiasAsignados().toString() : "", dataStyle);

        // Fecha Creación
        if (dto.getFechaCreacion() != null) {
            createCell(row, col++, dto.getFechaCreacion().format(datetimeFormatter), dateTimeStyle);
        } else {
            createCell(row, col++, "", dataStyle);
        }

        // Activo
        createCell(row, col++, dto.getActivo() != null ? dto.getActivo() : "NO", dataStyle);

        // Cliente
        createCell(row, col++, dto.getCliente() != null ? dto.getCliente() : "", dataStyle);

        // Área
        createCell(row, col++, dto.getArea() != null ? dto.getArea() : "", dataStyle);

        // Proyecto
        createCell(row, col++, dto.getProyecto() != null ? dto.getProyecto() : "", dataStyle);

        // Fase
        createCell(row, col++, dto.getFase() != null ? dto.getFase() : "", dataStyle);

        // Site
        createCell(row, col++, dto.getSite() != null ? dto.getSite() : "", dataStyle);

        // Región
        createCell(row, col++, dto.getRegion() != null ? dto.getRegion() : "", dataStyle);

        // Jefatura Cliente
        createCell(row, col++, dto.getJefaturaClienteSolicitante() != null ? dto.getJefaturaClienteSolicitante() : "", dataStyle);

        // Analista Cliente
        createCell(row, col++, dto.getAnalistaClienteSolicitante() != null ? dto.getAnalistaClienteSolicitante() : "", dataStyle);

        // Creador
        createCell(row, col++, dto.getCreador() != null ? dto.getCreador() : "", dataStyle);

        // Coordinador TI/CW
        createCell(row, col++, dto.getCoordinadorTiCw() != null ? dto.getCoordinadorTiCw() : "", dataStyle);

        // Jefatura Responsable
        createCell(row, col++, dto.getJefaturaResponsable() != null ? dto.getJefaturaResponsable() : "", dataStyle);

        // Liquidador
        createCell(row, col++, dto.getLiquidador() != null ? dto.getLiquidador() : "", dataStyle);

        // Ejecutante
        createCell(row, col++, dto.getEjecutante() != null ? dto.getEjecutante() : "", dataStyle);

        // Analista Contable
        createCell(row, col++, dto.getAnalistaContable() != null ? dto.getAnalistaContable() : "", dataStyle);

        // Estado
        createCell(row, col++, dto.getEstadoOt() != null ? dto.getEstadoOt() : "", dataStyle);
    }

    private void createCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private CellStyle createDateTimeStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        CreationHelper createHelper = workbook.getCreationHelper();
        style.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private ExcelOtExportDTO convertToExcelDto(OtDetailResponse detail) {
        return ExcelOtExportDTO.builder()
                .idOts(detail.getIdOts())
                .ot(detail.getOt())
                .idOtsAnterior(detail.getIdOtsAnterior())
                .descripcion(detail.getDescripcion())
                .fechaApertura(detail.getFechaApertura())
                .diasAsignados(detail.getDiasAsignados())
                .fechaCreacion(detail.getFechaCreacion())
                .activo(detail.getActivo() != null && detail.getActivo() ? "SI" : "NO")
                .cliente(detail.getClienteRazonSocial())
                .area(detail.getAreaNombre())
                .proyecto(detail.getProyectoNombre())
                .fase(detail.getFaseNombre())
                .site(detail.getSiteNombre())
                .region(detail.getRegionNombre())
                .jefaturaClienteSolicitante(detail.getJefaturaClienteSolicitanteNombre())
                .analistaClienteSolicitante(detail.getAnalistaClienteSolicitanteNombre())
                .creador(detail.getCreadorNombre())
                .coordinadorTiCw(detail.getCoordinadorTiCwNombre())
                .jefaturaResponsable(detail.getJefaturaResponsableNombre())
                .liquidador(detail.getLiquidadorNombre())
                .ejecutante(detail.getEjecutanteNombre())
                .analistaContable(detail.getAnalistaContableNombre())
                .estadoOt(detail.getEstadoOt())
                .build();
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 23; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}