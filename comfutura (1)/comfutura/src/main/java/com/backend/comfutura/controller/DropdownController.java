package com.backend.comfutura.controller;
import com.backend.comfutura.dto.DropTown.AreaDropdownDTO;
import com.backend.comfutura.dto.DropTown.ClienteDropdownDTO;
import com.backend.comfutura.service.DropdownService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropdown")
public class DropdownController {

    @Autowired
    private DropdownService dropdownService;

    @GetMapping("/clientes")
    public ResponseEntity<List<ClienteDropdownDTO>> getClientes() {
        return ResponseEntity.ok(dropdownService.getAllClientesActivos());
    }

    @GetMapping("/areas")
    public ResponseEntity<List<AreaDropdownDTO>> getAreas() {
        return ResponseEntity.ok(dropdownService.getAllAreasActivas());
    }

}