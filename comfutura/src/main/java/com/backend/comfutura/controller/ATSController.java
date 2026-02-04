package com.backend.comfutura.controller;

import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.request.SSOMA.ATSRequestDTO;
import com.backend.comfutura.dto.request.SSOMA.SSTFormRequestDTO;
import com.backend.comfutura.dto.response.SSOMA.ATSListDTO;
import com.backend.comfutura.dto.response.SSOMA.ATSResponseDTO;
import com.backend.comfutura.dto.response.SSOMA.SSTFormResponseDTO;
import com.backend.comfutura.service.ATSService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ssoma")
@RequiredArgsConstructor
public class ATSController {

    private final ATSService atsService;

    @GetMapping
    public ResponseEntity<PageResponseDTO<ATSListDTO>> getAllATS(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fecha,desc") String sort,
            @RequestParam(required = false) String search) {

        // Parsear el parámetro de ordenamiento
        String[] sortParams = sort.split(",");
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        PageResponseDTO<ATSListDTO> atsPage = atsService.findAllPaginado(pageable, search);
        return ResponseEntity.ok(atsPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ATSResponseDTO> getATSById(@PathVariable Integer id) {
        ATSResponseDTO ats = atsService.findById(id);
        return ResponseEntity.ok(ats);
    }

    @PostMapping
    public ResponseEntity<ATSResponseDTO> createATS(@RequestBody ATSRequestDTO atsRequestDTO) {
        ATSResponseDTO createdATS = atsService.create(atsRequestDTO);
        return new ResponseEntity<>(createdATS, HttpStatus.CREATED);
    }

    @PostMapping("/completo")
    public ResponseEntity<SSTFormResponseDTO> crearFormularioCompleto(
            @Valid @RequestBody SSTFormRequestDTO requestDTO) {

        SSTFormResponseDTO response = atsService.crearFormularioCompleto(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}