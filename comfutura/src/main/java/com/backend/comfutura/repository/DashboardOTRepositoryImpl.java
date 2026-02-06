package com.backend.comfutura.repository;

import com.backend.comfutura.dto.response.DashboardDTO.*;
import com.backend.comfutura.repository.DashboardOTRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class DashboardOTRepositoryImpl implements DashboardOTRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long countTotalOTs() {
        try {
            Query query = entityManager.createNativeQuery("SELECT COUNT(*) FROM ots WHERE activo = 1");
            Object result = query.getSingleResult();
            return convertToLong(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long countOTsByEstado(Integer estadoId) {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM ots WHERE activo = 1 AND id_estado_ot = :estadoId");
            query.setParameter("estadoId", estadoId);
            Object result = query.getSingleResult();
            return convertToLong(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long countOTsEsteAnio() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM ots WHERE activo = 1 AND YEAR(fecha_apertura) = YEAR(CURRENT_DATE)");
            Object result = query.getSingleResult();
            return convertToLong(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public Long countOTsEsteMes() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COUNT(*) FROM ots WHERE activo = 1 AND YEAR(fecha_apertura) = YEAR(CURRENT_DATE) AND MONTH(fecha_apertura) = MONTH(CURRENT_DATE)");
            Object result = query.getSingleResult();
            return convertToLong(result);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }

    @Override
    public List<OTPorEstadoDTO> findOTsPorEstado() {
        List<OTPorEstadoDTO> resultado = new ArrayList<>();
        try {
            String sql = "SELECT e.descripcion, COUNT(o.id_ots) as cantidad " +
                    "FROM ots o " +
                    "JOIN estado_ot e ON o.id_estado_ot = e.id_estado_ot " +
                    "WHERE o.activo = 1 " +
                    "GROUP BY e.descripcion " +
                    "ORDER BY cantidad DESC";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                resultado.add(new OTPorEstadoDTO(
                        (String) row[0],
                        convertToLong(row[1]),
                        BigDecimal.ZERO
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public List<OTPorClienteDTO> findOTsPorCliente() {
        List<OTPorClienteDTO> resultado = new ArrayList<>();
        try {
            String sql = "SELECT c.razon_social, COUNT(o.id_ots) as cantidad " +
                    "FROM ots o " +
                    "JOIN cliente c ON o.id_cliente = c.id_cliente " +
                    "WHERE o.activo = 1 " +
                    "GROUP BY c.razon_social " +
                    "ORDER BY cantidad DESC " +
                    "LIMIT 10";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                resultado.add(new OTPorClienteDTO(
                        (String) row[0],
                        convertToLong(row[1]),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public List<OTPorAreaDTO> findOTsPorArea() {
        List<OTPorAreaDTO> resultado = new ArrayList<>();
        try {
            String sql = "SELECT a.nombre, COUNT(o.id_ots) as cantidad " +
                    "FROM ots o " +
                    "JOIN area a ON o.id_area = a.id_area " +
                    "WHERE o.activo = 1 " +
                    "GROUP BY a.nombre " +
                    "ORDER BY cantidad DESC";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                resultado.add(new OTPorAreaDTO(
                        (String) row[0],
                        convertToLong(row[1]),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public List<OTPorProyectoDTO> findOTsPorProyecto() {
        List<OTPorProyectoDTO> resultado = new ArrayList<>();
        try {
            String sql = "SELECT p.nombre, COUNT(o.id_ots) as cantidad " +
                    "FROM ots o " +
                    "JOIN proyecto p ON o.id_proyecto = p.id_proyecto " +
                    "WHERE o.activo = 1 " +
                    "GROUP BY p.nombre " +
                    "ORDER BY cantidad DESC " +
                    "LIMIT 10";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                resultado.add(new OTPorProyectoDTO(
                        (String) row[0],
                        convertToLong(row[1]),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public List<OTPorRegionDTO> findOTsPorRegion() {
        List<OTPorRegionDTO> resultado = new ArrayList<>();
        try {
            String sql = "SELECT r.nombre, COUNT(o.id_ots) as cantidad " +
                    "FROM ots o " +
                    "JOIN region r ON o.id_region = r.id_region " +
                    "WHERE o.activo = 1 " +
                    "GROUP BY r.nombre " +
                    "ORDER BY cantidad DESC";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                resultado.add(new OTPorRegionDTO(
                        (String) row[0],
                        convertToLong(row[1]),
                        BigDecimal.ZERO,
                        BigDecimal.ZERO
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public BigDecimal getCostoTotalMateriales() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COALESCE(SUM(total), 0) FROM material_ot WHERE activo = 1");
            Object result = query.getSingleResult();
            return (BigDecimal) result;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getCostoTotalContratistas() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COALESCE(SUM(total), 0) FROM contratista_ot WHERE activo = 1");
            Object result = query.getSingleResult();
            return (BigDecimal) result;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getCostoTotalGastosLogisticos() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COALESCE(SUM(total), 0) FROM gasto_logistico_ot WHERE activo = 1");
            Object result = query.getSingleResult();
            return (BigDecimal) result;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getCostoTotalViaticos() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COALESCE(SUM(total), 0) FROM viatico_ot WHERE activo = 1");
            Object result = query.getSingleResult();
            return (BigDecimal) result;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public BigDecimal getCostoTotalPlanillas() {
        try {
            Query query = entityManager.createNativeQuery(
                    "SELECT COALESCE(SUM(total), 0) FROM planilla_trabajo_ot WHERE activo = 1");
            Object result = query.getSingleResult();
            return (BigDecimal) result;
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    @Override
    public List<OTPendienteDTO> findOTsPendientes() {
        List<OTPendienteDTO> resultado = new ArrayList<>();
        try {
            String sql = "SELECT " +
                    "o.ot, " +
                    "c.razon_social, " +
                    "o.descripcion, " +
                    "e.descripcion, " +
                    "o.fecha_apertura, " +
                    "DATEDIFF(CURRENT_DATE, o.fecha_apertura) as dias, " +
                    "CONCAT(t.nombres, ' ', t.apellidos) as responsable " +
                    "FROM ots o " +
                    "JOIN cliente c ON o.id_cliente = c.id_cliente " +
                    "JOIN estado_ot e ON o.id_estado_ot = e.id_estado_ot " +
                    "JOIN trabajador t ON o.id_trabajador = t.id_trabajador " +
                    "WHERE o.activo = 1 AND e.descripcion NOT IN ('COMPLETADO', 'CANCELADO') " +
                    "ORDER BY o.fecha_apertura DESC " +
                    "LIMIT 10";

            Query query = entityManager.createNativeQuery(sql);
            List<Object[]> results = query.getResultList();

            for (Object[] row : results) {
                resultado.add(new OTPendienteDTO(
                        ((Number) row[0]).intValue(),          // ot
                        (String) row[1],                       // cliente
                        (String) row[2],                       // descripcion
                        (String) row[3],                       // estado
                        row[4] != null ? ((java.sql.Date) row[4]).toLocalDate() : null, // fechaApertura
                        ((Number) row[5]).intValue(),          // diasPendientes
                        BigDecimal.ZERO,                       // costoEstimado (0 por ahora)
                        (String) row[6]                        // responsable
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public List<Object[]> findEvolucionMensual(LocalDate fechaInicio) {
        List<Object[]> resultado = new ArrayList<>();
        try {
            String sql = "SELECT YEAR(fecha_apertura) as anio, MONTH(fecha_apertura) as mes, COUNT(*) as cantidad " +
                    "FROM ots " +
                    "WHERE activo = 1 AND fecha_apertura >= :fechaInicio " +
                    "GROUP BY YEAR(fecha_apertura), MONTH(fecha_apertura) " +
                    "ORDER BY YEAR(fecha_apertura), MONTH(fecha_apertura)";

            Query query = entityManager.createNativeQuery(sql);
            query.setParameter("fechaInicio", fechaInicio);
            resultado = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    @Override
    public List<Object[]> findCostoPorOT() {
        List<Object[]> resultado = new ArrayList<>();
        try {
            String sql = "SELECT " +
                    "o.id_ots, " +
                    "COALESCE(SUM(m.total), 0) + " +
                    "COALESCE(SUM(c.total), 0) + " +
                    "COALESCE(SUM(g.total), 0) + " +
                    "COALESCE(SUM(v.total), 0) + " +
                    "COALESCE(SUM(p.total), 0) as costo_total " +
                    "FROM ots o " +
                    "LEFT JOIN material_ot m ON o.id_ots = m.id_ots AND m.activo = 1 " +
                    "LEFT JOIN contratista_ot c ON o.id_ots = c.id_ots AND c.activo = 1 " +
                    "LEFT JOIN gasto_logistico_ot g ON o.id_ots = g.id_ots AND g.activo = 1 " +
                    "LEFT JOIN viatico_ot v ON o.id_ots = v.id_ots AND v.activo = 1 " +
                    "LEFT JOIN planilla_trabajo_ot p ON o.id_ots = p.id_ots AND p.activo = 1 " +
                    "WHERE o.activo = 1 " +
                    "GROUP BY o.id_ots";

            Query query = entityManager.createNativeQuery(sql);
            resultado = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultado;
    }

    // Método auxiliar para convertir cualquier tipo numérico a Long
    private Long convertToLong(Object value) {
        if (value == null) return 0L;
        if (value instanceof BigInteger) {
            return ((BigInteger) value).longValue();
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).longValue();
        } else if (value instanceof Number) {
            return ((Number) value).longValue();
        } else {
            return Long.parseLong(value.toString());
        }
    }
}