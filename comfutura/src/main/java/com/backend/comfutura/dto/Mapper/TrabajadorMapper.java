package com.backend.comfutura.dto.Mapper;

import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.trabajadorDTO.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.trabajadorDTO.TrabajadorSimpleDTO;
import com.backend.comfutura.model.Trabajador;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TrabajadorMapper {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Entity → SimpleDTO
    public TrabajadorSimpleDTO toSimpleDTO(Trabajador trabajador) {
        if (trabajador == null) return null;

        TrabajadorSimpleDTO dto = new TrabajadorSimpleDTO();
        dto.setIdTrabajador(trabajador.getIdTrabajador());
        dto.setNombres(trabajador.getNombres());
        dto.setApellidos(trabajador.getApellidos());
        dto.setDni(trabajador.getDni());
        dto.setCelular(trabajador.getCelular());
        dto.setCorreoCorporativo(trabajador.getCorreoCorporativo());
        dto.setActivo(trabajador.getActivo());
        dto.setFechaCreacion(trabajador.getFechaCreacion());

        // Nuevos campos
        dto.setPuedeSerLiquidador(trabajador.getPuedeSerLiquidador());
        dto.setPuedeSerEjecutante(trabajador.getPuedeSerEjecutante());
        dto.setPuedeSerAnalistaContable(trabajador.getPuedeSerAnalistaContable());
        dto.setPuedeSerJefaturaResponsable(trabajador.getPuedeSerJefaturaResponsable());
        dto.setPuedeSerCoordinadorTiCw(trabajador.getPuedeSerCoordinadorTiCw());

        // Empresa
        if (trabajador.getEmpresa() != null) {
            dto.setEmpresaNombre(trabajador.getEmpresa().getNombre());
        }

        // Área
        if (trabajador.getArea() != null) {
            dto.setAreaNombre(trabajador.getArea().getNombre());
        }

        // Cargo
        if (trabajador.getCargo() != null) {
            dto.setCargoNombre(trabajador.getCargo().getNombre());
        }

        return dto;
    }

    // Entity → DetailDTO
    public TrabajadorDetailDTO toDetailDTO(Trabajador trabajador) {
        if (trabajador == null) return null;

        TrabajadorDetailDTO dto = new TrabajadorDetailDTO();
        dto.setIdTrabajador(trabajador.getIdTrabajador());
        dto.setNombres(trabajador.getNombres());
        dto.setApellidos(trabajador.getApellidos());
        dto.setDni(trabajador.getDni());
        dto.setCelular(trabajador.getCelular());
        dto.setCorreoCorporativo(trabajador.getCorreoCorporativo());
        dto.setActivo(trabajador.getActivo());
        dto.setFechaCreacion(trabajador.getFechaCreacion());

        // Nuevos campos
        dto.setPuedeSerLiquidador(trabajador.getPuedeSerLiquidador());
        dto.setPuedeSerEjecutante(trabajador.getPuedeSerEjecutante());
        dto.setPuedeSerAnalistaContable(trabajador.getPuedeSerAnalistaContable());
        dto.setPuedeSerJefaturaResponsable(trabajador.getPuedeSerJefaturaResponsable());
        dto.setPuedeSerCoordinadorTiCw(trabajador.getPuedeSerCoordinadorTiCw());

        // Empresa
        if (trabajador.getEmpresa() != null) {
            dto.setEmpresaId(trabajador.getEmpresa().getId());
            dto.setEmpresaNombre(trabajador.getEmpresa().getNombre());
        }

        // Área
        if (trabajador.getArea() != null) {
            dto.setAreaId(trabajador.getArea().getIdArea());
            dto.setAreaNombre(trabajador.getArea().getNombre());
        }

        // Cargo
        if (trabajador.getCargo() != null) {
            dto.setCargoId(trabajador.getCargo().getIdCargo());
            dto.setCargoNombre(trabajador.getCargo().getNombre());
            dto.setCargoNivel(trabajador.getCargo().getNivel().getCodigo());
        }

        return dto;
    }

    // RequestDTO → Entity
    public Trabajador toEntity(TrabajadorRequestDTO dto) {
        if (dto == null) return null;

        Trabajador trabajador = new Trabajador();
        trabajador.setNombres(dto.getNombres());
        trabajador.setApellidos(dto.getApellidos());
        trabajador.setDni(dto.getDni());
        trabajador.setCelular(dto.getCelular());
        trabajador.setCorreoCorporativo(dto.getCorreoCorporativo());
        trabajador.setActivo(dto.getActivo() != null ? dto.getActivo() : true);

        // Nuevos campos - con manejo de null para usar valores por defecto
        trabajador.setPuedeSerLiquidador(dto.getPuedeSerLiquidador() != null ? dto.getPuedeSerLiquidador() : false);
        trabajador.setPuedeSerEjecutante(dto.getPuedeSerEjecutante() != null ? dto.getPuedeSerEjecutante() : false);
        trabajador.setPuedeSerAnalistaContable(dto.getPuedeSerAnalistaContable() != null ? dto.getPuedeSerAnalistaContable() : false);
        trabajador.setPuedeSerJefaturaResponsable(dto.getPuedeSerJefaturaResponsable() != null ? dto.getPuedeSerJefaturaResponsable() : false);
        trabajador.setPuedeSerCoordinadorTiCw(dto.getPuedeSerCoordinadorTiCw() != null ? dto.getPuedeSerCoordinadorTiCw() : false);

        return trabajador;
    }

    // UpdateDTO → Entity (actualización)
    public void updateEntity(TrabajadorUpdateDTO dto, Trabajador trabajador) {
        if (dto == null || trabajador == null) return;

        if (dto.getNombres() != null) {
            trabajador.setNombres(dto.getNombres());
        }

        if (dto.getApellidos() != null) {
            trabajador.setApellidos(dto.getApellidos());
        }

        if (dto.getDni() != null) {
            trabajador.setDni(dto.getDni());
        }

        if (dto.getCelular() != null) {
            trabajador.setCelular(dto.getCelular());
        }

        if (dto.getCorreoCorporativo() != null) {
            trabajador.setCorreoCorporativo(dto.getCorreoCorporativo());
        }

        // Actualizar nuevos campos solo si se envían en el DTO
        if (dto.getPuedeSerLiquidador() != null) {
            trabajador.setPuedeSerLiquidador(dto.getPuedeSerLiquidador());
        }

        if (dto.getPuedeSerEjecutante() != null) {
            trabajador.setPuedeSerEjecutante(dto.getPuedeSerEjecutante());
        }

        if (dto.getPuedeSerAnalistaContable() != null) {
            trabajador.setPuedeSerAnalistaContable(dto.getPuedeSerAnalistaContable());
        }

        if (dto.getPuedeSerJefaturaResponsable() != null) {
            trabajador.setPuedeSerJefaturaResponsable(dto.getPuedeSerJefaturaResponsable());
        }

        if (dto.getPuedeSerCoordinadorTiCw() != null) {
            trabajador.setPuedeSerCoordinadorTiCw(dto.getPuedeSerCoordinadorTiCw());
        }
    }

    // Helper para mensajes
    public MessageResponseDTO toMessageResponse(String message) {
        return new MessageResponseDTO(
                message,
                LocalDateTime.now().format(formatter)
        );
    }
}