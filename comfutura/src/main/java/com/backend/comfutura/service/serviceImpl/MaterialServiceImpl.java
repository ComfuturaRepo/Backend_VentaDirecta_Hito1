package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.repository.MaterialOtRepository;
import com.backend.comfutura.dto.request.MaterialRequest;
import com.backend.comfutura.dto.response.MaterialResponse;
import com.backend.comfutura.model.MaestroMaterial;
import com.backend.comfutura.model.MaterialOt;
import com.backend.comfutura.model.Ots;
import com.backend.comfutura.repository.MaestroMaterialRepository;
import com.backend.comfutura.repository.OtsRepository;
import com.backend.comfutura.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.List;


@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialOtRepository materialRepository;
    private final OtsRepository otsRepository;
    private final MaestroMaterialRepository maestroMaterialRepository;

    // ➕ CREAR
    @Override
    public MaterialResponse crear(MaterialRequest request) {

        Ots ots = otsRepository.findById(request.getIdOts())
                .orElseThrow(() -> new RuntimeException("OT no encontrada"));

        MaestroMaterial maestro = maestroMaterialRepository
                .findById(request.getIdMaestroMaterial())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        // 👉 total = cantidad * costo_unitario
        BigDecimal total =
                request.getCantidad().multiply(request.getCostoUnitario());

        MaterialOt material = MaterialOt.builder()
                .ots(ots)
                .maestroMaterial(maestro)
                .idUnidadMedida(maestro.getIdUnidadMedida())
                .cantidad(request.getCantidad())
                .costoUnitario(request.getCostoUnitario())
                .total(total)
                .moneda("PEN")
                .build();

        materialRepository.save(material);

        return mapToResponse(material);
    }

    // ✏️ EDITAR
    @Override
    public MaterialResponse editar(Integer idMaterialOt, MaterialRequest request) {

        MaterialOt material = materialRepository.findById(idMaterialOt)
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        MaestroMaterial maestro = maestroMaterialRepository
                .findById(request.getIdMaestroMaterial())
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        BigDecimal total =
                request.getCantidad().multiply(request.getCostoUnitario());

        material.setMaestroMaterial(maestro);
        material.setCantidad(request.getCantidad());
        material.setCostoUnitario(request.getCostoUnitario());
        material.setTotal(total);

        materialRepository.save(material);

        return mapToResponse(material);
    }

    // 📄 LISTAR POR OT
    @Override
    public List<MaterialResponse> listarPorOt(Integer idOts) {

        return materialRepository.findByOts_IdOts(idOts)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔁 MAPPER (DTO ≠ ENTITY)
    private MaterialResponse mapToResponse(MaterialOt m) {
        return MaterialResponse.builder()
                .idMaterialOt(m.getIdMaterialOt())
                .material(m.getMaestroMaterial().getDescripcion())
                .unidadMedida(
                        String.valueOf(m.getIdUnidadMedida())
                )
                .cantidad(m.getCantidad())
                // 👇 Alias SOLO PARA EL FRONT
                .precioUnitario(m.getCostoUnitario())
                .subtotal(m.getTotal())
                .build();
    }
}

