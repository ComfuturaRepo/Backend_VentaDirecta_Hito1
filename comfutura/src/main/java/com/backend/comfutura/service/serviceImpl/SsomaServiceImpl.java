package com.backend.comfutura.service.serviceImpl;
import com.backend.comfutura.dto.request.ssoma.*;
import com.backend.comfutura.dto.response.ssomaDTO.*;
import com.backend.comfutura.model.Empresa;
import com.backend.comfutura.model.MaestroServicio;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.model.ssoma.*;
import com.backend.comfutura.repository.EmpresaRepository;
import com.backend.comfutura.repository.MaestroServicioRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.repository.TrabajadorRepository;
import com.backend.comfutura.repository.ssoma.*;
import com.backend.comfutura.service.SsomaService;
import com.backend.comfutura.service.cloudinary.CloudinaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SsomaServiceImpl implements SsomaService {

    private final SsomaFormularioRepository ssomaFormularioRepository;
    private final SsomaSecuenciaTareaRepository secuenciaTareaRepository;
    private final SsomaParticipanteRepository participanteRepository;
    private final SsomaParticipanteFirmaRepository participanteFirmaRepository;
    private final SsomaParticipanteFotoRepository participanteFotoRepository;
    private final SsomaChecklistSeguridadRepository checklistSeguridadRepository;
    private final SsomaChecklistFotoRepository checklistFotoRepository;
    private final SsomaEppCheckRepository eppCheckRepository;
    private final SsomaEppFotoRepository eppFotoRepository;
    private final SsomaTemaCharlaRepository temaCharlaRepository;
    private final SsomaCharlaRepository charlaRepository;
    private final SsomaCharlaVideoRepository charlaVideoRepository;
    private final SsomaInspeccionTrabajadorRepository inspeccionTrabajadorRepository;
    private final SsomaHerramientaMaestraRepository herramientaMaestraRepository;
    private final SsomaHerramientaInspeccionRepository herramientaInspeccionRepository;
    private final SsomaHerramientaFotoRepository herramientaFotoRepository;
    private final SsomaPetarRepository petarRepository;
    private final SsomaPetarPreguntaMaestraRepository petarPreguntaMaestraRepository;
    private final SsomaPetarRespuestaRepository petarRespuestaRepository;
    private final SsomaEquipoProteccionRepository equipoProteccionRepository;

    // Repositorios de entidades maestras
    private final OtsRepository otsRepository;
    private final EmpresaRepository empresaRepository;
    private final MaestroServicioRepository maestroServicioRepository;
    private final TrabajadorRepository trabajadorRepository;

    // Cloudinary Service
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional
    public SsomaResponseDTO crearFormularioCompleto(SsomaRequestDTO request) {
        log.info("Creando formulario SSOMA completo para OT: {}", request.getIdOts());

        // 1. Validar que la OT existe
        Ots ot = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada con ID: " + request.getIdOts()));

        // 2. Crear formulario principal
        SsomaFormulario formulario = crearFormularioPrincipal(request, ot);
        SsomaFormulario formularioGuardado = ssomaFormularioRepository.save(formulario);

        // 3. Crear participantes y subir fotos/firmas
        List<SsomaParticipante> participantes = crearParticipantes(
                formularioGuardado,
                request.getParticipantes(),
                request.getFotosParticipantes()
        );

        // 4. Crear secuencias de tarea
        crearSecuenciasTarea(formularioGuardado, request.getSecuenciasTarea());

        // 5. Crear checklist seguridad
        crearChecklistSeguridad(formularioGuardado, request.getChecklistSeguridad());

        // 6. Crear EPP checks
        crearEppChecks(formularioGuardado, request.getEppChecks(), request.getFotosEpp());

        // 7. Crear charla y video
        if (request.getCharla() != null) {
            crearCharla(formularioGuardado, request.getCharla(), request.getVideoCharla());
        }

        // 8. Crear inspecciones trabajador
        crearInspeccionesTrabajador(formularioGuardado, request.getInspeccionesTrabajador());

        // 9. Crear herramientas inspección
        crearHerramientasInspeccion(formularioGuardado, request.getHerramientasInspeccion(), request.getFotosHerramientas());

        // 10. Crear PETAR
        if (request.getPetar() != null) {
            crearPetar(formularioGuardado, request.getPetar());
        }

        log.info("Formulario SSOMA creado exitosamente con ID: {}", formularioGuardado.getIdSsoma());

        // Retornar respuesta con todos los datos
        return convertirAResponseDTO(formularioGuardado);
    }

    private SsomaFormulario crearFormularioPrincipal(SsomaRequestDTO request, Ots ot) {
        Empresa empresa = null;
        if (request.getEmpresaId() != null) {
            empresa = empresaRepository.findById(request.getEmpresaId())
                    .orElse(null);
        }

        MaestroServicio trabajo = null;
        if (request.getTrabajoId() != null) {
            trabajo = maestroServicioRepository.findById(request.getTrabajoId())
                    .orElse(null);
        }

        return SsomaFormulario.builder()
                .ots(ot)
                .empresa(empresa)
                .trabajo(trabajo)
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .fecha(request.getFecha() != null ? request.getFecha() : LocalDateTime.now())
                .horaInicio(request.getHoraInicio())
                .horaFin(request.getHoraFin())
                .horaInicioTrabajo(request.getHoraInicioTrabajo())
                .horaFinTrabajo(request.getHoraFinTrabajo())
                .supervisorTrabajo(request.getSupervisorTrabajo())
                .supervisorSst(request.getSupervisorSst())
                .responsableArea(request.getResponsableArea())
                .build();
    }

    private List<SsomaParticipante> crearParticipantes(
            SsomaFormulario formulario,
            List<ParticipanteDTO> participantesDTO,
            List<SsomaRequestDTO.FotoParticipante> fotosParticipantes) {

        if (participantesDTO == null) return new ArrayList<>();

        AtomicInteger index = new AtomicInteger(0);
        return participantesDTO.stream().map(dto -> {
            // Buscar trabajador si se proporciona ID
            Trabajador trabajador = null;
            if (dto.getTrabajadorId() != null) {
                trabajador = trabajadorRepository.findById(dto.getTrabajadorId())
                        .orElse(null);
            }

            // Crear participante
            SsomaParticipante participante = SsomaParticipante.builder()
                    .ssomaFormulario(formulario)
                    .trabajador(trabajador)
                    .nombre(dto.getNombre())
                    .cargo(dto.getCargo())
                    .build();

            SsomaParticipante participanteGuardado = participanteRepository.save(participante);

            // Subir firma si existe
            if (dto.getFirma() != null) {
                subirFirmaParticipante(participanteGuardado, dto.getFirma());
            }

            // Subir fotos del participante
            if (fotosParticipantes != null) {
                fotosParticipantes.stream()
                        .filter(foto -> foto.getParticipanteIndex() != null &&
                                foto.getParticipanteIndex().equals(index.get()))
                        .forEach(foto -> subirFotoParticipante(participanteGuardado, foto.getFoto(), foto.getTipoFoto()));
            }

            index.incrementAndGet();
            return participanteGuardado;
        }).collect(Collectors.toList());
    }

    private void subirFirmaParticipante(SsomaParticipante participante, MultipartFile firmaFile) {
        try {
            Map uploadResult = cloudinaryService.upload(firmaFile, Map.of(
                    "folder", "ssoma/firmas",
                    "resource_type", "raw" // Para mantener calidad de firma
            ));

            String firmaUrl = (String) uploadResult.get("secure_url");

            SsomaParticipanteFirma firma = SsomaParticipanteFirma.builder()
                    .participante(participante)
                    .firmaUrl(firmaUrl)
                    .fechaFirma(LocalDateTime.now())
                    .build();

            participanteFirmaRepository.save(firma);

        } catch (IOException e) {
            log.error("Error al subir firma del participante: {}", participante.getIdParticipante(), e);
            throw new RuntimeException("Error al subir firma: " + e.getMessage());
        }
    }

    private void subirFotoParticipante(SsomaParticipante participante, MultipartFile fotoFile, String tipoFoto) {
        try {
            Map uploadResult = cloudinaryService.upload(fotoFile, Map.of(
                    "folder", "ssoma/fotos_participantes",
                    "transformation", new com.cloudinary.Transformation()
                            .width(400).height(400).crop("fill").gravity("face")
            ));

            String fotoUrl = (String) uploadResult.get("secure_url");

            // CORRECCIÓN: Usar SsomaParticipanteFoto directamente, no .Foto
            SsomaParticipanteFoto fotoEntity = SsomaParticipanteFoto.builder()
                    .participante(participante)
                    .fotoUrl(fotoUrl)
                    .tipoFoto(tipoFoto != null ?
                            SsomaParticipanteFoto.TipoFoto.valueOf(tipoFoto) :
                            SsomaParticipanteFoto.TipoFoto.FRONTAL)
                    .build();

            participanteFotoRepository.save(fotoEntity);

        } catch (IOException e) {
            log.error("Error al subir foto del participante: {}", participante.getIdParticipante(), e);
            throw new RuntimeException("Error al subir foto: " + e.getMessage());
        }
    }

    private void crearSecuenciasTarea(SsomaFormulario formulario, List<SecuenciaTareaDTO> secuenciasDTO) {
        if (secuenciasDTO == null) return;

        secuenciasDTO.forEach(dto -> {
            SsomaSecuenciaTarea secuencia = SsomaSecuenciaTarea.builder()
                    .ssomaFormulario(formulario)
                    .secuenciaTarea(dto.getSecuenciaTarea())
                    .peligro(dto.getPeligro())
                    .riesgo(dto.getRiesgo())
                    .consecuencias(dto.getConsecuencias())
                    .medidasControl(dto.getMedidasControl())
                    .orden(dto.getOrden())
                    .build();

            secuenciaTareaRepository.save(secuencia);
        });
    }

    private void crearChecklistSeguridad(SsomaFormulario formulario, List<ChecklistSeguridadDTO> checklistDTO) {
        if (checklistDTO == null) return;

        checklistDTO.forEach(dto -> {
            SsomaChecklistSeguridad checklist = SsomaChecklistSeguridad.builder()
                    .ssomaFormulario(formulario)
                    .itemNombre(dto.getItemNombre())
                    .usado(dto.getUsado())
                    .observaciones(dto.getObservaciones())
                    .build();

            SsomaChecklistSeguridad checklistGuardado = checklistSeguridadRepository.save(checklist);

            // Subir foto si existe
            if (dto.getFoto() != null) {
                subirFotoChecklist(checklistGuardado, dto.getFoto());
            }
        });
    }

    private void subirFotoChecklist(SsomaChecklistSeguridad checklist, MultipartFile fotoFile) {
        try {
            Map uploadResult = cloudinaryService.upload(fotoFile, Map.of(
                    "folder", "ssoma/checklist_seguridad"
            ));

            String fotoUrl = (String) uploadResult.get("secure_url");

            SsomaChecklistFoto foto = SsomaChecklistFoto.builder()
                    .checklistSeguridad(checklist)
                    .fotoUrl(fotoUrl)
                    .build();

            checklistFotoRepository.save(foto);

        } catch (IOException e) {
            log.error("Error al subir foto del checklist: {}", checklist.getIdChecklist(), e);
            throw new RuntimeException("Error al subir foto: " + e.getMessage());
        }
    }

    private void crearEppChecks(
            SsomaFormulario formulario,
            List<EppCheckDTO> eppChecksDTO,
            List<SsomaRequestDTO.FotoEpp> fotosEpp) {

        if (eppChecksDTO == null) return;

        AtomicInteger index = new AtomicInteger(0);
        eppChecksDTO.forEach(dto -> {
            SsomaEppCheck eppCheck = SsomaEppCheck.builder()
                    .ssomaFormulario(formulario)
                    .eppNombre(dto.getEppNombre())
                    .usado(dto.getUsado())
                    .build();

            SsomaEppCheck eppCheckGuardado = eppCheckRepository.save(eppCheck);

            // Subir foto del EPP si existe en dto
            if (dto.getFoto() != null) {
                subirFotoEpp(eppCheckGuardado, dto.getFoto());
            }

            // O subir foto de la lista de fotosEpp
            if (fotosEpp != null) {
                fotosEpp.stream()
                        .filter(foto -> foto.getEppIndex() != null &&
                                foto.getEppIndex().equals(index.get()))
                        .forEach(foto -> subirFotoEpp(eppCheckGuardado, foto.getFoto()));
            }

            index.incrementAndGet();
        });
    }

    private void subirFotoEpp(SsomaEppCheck eppCheck, MultipartFile fotoFile) {
        try {
            Map uploadResult = cloudinaryService.upload(fotoFile, Map.of(
                    "folder", "ssoma/epp"
            ));

            String fotoUrl = (String) uploadResult.get("secure_url");

            SsomaEppFoto foto = SsomaEppFoto.builder()
                    .eppCheck(eppCheck)
                    .fotoUrl(fotoUrl)
                    .build();

            eppFotoRepository.save(foto);

        } catch (IOException e) {
            log.error("Error al subir foto del EPP: {}", eppCheck.getIdEpp(), e);
            throw new RuntimeException("Error al subir foto: " + e.getMessage());
        }
    }

    private void crearCharla(
            SsomaFormulario formulario,
            CharlaDTO charlaDTO,
            SsomaRequestDTO.VideoCharla videoCharla) {

        // Obtener tema
        SsomaTemaCharla tema = null;
        if (charlaDTO.getTemaId() != null) {
            tema = temaCharlaRepository.findById(charlaDTO.getTemaId())
                    .orElse(null);
        }

        // Obtener capacitador
        Trabajador capacitador = null;
        if (charlaDTO.getCapacitadorId() != null) {
            capacitador = trabajadorRepository.findById(charlaDTO.getCapacitadorId())
                    .orElse(null);
        }

        // Crear charla
        SsomaCharla charla = SsomaCharla.builder()
                .ssomaFormulario(formulario)
                .tema(tema)
                .fechaCharla(charlaDTO.getFechaCharla() != null ?
                        charlaDTO.getFechaCharla() : LocalDateTime.now())
                .duracionHoras(charlaDTO.getDuracionHoras())
                .capacitador(capacitador)
                .build();

        SsomaCharla charlaGuardada = charlaRepository.save(charla);

        // Subir video si existe
        if (videoCharla != null && videoCharla.getVideo() != null) {
            subirVideoCharla(charlaGuardada, videoCharla.getVideo(), videoCharla.getDuracionSegundos());
        }
    }

    private void subirVideoCharla(SsomaCharla charla, MultipartFile videoFile, Integer duracionSegundos) {
        try {
            Map uploadResult = cloudinaryService.upload(videoFile, Map.of(
                    "folder", "ssoma/videos_charla",
                    "resource_type", "video",
                    "eager", List.of(Map.of("format", "mp4")), // Generar formato MP4
                    "eager_async", true // Procesamiento asíncrono para videos largos
            ));

            String videoUrl = (String) uploadResult.get("secure_url");

            SsomaCharlaVideo video = SsomaCharlaVideo.builder()
                    .charla(charla)
                    .videoUrl(videoUrl)
                    .duracionSegundos(duracionSegundos)
                    .fechaSubida(LocalDateTime.now())
                    .build();

            charlaVideoRepository.save(video);

        } catch (IOException e) {
            log.error("Error al subir video de charla: {}", charla.getIdCharla(), e);
            throw new RuntimeException("Error al subir video: " + e.getMessage());
        }
    }

    private void crearInspeccionesTrabajador(
            SsomaFormulario formulario,
            List<InspeccionTrabajadorDTO> inspeccionesDTO) {

        if (inspeccionesDTO == null) return;

        inspeccionesDTO.forEach(dto -> {
            // Obtener trabajador
            Trabajador trabajador = null;
            if (dto.getTrabajadorId() != null) {
                trabajador = trabajadorRepository.findById(dto.getTrabajadorId())
                        .orElse(null);
            }

            // Obtener responsable
            Trabajador responsable = null;
            if (dto.getResponsableId() != null) {
                responsable = trabajadorRepository.findById(dto.getResponsableId())
                        .orElse(null);
            }

            SsomaInspeccionTrabajador inspeccion = SsomaInspeccionTrabajador.builder()
                    .ssomaFormulario(formulario)
                    .tipoInspeccion(dto.getTipoInspeccion() != null ?
                            SsomaInspeccionTrabajador.TipoInspeccion.valueOf(dto.getTipoInspeccion()) :
                            SsomaInspeccionTrabajador.TipoInspeccion.PLANIFICADA)
                    .trabajador(trabajador)
                    .trabajadorNombre(dto.getTrabajadorNombre())
                    .casco(dto.getCasco())
                    .lentes(dto.getLentes())
                    .orejeras(dto.getOrejeras())
                    .tapones(dto.getTapones())
                    .guantes(dto.getGuantes())
                    .botas(dto.getBotas())
                    .arnes(dto.getArnes())
                    .chaleco(dto.getChaleco())
                    .mascarilla(dto.getMascarilla())
                    .gafas(dto.getGafas())
                    .otros(dto.getOtros())
                    .accionCorrectiva(dto.getAccionCorrectiva())
                    .seguimiento(dto.getSeguimiento())
                    .responsable(responsable)
                    .build();

            inspeccionTrabajadorRepository.save(inspeccion);
        });
    }

    private void crearHerramientasInspeccion(
            SsomaFormulario formulario,
            List<HerramientaInspeccionDTO> herramientasDTO,
            List<SsomaRequestDTO.FotoHerramienta> fotosHerramientas) {

        if (herramientasDTO == null) return;

        AtomicInteger index = new AtomicInteger(0);
        herramientasDTO.forEach(dto -> {
            // Obtener herramienta maestra
            SsomaHerramientaMaestra herramientaMaestra = null;
            if (dto.getHerramientaMaestraId() != null) {
                herramientaMaestra = herramientaMaestraRepository.findById(dto.getHerramientaMaestraId())
                        .orElse(null);
            }

            SsomaHerramientaInspeccion herramienta = SsomaHerramientaInspeccion.builder()
                    .ssomaFormulario(formulario)
                    .herramientaMaestra(herramientaMaestra)
                    .herramientaNombre(dto.getHerramientaNombre())
                    .p1(dto.getP1())
                    .p2(dto.getP2())
                    .p3(dto.getP3())
                    .p4(dto.getP4())
                    .p5(dto.getP5())
                    .p6(dto.getP6())
                    .p7(dto.getP7())
                    .p8(dto.getP8())
                    .observaciones(dto.getObservaciones())
                    .build();

            SsomaHerramientaInspeccion herramientaGuardada = herramientaInspeccionRepository.save(herramienta);

            // Subir foto de la herramienta si existe en dto
            if (dto.getFoto() != null) {
                subirFotoHerramienta(herramientaGuardada, dto.getFoto());
            }

            // O subir foto de la lista de fotosHerramientas
            if (fotosHerramientas != null) {
                fotosHerramientas.stream()
                        .filter(foto -> foto.getHerramientaIndex() != null &&
                                foto.getHerramientaIndex().equals(index.get()))
                        .forEach(foto -> subirFotoHerramienta(herramientaGuardada, foto.getFoto()));
            }

            index.incrementAndGet();
        });
    }

    private void subirFotoHerramienta(SsomaHerramientaInspeccion herramienta, MultipartFile fotoFile) {
        try {
            Map uploadResult = cloudinaryService.upload(fotoFile, Map.of(
                    "folder", "ssoma/herramientas"
            ));

            String fotoUrl = (String) uploadResult.get("secure_url");

            SsomaHerramientaFoto foto = SsomaHerramientaFoto.builder()
                    .herramientaInspeccion(herramienta)
                    .fotoUrl(fotoUrl)
                    .build();

            herramientaFotoRepository.save(foto);

        } catch (IOException e) {
            log.error("Error al subir foto de herramienta: {}", herramienta.getIdHerramienta(), e);
            throw new RuntimeException("Error al subir foto: " + e.getMessage());
        }
    }

    private void crearPetar(SsomaFormulario formulario, PetarDTO petarDTO) {
        SsomaPetar petar = SsomaPetar.builder()
                .ssomaFormulario(formulario)
                .energiaPeligrosa(petarDTO.getEnergiaPeligrosa())
                .trabajoAltura(petarDTO.getTrabajoAltura())
                .izaje(petarDTO.getIzaje())
                .excavacion(petarDTO.getExcavacion())
                .espaciosConfinados(petarDTO.getEspaciosConfinados())
                .trabajoCaliente(petarDTO.getTrabajoCaliente())
                .otros(petarDTO.getOtros())
                .otrosDescripcion(petarDTO.getOtrosDescripcion())
                .velocidadAire(petarDTO.getVelocidadAire())
                .contenidoOxigeno(petarDTO.getContenidoOxigeno())
                .horaInicioPetar(petarDTO.getHoraInicioPetar())
                .horaFinPetar(petarDTO.getHoraFinPetar())
                .build();

        SsomaPetar petarGuardado = petarRepository.save(petar);

        // Crear respuestas PETAR
        if (petarDTO.getRespuestas() != null) {
            crearPetarRespuestas(petarGuardado, petarDTO.getRespuestas());
        }

        // Crear equipos de protección
        if (petarDTO.getEquiposProteccion() != null) {
            crearEquiposProteccion(petarGuardado, petarDTO.getEquiposProteccion());
        }
    }

    private void crearPetarRespuestas(SsomaPetar petar, List<PetarRespuestaDTO> respuestasDTO) {
        respuestasDTO.forEach(dto -> {
            // Obtener pregunta maestra
            SsomaPetarPreguntaMaestra pregunta = null;
            if (dto.getPreguntaId() != null) {
                pregunta = petarPreguntaMaestraRepository.findById(dto.getPreguntaId())
                        .orElse(null);
            }

            SsomaPetarRespuesta respuesta = SsomaPetarRespuesta.builder()
                    .petar(petar)
                    .pregunta(pregunta)
                    .respuesta(dto.getRespuesta())
                    .observaciones(dto.getObservaciones())
                    .build();

            petarRespuestaRepository.save(respuesta);
        });
    }

    private void crearEquiposProteccion(SsomaPetar petar, List<EquipoProteccionDTO> equiposDTO) {
        equiposDTO.forEach(dto -> {
            SsomaEquipoProteccion equipo = SsomaEquipoProteccion.builder()
                    .petar(petar)
                    .equipoNombre(dto.getEquipoNombre())
                    .usado(dto.getUsado())
                    .build();

            equipoProteccionRepository.save(equipo);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public SsomaResponseDTO obtenerFormularioCompleto(Integer idSsoma) {
        log.info("Obteniendo formulario SSOMA completo con ID: {}", idSsoma);

        SsomaFormulario formulario = ssomaFormularioRepository.findById(idSsoma)
                .orElseThrow(() -> new RuntimeException("Formulario SSOMA no encontrado con ID: " + idSsoma));

        return convertirAResponseDTO(formulario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SsomaResponseDTO> obtenerFormulariosPorOt(Integer idOts) {
        log.info("Obteniendo formularios SSOMA para OT: {}", idOts);

        List<SsomaFormulario> formularios = ssomaFormularioRepository.findByOtsIdOts(idOts);

        return formularios.stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    private SsomaResponseDTO convertirAResponseDTO(SsomaFormulario formulario) {
        // Cargar todas las relaciones necesarias
        SsomaResponseDTO response = SsomaResponseDTO.builder()
                .idSsoma(formulario.getIdSsoma())
                .idOts(formulario.getOts().getIdOts())
                .empresaNombre(formulario.getEmpresa() != null ? formulario.getEmpresa().getNombre() : null)
                .trabajoNombre(formulario.getTrabajo() != null ? formulario.getTrabajo().getDescripcion() : null)
                .latitud(formulario.getLatitud())
                .longitud(formulario.getLongitud())
                .fecha(formulario.getFecha())
                .horaInicio(formulario.getHoraInicio())
                .horaFin(formulario.getHoraFin())
                .horaInicioTrabajo(formulario.getHoraInicioTrabajo())
                .horaFinTrabajo(formulario.getHoraFinTrabajo())
                .supervisorTrabajo(formulario.getSupervisorTrabajo())
                .supervisorSst(formulario.getSupervisorSst())
                .responsableArea(formulario.getResponsableArea())
                .build();

        // Obtener participantes
        List<SsomaParticipante> participantes = participanteRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        response.setParticipantes(convertirParticipantesAResponse(participantes));

        // Obtener secuencias de tarea
        List<SsomaSecuenciaTarea> secuencias = secuenciaTareaRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        response.setSecuenciasTarea(convertirSecuenciasAResponse(secuencias));

        // Obtener checklist seguridad
        List<SsomaChecklistSeguridad> checklist = checklistSeguridadRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        response.setChecklistSeguridad(convertirChecklistAResponse(checklist));

        // Obtener EPP checks
        List<SsomaEppCheck> eppChecks = eppCheckRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        response.setEppChecks(convertirEppChecksAResponse(eppChecks));

        // Obtener charla
        Optional<SsomaCharla> charlaOpt = charlaRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        charlaOpt.ifPresent(charla -> response.setCharla(convertirCharlaAResponse(charla)));

        // Obtener inspecciones trabajador
        List<SsomaInspeccionTrabajador> inspecciones = inspeccionTrabajadorRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        response.setInspeccionesTrabajador(convertirInspeccionesAResponse(inspecciones));

        // Obtener herramientas inspección
        List<SsomaHerramientaInspeccion> herramientas = herramientaInspeccionRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        response.setHerramientasInspeccion(convertirHerramientasAResponse(herramientas));

        // Obtener PETAR
        Optional<SsomaPetar> petarOpt = petarRepository.findBySsomaFormularioIdSsoma(formulario.getIdSsoma());
        petarOpt.ifPresent(petar -> response.setPetar(convertirPetarAResponse(petar)));

        return response;
    }

    private List<ParticipanteResponseDTO> convertirParticipantesAResponse(List<SsomaParticipante> participantes) {
        return participantes.stream().map(p -> {
            List<String> fotoUrls = participanteFotoRepository.findByParticipanteIdParticipante(p.getIdParticipante())
                    .stream()
                    .map(SsomaParticipanteFoto::getFotoUrl)
                    .collect(Collectors.toList());

            String firmaUrl = participanteFirmaRepository.findByParticipanteIdParticipante(p.getIdParticipante())
                    .map(SsomaParticipanteFirma::getFirmaUrl)
                    .orElse(null);

            return ParticipanteResponseDTO.builder()
                    .idParticipante(p.getIdParticipante())
                    .nombre(p.getNombre())
                    .cargo(p.getCargo())
                    .firmaUrl(firmaUrl)
                    .fotoUrls(fotoUrls)
                    .build();
        }).collect(Collectors.toList());
    }

    private List<SecuenciaTareaResponseDTO> convertirSecuenciasAResponse(List<SsomaSecuenciaTarea> secuencias) {
        return secuencias.stream().map(s -> SecuenciaTareaResponseDTO.builder()
                        .secuenciaTarea(s.getSecuenciaTarea())
                        .peligro(s.getPeligro())
                        .riesgo(s.getRiesgo())
                        .consecuencias(s.getConsecuencias())
                        .medidasControl(s.getMedidasControl())
                        .build())
                .collect(Collectors.toList());
    }

    private List<ChecklistSeguridadResponseDTO> convertirChecklistAResponse(List<SsomaChecklistSeguridad> checklist) {
        return checklist.stream().map(c -> {
            List<String> fotoUrls = checklistFotoRepository.findByChecklistSeguridadIdChecklist(c.getIdChecklist())
                    .stream()
                    .map(SsomaChecklistFoto::getFotoUrl)
                    .collect(Collectors.toList());

            return ChecklistSeguridadResponseDTO.builder()
                    .itemNombre(c.getItemNombre())
                    .usado(c.getUsado())
                    .observaciones(c.getObservaciones())
                    .fotoUrls(fotoUrls)
                    .build();
        }).collect(Collectors.toList());
    }

    private List<EppCheckResponseDTO> convertirEppChecksAResponse(List<SsomaEppCheck> eppChecks) {
        return eppChecks.stream().map(e -> {
            List<String> fotoUrls = eppFotoRepository.findByEppCheckIdEpp(e.getIdEpp())
                    .stream()
                    .map(SsomaEppFoto::getFotoUrl)
                    .collect(Collectors.toList());

            return EppCheckResponseDTO.builder()
                    .eppNombre(e.getEppNombre())
                    .usado(e.getUsado())
                    .fotoUrls(fotoUrls)
                    .build();
        }).collect(Collectors.toList());
    }

    private CharlaResponseDTO convertirCharlaAResponse(SsomaCharla charla) {
        String videoUrl = charlaVideoRepository.findByCharlaIdCharla(charla.getIdCharla())
                .map(SsomaCharlaVideo::getVideoUrl)
                .orElse(null);

        Integer duracionSegundos = charlaVideoRepository.findByCharlaIdCharla(charla.getIdCharla())
                .map(SsomaCharlaVideo::getDuracionSegundos)
                .orElse(null);

        return CharlaResponseDTO.builder()
                .temaNombre(charla.getTema() != null ? charla.getTema().getNombre() : null)
                .fechaCharla(charla.getFechaCharla())
                .duracionHoras(charla.getDuracionHoras())
                .capacitadorNombre(charla.getCapacitador() != null ?
                        charla.getCapacitador().getNombres() + " " + charla.getCapacitador().getApellidos() : null)
                .videoUrl(videoUrl)
                .duracionSegundos(duracionSegundos)
                .build();
    }

    private List<InspeccionTrabajadorResponseDTO> convertirInspeccionesAResponse(List<SsomaInspeccionTrabajador> inspecciones) {
        return inspecciones.stream().map(i -> InspeccionTrabajadorResponseDTO.builder()
                        .tipoInspeccion(i.getTipoInspeccion().name())
                        .trabajadorNombre(i.getTrabajadorNombre())
                        .casco(i.getCasco())
                        .lentes(i.getLentes())
                        .orejeras(i.getOrejeras())
                        .tapones(i.getTapones())
                        .guantes(i.getGuantes())
                        .botas(i.getBotas())
                        .arnes(i.getArnes())
                        .chaleco(i.getChaleco())
                        .mascarilla(i.getMascarilla())
                        .gafas(i.getGafas())
                        .otros(i.getOtros())
                        .accionCorrectiva(i.getAccionCorrectiva())
                        .seguimiento(i.getSeguimiento())
                        .responsableNombre(i.getResponsable() != null ?
                                i.getResponsable().getNombres() + " " + i.getResponsable().getApellidos() : null)
                        .build())
                .collect(Collectors.toList());
    }

    private List<HerramientaInspeccionResponseDTO> convertirHerramientasAResponse(List<SsomaHerramientaInspeccion> herramientas) {
        return herramientas.stream().map(h -> {
            List<String> fotoUrls = herramientaFotoRepository.findByHerramientaInspeccionIdHerramienta(h.getIdHerramienta())
                    .stream()
                    .map(SsomaHerramientaFoto::getFotoUrl)
                    .collect(Collectors.toList());

            return HerramientaInspeccionResponseDTO.builder()
                    .herramientaNombre(h.getHerramientaNombre())
                    .p1(h.getP1())
                    .p2(h.getP2())
                    .p3(h.getP3())
                    .p4(h.getP4())
                    .p5(h.getP5())
                    .p6(h.getP6())
                    .p7(h.getP7())
                    .p8(h.getP8())
                    .observaciones(h.getObservaciones())
                    .fotoUrls(fotoUrls)
                    .build();
        }).collect(Collectors.toList());
    }

    private PetarResponseDTO convertirPetarAResponse(SsomaPetar petar) {
        // Obtener respuestas
        List<SsomaPetarRespuesta> respuestas = petarRespuestaRepository.findByPetarIdPetar(petar.getIdPetar());
        List<PetarRespuestaResponseDTO> respuestasDTO = respuestas.stream().map(r -> {
            String preguntaTexto = r.getPregunta() != null ? r.getPregunta().getPregunta() : null;
            return PetarRespuestaResponseDTO.builder()
                    .pregunta(preguntaTexto)
                    .respuesta(r.getRespuesta())
                    .observaciones(r.getObservaciones())
                    .build();
        }).collect(Collectors.toList());

        // Obtener equipos de protección
        List<SsomaEquipoProteccion> equipos = equipoProteccionRepository.findByPetarIdPetar(petar.getIdPetar());
        List<EquipoProteccionResponseDTO> equiposDTO = equipos.stream()
                .map(e -> EquipoProteccionResponseDTO.builder()
                        .equipoNombre(e.getEquipoNombre())
                        .usado(e.getUsado())
                        .build())
                .collect(Collectors.toList());

        return PetarResponseDTO.builder()
                .energiaPeligrosa(petar.getEnergiaPeligrosa())
                .trabajoAltura(petar.getTrabajoAltura())
                .izaje(petar.getIzaje())
                .excavacion(petar.getExcavacion())
                .espaciosConfinados(petar.getEspaciosConfinados())
                .trabajoCaliente(petar.getTrabajoCaliente())
                .otros(petar.getOtros())
                .otrosDescripcion(petar.getOtrosDescripcion())
                .velocidadAire(petar.getVelocidadAire())
                .contenidoOxigeno(petar.getContenidoOxigeno())
                .horaInicioPetar(petar.getHoraInicioPetar())
                .horaFinPetar(petar.getHoraFinPetar())
                .respuestas(respuestasDTO)
                .equiposProteccion(equiposDTO)
                .build();
    }
}
