package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.ssomaDTO.*;
import com.backend.comfutura.dto.response.ssomaDTO.*;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.ssoma.ats.*;
import com.backend.comfutura.model.ssoma.capacitacion.*;
import com.backend.comfutura.model.ssoma.catalogo.*;
import com.backend.comfutura.model.ssoma.inspeccion.*;
import com.backend.comfutura.model.ssoma.petar.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.repository.ssoma.*;
import com.backend.comfutura.service.SsomaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SsomaServiceImpl implements SsomaService {

    // Repositorios principales
    private final AtsRepository atsRepository;
    private final CapacitacionRepository capacitacionRepository;
    private final InspeccionEppRepository inspeccionEppRepository;
    private final InspeccionHerramientaRepository inspeccionHerramientaRepository;
    private final PetarRepository petarRepository;

    // Repositorios para relaciones
    private final TrabajadorRepository trabajadorRepository;
    private final OtsRepository otsRepository;

    // Repositorios para tablas relacionadas de ATS
    private final AtsParticipanteRepository atsParticipanteRepository;
    private final AtsRiesgoRepository atsRiesgoRepository;
    private final AtsEppRepository atsEppRepository;
    private final AtsTipoRiesgoRepository atsTipoRiesgoRepository;

    // Repositorios para Capacitación
    private final CapacitacionAsistenteRepository capacitacionAsistenteRepository;

    // Repositorios para Inspección EPP
    private final InspeccionEppDetalleRepository inspeccionEppDetalleRepository;

    // Repositorios para Inspección Herramienta
    private final InspeccionHerramientaDetalleRepository inspeccionHerramientaDetalleRepository;

    // Repositorios para PETAR
    private final PetarRespuestaRepository petarRespuestaRepository;
    private final PetarAutorizadoRepository petarAutorizadoRepository;
// En la sección de repositorios, agrega estos:

    // Repositorios para catálogos
    private final TrabajoRepository trabajoRepository;
    private final RolTrabajoRepository rolTrabajoRepository;
    private final TareaRepository tareaRepository;
    private final PeligroRepository peligroRepository;
    private final RiesgoRepository riesgoRepository;
    private final MedidaControlRepository medidaControlRepository;
    private final EppRepository eppRepository;
    private final TipoRiesgoTrabajoRepository tipoRiesgoTrabajoRepository;
    private final HerramientaRepository herramientaRepository;

    @Override
    @Transactional
    public SsomaCompletoResponse crearSsomaCompleto(SsomaCompletoRequest request) {
        String transaccionId = UUID.randomUUID().toString();

        try {
            // 1. Crear ATS
            AtsResponse atsResponse = null;
            if (request.getAts() != null) {
                atsResponse = crearAts(request.getAts());
            }

            // 2. Crear Capacitación
            CapacitacionResponse capacitacionResponse = null;
            if (request.getCapacitacion() != null) {
                capacitacionResponse = crearCapacitacion(request.getCapacitacion());
            }

            // 3. Crear Inspección EPP
            InspeccionEppResponse inspeccionEppResponse = null;
            if (request.getInspeccionEpp() != null) {
                inspeccionEppResponse = crearInspeccionEpp(request.getInspeccionEpp());
            }

            // 4. Crear Inspección Herramientas
            InspeccionHerramientaResponse inspeccionHerramientaResponse = null;
            if (request.getInspeccionHerramienta() != null) {
                inspeccionHerramientaResponse = crearInspeccionHerramienta(request.getInspeccionHerramienta());
            }

            // 5. Crear PETAR
            PetarResponse petarResponse = null;
            if (request.getPetar() != null) {
                petarResponse = crearPetar(request.getPetar());
            }

            // Construir respuesta
            SsomaCompletoResponse response = new SsomaCompletoResponse();
            response.setMensaje("SSOMA completo creado exitosamente");
            response.setTransaccionId(transaccionId);
            response.setAts(atsResponse);
            response.setCapacitacion(capacitacionResponse);
            response.setInspeccionEpp(inspeccionEppResponse);
            response.setInspeccionHerramienta(inspeccionHerramientaResponse);
            response.setPetar(petarResponse);

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear SSOMA completo: " + e.getMessage(), e);
        }
    }
    @Override
    public SsomaCompletoResponse obtenerSsomaCompletoPorOts(Integer idOts) {
        SsomaCompletoResponse response = new SsomaCompletoResponse();

        // Obtener ATS de esta OT
        List<Ats> atsList = atsRepository.findByOtsIdOts(idOts);
        if (!atsList.isEmpty()) {
            response.setAts(construirAtsResponse(atsList.get(0))); // O tomar el más reciente
        }

        // Obtener Capacitaciones de esta OT
        List<Capacitacion> capacitaciones = capacitacionRepository.findByOtsIdOts(idOts);
        if (!capacitaciones.isEmpty()) {
            response.setCapacitacion(convertirCapacitacionAResponse(capacitaciones.get(0)));
        }

        // Obtener Inspecciones EPP de esta OT
        List<InspeccionEpp> inspeccionesEpp = inspeccionEppRepository.findByOtsIdOts(idOts);
        if (!inspeccionesEpp.isEmpty()) {
            response.setInspeccionEpp(convertirInspeccionEppAResponse(inspeccionesEpp.get(0)));
        }

        // Obtener Inspecciones Herramienta de esta OT
        List<InspeccionHerramienta> inspeccionesHerramienta = inspeccionHerramientaRepository.findByOtsIdOts(idOts);
        if (!inspeccionesHerramienta.isEmpty()) {
            response.setInspeccionHerramienta(convertirInspeccionHerramientaAResponse(inspeccionesHerramienta.get(0)));
        }

        // Obtener PETAR de esta OT
        List<Petar> petares = petarRepository.findByOtsIdOts(idOts);
        if (!petares.isEmpty()) {
            response.setPetar(convertirPetarAResponse(petares.get(0)));
        }

        response.setMensaje("SSOMA completo obtenido para OT: " + idOts);

        return response;
    }



    // =====================================================
    // 1. CREAR ATS
    // =====================================================

    @Override
    @Transactional
    public AtsResponse crearAts(AtsRequest request) {
        try {
            // Generar número de registro
            String numeroRegistro = generarNumeroRegistro("ATS");

            // Llamar al método privado
            return crearAtsPrivado(request, numeroRegistro);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear ATS: " + e.getMessage(), e);
        }
    }

    private AtsResponse crearAtsPrivado(AtsRequest request, String numeroRegistro) {
        if (request == null) {
            throw new IllegalArgumentException("La solicitud ATS no puede ser nula");
        }

        // Crear ATS principal
        Ats ats = new Ats();
        ats.setFecha(LocalDate.now()); // Automático
        ats.setHora(LocalTime.now()); // Automático
        ats.setNumeroRegistro(numeroRegistro);
        ats.setEmpresa(request.getEmpresa());
        ats.setLugarTrabajo(request.getLugarTrabajo());
        ats.setCoordenadas(request.getCoordenadas());

        // Relación con OT
        if (request.getIdOts() != null) {
            Ots ot = otsRepository.findById(request.getIdOts())
                    .orElseThrow(() -> new RuntimeException("OT no encontrada con ID: " + request.getIdOts()));
            ats.setOts(ot);
        }

        // Personas (supervisor, responsable, SST)
        if (request.getIdSupervisorTrabajo() != null) {
            Trabajador supervisor = trabajadorRepository.findById(request.getIdSupervisorTrabajo())
                    .orElseThrow(() -> new RuntimeException("Supervisor no encontrado con ID: " + request.getIdSupervisorTrabajo()));
            ats.setSupervisorTrabajo(supervisor);
        }

        if (request.getIdResponsableLugar() != null) {
            Trabajador responsable = trabajadorRepository.findById(request.getIdResponsableLugar())
                    .orElseThrow(() -> new RuntimeException("Responsable no encontrado con ID: " + request.getIdResponsableLugar()));
            ats.setResponsableLugar(responsable);
        }

        if (request.getIdSupervisorSst() != null) {
            Trabajador supervisorSst = trabajadorRepository.findById(request.getIdSupervisorSst())
                    .orElseThrow(() -> new RuntimeException("Supervisor SST no encontrado con ID: " + request.getIdSupervisorSst()));
            ats.setSupervisorSst(supervisorSst);
        }

        // Trabajo
        if (request.getIdTrabajo() != null) {
            Trabajo trabajo = trabajoRepository.findById(request.getIdTrabajo())
                    .orElseThrow(() -> new RuntimeException("Trabajo no encontrado con ID: " + request.getIdTrabajo()));
            ats.setTrabajo(trabajo);
        }

        Ats atsGuardado = atsRepository.save(ats);

        // Guardar participantes
        if (request.getParticipantes() != null && !request.getParticipantes().isEmpty()) {
            for (AtsParticipanteRequest participanteReq : request.getParticipantes()) {
                if (participanteReq != null && participanteReq.getIdTrabajador() != null) {
                    AtsParticipante participante = new AtsParticipante();
                    participante.setAts(atsGuardado);

                    Trabajador trabajador = trabajadorRepository.findById(participanteReq.getIdTrabajador())
                            .orElseThrow(() -> new RuntimeException("Trabajador participante no encontrado con ID: " + participanteReq.getIdTrabajador()));
                    participante.setTrabajador(trabajador);

                    if (participanteReq.getIdRol() != null) {
                        RolTrabajo rol = rolTrabajoRepository.findById(participanteReq.getIdRol())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado con ID: " + participanteReq.getIdRol()));
                        participante.setRol(rol);
                    }

                    atsParticipanteRepository.save(participante);
                }
            }
        }

        // Guardar riesgos (secuencia tarea -> peligro -> riesgo -> medida control)
        if (request.getRiesgos() != null && !request.getRiesgos().isEmpty()) {
            for (AtsRiesgoRequest riesgoReq : request.getRiesgos()) {
                if (riesgoReq != null) {
                    AtsRiesgo riesgo = new AtsRiesgo();
                    riesgo.setAts(atsGuardado);

                    // Tarea
                    if (riesgoReq.getIdTarea() != null) {
                        Tarea tarea = tareaRepository.findById(riesgoReq.getIdTarea())
                                .orElseThrow(() -> new RuntimeException("Tarea no encontrada con ID: " + riesgoReq.getIdTarea()));
                        riesgo.setTarea(tarea);
                    }

                    // Peligro
                    if (riesgoReq.getIdPeligro() != null) {
                        Peligro peligro = peligroRepository.findById(riesgoReq.getIdPeligro())
                                .orElseThrow(() -> new RuntimeException("Peligro no encontrado con ID: " + riesgoReq.getIdPeligro()));
                        riesgo.setPeligro(peligro);
                    }

                    // Riesgo
                    if (riesgoReq.getIdRiesgo() != null) {
                        Riesgo riesgoCatalogo = riesgoRepository.findById(riesgoReq.getIdRiesgo())
                                .orElseThrow(() -> new RuntimeException("Riesgo no encontrado con ID: " + riesgoReq.getIdRiesgo()));
                        riesgo.setRiesgo(riesgoCatalogo);
                    }

                    // Medida de control
                    if (riesgoReq.getIdMedida() != null) {
                        MedidaControl medida = medidaControlRepository.findById(riesgoReq.getIdMedida())
                                .orElseThrow(() -> new RuntimeException("Medida de control no encontrada con ID: " + riesgoReq.getIdMedida()));
                        riesgo.setMedida(medida);
                    }

                    // Observación opcional
                    if (riesgoReq.getObservacion() != null) {
                        riesgo.setObservacion(riesgoReq.getObservacion());
                    }

                    atsRiesgoRepository.save(riesgo);
                }
            }
        }

        // Guardar EPP
        if (request.getEppIds() != null && !request.getEppIds().isEmpty()) {
            for (Integer eppId : request.getEppIds()) {
                if (eppId != null) {
                    AtsEpp atsEpp = new AtsEpp();
                    atsEpp.setAts(atsGuardado);

                    Epp epp = eppRepository.findById(eppId)
                            .orElseThrow(() -> new RuntimeException("EPP no encontrado con ID: " + eppId));
                    atsEpp.setEpp(epp);

                    atsEppRepository.save(atsEpp);
                }
            }
        }

        // Guardar tipos de riesgo
        if (request.getTipoRiesgoIds() != null && !request.getTipoRiesgoIds().isEmpty()) {
            for (Integer tipoRiesgoId : request.getTipoRiesgoIds()) {
                if (tipoRiesgoId != null) {
                    AtsTipoRiesgo tipoRiesgo = new AtsTipoRiesgo();
                    tipoRiesgo.setAts(atsGuardado);

                    TipoRiesgoTrabajo tipoRiesgoObj = tipoRiesgoTrabajoRepository.findById(tipoRiesgoId)
                            .orElseThrow(() -> new RuntimeException("Tipo riesgo no encontrado con ID: " + tipoRiesgoId));
                    tipoRiesgo.setTipoRiesgo(tipoRiesgoObj);

                    atsTipoRiesgoRepository.save(tipoRiesgo);
                }
            }
        }

        // Construir respuesta
        return construirAtsResponse(atsGuardado);
    }

    private AtsResponse construirAtsResponse(Ats ats) {
        AtsResponse response = new AtsResponse();
        response.setIdAts(ats.getIdAts());
        response.setFecha(ats.getFecha());
        response.setHora(ats.getHora());
        response.setNumeroRegistro(ats.getNumeroRegistro());
        response.setEmpresa(ats.getEmpresa());
        response.setLugarTrabajo(ats.getLugarTrabajo());
        response.setCoordenadas(ats.getCoordenadas());

        // Información de OT
        if (ats.getOts() != null) {
            response.setIdOts(ats.getOts().getIdOts());
            response.setCodigoOt(ats.getOts().getOt());
        }

        // Información de Trabajo
        if (ats.getTrabajo() != null) {
            response.setIdTrabajo(ats.getTrabajo().getIdTrabajo());
            response.setNombreTrabajo(ats.getTrabajo().getNombre());
        }

        // Información de personas
        if (ats.getSupervisorTrabajo() != null) {
            response.setIdSupervisorTrabajo(ats.getSupervisorTrabajo().getIdTrabajador());
            response.setNombreSupervisorTrabajo(
                    ats.getSupervisorTrabajo().getNombres() + " " +
                            ats.getSupervisorTrabajo().getApellidos()
            );
        }

        if (ats.getResponsableLugar() != null) {
            response.setIdResponsableLugar(ats.getResponsableLugar().getIdTrabajador());
            response.setNombreResponsableLugar(
                    ats.getResponsableLugar().getNombres() + " " +
                            ats.getResponsableLugar().getApellidos()
            );
        }

        if (ats.getSupervisorSst() != null) {
            response.setIdSupervisorSst(ats.getSupervisorSst().getIdTrabajador());
            response.setNombreSupervisorSst(
                    ats.getSupervisorSst().getNombres() + " " +
                            ats.getSupervisorSst().getApellidos()
            );
        }

        return response;
    }




    @Override
    public AtsResponse obtenerAtsPorId(Integer id) {
        Ats ats = atsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ATS no encontrado"));
        return convertirAtsAResponse(ats);
    }

    @Override
    public List<AtsResponse> listarTodosAts() {
        return atsRepository.findAll().stream()
                .map(this::convertirAtsAResponse)
                .toList();
    }

    // =====================================================
    // 2. CREAR CAPACITACIÓN
    // =====================================================

    @Override
    @Transactional
    public CapacitacionResponse crearCapacitacion(CapacitacionRequest request) {
        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNumeroRegistro(generarNumeroRegistro("CAP"));
        capacitacion.setTema(request.getTema());
        capacitacion.setFecha(LocalDate.now());
        capacitacion.setHora(LocalTime.now());
        capacitacion.setTipoCharla(request.getTipoCharla() != null ? request.getTipoCharla() : "CHARLA_5_MINUTOS");

        // Relación con OT
        if (request.getIdOts() != null) {
            Ots ot = otsRepository.findById(request.getIdOts())
                    .orElseThrow(() -> new RuntimeException("OT no encontrada"));
            capacitacion.setOts(ot);
        }

        // Personas
        if (request.getIdCapacitador() != null) {
            capacitacion.setCapacitador(trabajadorRepository.findById(request.getIdCapacitador()).orElse(null));
        }
        if (request.getIdSupervisorTrabajo() != null) {
            capacitacion.setSupervisorTrabajo(trabajadorRepository.findById(request.getIdSupervisorTrabajo()).orElse(null));
        }
        if (request.getIdResponsableLugar() != null) {
            capacitacion.setResponsableLugar(trabajadorRepository.findById(request.getIdResponsableLugar()).orElse(null));
        }
        if (request.getIdSupervisorSst() != null) {
            capacitacion.setSupervisorSst(trabajadorRepository.findById(request.getIdSupervisorSst()).orElse(null));
        }

        Capacitacion capacitacionGuardada = capacitacionRepository.save(capacitacion);

        // Guardar asistentes
        if (request.getAsistentes() != null && !request.getAsistentes().isEmpty()) {
            for (CapacitacionAsistenteRequest asistenteReq : request.getAsistentes()) {
                CapacitacionAsistente asistente = new CapacitacionAsistente();
                asistente.setCapacitacion(capacitacionGuardada);

                Trabajador trabajador = trabajadorRepository.findById(asistenteReq.getIdTrabajador())
                        .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
                asistente.setTrabajador(trabajador);

                asistente.setObservaciones(asistenteReq.getObservaciones());
                capacitacionAsistenteRepository.save(asistente);
            }
        }

        return convertirCapacitacionAResponse(capacitacionGuardada);
    }

    @Override
    public CapacitacionResponse obtenerCapacitacionPorId(Integer id) {
        Capacitacion capacitacion = capacitacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Capacitación no encontrada"));
        return convertirCapacitacionAResponse(capacitacion);
    }

    @Override
    public List<CapacitacionResponse> listarTodasCapacitaciones() {
        return capacitacionRepository.findAll().stream()
                .map(this::convertirCapacitacionAResponse)
                .toList();
    }

    // =====================================================
    // 3. CREAR INSPECCIÓN EPP
    // =====================================================

    @Override
    @Transactional
    public InspeccionEppResponse crearInspeccionEpp(InspeccionEppRequest request) {
        InspeccionEpp inspeccion = new InspeccionEpp();
        inspeccion.setNumeroRegistro(generarNumeroRegistro("IEPP"));
        inspeccion.setFecha(LocalDate.now());
        inspeccion.setTipoInspeccion(request.getTipoInspeccion() != null ? request.getTipoInspeccion() : "PLANIFICADA");
        inspeccion.setAreaTrabajo(request.getAreaTrabajo());

        // Relación con OT
        if (request.getIdOts() != null) {
            Ots ot = otsRepository.findById(request.getIdOts())
                    .orElseThrow(() -> new RuntimeException("OT no encontrada"));
            inspeccion.setOts(ot);
        }

        // Personas
        if (request.getIdInspector() != null) {
            inspeccion.setInspector(trabajadorRepository.findById(request.getIdInspector()).orElse(null));
        }
        if (request.getIdSupervisorTrabajo() != null) {
            inspeccion.setSupervisorTrabajo(trabajadorRepository.findById(request.getIdSupervisorTrabajo()).orElse(null));
        }
        if (request.getIdResponsableLugar() != null) {
            inspeccion.setResponsableLugar(trabajadorRepository.findById(request.getIdResponsableLugar()).orElse(null));
        }
        if (request.getIdSupervisorSst() != null) {
            inspeccion.setSupervisorSst(trabajadorRepository.findById(request.getIdSupervisorSst()).orElse(null));
        }

        InspeccionEpp inspeccionGuardada = inspeccionEppRepository.save(inspeccion);

        // Guardar detalles
        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            for (InspeccionEppDetalleRequest detalleReq : request.getDetalles()) {
                InspeccionEppDetalle detalle = new InspeccionEppDetalle();
                detalle.setInspeccion(inspeccionGuardada);

                Trabajador trabajador = trabajadorRepository.findById(detalleReq.getIdTrabajador())
                        .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
                detalle.setTrabajador(trabajador);

                Epp epp = eppRepository.findById(detalleReq.getIdEpp()).orElse(null);
                detalle.setEpp(epp);

                detalle.setCumple(detalleReq.getCumple());
                detalle.setObservacion(detalleReq.getObservacion());
                detalle.setAccionCorrectiva(detalleReq.getAccionCorrectiva());

                inspeccionEppDetalleRepository.save(detalle);
            }
        }

        return convertirInspeccionEppAResponse(inspeccionGuardada);
    }

    @Override
    public InspeccionEppResponse obtenerInspeccionEppPorId(Integer id) {
        InspeccionEpp inspeccion = inspeccionEppRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspección EPP no encontrada"));
        return convertirInspeccionEppAResponse(inspeccion);
    }

    @Override
    public List<InspeccionEppResponse> listarTodasInspeccionesEpp() {
        return inspeccionEppRepository.findAll().stream()
                .map(this::convertirInspeccionEppAResponse)
                .toList();
    }

    // =====================================================
    // 4. CREAR INSPECCIÓN HERRAMIENTAS
    // =====================================================

    @Override
    @Transactional
    public InspeccionHerramientaResponse crearInspeccionHerramienta(InspeccionHerramientaRequest request) {
        InspeccionHerramienta inspeccion = new InspeccionHerramienta();
        inspeccion.setNumeroRegistro(generarNumeroRegistro("IHER"));
        inspeccion.setFecha(LocalDate.now());
        inspeccion.setUbicacionSede(request.getUbicacionSede());

        // Relación con OT
        if (request.getIdOts() != null) {
            Ots ot = otsRepository.findById(request.getIdOts())
                    .orElseThrow(() -> new RuntimeException("OT no encontrada"));
            inspeccion.setOts(ot);
        }

        // Cliente y Proyecto
        if (request.getIdCliente() != null) {
            inspeccion.setCliente(new com.backend.comfutura.model.Cliente());
            inspeccion.getCliente().setIdCliente(request.getIdCliente());
        }
        if (request.getIdProyecto() != null) {
            inspeccion.setProyecto(new com.backend.comfutura.model.Proyecto());
            inspeccion.getProyecto().setIdProyecto(request.getIdProyecto());
        }

        // Personas
        if (request.getIdSupervisor() != null) {
            inspeccion.setSupervisor(trabajadorRepository.findById(request.getIdSupervisor()).orElse(null));
        }
        if (request.getIdSupervisorTrabajo() != null) {
            inspeccion.setSupervisorTrabajo(trabajadorRepository.findById(request.getIdSupervisorTrabajo()).orElse(null));
        }
        if (request.getIdResponsableLugar() != null) {
            inspeccion.setResponsableLugar(trabajadorRepository.findById(request.getIdResponsableLugar()).orElse(null));
        }
        if (request.getIdSupervisorSst() != null) {
            inspeccion.setSupervisorSst(trabajadorRepository.findById(request.getIdSupervisorSst()).orElse(null));
        }

        InspeccionHerramienta inspeccionGuardada = inspeccionHerramientaRepository.save(inspeccion);

        // Guardar detalles
        if (request.getDetalles() != null && !request.getDetalles().isEmpty()) {
            for (InspeccionHerramientaDetalleRequest detalleReq : request.getDetalles()) {
                InspeccionHerramientaDetalle detalle = new InspeccionHerramientaDetalle();
                detalle.setInspeccion(inspeccionGuardada);

                Herramienta herramienta = herramientaRepository.findById(detalleReq.getIdHerramienta()).orElse(null);
                detalle.setHerramienta(herramienta);

                detalle.setObservacion(detalleReq.getObservacion());
                inspeccionHerramientaDetalleRepository.save(detalle);
            }
        }

        return convertirInspeccionHerramientaAResponse(inspeccionGuardada);
    }

    @Override
    public InspeccionHerramientaResponse obtenerInspeccionHerramientaPorId(Integer id) {
        InspeccionHerramienta inspeccion = inspeccionHerramientaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspección de herramientas no encontrada"));
        return convertirInspeccionHerramientaAResponse(inspeccion);
    }

    @Override
    public List<InspeccionHerramientaResponse> listarTodasInspeccionesHerramientas() {
        return inspeccionHerramientaRepository.findAll().stream()
                .map(this::convertirInspeccionHerramientaAResponse)
                .toList();
    }

    // =====================================================
    // 5. CREAR PETAR
    // =====================================================

    @Override
    @Transactional
    public PetarResponse crearPetar(PetarRequest request) {
        Petar petar = new Petar();
        petar.setNumeroRegistro(generarNumeroRegistro("PETAR"));
        petar.setFecha(LocalDate.now());
        petar.setRequiereEvaluacionAmbiente(request.getRequiereEvaluacionAmbiente());
        petar.setAperturaLineaEquipos(request.getAperturaLineaEquipos());
        petar.setHoraInicio(request.getHoraInicio());
        petar.setRecursosNecesarios(request.getRecursosNecesarios());
        petar.setProcedimiento(request.getProcedimiento());

        // Relación con OT
        if (request.getIdOts() != null) {
            Ots ot = otsRepository.findById(request.getIdOts())
                    .orElseThrow(() -> new RuntimeException("OT no encontrada"));
            petar.setOts(ot);
        }

        // Personas
        if (request.getIdSupervisorTrabajo() != null) {
            petar.setSupervisorTrabajo(trabajadorRepository.findById(request.getIdSupervisorTrabajo()).orElse(null));
        }
        if (request.getIdResponsableLugar() != null) {
            petar.setResponsableLugar(trabajadorRepository.findById(request.getIdResponsableLugar()).orElse(null));
        }
        if (request.getIdSupervisorSst() != null) {
            petar.setSupervisorSst(trabajadorRepository.findById(request.getIdSupervisorSst()).orElse(null));
        }
        if (request.getIdBrigadista() != null) {
            petar.setBrigadista(trabajadorRepository.findById(request.getIdBrigadista()).orElse(null));
        }
        if (request.getIdResponsableTrabajo() != null) {
            petar.setResponsableTrabajo(trabajadorRepository.findById(request.getIdResponsableTrabajo()).orElse(null));
        }

        Petar petarGuardado = petarRepository.save(petar);

        // Guardar respuestas generales
        if (request.getRespuestas() != null && !request.getRespuestas().isEmpty()) {
            for (PetarRespuestaRequest respuestaReq : request.getRespuestas()) {
                PetarRespuesta respuesta = new PetarRespuesta();
                respuesta.setPetar(petarGuardado);

                respuesta.setPregunta(new PetarPregunta());
                respuesta.getPregunta().setId(respuestaReq.getIdPregunta());

                respuesta.setRespuesta(respuestaReq.getRespuesta());
                respuesta.setObservacion(respuestaReq.getObservacion());

                petarRespuestaRepository.save(respuesta);
            }
        }

        // Guardar trabajadores autorizados
        if (request.getTrabajadoresAutorizadosIds() != null && !request.getTrabajadoresAutorizadosIds().isEmpty()) {
            for (Integer trabajadorId : request.getTrabajadoresAutorizadosIds()) {
                PetarAutorizado autorizado = new PetarAutorizado();
                autorizado.setPetar(petarGuardado);

                Trabajador trabajador = trabajadorRepository.findById(trabajadorId)
                        .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
                autorizado.setTrabajador(trabajador);

                autorizado.setConformidadRequerida(request.getConformidadRequerida());

                petarAutorizadoRepository.save(autorizado);
            }
        }

        return convertirPetarAResponse(petarGuardado);
    }

    @Override
    public PetarResponse obtenerPetarPorId(Integer id) {
        Petar petar = petarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("PETAR no encontrado"));
        return convertirPetarAResponse(petar);
    }

    @Override
    public List<PetarResponse> listarTodosPetar() {
        return petarRepository.findAll().stream()
                .map(this::convertirPetarAResponse)
                .toList();
    }

    // =====================================================
    // MÉTODOS AUXILIARES
    // =====================================================

    // Método para generar número de registro (si no lo tienes)
    private String generarNumeroRegistro(String tipo) {
        String anio = String.valueOf(LocalDate.now().getYear());
        String mes = String.format("%02d", LocalDate.now().getMonthValue());
        String secuencia = String.format("%06d", new Random().nextInt(999999));
        return tipo + "-" + anio + mes + "-" + secuencia;
    }
    private AtsResponse convertirAtsAResponse(Ats ats) {
        AtsResponse response = new AtsResponse();
        response.setIdAts(ats.getIdAts());
        response.setFecha(ats.getFecha());
        response.setHora(ats.getHora());
        response.setNumeroRegistro(ats.getNumeroRegistro());
        response.setEmpresa(ats.getEmpresa());
        response.setLugarTrabajo(ats.getLugarTrabajo());
        response.setCoordenadas(ats.getCoordenadas());

        if (ats.getOts() != null) {
            response.setIdOts(ats.getOts().getIdOts());
            response.setCodigoOt(ats.getOts().getOt());
        }

        if (ats.getSupervisorTrabajo() != null) {
            response.setIdSupervisorTrabajo(ats.getSupervisorTrabajo().getIdTrabajador());
            response.setNombreSupervisorTrabajo(ats.getSupervisorTrabajo().getNombres() + " " +
                    ats.getSupervisorTrabajo().getApellidos());
        }

        if (ats.getResponsableLugar() != null) {
            response.setIdResponsableLugar(ats.getResponsableLugar().getIdTrabajador());
            response.setNombreResponsableLugar(ats.getResponsableLugar().getNombres() + " " +
                    ats.getResponsableLugar().getApellidos());
        }

        if (ats.getSupervisorSst() != null) {
            response.setIdSupervisorSst(ats.getSupervisorSst().getIdTrabajador());
            response.setNombreSupervisorSst(ats.getSupervisorSst().getNombres() + " " +
                    ats.getSupervisorSst().getApellidos());
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
        response.setTipoCharla(capacitacion.getTipoCharla());

        if (capacitacion.getCapacitador() != null) {
            response.setIdCapacitador(capacitacion.getCapacitador().getIdTrabajador());
            response.setNombreCapacitador(capacitacion.getCapacitador().getNombres() + " " +
                    capacitacion.getCapacitador().getApellidos());
        }

        if (capacitacion.getSupervisorTrabajo() != null) {
            response.setIdSupervisorTrabajo(capacitacion.getSupervisorTrabajo().getIdTrabajador());
            response.setNombreSupervisorTrabajo(capacitacion.getSupervisorTrabajo().getNombres() + " " +
                    capacitacion.getSupervisorTrabajo().getApellidos());
        }

        if (capacitacion.getResponsableLugar() != null) {
            response.setIdResponsableLugar(capacitacion.getResponsableLugar().getIdTrabajador());
            response.setNombreResponsableLugar(capacitacion.getResponsableLugar().getNombres() + " " +
                    capacitacion.getResponsableLugar().getApellidos());
        }

        if (capacitacion.getSupervisorSst() != null) {
            response.setIdSupervisorSst(capacitacion.getSupervisorSst().getIdTrabajador());
            response.setNombreSupervisorSst(capacitacion.getSupervisorSst().getNombres() + " " +
                    capacitacion.getSupervisorSst().getApellidos());
        }

        return response;
    }

    private InspeccionEppResponse convertirInspeccionEppAResponse(InspeccionEpp inspeccion) {
        InspeccionEppResponse response = new InspeccionEppResponse();
        response.setId(inspeccion.getId());
        response.setNumeroRegistro(inspeccion.getNumeroRegistro());
        response.setFecha(inspeccion.getFecha());
        response.setTipoInspeccion(inspeccion.getTipoInspeccion());
        response.setAreaTrabajo(inspeccion.getAreaTrabajo());

        if (inspeccion.getInspector() != null) {
            response.setIdInspector(inspeccion.getInspector().getIdTrabajador());
            response.setNombreInspector(inspeccion.getInspector().getNombres() + " " +
                    inspeccion.getInspector().getApellidos());
        }

        if (inspeccion.getSupervisorTrabajo() != null) {
            response.setIdSupervisorTrabajo(inspeccion.getSupervisorTrabajo().getIdTrabajador());
            response.setNombreSupervisorTrabajo(inspeccion.getSupervisorTrabajo().getNombres() + " " +
                    inspeccion.getSupervisorTrabajo().getApellidos());
        }

        if (inspeccion.getResponsableLugar() != null) {
            response.setIdResponsableLugar(inspeccion.getResponsableLugar().getIdTrabajador());
            response.setNombreResponsableLugar(inspeccion.getResponsableLugar().getNombres() + " " +
                    inspeccion.getResponsableLugar().getApellidos());
        }

        if (inspeccion.getSupervisorSst() != null) {
            response.setIdSupervisorSst(inspeccion.getSupervisorSst().getIdTrabajador());
            response.setNombreSupervisorSst(inspeccion.getSupervisorSst().getNombres() + " " +
                    inspeccion.getSupervisorSst().getApellidos());
        }

        return response;
    }

    private InspeccionHerramientaResponse convertirInspeccionHerramientaAResponse(InspeccionHerramienta inspeccion) {
        InspeccionHerramientaResponse response = new InspeccionHerramientaResponse();
        response.setId(inspeccion.getId());
        response.setNumeroRegistro(inspeccion.getNumeroRegistro());
        response.setFecha(inspeccion.getFecha());
        response.setUbicacionSede(inspeccion.getUbicacionSede());

        if (inspeccion.getSupervisor() != null) {
            response.setIdSupervisor(inspeccion.getSupervisor().getIdTrabajador());
            response.setNombreSupervisor(inspeccion.getSupervisor().getNombres() + " " +
                    inspeccion.getSupervisor().getApellidos());
        }

        if (inspeccion.getSupervisorTrabajo() != null) {
            response.setIdSupervisorTrabajo(inspeccion.getSupervisorTrabajo().getIdTrabajador());
            response.setNombreSupervisorTrabajo(inspeccion.getSupervisorTrabajo().getNombres() + " " +
                    inspeccion.getSupervisorTrabajo().getApellidos());
        }

        if (inspeccion.getResponsableLugar() != null) {
            response.setIdResponsableLugar(inspeccion.getResponsableLugar().getIdTrabajador());
            response.setNombreResponsableLugar(inspeccion.getResponsableLugar().getNombres() + " " +
                    inspeccion.getResponsableLugar().getApellidos());
        }

        if (inspeccion.getSupervisorSst() != null) {
            response.setIdSupervisorSst(inspeccion.getSupervisorSst().getIdTrabajador());
            response.setNombreSupervisorSst(inspeccion.getSupervisorSst().getNombres() + " " +
                    inspeccion.getSupervisorSst().getApellidos());
        }

        return response;
    }

    private PetarResponse convertirPetarAResponse(Petar petar) {
        PetarResponse response = new PetarResponse();
        response.setId(petar.getId());
        response.setNumeroRegistro(petar.getNumeroRegistro());
        response.setFecha(petar.getFecha());
        response.setRequiereEvaluacionAmbiente(petar.getRequiereEvaluacionAmbiente());
        response.setAperturaLineaEquipos(petar.getAperturaLineaEquipos());
        response.setHoraInicio(petar.getHoraInicio());
        response.setRecursosNecesarios(petar.getRecursosNecesarios());
        response.setProcedimiento(petar.getProcedimiento());

        if (petar.getSupervisorTrabajo() != null) {
            response.setIdSupervisorTrabajo(petar.getSupervisorTrabajo().getIdTrabajador());
            response.setNombreSupervisorTrabajo(petar.getSupervisorTrabajo().getNombres() + " " +
                    petar.getSupervisorTrabajo().getApellidos());
        }

        if (petar.getResponsableLugar() != null) {
            response.setIdResponsableLugar(petar.getResponsableLugar().getIdTrabajador());
            response.setNombreResponsableLugar(petar.getResponsableLugar().getNombres() + " " +
                    petar.getResponsableLugar().getApellidos());
        }

        if (petar.getSupervisorSst() != null) {
            response.setIdSupervisorSst(petar.getSupervisorSst().getIdTrabajador());
            response.setNombreSupervisorSst(petar.getSupervisorSst().getNombres() + " " +
                    petar.getSupervisorSst().getApellidos());
        }

        if (petar.getBrigadista() != null) {
            response.setIdBrigadista(petar.getBrigadista().getIdTrabajador());
            response.setNombreBrigadista(petar.getBrigadista().getNombres() + " " +
                    petar.getBrigadista().getApellidos());
        }

        if (petar.getResponsableTrabajo() != null) {
            response.setIdResponsableTrabajo(petar.getResponsableTrabajo().getIdTrabajador());
            response.setNombreResponsableTrabajo(petar.getResponsableTrabajo().getNombres() + " " +
                    petar.getResponsableTrabajo().getApellidos());
        }

        return response;
    }
}