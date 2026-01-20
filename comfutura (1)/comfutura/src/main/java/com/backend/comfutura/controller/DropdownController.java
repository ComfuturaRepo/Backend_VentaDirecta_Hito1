package com.backend.comfutura.controller;

import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.service.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropdowns")
@RequiredArgsConstructor
public class DropdownController {

    private final DropdownService dropdownService;

    @GetMapping("/clientes")
    public List<DropdownDTO> getClientes() {
        return dropdownService.getClientes();
    }

    @GetMapping("/areas")
    public List<DropdownDTO> getAreas() {
        return dropdownService.getAreas();
    }

    @GetMapping("/proyectos")
    public List<DropdownDTO> getProyectos() {
        return dropdownService.getProyectos();
    }

    @GetMapping("/fases")
    public List<DropdownDTO> getFases() {
        return dropdownService.getFases();
    }

    @GetMapping("/sites")
    public List<DropdownDTO> getSites() {
        return dropdownService.getSites();
    }

    @GetMapping("/regiones")
    public List<DropdownDTO> getRegiones() {
        return dropdownService.getRegiones();
    }

    @GetMapping("/ots")
    public List<DropdownDTO> getOtsActivas() {
        return dropdownService.getOtsActivas();
    }
}
