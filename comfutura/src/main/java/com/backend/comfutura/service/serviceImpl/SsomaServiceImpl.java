package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.ssomaDTO.*;
import com.backend.comfutura.dto.response.ssomaDTO.*;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.ssoma.ats.*;
import com.backend.comfutura.model.ssoma.capacitacion.*;
import com.backend.comfutura.model.ssoma.inspeccion.*;
import com.backend.comfutura.model.ssoma.petar.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.repository.ssoma.*;
import com.backend.comfutura.service.SsomaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SsomaServiceImpl implements SsomaService {

    private final AtsRepository atsRepository;
    private final CapacitacionRepository capacitacionRepository;
    private final InspeccionEppRepository inspeccionEppRepository;
    private final InspeccionHerramientaRepository inspeccionHerramientaRepository;
    private final PetarRepository petarRepository;
    private final TrabajadorRepository trabajadorRepository;

    // Repositorios para tablas relacionadas
    private final AtsParticipanteRepository atsParticipanteRepository;
    private final AtsRiesgoRepository atsRiesgoRepository;
    private final AtsEppRepository atsEppRepository;
    private final AtsTipoRiesgoRepository atsTipoRiesgoRepository;
    private final CapacitacionAsistenteRepository capacitacionAsistenteRepository;
    private final InspeccionEppDetalleRepository inspeccionEppDetalleRepository;
    private final InspeccionHerramientaDetalleRepository inspeccionHerramientaDetalleRepository;
    private final PetarRespuestaRepository petarRespuestaRepository;
    private final PetarAutorizadoRepository petarAutorizadoRepository;

    @Override
    @Transactional
    public SsomaResponse crearCompletoSsoma(SsomaRequest request) {
        String transaccionId = UUID.randomUUID().toString();

        try {
            // 1. Crear ATS
            AtsResponse atsResponse = crearAts(request.getAts());

            // 2. Crear Capacitación
            CapacitacionResponse capacitacionResponse = crearCapacitacion(request.getCapacitacion());

            // 3. Crear Inspección EPP
            InspeccionEppResponse inspeccionEppResponse = crearInspeccionEpp(request.getInspeccionEpp());

            // 4. Crear Inspección Herramientas
            InspeccionHerramientaResponse inspeccionHerramientaResponse =
                    crearInspeccionHerramienta(request.getInspeccionHerramienta());

            // 5. Crear PETAR
            PetarResponse petarResponse = crearPetar(request.getPetar());

            // Construir respuesta
            SsomaResponse response = new SsomaResponse();
            response.setMensaje("SSOMA creado exitosamente");
            response.setTransaccionId(transaccionId);
            response.setAts(atsResponse);
            response.setCapacitacion(capacitacionResponse);
            response.setInspeccionEpp(inspeccionEppResponse);
            response.setInspeccionHerramienta(inspeccionHerramientaResponse);
            response.setPetar(petarResponse);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear SSOMA: " + e.getMessage(), e);
        }
    }

    private AtsResponse crearAts(AtsRequest request) {
        if (request == null) return null;

        // Crear ATS principal
        Ats ats = new Ats();
        ats.setFecha(request.getFecha());
        ats.setHora(request.getHora());
        ats.setEmpresa(request.getEmpresa());
        ats.setLugarTrabajo(request.getLugarTrabajo());
        ats.setTrabajo(new com.backend.comfutura.model.ssoma.catalogo.TipoTrabajo());
        ats.getTrabajo().setIdTrabajo(request.getIdTrabajo());

        Ats atsGuardado = atsRepository.save(ats);

        // Guardar participantes
        if (request.getParticipantes() != null) {
            for (AtsParticipanteRequest participanteReq : request.getParticipantes()) {
                AtsParticipante participante = new AtsParticipante();
                participante.setAts(atsGuardado);
                participante.setTrabajador(new Trabajador());
                participante.getTrabajador().setIdTrabajador(participanteReq.getIdTrabajador());
                participante.setRol(new RolTrabajo());
                participante.getRol().setIdRol(participanteReq.getIdRol());
                atsParticipanteRepository.save(participante);
            }
        }

        // Guardar riesgos
        if (request.getRiesgos() != null) {
            for (AtsRiesgoRequest riesgoReq : request.getRiesgos()) {
                AtsRiesgo riesgo = new AtsRiesgo();
                riesgo.setAts(atsGuardado);
                riesgo.setTarea(new Tarea());
                riesgo.getTarea().setIdTarea(riesgoReq.getIdTarea());
                riesgo.setPeligro(new com.backend.comfutura.model.ssoma.catalogo.Peligro());
                riesgo.getPeligro().setIdPeligro(riesgoReq.getIdPeligro());
                riesgo.setRiesgo(new com.backend.comfutura.model.ssoma.catalogo.Riesgo());
                riesgo.getRiesgo().setIdRiesgo(riesgoReq.getIdRiesgo());
                riesgo.setMedida(new com.backend.comfutura.model.ssoma.catalogo.MedidaControl());
                riesgo.getMedida().setIdMedida(riesgoReq.getIdMedida());
                atsRiesgoRepository.save(riesgo);
            }
        }

        // Guardar EPP
        if (request.getEppIds() != null) {
            for (Integer eppId : request.getEppIds()) {
                com.backend.comfutura.model.ssoma.ats.AtsEpp atsEpp =
                        new com.backend.comfutura.model.ssoma.ats.AtsEpp();
                atsEpp.setAts(atsGuardado);
                atsEpp.setEpp(new com.backend.comfutura.model.ssoma.catalogo.Epp());
                atsEpp.getEpp().setIdEpp(eppId);
                atsEppRepository.save(atsEpp);
            }
        }

        // Guardar tipos de riesgo
        if (request.getTipoRiesgoIds() != null) {
            for (Integer tipoRiesgoId : request.getTipoRiesgoIds()) {
                AtsTipoRiesgo tipoRiesgo = new AtsTipoRiesgo();
                tipoRiesgo.setAts(atsGuardado);
                tipoRiesgo.setTipoRiesgo(new com.backend.comfutura.model.ssoma.catalogo.TipoRiesgoTrabajo());
                tipoRiesgo.getTipoRiesgo().setId(tipoRiesgoId);
                atsTipoRiesgoRepository.save(tipoRiesgo);
            }
        }

        // Construir respuesta
        AtsResponse response = new AtsResponse();
        response.setIdAts(atsGuardado.getIdAts());
        response.setFecha(atsGuardado.getFecha());
        response.setHora(atsGuardado.getHora());
        response.setEmpresa(atsGuardado.getEmpresa());
        response.setLugarTrabajo(atsGuardado.getLugarTrabajo());
        response.setIdTrabajo(request.getIdTrabajo());
        response.setParticipantes(request.getParticipantes());
        response.setRiesgos(request.getRiesgos());
        response.setEppIds(request.getEppIds());
        response.setTipoRiesgoIds(request.getTipoRiesgoIds());

        return response;
    }

    private CapacitacionResponse crearCapacitacion(CapacitacionRequest request) {
        if (request == null) return null;

        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNumeroRegistro(request.getNumeroRegistro());
        capacitacion.setTema(request.getTema());
        capacitacion.setFecha(request.getFecha());
        capacitacion.setHora(request.getHora());
        capacitacion.setCapacitador(new Trabajador());
        capacitacion.getCapacitador().setIdTrabajador(request.getIdCapacitador());

        Capacitacion capacitacionGuardada = capacitacionRepository.save(capacitacion);

        // Guardar asistentes
        if (request.getAsistentes() != null) {
            for (CapacitacionAsistenteRequest asistenteReq : request.getAsistentes()) {
                CapacitacionAsistente asistente = new CapacitacionAsistente();
                asistente.setCapacitacion(capacitacionGuardada);
                asistente.setTrabajador(new Trabajador());
                asistente.getTrabajador().setIdTrabajador(asistenteReq.getIdTrabajador());
                asistente.setObservaciones(asistenteReq.getObservaciones());
                capacitacionAsistenteRepository.save(asistente);
            }
        }

        // Construir respuesta
        CapacitacionResponse response = new CapacitacionResponse();
        response.setIdCapacitacion(capacitacionGuardada.getIdCapacitacion());
        response.setNumeroRegistro(capacitacionGuardada.getNumeroRegistro());
        response.setTema(capacitacionGuardada.getTema());
        response.setFecha(capacitacionGuardada.getFecha());
        response.setHora(capacitacionGuardada.getHora());
        response.setIdCapacitador(request.getIdCapacitador());
        response.setAsistentes(request.getAsistentes());

        // Obtener nombre del capacitador
        Optional<Trabajador> capacitadorOpt = trabajadorRepository.findById(request.getIdCapacitador());
        capacitadorOpt.ifPresent(t -> response.setNombreCapacitador(
                t.getNombres() + " " + t.getApellidos()
        ));

        return response;
    }

    private InspeccionEppResponse crearInspeccionEpp(InspeccionEppRequest request) {
        if (request == null) return null;

        InspeccionEpp inspeccion = new InspeccionEpp();
        inspeccion.setNumeroRegistro(request.getNumeroRegistro());
        inspeccion.setFecha(request.getFecha());
        inspeccion.setInspector(new Trabajador());
        inspeccion.getInspector().setIdTrabajador(request.getIdInspector());

        InspeccionEpp inspeccionGuardada = inspeccionEppRepository.save(inspeccion);

        // Guardar detalles
        if (request.getDetalles() != null) {
            for (InspeccionEppDetalleRequest detalleReq : request.getDetalles()) {
                InspeccionEppDetalle detalle = new InspeccionEppDetalle();
                detalle.setInspeccion(inspeccionGuardada);
                detalle.setTrabajador(new Trabajador());
                detalle.getTrabajador().setIdTrabajador(detalleReq.getIdTrabajador());
                detalle.setEpp(new com.backend.comfutura.model.ssoma.catalogo.Epp());
                detalle.getEpp().setIdEpp(detalleReq.getIdEpp());
                detalle.setCumple(detalleReq.getCumple());
                detalle.setObservacion(detalleReq.getObservacion());
                inspeccionEppDetalleRepository.save(detalle);
            }
        }

        // Construir respuesta
        InspeccionEppResponse response = new InspeccionEppResponse();
        response.setId(inspeccionGuardada.getId());
        response.setNumeroRegistro(inspeccionGuardada.getNumeroRegistro());
        response.setFecha(inspeccionGuardada.getFecha());
        response.setIdInspector(request.getIdInspector());
        response.setDetalles(request.getDetalles());

        // Obtener nombre del inspector
        Optional<Trabajador> inspectorOpt = trabajadorRepository.findById(request.getIdInspector());
        inspectorOpt.ifPresent(t -> response.setNombreInspector(
                t.getNombres() + " " + t.getApellidos()
        ));

        return response;
    }

    private InspeccionHerramientaResponse crearInspeccionHerramienta(InspeccionHerramientaRequest request) {
        if (request == null) return null;

        InspeccionHerramienta inspeccion = new InspeccionHerramienta();
        inspeccion.setNumeroRegistro(request.getNumeroRegistro());
        inspeccion.setFecha(request.getFecha());
        inspeccion.setSupervisor(new Trabajador());
        inspeccion.getSupervisor().setIdTrabajador(request.getIdSupervisor());

        InspeccionHerramienta inspeccionGuardada = inspeccionHerramientaRepository.save(inspeccion);

        // Guardar detalles
        if (request.getDetalles() != null) {
            for (InspeccionHerramientaDetalleRequest detalleReq : request.getDetalles()) {
                InspeccionHerramientaDetalle detalle = new InspeccionHerramientaDetalle();
                detalle.setInspeccion(inspeccionGuardada);
                detalle.setHerramienta(new com.backend.comfutura.model.ssoma.catalogo.Herramienta());
                detalle.getHerramienta().setId(detalleReq.getIdHerramienta());
                detalle.setCumple(detalleReq.getCumple());
                detalle.setFotoUrl(detalleReq.getFotoUrl());
                detalle.setObservacion(detalleReq.getObservacion());
                inspeccionHerramientaDetalleRepository.save(detalle);
            }
        }

        // Construir respuesta
        InspeccionHerramientaResponse response = new InspeccionHerramientaResponse();
        response.setId(inspeccionGuardada.getId());
        response.setNumeroRegistro(inspeccionGuardada.getNumeroRegistro());
        response.setFecha(inspeccionGuardada.getFecha());
        response.setIdSupervisor(request.getIdSupervisor());
        response.setDetalles(request.getDetalles());

        // Obtener nombre del supervisor
        Optional<Trabajador> supervisorOpt = trabajadorRepository.findById(request.getIdSupervisor());
        supervisorOpt.ifPresent(t -> response.setNombreSupervisor(
                t.getNombres() + " " + t.getApellidos()
        ));

        return response;
    }

    private PetarResponse crearPetar(PetarRequest request) {
        if (request == null) return null;

        Petar petar = new Petar();
        petar.setNumeroRegistro(request.getNumeroRegistro());
        petar.setFecha(request.getFecha());

        Petar petarGuardado = petarRepository.save(petar);

        // Guardar respuestas
        if (request.getRespuestas() != null) {
            for (PetarRespuestaRequest respuestaReq : request.getRespuestas()) {
                PetarRespuesta respuesta = new PetarRespuesta();
                respuesta.setPetar(petarGuardado);
                respuesta.setPregunta(new PetarPregunta());
                respuesta.getPregunta().setId(respuestaReq.getIdPregunta());
                respuesta.setRespuesta(respuestaReq.getRespuesta());
                petarRespuestaRepository.save(respuesta);
            }
        }

        // Guardar trabajadores autorizados
        if (request.getTrabajadoresAutorizadosIds() != null) {
            for (Integer trabajadorId : request.getTrabajadoresAutorizadosIds()) {
                PetarAutorizado autorizado = new PetarAutorizado();
                autorizado.setPetar(petarGuardado);
                autorizado.setTrabajador(new Trabajador());
                autorizado.getTrabajador().setIdTrabajador(trabajadorId);
                petarAutorizadoRepository.save(autorizado);
            }
        }

        // Construir respuesta
        PetarResponse response = new PetarResponse();
        response.setId(petarGuardado.getId());
        response.setNumeroRegistro(petarGuardado.getNumeroRegistro());
        response.setFecha(petarGuardado.getFecha());
        response.setRespuestas(request.getRespuestas());
        response.setTrabajadoresAutorizadosIds(request.getTrabajadoresAutorizadosIds());

        return response;
    }

    @Override
    public List<Object> listarTodosAts() {
        List<Ats> atsList = atsRepository.findAllWithTrabajo();
        return atsList.stream().map(this::convertirAtsAResponse).collect(Collectors.toList());
    }

    @Override
    public List<Object> listarTodasCapacitaciones() {
        List<Capacitacion> capacitacionList = capacitacionRepository.findAllWithCapacitador();
        return capacitacionList.stream()
                .map(this::convertirCapacitacionAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> listarTodasInspeccionesEpp() {
        List<InspeccionEpp> inspeccionList = inspeccionEppRepository.findAllWithInspector();
        return inspeccionList.stream()
                .map(this::convertirInspeccionEppAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> listarTodasInspeccionesHerramientas() {
        List<InspeccionHerramienta> inspeccionList = inspeccionHerramientaRepository.findAllWithSupervisor();
        return inspeccionList.stream()
                .map(this::convertirInspeccionHerramientaAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> listarTodosPetar() {
        List<Petar> petarList = petarRepository.findAllPetar();
        return petarList.stream()
                .map(this::convertirPetarAResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<Object> obtenerSsomaPorFecha(String fecha) {
        LocalDate fechaBusqueda = LocalDate.parse(fecha, DateTimeFormatter.ISO_DATE);
        List<Object> resultados = new ArrayList<>();

        // Obtener ATS por fecha
        List<Ats> atsList = atsRepository.findByFecha(fechaBusqueda);
        atsList.forEach(ats -> resultados.add(convertirAtsAResponse(ats)));

        // Obtener Capacitaciones por fecha (necesitarías agregar este método al repository)
        // List<Capacitacion> capacitacionList = capacitacionRepository.findByFecha(fechaBusqueda);
        // capacitacionList.forEach(c -> resultados.add(convertirCapacitacionAResponse(c)));

        return resultados;
    }

    // Métodos de conversión auxiliares
    private AtsResponse convertirAtsAResponse(Ats ats) {
        AtsResponse response = new AtsResponse();
        response.setIdAts(ats.getIdAts());
        response.setFecha(ats.getFecha());
        response.setHora(ats.getHora());
        response.setEmpresa(ats.getEmpresa());
        response.setLugarTrabajo(ats.getLugarTrabajo());
        if (ats.getTrabajo() != null) {
            response.setIdTrabajo(ats.getTrabajo().getIdTrabajo());
            response.setNombreTrabajo(ats.getTrabajo().getNombre());
        }
        return response;
    }

    private CapacitacionResponse convertirCapacitacionAResponse(Capacitacion capacitacion) {
        CapacitacionResponse response = new CapacitacionResponse();
        response.setIdCapacitacion(capacitacion.getIdCapacitacion());
        response.setNumeroRegistro(capacitacion.getNumeroRegistro());
        response.setTema(capacitacion.getTema());
        response.setFecha(capacitacion.getFecha());
        response.setHora(capacitacion.getHora());
        if (capacitacion.getCapacitador() != null) {
            response.setIdCapacitador(capacitacion.getCapacitador().getIdTrabajador());
            response.setNombreCapacitador(
                    capacitacion.getCapacitador().getNombres() + " " +
                            capacitacion.getCapacitador().getApellidos()
            );
        }
        return response;
    }

    private InspeccionEppResponse convertirInspeccionEppAResponse(InspeccionEpp inspeccion) {
        InspeccionEppResponse response = new InspeccionEppResponse();
        response.setId(inspeccion.getId());
        response.setNumeroRegistro(inspeccion.getNumeroRegistro());
        response.setFecha(inspeccion.getFecha());
        if (inspeccion.getInspector() != null) {
            response.setIdInspector(inspeccion.getInspector().getIdTrabajador());
            response.setNombreInspector(
                    inspeccion.getInspector().getNombres() + " " +
                            inspeccion.getInspector().getApellidos()
            );
        }
        return response;
    }

    private InspeccionHerramientaResponse convertirInspeccionHerramientaAResponse(InspeccionHerramienta inspeccion) {
        InspeccionHerramientaResponse response = new InspeccionHerramientaResponse();
        response.setId(inspeccion.getId());
        response.setNumeroRegistro(inspeccion.getNumeroRegistro());
        response.setFecha(inspeccion.getFecha());
        if (inspeccion.getSupervisor() != null) {
            response.setIdSupervisor(inspeccion.getSupervisor().getIdTrabajador());
            response.setNombreSupervisor(
                    inspeccion.getSupervisor().getNombres() + " " +
                            inspeccion.getSupervisor().getApellidos()
            );
        }
        return response;
    }

    private PetarResponse convertirPetarAResponse(Petar petar) {
        PetarResponse response = new PetarResponse();
        response.setId(petar.getId());
        response.setNumeroRegistro(petar.getNumeroRegistro());
        response.setFecha(petar.getFecha());
        return response;
    }
}