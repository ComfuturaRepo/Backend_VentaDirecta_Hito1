package com.backend.comfutura.controller;
import com.backend.comfutura.dto.request.ssoma.SsomaRequestDTO;
import com.backend.comfutura.dto.response.ssomaDTO.SsomaResponseDTO;
import com.backend.comfutura.service.SsomaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ssoma")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SsomaController {

    private final SsomaService ssomaService;

    /**
     * POST /api/ssoma/crear
     * Crea todo el formulario SSOMA completo (las 5 hojas)
     */
    @PostMapping("/crear")
    public ResponseEntity<SsomaResponseDTO> crearFormularioCompleto(
            @Valid @ModelAttribute SsomaRequestDTO request) {

        try {
            SsomaResponseDTO response = ssomaService.crearFormularioCompleto(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * GET /api/ssoma/{idSsoma}
     * Obtiene todo el formulario SSOMA por ID
     */
    @GetMapping("/{idSsoma}")
    public ResponseEntity<SsomaResponseDTO> obtenerFormularioCompleto(
            @PathVariable Integer idSsoma) {

        try {
            SsomaResponseDTO response = ssomaService.obtenerFormularioCompleto(idSsoma);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    /**
     * GET /api/ssoma/ots/{idOts}
     * Obtiene todos los formularios SSOMA por OT
     */
    @GetMapping("/ots/{idOts}")
    public ResponseEntity<List<SsomaResponseDTO>> obtenerFormulariosPorOt(
            @PathVariable Integer idOts) {

        try {
            List<SsomaResponseDTO> response = ssomaService.obtenerFormulariosPorOt(idOts);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}