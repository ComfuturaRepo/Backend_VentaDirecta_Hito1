package com.backend.comfutura.service;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.ExcelOtExportDTO;
import com.backend.comfutura.dto.response.SiteDTO;
import com.backend.comfutura.dto.response.SiteDescripcionDTO;
import com.backend.comfutura.dto.response.otDTO.OtDetailResponse;
import com.backend.comfutura.dto.response.otDTO.OtListDto;
import com.backend.comfutura.model.Site;
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
    private final SiteService siteService;

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
                "OT ANTERIOR",           // col 0
                "OT",                    // col 1
                "PROYECTO",              // col 2
                "FASE",                  // col 3
                "DESCRIPCION",           // col 4
                "AREA",                  // col 5
                "NOMBRE DEL SITE",       // col 6 (antes 7)
                "REGION",                // col 7 (antes 8)
                "FECHA DE APERTURA",     // col 8 (antes 9)
                "CLIENTE",               // col 9 (antes 10)
                "JEFATURA DEL CLIENTE SOLICITANTE",    // col 10 (antes 11)
                "ANALISTA DEL CLIENTE SOLICITANTE",    // col 11 (antes 12)
                "COORDINADORES TI/CW/PEXT/ENERGIA",    // col 12 (antes 13)
                "JEFATURA RESPONSABLE",  // col 13 (antes 14)
                "LIQUIDADOR",            // col 14 (antes 15)
                "EJECUTANTE",            // col 15 (antes 16)
                "ANALISTA CONTABLE",     // col 16 (antes 17)
                "DIAS ASIGNADOS A LA FECHA"  // col 17 (antes 18)
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
        // Ya no necesitamos dateTimeStyle porque quitamos Fecha Creación

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        int col = 0; // Iniciamos columna en 0

        // 1. OT ANTERIOR (col 0)
        createCell(row, col++, dto.getIdOtsAnterior() != null ? dto.getIdOtsAnterior().toString() : "", dataStyle);

        // 2. OT (col 1)
        createCell(row, col++, dto.getOt() != null ? dto.getOt().toString() : "", dataStyle);

        // 3. PROYECTO (col 2)
        createCell(row, col++, dto.getProyecto() != null ? dto.getProyecto() : "", dataStyle);

        // 4. FASE (col 3)
        createCell(row, col++, dto.getFase() != null ? dto.getFase() : "", dataStyle);

        // 5. DESCRIPCION (col 4)
        createCell(row, col++, dto.getDescripcion() != null ? dto.getDescripcion() : "", dataStyle);

        // 6. AREA (col 5)
        createCell(row, col++, dto.getArea() != null ? dto.getArea() : "", dataStyle);


        // 8. NOMBRE DEL SITE (col 7) - Combinar Site Código + Descripción
        String nombreSite = "";
        if (dto.getSite() != null && !dto.getSite().isEmpty()) {
            if (dto.getSiteDescripcion() != null && !dto.getSiteDescripcion().isEmpty()) {
                nombreSite = dto.getSite() + " - " + dto.getSiteDescripcion();
            } else {
                nombreSite = dto.getSite();
            }
        }
        createCell(row, col++, nombreSite, dataStyle);

        // 9. REGION (col 8)
        createCell(row, col++, dto.getRegion() != null ? dto.getRegion() : "", dataStyle);

        // 10. FECHA DE APERTURA (col 9)
        if (dto.getFechaApertura() != null) {
            createCell(row, col++, dto.getFechaApertura().format(dateFormatter), dateStyle);
        } else {
            createCell(row, col++, "", dataStyle);
        }

        // 11. CLIENTE (col 10)
        createCell(row, col++, dto.getCliente() != null ? dto.getCliente() : "", dataStyle);

        // 12. JEFATURA DEL CLIENTE SOLICITANTE (col 11)
        createCell(row, col++, dto.getJefaturaClienteSolicitante() != null ? dto.getJefaturaClienteSolicitante() : "", dataStyle);

        // 13. ANALISTA DEL CLIENTE SOLICITANTE (col 12)
        createCell(row, col++, dto.getAnalistaClienteSolicitante() != null ? dto.getAnalistaClienteSolicitante() : "", dataStyle);

        // 14. COORDINADORES TI/CW/PEXT/ENERGIA (col 13)
        createCell(row, col++, dto.getCoordinadorTiCw() != null ? dto.getCoordinadorTiCw() : "", dataStyle);

        // 15. JEFATURA RESPONSABLE (col 14)
        createCell(row, col++, dto.getJefaturaResponsable() != null ? dto.getJefaturaResponsable() : "", dataStyle);

        // 16. LIQUIDADOR (col 15)
        createCell(row, col++, dto.getLiquidador() != null ? dto.getLiquidador() : "", dataStyle);

        // 17. EJECUTANTE (col 16)
        createCell(row, col++, dto.getEjecutante() != null ? dto.getEjecutante() : "", dataStyle);

        // 18. ANALISTA CONTABLE (col 17)
        createCell(row, col++, dto.getAnalistaContable() != null ? dto.getAnalistaContable() : "", dataStyle);

        // 19. DIAS ASIGNADOS A LA FECHA (col 18)
        createCell(row, col++, dto.getDiasAsignados() != null ? dto.getDiasAsignados().toString() : "", dataStyle);

        // NOTA: Quitamos: ID OT, Fecha Creación, Activo, Creador, Estado
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
        Integer siteId = null;
        try {
            siteId = detail.getIdSite();
        } catch (Exception e) {
            System.err.println("Error: OtDetailResponse no tiene getSiteId()");
        }

        return ExcelOtExportDTO.builder()
                .idOts(detail.getIdOts())
                .ot(detail.getOt())
                .idOtsAnterior(detail.getIdOtsAnterior())
                .descripcion(detail.getDescripcion())
                .fechaApertura(detail.getFechaApertura())
                .diasAsignados(detail.getDiasAsignados())
                .cliente(detail.getClienteRazonSocial())
                .area(detail.getAreaNombre())
                .proyecto(detail.getProyectoNombre())
                .fase(detail.getFaseNombre())
                .site(siteId != null ? obtenerCodigoSitio(siteId) : "")
                .siteDescripcion(siteId != null ? obtenerDescripcionSitio(siteId) : detail.getSiteNombre())
                .region(detail.getRegionNombre())
                .jefaturaClienteSolicitante(detail.getJefaturaClienteSolicitanteNombre())
                .analistaClienteSolicitante(detail.getAnalistaClienteSolicitanteNombre())
                .coordinadorTiCw(detail.getCoordinadorTiCwNombre())
                .jefaturaResponsable(detail.getJefaturaResponsableNombre())
                .liquidador(detail.getLiquidadorNombre())
                .ejecutante(detail.getEjecutanteNombre())
                .analistaContable(detail.getAnalistaContableNombre())
                // QUITADOS: fechaCreacion, activo, creador, estadoOt
                .build();
    }
    private String obtenerCodigoSitio(Integer siteId) {
        if (siteId == null) return "";

        try {
            SiteDTO siteDTO = siteService.obtenerPorId(siteId);
            return siteDTO != null ? siteDTO.getCodigoSitio() : "";
        } catch (Exception e) {
            System.err.println("Error al obtener código del sitio ID: " + siteId + " - " + e.getMessage());
            return "";
        }
    }

    private String obtenerDescripcionSitio(Integer siteId) {
        if (siteId == null) return "";

        try {
            SiteDTO siteDTO = siteService.obtenerPorId(siteId);
            if (siteDTO == null) return "";

            // Si el DTO tiene una lista de descripciones
            if (siteDTO.getDescripciones() != null && !siteDTO.getDescripciones().isEmpty()) {
                // Toma la primera descripción activa
                return siteDTO.getDescripciones().stream()
                        .filter(desc -> Boolean.TRUE.equals(desc.getActivo()))
                        .findFirst()
                        .map(SiteDescripcionDTO::getDescripcion)
                        .orElse("");
            }
            return "";
        } catch (Exception e) {
            System.err.println("Error al obtener descripción del sitio ID: " + siteId + " - " + e.getMessage());
            return "";
        }
    }
    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 18; i++) {  // Ahora 18 columnas (0-17)
            sheet.autoSizeColumn(i);
        }
    }
}