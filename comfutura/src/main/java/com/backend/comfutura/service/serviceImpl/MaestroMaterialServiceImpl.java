package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.dto.response.MaestroMaterialResponse;
import com.backend.comfutura.model.MaestroMaterial;
import com.backend.comfutura.repository.MaestroMaterialRepository;
import com.backend.comfutura.service.MaestroMaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaestroMaterialServiceImpl implements MaestroMaterialService {

    private final MaestroMaterialRepository repository;

    @Override
    public Page<MaestroMaterialResponse> listar(int page) {
        PageRequest pageable = PageRequest.of(page, 20);

        return repository.findByActivoTrueOrderByCodigoAsc(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public MaestroMaterialResponse crear(MaestroMaterialResponse request) {
        MaestroMaterial material = MaestroMaterial.builder()
                .codigo(request.getCodigo())
                .descripcion(request.getDescripcion())
                .idUnidadMedida(request.getIdUnidadMedida())
                .costoBase(request.getCostoBase())
                .activo(true)
                .build();

        return mapToResponse(repository.save(material));
    }

    @Override
    public MaestroMaterialResponse editar(Integer id, MaestroMaterialResponse request) {
        MaestroMaterial material = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Material no encontrado"));

        material.setCodigo(request.getCodigo());
        material.setDescripcion(request.getDescripcion());
        material.setIdUnidadMedida(request.getIdUnidadMedida());
        material.setCostoBase(request.getCostoBase());

        return mapToResponse(repository.save(material));
    }

    @Override
    public MaestroMaterialResponse obtenerPorId(Integer id) {
        MaestroMaterial material = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Material no encontrado"));

        return mapToResponse(material);
    }

    @Override
    public void cambiarEstado(Integer id) {
        MaestroMaterial material = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maestro Material no encontrado"));

        material.setActivo(!material.getActivo());
        repository.save(material);
    }

    // ==========================
    // Mapper interno
    // ==========================
    private MaestroMaterialResponse mapToResponse(MaestroMaterial entity) {
        return MaestroMaterialResponse.builder()
                .idMaestroMaterial(entity.getIdMaestroMaterial())
                .codigo(entity.getCodigo())
                .descripcion(entity.getDescripcion())
                .idUnidadMedida(entity.getIdUnidadMedida())
                .costoBase(entity.getCostoBase())
                .activo(entity.getActivo())
                .build();
    }
}