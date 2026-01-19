package com.backend.comfutura.record;

//public record UserJwtDto(
//        Integer id,
//        String username,
//        String email,           // correoCorporativo
//        String nombres,
//        String apellidos,
//        String dni,
//        String cargo,
//        String area,
//        String empresa
//) {
//}


public record UserJwtDto(
        Integer id,
        String username,
        Boolean activo
) {
}