package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.request.CronogramaRequest;
import com.backend.comfutura.dto.response.CronogramaResponse;
import com.backend.comfutura.model.CronogramaOt;
import com.backend.comfutura.model.MaestroPartida;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.repository.CronogramaOtRepository;
import com.backend.comfutura.repository.MaestroPartidaRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.service.CronogramaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CronogramaServiceImpl implements CronogramaService {

    private final CronogramaOtRepository cronogramaOtRepository;
    private final OtsRepository otsRepository;
    private final MaestroPartidaRepository maestroPartidaRepository;

    // =========================
    // ➕ CREAR
    // =========================
    @Override
    public CronogramaResponse crear(CronogramaRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        MaestroPartida partida = maestroPartidaRepository
                .findById(request.getIdMaestroPartida())
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        CronogramaOt cronograma = CronogramaOt.builder()
                .ots(ots)
                .maestroPartida(partida)
                .duracionDias(request.getDuracionDias())
                .fechaInicio(request.getFechaInicio())
                .fechaFin(request.getFechaFin())
                .build();

        cronogramaOtRepository.save(cronograma);

        return mapToResponse(cronograma);
    }

    // =========================
    // ✏️ EDITAR
    // =========================
    @Override
    public CronogramaResponse editar(Integer idCronograma, CronogramaRequest request) {

        CronogramaOt cronograma = cronogramaOtRepository.findById(idCronograma)
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));

        MaestroPartida partida = maestroPartidaRepository
                .findById(request.getIdMaestroPartida())
                .orElseThrow(() -> new RuntimeException("Partida no encontrada"));

        cronograma.setMaestroPartida(partida);
        cronograma.setDuracionDias(request.getDuracionDias());
        cronograma.setFechaInicio(request.getFechaInicio());
        cronograma.setFechaFin(request.getFechaFin());

        cronogramaOtRepository.save(cronograma);

        return mapToResponse(cronograma);
    }

    // =========================
    // 📄 LISTAR POR OT (FIX 🔥)
    // =========================
    @Override
    public List<CronogramaResponse> listarPorOt(Integer idOts) {

        //  USAR EL MÉTODO CORRECTO DEL REPOSITORY
        return cronogramaOtRepository.listarCronogramaPorOt(idOts);
    }

    // =========================
    // 🔁 MAPPER
    // =========================
    private CronogramaResponse mapToResponse(CronogramaOt c) {
        return new CronogramaResponse(
                c.getIdCronograma(),
                c.getMaestroPartida().getDescripcion(),
                c.getDuracionDias(),
                c.getFechaInicio(),
                c.getFechaFin()
        );
    }
}


