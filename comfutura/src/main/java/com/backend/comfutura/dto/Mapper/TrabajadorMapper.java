package com.backend.comfutura.dto.Mapper;

import com.backend.comfutura.dto.*;
import com.backend.comfutura.dto.Page.MessageResponseDTO;
import com.backend.comfutura.dto.TrabajadorRequestDTO;
import com.backend.comfutura.dto.request.TrabajadorUpdateDTO;
import com.backend.comfutura.dto.response.TrabajadorDetailDTO;
import com.backend.comfutura.dto.response.TrabajadorSimpleDTO;
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
            dto.setCargoId(trabajador.getCargo().getId());
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

        return trabajador;
    }

    // UpdateDTO → Entity (actualización)
    public void updateEntity(TrabajadorUpdateDTO dto, Trabajador trabajador) {
        if (dto == null || trabajador == null) return;

        trabajador.setNombres(dto.getNombres());
        trabajador.setApellidos(dto.getApellidos());
        trabajador.setDni(dto.getDni());
        trabajador.setCelular(dto.getCelular());
        trabajador.setCorreoCorporativo(dto.getCorreoCorporativo());
    }

    // Helper para mensajes
    public MessageResponseDTO toMessageResponse(String message) {
        return new MessageResponseDTO(
                message,
                LocalDateTime.now().format(formatter)
        );
    }
}