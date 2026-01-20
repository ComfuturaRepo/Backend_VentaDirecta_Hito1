package com.backend.comfutura.controller;

import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.service.serviceImpl.DropdownServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/dropdowns")
@RequiredArgsConstructor
public class DropdownController {

    private final DropdownServiceImpl dropdownService;

    @GetMapping("/clientes")
    public ResponseEntity<List<DropdownDTO>> getClientes() {
        return ResponseEntity.ok(dropdownService.getClientes());
    }

    @GetMapping("/clientes/{idCliente}/areas")
    public ResponseEntity<List<DropdownDTO>> getAreasByCliente(@PathVariable Integer idCliente) {
        List<DropdownDTO> areas = dropdownService.getAreasByCliente(idCliente);
        return ResponseEntity.ok(areas);
    }

    @GetMapping("/proyectos")
    public ResponseEntity<List<DropdownDTO>> getProyectos() {
        return ResponseEntity.ok(dropdownService.getProyectos());
    }

    @GetMapping("/fases")
    public ResponseEntity<List<DropdownDTO>> getFases() {
        return ResponseEntity.ok(dropdownService.getFases());
    }

    @GetMapping("/sites")
    public ResponseEntity<List<DropdownDTO>> getSites() {
        return ResponseEntity.ok(dropdownService.getSites());
    }

    @GetMapping("/regiones")
    public ResponseEntity<List<DropdownDTO>> getRegiones() {
        return ResponseEntity.ok(dropdownService.getRegiones());
    }

    @GetMapping("/ots")
    public ResponseEntity<List<DropdownDTO>> getOtsActivas() {
        return ResponseEntity.ok(dropdownService.getOtsActivas());
    }
}