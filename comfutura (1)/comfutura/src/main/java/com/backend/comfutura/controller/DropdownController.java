package com.backend.comfutura.controller;

import com.backend.comfutura.record.DropdownDTO;
import com.backend.comfutura.service.DropdownService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropdowns")
@RequiredArgsConstructor
public class DropdownController {

    private final DropdownService dropdownService;

    // ────────────────────────────────────────────────────────
    // Endpoints existentes
    // ────────────────────────────────────────────────────────

    @GetMapping("/clientes")
    public ResponseEntity<List<DropdownDTO>> getClientes() {
        return ResponseEntity.ok(dropdownService.getClientes());
    }

    @GetMapping("/clientes/{idCliente}/areas")
    public ResponseEntity<List<DropdownDTO>> getAreasByCliente(@PathVariable Integer idCliente) {
        return ResponseEntity.ok(dropdownService.getAreasByCliente(idCliente));
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

    // ────────────────────────────────────────────────────────
    // Nuevos endpoints para los responsables
    // ────────────────────────────────────────────────────────

    @GetMapping("/jefaturas-cliente-solicitante")
    public ResponseEntity<List<DropdownDTO>> getJefaturasClienteSolicitante() {
        return ResponseEntity.ok(dropdownService.getJefaturasClienteSolicitante());
    }

    @GetMapping("/analistas-cliente-solicitante")
    public ResponseEntity<List<DropdownDTO>> getAnalistasClienteSolicitante() {
        return ResponseEntity.ok(dropdownService.getAnalistasClienteSolicitante());
    }

    @GetMapping("/coordinadores-ti-cw")
    public ResponseEntity<List<DropdownDTO>> getCoordinadoresTiCw() {
        return ResponseEntity.ok(dropdownService.getCoordinadoresTiCw());
    }

    @GetMapping("/jefaturas-responsable")
    public ResponseEntity<List<DropdownDTO>> getJefaturasResponsable() {
        return ResponseEntity.ok(dropdownService.getJefaturasResponsable());
    }

    @GetMapping("/liquidadores")
    public ResponseEntity<List<DropdownDTO>> getLiquidador() {
        return ResponseEntity.ok(dropdownService.getLiquidador());
    }

    @GetMapping("/ejecutantes")
    public ResponseEntity<List<DropdownDTO>> getEjecutantes() {
        return ResponseEntity.ok(dropdownService.getEjecutantes());
    }

    @GetMapping("/analistas-contable")
    public ResponseEntity<List<DropdownDTO>> getAnalistasContable() {
        return ResponseEntity.ok(dropdownService.getAnalistasContable());
    }

    // Opcional: endpoint que devuelve TODOS los dropdowns necesarios para el formulario de OT
    @GetMapping("/form-ots")
    public ResponseEntity<?> getAllDropdownsForOtForm() {
        var data = new java.util.HashMap<String, Object>();

        data.put("clientes", dropdownService.getClientes());
        data.put("proyectos", dropdownService.getProyectos());
        data.put("fases", dropdownService.getFases());
        data.put("sites", dropdownService.getSites());
        data.put("regiones", dropdownService.getRegiones());
        data.put("jefaturasClienteSolicitante", dropdownService.getJefaturasClienteSolicitante());
        data.put("analistasClienteSolicitante", dropdownService.getAnalistasClienteSolicitante());
        data.put("coordinadoresTiCw", dropdownService.getCoordinadoresTiCw());
        data.put("jefaturasResponsable", dropdownService.getJefaturasResponsable());
        data.put("liquidadores", dropdownService.getLiquidador());
        data.put("ejecutantes", dropdownService.getEjecutantes());
        data.put("analistasContable", dropdownService.getAnalistasContable());

        return ResponseEntity.ok(data);
    }
}