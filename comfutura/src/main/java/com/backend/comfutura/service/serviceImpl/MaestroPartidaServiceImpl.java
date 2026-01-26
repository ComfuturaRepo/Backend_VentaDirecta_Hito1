package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.MaestroPartidaResponse;
import com.backend.comfutura.model.MaestroPartida;
import com.backend.comfutura.repository.MaestroPartidaRepository;
import com.backend.comfutura.service.MaestroPartidaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaestroPartidaServiceImpl implements MaestroPartidaService {

    private final MaestroPartidaRepository repository;

    @Override
    public Page<MaestroPartidaResponse> listar(int page) {
        PageRequest pageable = PageRequest.of(page, 20);

        return repository.findByActivoTrueOrderByCodigoAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public MaestroPartidaResponse crear(MaestroPartidaResponse request) {
        MaestroPartida partida = MaestroPartida.builder()
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .activo(true)
                .build();

        return mapToResponse(repository.save(partida));
    }

    @Override
    public MaestroPartidaResponse editar(Integer id, MaestroPartidaResponse request) {
        MaestroPartida partida = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Partida no encontrada"));

        partida.setCodigo(request.getCodigo());
        partida.setDescripcion(request.getDescripcion());

        return mapToResponse(repository.save(partida));
    }

    @Override
    public MaestroPartidaResponse obtenerPorId(Integer id) {
        MaestroPartida partida = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Partida no encontrada"));

        return mapToResponse(partida);
    }

    @Override
    public void cambiarEstado(Integer id) {
        MaestroPartida partida = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Partida no encontrada"));

        partida.setActivo(!partida.getActivo());
        repository.save(partida);
    }

    // ==========================
    // Mapper interno
    // ==========================
    private MaestroPartidaResponse mapToResponse(MaestroPartida entity) {
        return MaestroPartidaResponse.builder()
                .idMaestroPartida(entity.getIdMaestroPartida())
                .codigo(entity.getCodigo())
                .descripcion(entity.getDescripcion())
                .activo(entity.getActivo())
                .build();
    }
}