package com.backend.comfutura.service;

import com.backend.comfutura.dto.request.TrabajadorRequestDTO;
import com.backend.comfutura.dto.response.TrabajadorResponseDTO;
import com.backend.comfutura.model.Trabajador;
import com.backend.comfutura.repository.TrabajadorRepository;
import com.backend.comfutura.repository.EmpresaRepository;
import com.backend.comfutura.repository.AreaRepository;
import com.backend.comfutura.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TrabajadorService {

    private final TrabajadorRepository trabajadorRepository;
    private final EmpresaRepository empresaRepository;
    private final AreaRepository areaRepository;
    private final CargoRepository cargoRepository;

    // ================= CREAR / EDITAR =================
    public TrabajadorResponseDTO saveOrUpdate(TrabajadorRequestDTO dto) {
        Trabajador trabajador;

        if (dto.getIdTrabajador() != null) {
            // Editar
            trabajador = trabajadorRepository.findById(dto.getIdTrabajador())
                    .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
        } else {
            // Crear
            trabajador = new Trabajador();
        }

        trabajador.setNombres(dto.getNombres());
        trabajador.setApellidos(dto.getApellidos());
        trabajador.setDni(dto.getDni());
        trabajador.setCelular(dto.getCelular());
        trabajador.setCorreoCorporativo(dto.getCorreoCorporativo());

        // Relaciones
        trabajador.setEmpresa(empresaRepository.findById(dto.getIdEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada")));
        trabajador.setArea(areaRepository.findById(dto.getIdArea())
                .orElseThrow(() -> new RuntimeException("Area no encontrada")));
        trabajador.setCargo(cargoRepository.findById(dto.getIdCargo())
                .orElseThrow(() -> new RuntimeException("Cargo no encontrado")));

        Trabajador saved = trabajadorRepository.save(trabajador);

        return mapToResponse(saved);
    }

    // ================= PAGINADO =================
    public Page<TrabajadorResponseDTO> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return trabajadorRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    // ================= TOGGLE ACTIVO =================
    public void toggleActivo(Integer id) {
        Trabajador trabajador = trabajadorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trabajador no encontrado"));
        trabajador.setActivo(!trabajador.getActivo());
        trabajadorRepository.save(trabajador);
    }

    // ================= MAPEO =================
    private TrabajadorResponseDTO mapToResponse(Trabajador t) {
        return TrabajadorResponseDTO.builder()
                .idTrabajador(t.getIdTrabajador())
                .nombres(t.getNombres())
                .apellidos(t.getApellidos())
                .dni(t.getDni())
                .celular(t.getCelular())
                .correoCorporativo(t.getCorreoCorporativo())
                .empresa(t.getEmpresa() != null ? t.getEmpresa().getNombre() : null)
                .area(t.getArea() != null ? t.getArea().getNombre() : null)
                .cargo(t.getCargo() != null ? t.getCargo().getNombre() : null)
                .activo(t.getActivo())
                .fechaCreacion(t.getFechaCreacion())
                .build();
    }
}