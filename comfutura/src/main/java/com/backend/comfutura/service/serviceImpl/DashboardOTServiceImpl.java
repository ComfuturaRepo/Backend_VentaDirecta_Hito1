package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.DashboardDTO.*;
import com.backend.comfutura.repository.DashboardOTRepository;
import com.backend.comfutura.service.DashboardOTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardOTServiceImpl implements DashboardOTService {

    private final DashboardOTRepository dashboardOTRepository;

    // ===========================================
    // MÉTODO PRINCIPAL
    // ===========================================

    @Override
    @Transactional(readOnly = true)
    public DashboardOTDTO getDashboardData(FiltroDashboardDTO filtro) {
        log.info("Generando dashboard con filtro: {}", filtro);

        try {
            DashboardOTDTO dashboard = new DashboardOTDTO();

            // 1. Estadísticas generales
            dashboard.setGenerales(calcularEstadisticasGenerales(filtro));

            // 2. OTs por estado
            dashboard.setOtsPorEstado(calcularOTsPorEstado(filtro));

            // 3. OTs por cliente
            dashboard.setOtsPorCliente(calcularOTsPorCliente(filtro));

            // 4. OTs por área
            dashboard.setOtsPorArea(calcularOTsPorArea(filtro));

            // 5. OTs por proyecto
            dashboard.setOtsPorProyecto(calcularOTsPorProyecto(filtro));

            // 6. OTs por región
            dashboard.setOtsPorRegion(calcularOTsPorRegion(filtro));

            // 7. Evolución mensual
            dashboard.setEvolucionMensual(calcularEvolucionMensual(filtro));

            // 8. Resumen de costos
            dashboard.setResumenCostos(calcularResumenCostos(filtro));

            // 9. OTs pendientes
            dashboard.setOtsPendientes(calcularOTsPendientes(filtro));

            log.info("Dashboard generado exitosamente");
            return dashboard;

        } catch (Exception e) {
            log.error("Error al generar dashboard: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar dashboard: " + e.getMessage(), e);
        }
    }

    // ===========================================
    // MÉTODOS ESPECÍFICOS POR FILTRO
    // ===========================================

    @Override
    @Transactional(readOnly = true)
    public DashboardOTDTO getDashboardDataByCliente(Integer clienteId) {
        FiltroDashboardDTO filtro = new FiltroDashboardDTO();
        filtro.setClienteId(clienteId);
        // Crear query específica para cliente
        return getDashboardDataConFiltroPersonalizado(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardOTDTO getDashboardDataByArea(Integer areaId) {
        FiltroDashboardDTO filtro = new FiltroDashboardDTO();
        filtro.setAreaId(areaId);
        // Crear query específica para área
        return getDashboardDataConFiltroPersonalizado(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardOTDTO getDashboardDataByProyecto(Integer proyectoId) {
        FiltroDashboardDTO filtro = new FiltroDashboardDTO();
        filtro.setProyectoId(proyectoId);
        // Crear query específica para proyecto
        return getDashboardDataConFiltroPersonalizado(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardOTDTO getDashboardDataByEstado(Integer estadoId) {
        FiltroDashboardDTO filtro = new FiltroDashboardDTO();
        filtro.setEstadoId(estadoId);
        // Crear query específica para estado
        return getDashboardDataConFiltroPersonalizado(filtro);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardOTDTO getDashboardDataByFecha(String rango) {
        FiltroDashboardDTO filtro = crearFiltroPorRango(rango);
        return getDashboardDataConFiltroPersonalizado(filtro);
    }

    // ===========================================
    // MÉTODOS PRIVADOS DE CÁLCULO
    // ===========================================

    private DashboardOTDTO getDashboardDataConFiltroPersonalizado(FiltroDashboardDTO filtro) {
        // Para filtros específicos, puedes personalizar las queries
        // Por ahora, usamos el mismo método general
        return getDashboardData(filtro);
    }

    private EstadisticasGeneralesDTO calcularEstadisticasGenerales(FiltroDashboardDTO filtro) {
        log.info("Calculando estadísticas generales con filtro: {}", filtro);

        EstadisticasGeneralesDTO generales = new EstadisticasGeneralesDTO();

        try {
            // Usar los métodos del repositorio que ya tienes
            Long totalOTs = dashboardOTRepository.countTotalOTs();
            Long otsEsteMes = dashboardOTRepository.countOTsEsteMes();
            Long otsEsteAnio = dashboardOTRepository.countOTsEsteAnio();

            // Para estados específicos, necesitarías métodos filtrados
            // Por ahora uso los métodos existentes (ajusta según tus estados)
            Long otsPendientes = dashboardOTRepository.countOTsByEstado(1);   // Ajusta según tus IDs
            Long otsEnProceso = dashboardOTRepository.countOTsByEstado(2);
            Long otsCompletadas = dashboardOTRepository.countOTsByEstado(3);
            Long otsCanceladas = dashboardOTRepository.countOTsByEstado(4);

            // Calcular costos totales
            BigDecimal costoTotal = calcularCostoTotalOTs();

            // Establecer valores
            generales.setTotalOTs(totalOTs);
            generales.setOtsActivas(otsPendientes + otsEnProceso);
            generales.setOtsPendientes(otsPendientes);
            generales.setOtsCompletadas(otsCompletadas);
            generales.setOtsCanceladas(otsCanceladas);
            generales.setOtsEsteMes(otsEsteMes);
            generales.setOtsEsteAnio(otsEsteAnio);
            generales.setCostoTotalOTs(costoTotal);

            // Calcular costo promedio
            if (totalOTs > 0) {
                BigDecimal costoPromedio = costoTotal.divide(
                        BigDecimal.valueOf(totalOTs),
                        2,
                        RoundingMode.HALF_UP
                );
                generales.setCostoPromedioOT(costoPromedio);
            } else {
                generales.setCostoPromedioOT(BigDecimal.ZERO);
            }

        } catch (Exception e) {
            log.error("Error calculando estadísticas generales: {}", e.getMessage(), e);
            // Establecer valores por defecto
            generales.setTotalOTs(0L);
            generales.setOtsActivas(0L);
            generales.setOtsPendientes(0L);
            generales.setOtsCompletadas(0L);
            generales.setOtsCanceladas(0L);
            generales.setOtsEsteMes(0L);
            generales.setOtsEsteAnio(0L);
            generales.setCostoTotalOTs(BigDecimal.ZERO);
            generales.setCostoPromedioOT(BigDecimal.ZERO);
        }

        return generales;
    }

    private BigDecimal calcularCostoTotalOTs() {
        try {
            BigDecimal materiales = dashboardOTRepository.getCostoTotalMateriales();
            BigDecimal contratistas = dashboardOTRepository.getCostoTotalContratistas();
            BigDecimal gastosLogisticos = dashboardOTRepository.getCostoTotalGastosLogisticos();
            BigDecimal viaticos = dashboardOTRepository.getCostoTotalViaticos();
            BigDecimal planillas = dashboardOTRepository.getCostoTotalPlanillas();

            log.debug("Costos parciales - Materiales: {}, Contratistas: {}, Gastos Logisticos: {}, Viaticos: {}, Planillas: {}",
                    materiales, contratistas, gastosLogisticos, viaticos, planillas);

            return materiales.add(contratistas).add(gastosLogisticos).add(viaticos).add(planillas);
        } catch (Exception e) {
            log.error("Error calculando costo total: {}", e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }

    private List<OTPorEstadoDTO> calcularOTsPorEstado(FiltroDashboardDTO filtro) {
        try {
            List<OTPorEstadoDTO> otsPorEstado = dashboardOTRepository.findOTsPorEstado();

            // Calcular total para porcentajes
            Long total = otsPorEstado.stream()
                    .mapToLong(OTPorEstadoDTO::getCantidad)
                    .sum();

            // Calcular porcentajes
            for (OTPorEstadoDTO item : otsPorEstado) {
                if (total > 0) {
                    BigDecimal porcentaje = BigDecimal.valueOf(item.getCantidad())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                    item.setPorcentaje(porcentaje);
                } else {
                    item.setPorcentaje(BigDecimal.ZERO);
                }
            }

            return otsPorEstado;
        } catch (Exception e) {
            log.error("Error calculando OTs por estado: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<OTPorClienteDTO> calcularOTsPorCliente(FiltroDashboardDTO filtro) {
        try {
            List<OTPorClienteDTO> otsPorCliente = dashboardOTRepository.findOTsPorCliente();

            // Calcular total para porcentajes
            Long total = otsPorCliente.stream()
                    .mapToLong(OTPorClienteDTO::getCantidad)
                    .sum();

            // Calcular porcentajes
            for (OTPorClienteDTO item : otsPorCliente) {
                if (total > 0) {
                    BigDecimal porcentaje = BigDecimal.valueOf(item.getCantidad())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                    item.setPorcentaje(porcentaje);
                } else {
                    item.setPorcentaje(BigDecimal.ZERO);
                }

                // Para costo total por cliente, necesitarías una query específica
                item.setCostoTotal(BigDecimal.ZERO);
            }

            return otsPorCliente;
        } catch (Exception e) {
            log.error("Error calculando OTs por cliente: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<OTPorAreaDTO> calcularOTsPorArea(FiltroDashboardDTO filtro) {
        try {
            List<OTPorAreaDTO> otsPorArea = dashboardOTRepository.findOTsPorArea();

            // Calcular total para porcentajes
            Long total = otsPorArea.stream()
                    .mapToLong(OTPorAreaDTO::getCantidad)
                    .sum();

            // Calcular porcentajes
            for (OTPorAreaDTO item : otsPorArea) {
                if (total > 0) {
                    BigDecimal porcentaje = BigDecimal.valueOf(item.getCantidad())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                    item.setPorcentaje(porcentaje);
                } else {
                    item.setPorcentaje(BigDecimal.ZERO);
                }

                item.setCostoTotal(BigDecimal.ZERO);
            }

            return otsPorArea;
        } catch (Exception e) {
            log.error("Error calculando OTs por área: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<OTPorProyectoDTO> calcularOTsPorProyecto(FiltroDashboardDTO filtro) {
        try {
            List<OTPorProyectoDTO> otsPorProyecto = dashboardOTRepository.findOTsPorProyecto();

            // Calcular total para porcentajes
            Long total = otsPorProyecto.stream()
                    .mapToLong(OTPorProyectoDTO::getCantidad)
                    .sum();

            // Calcular porcentajes
            for (OTPorProyectoDTO item : otsPorProyecto) {
                if (total > 0) {
                    BigDecimal porcentaje = BigDecimal.valueOf(item.getCantidad())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                    item.setPorcentaje(porcentaje);
                } else {
                    item.setPorcentaje(BigDecimal.ZERO);
                }

                item.setCostoTotal(BigDecimal.ZERO);
            }

            return otsPorProyecto;
        } catch (Exception e) {
            log.error("Error calculando OTs por proyecto: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private List<OTPorRegionDTO> calcularOTsPorRegion(FiltroDashboardDTO filtro) {
        try {
            List<OTPorRegionDTO> otsPorRegion = dashboardOTRepository.findOTsPorRegion();

            // Calcular total para porcentajes
            Long total = otsPorRegion.stream()
                    .mapToLong(OTPorRegionDTO::getCantidad)
                    .sum();

            // Calcular porcentajes
            for (OTPorRegionDTO item : otsPorRegion) {
                if (total > 0) {
                    BigDecimal porcentaje = BigDecimal.valueOf(item.getCantidad())
                            .multiply(BigDecimal.valueOf(100))
                            .divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP);
                    item.setPorcentaje(porcentaje);
                } else {
                    item.setPorcentaje(BigDecimal.ZERO);
                }

                item.setCostoTotal(BigDecimal.ZERO);
            }

            return otsPorRegion;
        } catch (Exception e) {
            log.error("Error calculando OTs por región: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private EvolucionMensualDTO calcularEvolucionMensual(FiltroDashboardDTO filtro) {
        try {
            LocalDate fechaInicio = (filtro.getFechaInicio() != null)
                    ? filtro.getFechaInicio()
                    : LocalDate.now().minusMonths(6);

            List<Object[]> datosMensuales = dashboardOTRepository.findEvolucionMensual(fechaInicio);

            List<MesDTO> meses = new ArrayList<>();
            List<BigDecimal> costos = new ArrayList<>();
            List<Long> cantidadOTs = new ArrayList<>();

            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMM", new Locale("es", "ES"));

            for (Object[] dato : datosMensuales) {
                Integer anio = ((Number) dato[0]).intValue();
                Integer mes = ((Number) dato[1]).intValue();
                Long cantidad = ((Number) dato[2]).longValue();

                LocalDate fecha = LocalDate.of(anio, mes, 1);
                String nombreMes = fecha.format(monthFormatter).toUpperCase();

                meses.add(new MesDTO(nombreMes, anio));
                cantidadOTs.add(cantidad);

                // Costo estimado (simplificado)
                BigDecimal costo = BigDecimal.valueOf(cantidad).multiply(BigDecimal.valueOf(1000));
                costos.add(costo);
            }

            return new EvolucionMensualDTO(meses, costos, cantidadOTs);
        } catch (Exception e) {
            log.error("Error calculando evolución mensual: {}", e.getMessage(), e);
            return new EvolucionMensualDTO(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }
    }

    private ResumenCostosDTO calcularResumenCostos(FiltroDashboardDTO filtro) {
        try {
            ResumenCostosDTO resumen = new ResumenCostosDTO();

            BigDecimal materiales = dashboardOTRepository.getCostoTotalMateriales();
            BigDecimal contratistas = dashboardOTRepository.getCostoTotalContratistas();
            BigDecimal gastosLogisticos = dashboardOTRepository.getCostoTotalGastosLogisticos();
            BigDecimal viaticos = dashboardOTRepository.getCostoTotalViaticos();
            BigDecimal planillas = dashboardOTRepository.getCostoTotalPlanillas();

            resumen.setCostoMateriales(materiales);
            resumen.setCostoContratistas(contratistas);
            resumen.setCostoGastosLogisticos(gastosLogisticos);
            resumen.setCostoViaticos(viaticos);
            resumen.setCostoPlanillas(planillas);

            BigDecimal total = materiales.add(contratistas)
                    .add(gastosLogisticos)
                    .add(viaticos)
                    .add(planillas);

            resumen.setCostoTotal(total);

            // Calcular porcentajes
            if (total.compareTo(BigDecimal.ZERO) > 0) {
                resumen.setPorcentajeMateriales(calcularPorcentaje(materiales, total));
                resumen.setPorcentajeContratistas(calcularPorcentaje(contratistas, total));
                resumen.setPorcentajeGastosLogisticos(calcularPorcentaje(gastosLogisticos, total));
                resumen.setPorcentajeViaticos(calcularPorcentaje(viaticos, total));
                resumen.setPorcentajePlanillas(calcularPorcentaje(planillas, total));
            } else {
                resumen.setPorcentajeMateriales(BigDecimal.ZERO);
                resumen.setPorcentajeContratistas(BigDecimal.ZERO);
                resumen.setPorcentajeGastosLogisticos(BigDecimal.ZERO);
                resumen.setPorcentajeViaticos(BigDecimal.ZERO);
                resumen.setPorcentajePlanillas(BigDecimal.ZERO);
            }

            return resumen;
        } catch (Exception e) {
            log.error("Error calculando resumen de costos: {}", e.getMessage(), e);
            ResumenCostosDTO resumen = new ResumenCostosDTO();
            resumen.setCostoTotal(BigDecimal.ZERO);
            return resumen;
        }
    }

    private List<OTPendienteDTO> calcularOTsPendientes(FiltroDashboardDTO filtro) {
        try {
            return dashboardOTRepository.findOTsPendientes();
        } catch (Exception e) {
            log.error("Error obteniendo OTs pendientes: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    private BigDecimal calcularPorcentaje(BigDecimal valor, BigDecimal total) {
        return valor.multiply(BigDecimal.valueOf(100))
                .divide(total, 2, RoundingMode.HALF_UP);
    }

    private FiltroDashboardDTO crearFiltroPorRango(String rango) {
        LocalDate fechaInicio;
        LocalDate fechaFin = LocalDate.now();

        switch (rango.toUpperCase()) {
            case "HOY":
                fechaInicio = LocalDate.now();
                break;
            case "SEMANA":
                fechaInicio = LocalDate.now().minusDays(7);
                break;
            case "MES":
                fechaInicio = LocalDate.now().minusMonths(1);
                break;
            case "TRIMESTRE":
                fechaInicio = LocalDate.now().minusMonths(3);
                break;
            case "SEMESTRE":
                fechaInicio = LocalDate.now().minusMonths(6);
                break;
            case "ANIO":
                fechaInicio = LocalDate.now().minusYears(1);
                break;
            default:
                fechaInicio = LocalDate.now().minusMonths(6);
        }

        return new FiltroDashboardDTO(fechaInicio, fechaFin);
    }
}