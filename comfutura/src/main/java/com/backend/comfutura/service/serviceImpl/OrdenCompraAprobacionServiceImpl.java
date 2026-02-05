package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.OrdenCompraAprobacionRequest;
import com.backend.comfutura.dto.response.OrdenCompraAprobacionResponse;
import com.backend.comfutura.model.EstadoOc;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.model.OrdenCompraAprobacion;
import com.backend.comfutura.repository.EstadoOcRepository;
import com.backend.comfutura.repository.OrdenCompraAprobacionRepository;
import com.backend.comfutura.repository.OrdenCompraRepository;
import com.backend.comfutura.repository.UsuarioRepository;
import com.backend.comfutura.service.OrdenCompraAprobacionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class OrdenCompraAprobacionServiceImpl
        implements OrdenCompraAprobacionService {

    @Autowired
    private OrdenCompraAprobacionRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstadoOcRepository estadoOcRepository;

    @Autowired
    private OrdenCompraRepository ordenCompraRepository;

    // ======================================================
    // ✅ APROBAR / RECHAZAR NIVEL (SIN SEGURIDAD)
    // ======================================================
    @Override
    @Transactional
    public OrdenCompraAprobacionResponse aprobar(
            Integer idOc,
            Integer nivel,
            String estado,
            OrdenCompraAprobacionRequest request) {

        // 1️⃣ Obtener aprobación
        OrdenCompraAprobacion actual = repository
                .findByOrdenCompra_IdOcAndNivel(idOc, nivel)
                .orElseThrow(() ->
                        new RuntimeException("No existe aprobación para este nivel")
                );

        // 2️⃣ Validar estado actual
        if (!"PENDIENTE".equals(actual.getEstado())) {
            throw new RuntimeException("Este nivel no está pendiente");
        }

        // 3️⃣ Guardar aprobación
        actual.setEstado(estado);
        actual.setAprobadoPor(obtenerNombreAprobador());
        actual.setFechaFin(LocalDateTime.now());
        repository.save(actual);

        OrdenCompra oc = actual.getOrdenCompra();

        // 🔴 SI SE RECHAZA → OC RECHAZADA
        if ("RECHAZADO".equals(estado)) {

            EstadoOc rechazada = estadoOcRepository.findById(3)
                    .orElseThrow(() ->
                            new RuntimeException("Estado RECHAZADA no encontrado")
                    );

            oc.setEstadoOC(rechazada);
            ordenCompraRepository.save(oc);

            return mapToResponse(actual);
        }

        // 🟢 SI SE APRUEBA
        if ("APROBADO".equals(estado)) {

            // Habilitar siguiente nivel
            repository.findByOrdenCompra_IdOcAndNivel(idOc, nivel + 1)
                    .ifPresent(sig -> {
                        if ("BLOQUEADO".equals(sig.getEstado())) {
                            sig.setEstado("PENDIENTE");
                            sig.setFechaInicio(LocalDateTime.now());
                            repository.save(sig);
                        }
                    });

            // 🟢 Último nivel → OC APROBADA
            if (nivel == 3) {

                EstadoOc aprobada = estadoOcRepository.findById(2)
                        .orElseThrow(() ->
                                new RuntimeException("Estado APROBADA no encontrado")
                        );

                oc.setEstadoOC(aprobada);
                ordenCompraRepository.save(oc);
            }
        }

        return mapToResponse(actual);
    }


    // ======================================================
    // 📄 HISTORIAL
    // ======================================================
    @Override
    public List<OrdenCompraAprobacionResponse> obtenerHistorial(Integer idOc) {
        return repository.findByOrdenCompra_IdOcOrderByNivel(idOc)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ======================================================
    // 🔁 MAP RESPONSE
    // ======================================================
    private OrdenCompraAprobacionResponse mapToResponse(OrdenCompraAprobacion a) {

        OrdenCompraAprobacionResponse r = new OrdenCompraAprobacionResponse();

        r.setNivel(a.getNivel());
        r.setEstado(a.getEstado());
        r.setAprobadoPor(a.getAprobadoPor());
        r.setFechaInicio(a.getFechaInicio());
        r.setFechaFin(a.getFechaFin());

        if (a.getFechaInicio() != null) {
            LocalDateTime fin = a.getFechaFin() != null
                    ? a.getFechaFin()
                    : LocalDateTime.now();

            r.setDiasEnEstado(
                    (int) ChronoUnit.DAYS.between(a.getFechaInicio(), fin)
            );
        }

        // 👉 SIN SEGURIDAD: solo depende del estado
        r.setPuedeAprobar("PENDIENTE".equals(a.getEstado()));

        return r;
    }

    // ======================================================
// 🚀 INICIALIZAR APROBACIONES
// ======================================================
    @Transactional
    @Override
    public void inicializarAprobaciones(OrdenCompra oc) {

        // 1️⃣ Crear los niveles de aprobación
        for (int nivel = 1; nivel <= 3; nivel++) {

            OrdenCompraAprobacion a = new OrdenCompraAprobacion();
            a.setOrdenCompra(oc);
            a.setNivel(nivel);
            a.setEstado(nivel == 1 ? "PENDIENTE" : "BLOQUEADO");

            if (nivel == 1) {
                a.setFechaInicio(LocalDateTime.now());
            }

            repository.save(a);
        }

        // 2️⃣ Cambiar estado de la OC a EN PROCESO
        EstadoOc enProceso = estadoOcRepository.findById(5)
                .orElseThrow(() -> new RuntimeException("Estado EN PROCESO no existe"));

        oc.setEstadoOC(enProceso);
        ordenCompraRepository.save(oc);
    }


    ///
    private String obtenerNombreAprobador() {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return "DESCONOCIDO";
        }

        String username = auth.getName();

        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    if (usuario.getTrabajador() != null) {
                        return usuario.getTrabajador().getNombres()
                                + " "
                                + usuario.getTrabajador().getApellidos();
                    }
                    return usuario.getUsername();
                })
                .orElse(username);
    }

}
