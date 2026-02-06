package com.backend.comfutura.record;

import java.util.List;

public record UserJwtDto(
        Integer idUsuario,
        Integer idTrabajador,
        String username,
        String empresa,
        String cargo,
        String area,
        String nombreCompleto,
        Boolean activo,
        List<String> nivel
) {}
