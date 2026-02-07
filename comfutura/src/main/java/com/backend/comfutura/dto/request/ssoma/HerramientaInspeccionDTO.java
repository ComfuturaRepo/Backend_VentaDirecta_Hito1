package com.backend.comfutura.dto.request.ssoma;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HerramientaInspeccionDTO {
    private Integer herramientaMaestraId;
    private String herramientaNombre;
    private Boolean p1;
    private Boolean p2;
    private Boolean p3;
    private Boolean p4;
    private Boolean p5;
    private Boolean p6;
    private Boolean p7;
    private Boolean p8;
    private String observaciones;
    private MultipartFile foto;
}