package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.OrdenCompraAprobacionRequest;
import com.backend.comfutura.dto.response.OrdenCompraAprobacionResponse;
import com.backend.comfutura.model.Aprobador;
import com.backend.comfutura.model.EstadoOc;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.model.OrdenCompraAprobacion;
import com.backend.comfutura.repository.AprobadorRepository;
import com.backend.comfutura.repository.EstadoOcRepository;
import com.backend.comfutura.repository.OrdenCompraAprobacionRepository;
import com.backend.comfutura.repository.OrdenCompraRepository;
import com.backend.comfutura.service.EmailService;
import com.backend.comfutura.service.OrdenCompraAprobacionService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.time.LocalDateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrdenCompraAprobacionServiceImpl
        implements OrdenCompraAprobacionService {
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrdenCompraAprobacionRepository repository;
    @Autowired
    private EstadoOcRepository estadoOcRepository;
    @Autowired
    private AprobadorRepository aprobadorRepository;
    @Autowired
    private OrdenCompraRepository ordenCompraRepository; // Si no lo tienes, créalo

    @Override
    @Transactional
    public OrdenCompraAprobacionResponse aprobar(
            Integer idOc,
            Integer nivel,
            String estado,
            OrdenCompraAprobacionRequest request) {

        OrdenCompraAprobacion actual = repository
                .findByOrdenCompra_IdOcAndNivel(idOc, nivel)
                .orElseThrow(() ->
                        new RuntimeException("No existe aprobación para este nivel")
                );

        if (!"PENDIENTE".equals(actual.getEstado())) {
            throw new RuntimeException("Este nivel no está pendiente");
        }

        if (nivel > 1) {
            OrdenCompraAprobacion anterior = repository
                    .findByOrdenCompra_IdOcAndNivel(idOc, nivel - 1)
                    .orElseThrow(() ->
                            new RuntimeException("Nivel anterior no encontrado")
                    );

            if (!"APROBADO".equals(anterior.getEstado())) {
                throw new RuntimeException("El nivel anterior aún no está aprobado");
            }
        }

        // ✅ aprobar / rechazar
        actual.setEstado(estado);
        actual.setAprobadoPor(request.getAprobadoPor());

        actual.setFechaFin(LocalDateTime.now());

        repository.save(actual);

        // 🔓 desbloquear siguiente nivel + correo
        if ("APROBADO".equals(estado)) {

            repository.findByOrdenCompra_IdOcAndNivel(idOc, nivel + 1)
                    .ifPresent(sig -> {
                        if ("BLOQUEADO".equals(sig.getEstado())) {
                            sig.setEstado("PENDIENTE");
                            sig.setFechaInicio(LocalDateTime.now());
                            repository.save(sig);

                            // 📩 correo SOLO aviso
                            enviarCorreoAprobadoresNivel(
                                    sig.getOrdenCompra(),
                                    sig.getNivel()
                            );

                        }
                    });

            // ✅ último nivel → aprobar OC
            if (nivel == 3) {
                OrdenCompra oc = actual.getOrdenCompra();
                EstadoOc aprobado = estadoOcRepository.findById(3)
                        .orElseThrow(() -> new RuntimeException("Estado APROBADA no encontrado"));
                oc.setEstadoOC(aprobado);
                ordenCompraRepository.save(oc);
            }
        }

        return mapToResponse(actual);
    }


    @Override
    public List<OrdenCompraAprobacionResponse> obtenerHistorial(Integer idOc) {
        return repository.findByOrdenCompra_IdOcOrderByNivel(idOc)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private OrdenCompraAprobacionResponse mapToResponse(
            OrdenCompraAprobacion a) {

        OrdenCompraAprobacionResponse r =
                new OrdenCompraAprobacionResponse();

        r.setNivel(a.getNivel());
        r.setEstado(a.getEstado());
        r.setAprobadoPor(a.getAprobadoPor());

        r.setFechaInicio(a.getFechaInicio());
        r.setFechaFin(a.getFechaFin());

        // 🔥 cálculo aquí (NO DB)
        if (a.getFechaInicio() != null) {
            LocalDateTime fin = a.getFechaFin() != null
                    ? a.getFechaFin()
                    : LocalDateTime.now();

            r.setDiasEnEstado(
                    (int) ChronoUnit.DAYS.between(a.getFechaInicio(), fin)
            );
        }

        return r;
    }




    @Transactional
    @Override
    public void inicializarAprobaciones(OrdenCompra oc) {
        if (oc.getOts() == null || oc.getOts().getCliente() == null || oc.getOts().getArea() == null) {
            throw new RuntimeException("OT, cliente o área no están asignados a la OC");
        }

        for (int nivel = 1; nivel <= 3; nivel++) {
            OrdenCompraAprobacion a = new OrdenCompraAprobacion();
            a.setOrdenCompra(oc);
            a.setNivel(nivel);
            a.setEstado(nivel == 1 ? "PENDIENTE" : "BLOQUEADO");
            if (nivel == 1) a.setFechaInicio(LocalDateTime.now());

            repository.save(a);  // Guardar cada nivel
        }

        // Enviar correo solo al primer nivel
        enviarCorreoAprobadoresNivel(oc, 1);
    }

    private void enviarCorreoAprobadoresNivel(
            OrdenCompra oc,
            Integer nivel) {

        Integer idCliente = oc.getOts().getCliente().getIdCliente();
        Integer idArea = oc.getOts().getArea().getIdArea();

        List<Aprobador> aprobadores =
                aprobadorRepository
                        .findByCliente_IdClienteAndArea_IdAreaAndNivelAndActivoTrue(
                                idCliente, idArea, nivel
                        );

        if (aprobadores.isEmpty()) return;

        String[] correos = aprobadores.stream()
                .map(a -> a.getTrabajador().getCorreoCorporativo())
                .toArray(String[]::new);

        String asunto = "Aprobación pendiente OC #" + oc.getIdOc();

        String cuerpo = """
        <h3>Orden de Compra pendiente</h3>
        <p>Tienes una OC pendiente de aprobación.</p>
        <p><b>Nivel:</b> %d</p>
        <p>Ingresa al sistema para revisarla.</p>
        """.formatted(nivel);

//        emailService.enviarCorreo(correos, asunto, cuerpo);
    }







}
