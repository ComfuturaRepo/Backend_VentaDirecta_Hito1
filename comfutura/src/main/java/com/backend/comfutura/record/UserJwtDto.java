package com.backend.comfutura.record;

import java.util.List;

public record UserJwtDto(
        Integer idUsuario,
        Integer idTrabajador,
        String username,
        Boolean activo,
        List<String> nivel
) {}
