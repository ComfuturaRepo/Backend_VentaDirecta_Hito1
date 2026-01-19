package com.backend.comfutura.controller;

import com.backend.comfutura.dto.request.OtCreateRequest;
import com.backend.comfutura.dto.response.OtResponse;
import com.backend.comfutura.service.OtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ots")
@RequiredArgsConstructor
public class OtController {

    private final OtService otService;

    @PostMapping
    public ResponseEntity<OtResponse> createOt(@Valid @RequestBody OtCreateRequest request) {
        OtResponse response = otService.createOt(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}