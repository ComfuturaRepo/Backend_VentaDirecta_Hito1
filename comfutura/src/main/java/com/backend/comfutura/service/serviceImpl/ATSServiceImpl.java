package com.backend.comfutura.service.impl;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.SSOMA.*;
import com.backend.comfutura.dto.response.SSOMA.*;
import com.backend.comfutura.model.ssoma.*;
import com.backend.comfutura.repository.*;
import com.backend.comfutura.service.ATSService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ATSServiceImpl implements ATSService {


    private final ATSRepository atsRepository;
    private final TrabajoRepository trabajoRepository;
    private final TrabajadorRepository trabajadorRepository;
    private final TareaRepository tareaRepository;
    private final PeligroRepository peligroRepository;
    private final RiesgoRepository riesgoRepository;
    private final MedidaControlRepository medidaControlRepository;
    private final EPPRepository eppRepository;
    private final TipoRiesgoTrabajoRepository tipoRiesgoTrabajoRepository;
    private final RolTrabajoRepository rolTrabajoRepository;
    private final CapacitacionRepository capacitacionRepository;
    private final InspeccionEPPRepository inspeccionEPPRepository;
    private final InspeccionHerramientaRepository inspeccionHerramientaRepository;
    private final PETARRepository petarRepository;
    @Override
    public PageResponseDTO<ATSListDTO> findAllPaginado(Pageable pageable, String search) {
        Page<ATS> atsPage;

        if (search != null && !search.trim().isEmpty()) {
            atsPage = atsRepository.search("%" + search + "%", pageable);
        } else {
            atsPage = atsRepository.findAllOrderByFechaDesc(pageable);
        }

        // Convertir Page de Spring a tu PageResponseDTO
        PageResponseDTO<ATSListDTO> response = new PageResponseDTO<>();
        response.setContent(atsPage.getContent().stream()
                .map(this::convertToListDTO)
                .collect(Collectors.toList()));
        response.setCurrentPage(atsPage.getNumber());
        response.setTotalItems(atsPage.getTotalElements());
        response.setTotalPages(atsPage.getTotalPages());
        response.setFirst(atsPage.isFirst());
        response.setLast(atsPage.isLast());
        response.setPageSize(atsPage.getSize());

        return response;
    }

    @Override
    public ATSResponseDTO findById(Integer id) {
        ATS ats = atsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ATS no encontrado con id: " + id));

        return convertToResponseDTO(ats);
    }

    @Override
    @Transactional
    public ATSResponseDTO create(ATSRequestDTO requestDTO) {
        // Validar trabajo
        Trabajo trabajo = trabajoRepository.findById(requestDTO.getIdTrabajo())
                .orElseThrow(() -> new RuntimeException("Trabajo no encontrado"));

        // Crear ATS principal
        ATS ats = new ATS();
        ats.setFecha(requestDTO.getFecha());
        ats.setHora(requestDTO.getHora());
        ats.setEmpresa(requestDTO.getEmpresa());
        ats.setLugarTrabajo(requestDTO.getLugarTrabajo());
        ats.setTrabajo(trabajo);

        ATS savedATS = atsRepository.save(ats);

        // Guardar participantes
        if (requestDTO.getParticipantes() != null && !requestDTO.getParticipantes().isEmpty()) {
            List<ATSParticipante> participantes = requestDTO.getParticipantes().stream()
                    .map(p -> {
                        ATSParticipante participante = new ATSParticipante();
                        participante.setAts(savedATS);
                        participante.setTrabajador(trabajadorRepository.findById(p.getIdTrabajador())
                                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado")));
                        participante.setRol(rolTrabajoRepository.findById(p.getIdRol())
                                .orElseThrow(() -> new RuntimeException("Rol no encontrado")));
                        return participante;
                    })
                    .collect(Collectors.toList());

            savedATS.setParticipantes(participantes);
        }

        // Guardar EPPs
        if (requestDTO.getEppIds() != null && !requestDTO.getEppIds().isEmpty()) {
            List<ATSEPP> atsEpps = requestDTO.getEppIds().stream()
                    .map(eppId -> {
                        ATSEPP atsEPP = new ATSEPP();
                        atsEPP.setAts(savedATS);
                        atsEPP.setEpp(eppRepository.findById(eppId)
                                .orElseThrow(() -> new RuntimeException("EPP no encontrado")));
                        return atsEPP;
                    })
                    .collect(Collectors.toList());

            savedATS.setEpps(atsEpps);
        }

        // Guardar tipos de riesgo
        if (requestDTO.getTipoRiesgoIds() != null && !requestDTO.getTipoRiesgoIds().isEmpty()) {
            List<ATSTipoRiesgo> tiposRiesgo = requestDTO.getTipoRiesgoIds().stream()
                    .map(tipoId -> {
                        ATSTipoRiesgo tipoRiesgo = new ATSTipoRiesgo();
                        tipoRiesgo.setAts(savedATS);
                        tipoRiesgo.setTipoRiesgo(tipoRiesgoTrabajoRepository.findById(tipoId)
                                .orElseThrow(() -> new RuntimeException("Tipo de riesgo no encontrado")));
                        return tipoRiesgo;
                    })
                    .collect(Collectors.toList());

            savedATS.setTiposRiesgo(tiposRiesgo);
        }

        // Guardar todo en cascada
        ATS finalATS = atsRepository.save(savedATS);
        return convertToResponseDTO(finalATS);
    }

    @Override
    @Transactional
    public SSTFormResponseDTO crearFormularioCompleto(SSTFormRequestDTO requestDTO) {
        // Generar números de registro secuenciales
        String fechaStr = LocalDate.now().toString().replace("-", "");
        Long secuencia = atsRepository.count() + 1;
        String numeroBase = "SST-" + fechaStr + "-" + String.format("%04d", secuencia);

        // 1. Crear ATS
        ATS ats = crearATS(requestDTO, numeroBase + "-ATS");

        // 2. Crear Capacitación
        Capacitacion capacitacion = crearCapacitacion(requestDTO, numeroBase + "-CAP");

        // 3. Crear Inspección EPP
        InspeccionEPP inspeccionEPP = crearInspeccionEPP(requestDTO, numeroBase + "-EPP");

        // 4. Crear Inspección Herramientas
        InspeccionHerramienta inspeccionHerramienta = crearInspeccionHerramienta(requestDTO, numeroBase + "-HERR");

        // 5. Crear PETAR
        PETAR petar = crearPETAR(requestDTO, numeroBase + "-PETAR");

        // Construir respuesta
        return new SSTFormResponseDTO(
                ats.getIdATS(),
                capacitacion.getIdCapacitacion(),
                inspeccionEPP.getId(),
                inspeccionHerramienta.getId(),
                petar.getId(),
                numeroBase + "-ATS",
                numeroBase + "-CAP",
                numeroBase + "-EPP",
                numeroBase + "-HERR",
                numeroBase + "-PETAR",
                requestDTO.getFecha(),
                requestDTO.getHora(),
                requestDTO.getEmpresa(),
                requestDTO.getLugarTrabajo(),
                "Formulario SST creado exitosamente"
        );
    }

    // Métodos auxiliares privados para crear cada entidad (mantén los mismos que tenías)
    private ATS crearATS(SSTFormRequestDTO requestDTO, String numeroRegistro) {
        ATSRequestDTO atsRequest = requestDTO.getAts();

        ATS ats = new ATS();
        ats.setFecha(requestDTO.getFecha());
        ats.setHora(requestDTO.getHora());
        ats.setEmpresa(requestDTO.getEmpresa());
        ats.setLugarTrabajo(requestDTO.getLugarTrabajo());

        // Trabajo
        ats.setTrabajo(trabajoRepository.findById(atsRequest.getIdTrabajo())
                .orElseThrow(() -> new RuntimeException("Trabajo no encontrado")));

        ATS savedATS = atsRepository.save(ats);

        // Implementar lógica para participantes, EPPs, tipos de riesgo
        // (similar a create() pero para SSTForm)

        return atsRepository.save(savedATS);
    }

    private Capacitacion crearCapacitacion(SSTFormRequestDTO requestDTO, String numeroRegistro) {
        CapacitacionRequestDTO capRequest = requestDTO.getCapacitacion();

        Capacitacion capacitacion = new Capacitacion();
        capacitacion.setNumeroRegistro(numeroRegistro);
        capacitacion.setTema(capRequest.getTema());
        capacitacion.setFecha(capRequest.getFecha() != null ? capRequest.getFecha() : requestDTO.getFecha());
        capacitacion.setHora(capRequest.getHora() != null ? capRequest.getHora() : requestDTO.getHora());

        // Capacitador
        capacitacion.setCapacitador(trabajadorRepository.findById(capRequest.getIdCapacitador())
                .orElseThrow(() -> new RuntimeException("Capacitador no encontrado")));

        return capacitacionRepository.save(capacitacion);
    }

    private InspeccionEPP crearInspeccionEPP(SSTFormRequestDTO requestDTO, String numeroRegistro) {
        InspeccionEPPRequestDTO eppRequest = requestDTO.getInspeccionEPP();

        InspeccionEPP inspeccion = new InspeccionEPP();
        inspeccion.setNumeroRegistro(numeroRegistro);
        inspeccion.setFecha(requestDTO.getFecha());
        inspeccion.setInspector(trabajadorRepository.findById(eppRequest.getIdInspector())
                .orElseThrow(() -> new RuntimeException("Inspector no encontrado")));

        return inspeccionEPPRepository.save(inspeccion);
    }

    private InspeccionHerramienta crearInspeccionHerramienta(SSTFormRequestDTO requestDTO, String numeroRegistro) {
        InspeccionHerramientaRequestDTO herrRequest = requestDTO.getInspeccionHerramienta();

        InspeccionHerramienta inspeccion = new InspeccionHerramienta();
        inspeccion.setNumeroRegistro(numeroRegistro);
        inspeccion.setFecha(requestDTO.getFecha());
        inspeccion.setSupervisor(trabajadorRepository.findById(herrRequest.getIdSupervisor())
                .orElseThrow(() -> new RuntimeException("Supervisor no encontrado")));

        return inspeccionHerramientaRepository.save(inspeccion);
    }

    private PETAR crearPETAR(SSTFormRequestDTO requestDTO, String numeroRegistro) {
        PETARRequestDTO petarRequest = requestDTO.getPetar();

        PETAR petar = new PETAR();
        petar.setNumeroRegistro(numeroRegistro);
        petar.setFecha(requestDTO.getFecha());

        return petarRepository.save(petar);
    }

    // Métodos de conversión
    private ATSListDTO convertToListDTO(ATS ats) {
        ATSListDTO dto = new ATSListDTO();
        dto.setIdATS(ats.getIdATS());
        dto.setFecha(ats.getFecha());
        dto.setHora(ats.getHora());
        dto.setEmpresa(ats.getEmpresa());
        dto.setLugarTrabajo(ats.getLugarTrabajo());
        dto.setTrabajo(ats.getTrabajo() != null ? ats.getTrabajo().getNombre() : null);
        dto.setCantidadParticipantes(ats.getParticipantes() != null ? ats.getParticipantes().size() : 0);
        return dto;
    }

    private ATSResponseDTO convertToResponseDTO(ATS ats) {
        ATSResponseDTO dto = new ATSResponseDTO();
        dto.setIdATS(ats.getIdATS());
        dto.setFecha(ats.getFecha());
        dto.setHora(ats.getHora());
        dto.setEmpresa(ats.getEmpresa());
        dto.setLugarTrabajo(ats.getLugarTrabajo());

        if (ats.getTrabajo() != null) {
            dto.setTrabajo(new TrabajoDTO(
                    ats.getTrabajo().getIdTrabajo(),
                    ats.getTrabajo().getNombre()
            ));
        }

        if (ats.getParticipantes() != null) {
            List<ParticipanteDTO> participantes = ats.getParticipantes().stream()
                    .map(p -> new ParticipanteDTO(
                            p.getTrabajador().getIdTrabajador(),
                            p.getTrabajador().getNombres(),
                            p.getTrabajador().getApellidos(),
                            p.getTrabajador().getCargo() != null ? p.getTrabajador().getCargo().getNombre() : null,
                            p.getRol() != null ? p.getRol().getNombre() : null
                    ))
                    .collect(Collectors.toList());
            dto.setParticipantes(participantes);
        }

        if (ats.getEpps() != null) {
            List<EPPDTO> epps = ats.getEpps().stream()
                    .map(e -> new EPPDTO(
                            e.getEpp().getIdEPP(),
                            e.getEpp().getNombre()
                    ))
                    .collect(Collectors.toList());
            dto.setEpps(epps);
        }

        if (ats.getTiposRiesgo() != null) {
            List<TipoRiesgoDTO> tiposRiesgo = ats.getTiposRiesgo().stream()
                    .map(t -> new TipoRiesgoDTO(
                            t.getTipoRiesgo().getId(),
                            t.getTipoRiesgo().getNombre()
                    ))
                    .collect(Collectors.toList());
            dto.setTiposRiesgo(tiposRiesgo);
        }

        // Riesgos (si implementaste esa lógica)
        dto.setRiesgos(new ArrayList<>());

        return dto;
    }
}