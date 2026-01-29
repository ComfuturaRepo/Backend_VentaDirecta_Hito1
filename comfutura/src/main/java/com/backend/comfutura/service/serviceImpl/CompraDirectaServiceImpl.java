package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.CompraDirectaCreateRequest;
import com.backend.comfutura.dto.response.CompraDirectaDetalleResponse;
import com.backend.comfutura.dto.response.CompraDirectaResponse;
import com.backend.comfutura.dto.response.CronogramaResponse;
import com.backend.comfutura.model.CompraDirecta;
import com.backend.comfutura.model.EstadoCompraDirecta;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Usuario;
import com.backend.comfutura.repository.CompraDirectaRepository;
import com.backend.comfutura.repository.CronogramaOtRepository;
import com.backend.comfutura.repository.EstadoCompraDirectaRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.repository.UsuarioRepository;
import com.backend.comfutura.service.CompraDirectaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraDirectaServiceImpl implements CompraDirectaService {

    private final CompraDirectaRepository compraDirectaRepository;
    private final EstadoCompraDirectaRepository estadoCompraDirectaRepository;
    private final OtsRepository otsRepository;
    private final UsuarioRepository usuarioRepository;
    private final CronogramaOtRepository cronogramaOtRepository;

    // =========================
    // CREAR COMPRA DIRECTA
    // =========================
    @Override
    public CompraDirectaResponse crear(CompraDirectaCreateRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        EstadoCompraDirecta estado = estadoCompraDirectaRepository
                .findByDescripcion("REGISTRADO")
                .orElseThrow(() -> new RuntimeException("Estado REGISTRADO no existe"));

        Usuario usuario = usuarioRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String nroRequerimiento = generarNroRequerimiento(usuario, ots);

        CompraDirecta compra = CompraDirecta.builder()
                .nroRequerimiento(nroRequerimiento)
                .fechaCosto(request.getFechaCosto())
                .ots(ots)
                .estado(estado)
                .observacion(request.getObservacion())
                .usuarioCreacion(usuario)
                .activo(true)
                .build();

        CompraDirecta guardado = compraDirectaRepository.save(compra);

        Integer tiempoEjecucion = calcularTiempoEjecucion(ots.getIdOts());

        return CompraDirectaResponse.builder()
                .idCompraDirecta(guardado.getIdCompraDirecta())
                .nroRequerimiento(guardado.getNroRequerimiento())
                .idOts(ots.getIdOts())
                .site(ots.getSite().getDescripcion())
                .proyecto(ots.getProyecto().getNombre())
                .cliente(ots.getCliente().getRazonSocial())
                .tiempoEjecucion(tiempoEjecucion)
                .importeTotal(BigDecimal.ZERO)
                .estado(estado.getDescripcion())
                .build();
    }

    // =========================
    // LISTAR (GRILLA)
    // =========================
    @Override
    public Page<CompraDirectaResponse> listar(int page) {

        Pageable pageable = PageRequest.of(page, 20);

        return compraDirectaRepository.findByActivoTrue(pageable)
                .map(cd -> CompraDirectaResponse.builder()
                        .idCompraDirecta(cd.getIdCompraDirecta())
                        .nroRequerimiento(cd.getNroRequerimiento())
                        .idOts(cd.getOts().getIdOts())
                        .site(cd.getOts().getSite().getDescripcion())
                        .proyecto(cd.getOts().getProyecto().getNombre())
                        .cliente(cd.getOts().getCliente().getRazonSocial())
                        .tiempoEjecucion(calcularTiempoEjecucion(cd.getOts().getIdOts()))
                        .importeTotal(BigDecimal.ZERO)
                        .estado(cd.getEstado().getDescripcion())
                        .build()
                );
    }

    // =========================
    // CAMBIAR ESTADO
    // =========================
    @Override
    public void cambiarEstado(Integer idCompraDirecta, String nuevoEstado, String observacion) {

        CompraDirecta compra = compraDirectaRepository.findById(idCompraDirecta)
                .orElseThrow(() -> new RuntimeException("Compra Directa no encontrada"));

        EstadoCompraDirecta estado = estadoCompraDirectaRepository
                .findByDescripcion(nuevoEstado)
                .orElseThrow(() ->
                        new RuntimeException("Estado no válido: " + nuevoEstado)
                );

        compra.setEstado(estado);
        compra.setObservacion(observacion);

        compraDirectaRepository.save(compra);
    }

    // =========================
    // OBTENER POR OT (BÁSICO)
    // =========================
    @Override
    public List<CompraDirectaResponse> obtenerPorOt(Integer idOts) {


        List<CompraDirecta> compras =
                compraDirectaRepository.findAllByOts_IdOtsAndActivoTrue(idOts);


        return compras.stream()
                .map(compra -> {


                    Integer tiempoEjecucion =
                            calcularTiempoEjecucion(compra.getOts().getIdOts());


                    return CompraDirectaResponse.builder()
                            .idCompraDirecta(compra.getIdCompraDirecta())
                            .nroRequerimiento(compra.getNroRequerimiento())
                            .idOts(compra.getOts().getIdOts())
                            .tiempoEjecucion(tiempoEjecucion)
                            .estado(compra.getEstado().getDescripcion())
                            .build();
                })
                .toList();
    }

    // =========================
    // 🟢 DETALLE COMPLETO (CRONOGRAMA)
    // =========================
    @Transactional
    public CompraDirectaDetalleResponse obtenerDetalleCompleto(Integer idOts) {

        CompraDirecta cd = compraDirectaRepository
                .findByOts_IdOts(idOts)
                .orElseThrow(() ->
                        new RuntimeException("No existe compra directa para la OT " + idOts)
                );

        Integer tiempoEjecucion = calcularTiempoEjecucion(idOts);

        List<CronogramaResponse> cronograma =
                cronogramaOtRepository.listarCronogramaPorOt(idOts);

        return CompraDirectaDetalleResponse.builder()
                .idCompraDirecta(cd.getIdCompraDirecta())
                .nroRequerimiento(cd.getNroRequerimiento())
                .estado(cd.getEstado().getDescripcion())
                .site(cd.getOts().getSite().getDescripcion())
                .proyecto(cd.getOts().getProyecto().getNombre())
                .cliente(cd.getOts().getCliente().getRazonSocial())
                .tiempoEjecucion(tiempoEjecucion)
                .importeTotal(BigDecimal.ZERO)
                .cronograma(cronograma)
                .build();
    }

    // =========================
    // MÉTODOS AUXILIARES
    // =========================
    private Integer calcularTiempoEjecucion(Integer idOts) {
        return cronogramaOtRepository
                .sumarDuracionPorOt(idOts)
                .map(BigDecimal::intValue)
                .orElse(0);
    }

    private String generarNroRequerimiento(Usuario usuario, Ots ots) {

        String nombres = usuario.getTrabajador().getNombres().trim();
        String apellidos = usuario.getTrabajador().getApellidos().trim();

        String iniNombre = nombres.substring(0, 2).toUpperCase();
        String iniApellido = apellidos.substring(0, 2).toUpperCase();

        String numeroOt = String.format("%06d", ots.getOt());

        return "C_" + iniNombre + "_" + iniApellido + "_" + numeroOt;
    }
}

