package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.MaestroServicioResponse;
import com.backend.comfutura.model.MaestroServicio;
import com.backend.comfutura.repository.MaestroServicioRepository;
import com.backend.comfutura.service.MaestroServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaestroServicioServiceImpl implements MaestroServicioService {

    private final MaestroServicioRepository repository;

    @Override
    public Page<MaestroServicioResponse> listar(int page) {
        PageRequest pageable = PageRequest.of(page, 20);

        return repository.findByActivoTrueOrderByCodigoAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public MaestroServicioResponse crear(MaestroServicioResponse request) {
        MaestroServicio servicio = MaestroServicio.builder()
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .idUnidadMedida(request.getIdUnidadMedida())
                .costoBase(request.getCostoBase())
                .activo(true)
                .build();

        return mapToResponse(repository.save(servicio));
    }

    @Override
    public MaestroServicioResponse editar(Integer id, MaestroServicioResponse request) {
        MaestroServicio servicio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Servicio no encontrado"));

        servicio.setCodigo(request.getCodigo());
        servicio.setDescripcion(request.getDescripcion());
        servicio.setIdUnidadMedida(request.getIdUnidadMedida());
        servicio.setCostoBase(request.getCostoBase());

        return mapToResponse(repository.save(servicio));
    }

    @Override
    public MaestroServicioResponse obtenerPorId(Integer id) {
        MaestroServicio servicio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Servicio no encontrado"));

        return mapToResponse(servicio);
    }

    @Override
    public void cambiarEstado(Integer id) {
        MaestroServicio servicio = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Servicio no encontrado"));

        servicio.setActivo(!servicio.getActivo());
        repository.save(servicio);
    }

    // ==========================
    // Mapper interno
    // ==========================
    private MaestroServicioResponse mapToResponse(MaestroServicio entity) {
        return MaestroServicioResponse.builder()
                .idMaestroServicio(entity.getIdMaestroServicio())
                .codigo(entity.getCodigo())
                .descripcion(entity.getDescripcion())
                .idUnidadMedida(entity.getIdUnidadMedida())
                .costoBase(entity.getCostoBase())
                .activo(entity.getActivo())
                .build();
    }
}