package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.OrdenCompraAprobacionRequest;
import com.backend.comfutura.dto.response.OrdenCompraAprobacionResponse;
import com.backend.comfutura.model.EstadoOc;
import com.backend.comfutura.model.OrdenCompra;
import com.backend.comfutura.model.OrdenCompraAprobacion;
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
        actual.setAprobadoEmail(request.getAprobadoEmail());
        actual.setComentario(request.getComentario());
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
                            enviarCorreoAprobacionPendiente(sig);
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
        r.setAprobadoEmail(a.getAprobadoEmail());
        r.setComentario(a.getComentario());
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


    private void inicializarAprobaciones(OrdenCompra oc) {

        // 🔹 Normalizar cliente y área
        String cliente = oc.getOts()
                .getCliente()
                .getRazonSocial()
                .trim()
                .toUpperCase();

        String area = oc.getOts()
                .getArea()
                .getNombre()
                .trim()
                .toUpperCase();

        String key = cliente + "-" + area;

        // 🔹 Tabla simulada de aprobadores
        Map<String, String[]> aprobadores = new HashMap<>();

        // ===== CLIENTE CLARO =====
        aprobadores.put("CLARO-CW", new String[]{"LUIS ÑIQUEN", "l.loayza@sudcomgroup.com"});
        aprobadores.put("CLARO-ENERGIA", new String[]{"LUIS ÑIQUEN", "l.loayza@sudcomgroup.com"});
        aprobadores.put("CLARO-PEXT", new String[]{"ISAAC MELENDREZ", "imelendez@comfutura.com"});
        aprobadores.put("CLARO-SAQ", new String[]{"KELLY CLEMENTE", "kclementem@comfutura.com"});
        aprobadores.put("CLARO-TI", new String[]{"JOSE GONZALEZ", "jgonzales@comfutura.com"});

        // ===== CLIENTE ENTEL =====
        aprobadores.put("ENTEL-CW", new String[]{"LUIS ÑIQUEN", "l.loayza@sudcomgroup.com"});
        aprobadores.put("ENTEL-ENERGIA", new String[]{"FRANKLIN MERINO", "fmerino@comfutura.com"});
        aprobadores.put("ENTEL-PEXT", new String[]{"PEDRO COLQUE", "pcolque@comfutura.com"});
        aprobadores.put("ENTEL-SAQ", new String[]{"KELLY CLEMENTE", "kclementem@comfutura.com"});
        aprobadores.put("ENTEL-TI", new String[]{"FRANKLIN MERINO", "fmerino@comfutura.com"});

        // ===== CLIENTE GYGA =====
        aprobadores.put("GYGA-PEXT", new String[]{"ISAAC MELENDREZ", "imelendez@comfutura.com"});

        // ===== CLIENTE STL =====
        aprobadores.put("STL-CW", new String[]{"LUIS ÑIQUEN", "l.loayza@sudcomgroup.com"});
        aprobadores.put("STL-PEXT", new String[]{"ISAAC MELENDREZ", "imelendez@comfutura.com"});
        aprobadores.put("STL-SAQ", new String[]{"KELLY CLEMENTE", "kclementem@comfutura.com"});
        aprobadores.put("STL-TI", new String[]{"JOHN SANCHEZ", "jsanchez@comfutura.com"});

        // ===== CLIENTE COMFUTURA =====
        aprobadores.put("COMFUTURA-LOGISTICA", new String[]{"JOSUE OTERO", "jotero@comfutura.com"});

        // 🔹 Defaults niveles 2 y 3
        String nivel2Correos = "saq@comfutura.com,finanzas@comfutura.com";
        String nivel3Correos = "omasias@comfutura.com";

        // 🔹 Nivel 1 dinámico
        String[] nivel1 = aprobadores.getOrDefault(
                key,
                new String[]{"APROBADOR POR DEFECTO", "default@comfutura.com"}
        );

        for (int nivel = 1; nivel <= 3; nivel++) {

            OrdenCompraAprobacion a = new OrdenCompraAprobacion();
            a.setOrdenCompra(oc);
            a.setNivel(nivel);

            if (nivel == 1) {
                a.setEstado("PENDIENTE");
                a.setFechaInicio(LocalDateTime.now());
                a.setAprobadoPor(nivel1[0]);
                a.setAprobadoEmail(nivel1[1]);

                repository.save(a);

                // 📩 correo inicial
                enviarCorreoAprobacionPendiente(a);
            }

            else if (nivel == 2) {
                a.setEstado("BLOQUEADO");
                a.setAprobadoPor("SEGUNDO NIVEL");
                a.setAprobadoEmail(nivel2Correos);

                repository.save(a);
            }

            else {
                a.setEstado("BLOQUEADO");
                a.setAprobadoPor("TERCER NIVEL");
                a.setAprobadoEmail(nivel3Correos);

                repository.save(a);
            }
        }}


    private void enviarCorreoAprobacionPendiente(OrdenCompraAprobacion aprobacion) {

        String[] correos = aprobacion
                .getAprobadoEmail()
                .split(",");

        Integer idOc = aprobacion
                .getOrdenCompra()
                .getIdOc();

        String asunto = "Aprobación pendiente OC #" + idOc;

        String cuerpo = """
        <h3>Orden de Compra pendiente de aprobación</h3>
        <p>Hola <b>%s</b>,</p>
        <p>Tienes una <b>Orden de Compra</b> pendiente de aprobación.</p>
        <p><b>Nivel:</b> %d</p>
        <p>Por favor ingresa al sistema para aprobar o rechazar.</p>
    """.formatted(
                aprobacion.getAprobadoPor(),
                aprobacion.getNivel()
        );

        emailService.enviarCorreo(
                correos,
                asunto,
                cuerpo
        );
    }




}
