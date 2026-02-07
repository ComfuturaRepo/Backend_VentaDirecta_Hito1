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
public class ParticipanteDTO {
    private Integer trabajadorId;
    private String nombre;
    private String cargo;
    private MultipartFile firma;
}