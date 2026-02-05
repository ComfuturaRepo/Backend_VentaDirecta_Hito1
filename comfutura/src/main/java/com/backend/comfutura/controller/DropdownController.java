package com.backend.comfutura.controller;

import com.backend.comfutura.model.Site;
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
    @GetMapping("/areas")
    public ResponseEntity<List<DropdownDTO>> getAreas() {
        return ResponseEntity.ok(dropdownService.getAreas());
    }


    @GetMapping("/cargos")
    public ResponseEntity<List<DropdownDTO>> getCargos() {
        return ResponseEntity.ok(dropdownService.getCargos());
    }
    @GetMapping("/empresas")
    public ResponseEntity<List<DropdownDTO>> getEmpresas() {
        return ResponseEntity.ok(dropdownService.getEmpresas());
    }

    @GetMapping("/fases")
    public ResponseEntity<List<DropdownDTO>> getFases() {
        return ResponseEntity.ok(dropdownService.getFases());
    }

    @GetMapping("/sites")
    public ResponseEntity<List<DropdownDTO>> getSites() {
        return ResponseEntity.ok(dropdownService.getSites());
    }

    @GetMapping("/DescripcionesBySiteCodigo")
    public ResponseEntity<List<DropdownDTO>> getDescripcionesBySiteCodigo(@RequestParam(required = false) String siteCodigo) {
        return ResponseEntity.ok(dropdownService.getDescripcionesBySiteCodigo(siteCodigo));
    }

    @GetMapping("/SitesConDescripciones")
    public ResponseEntity<List<DropdownDTO>> getSitesConDescripciones() {
        return ResponseEntity.ok(dropdownService.getSitesConDescripciones());
    }



    @GetMapping("/sitesCompuesto")
    public ResponseEntity<List<DropdownDTO>> getSiteCompuesto() {
        return ResponseEntity.ok(dropdownService.getSiteCompuesto());
    }

    @GetMapping("/regiones")
    public ResponseEntity<List<DropdownDTO>> getRegiones() {
        return ResponseEntity.ok(dropdownService.getRegiones());
    }

    @GetMapping("/ots")
    public ResponseEntity<List<DropdownDTO>> getOtsActivas() {
        return ResponseEntity.ok(dropdownService.getOtsActivas());
    }
    @GetMapping("/nivel")
    public ResponseEntity<List<DropdownDTO>> getnivel() {
        return ResponseEntity.ok(dropdownService.getNivelesAll());
    }

    @GetMapping("/tipoOt")
    public ResponseEntity<List<DropdownDTO>> getTipoOt() {
        return ResponseEntity.ok(dropdownService.getOtTipo());
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

    @GetMapping("/estado-ot")
    public ResponseEntity<List<DropdownDTO>> getEstadoOt() {
        return ResponseEntity.ok(dropdownService.getEstadosOt());
    }

    @GetMapping("/ejecutantes")
    public ResponseEntity<List<DropdownDTO>> getEjecutantes() {
        return ResponseEntity.ok(dropdownService.getEjecutantes());
    }

    @GetMapping("/analistas-contable")
    public ResponseEntity<List<DropdownDTO>> getAnalistasContable() {
        return ResponseEntity.ok(dropdownService.getAnalistasContable());
    }
    @GetMapping("/maestro-codigos")
    public ResponseEntity<List<DropdownDTO>> getMaestroCodigos() {
        return ResponseEntity.ok(dropdownService.getMaestroCodigos());
    }

    @GetMapping("/trabajador")
    public ResponseEntity<List<DropdownDTO>> getTraabajadores() {
        return ResponseEntity.ok(dropdownService.getTrabajadores());
    }

    @GetMapping("/trabajadorSinUsuarioActivo")
    public ResponseEntity<List<DropdownDTO>> getSinUsuarioActivoTrabajador() {
        return ResponseEntity.ok(dropdownService.getTrabajadoresSinUsuarioActivo());
    }

    @GetMapping("/proveedores")
    public ResponseEntity<List<DropdownDTO>> getProveedores() {
        return ResponseEntity.ok(dropdownService.getProveedores());
    }
    //SSOMA
    // Agrega estos endpoints a tu DropdownController

    @GetMapping("/ssoma/trabajos")
    public ResponseEntity<List<DropdownDTO>> getTrabajos() {
        return ResponseEntity.ok(dropdownService.getTrabajos());
    }

    @GetMapping("/ssoma/roles-trabajo")
    public ResponseEntity<List<DropdownDTO>> getRolesTrabajo() {
        return ResponseEntity.ok(dropdownService.getRolesTrabajo());
    }

    @GetMapping("/ssoma/tareas")
    public ResponseEntity<List<DropdownDTO>> getTareas() {
        return ResponseEntity.ok(dropdownService.getTareas());
    }

    @GetMapping("/ssoma/tareas/trabajo/{idTrabajo}")
    public ResponseEntity<List<DropdownDTO>> getTareasByTrabajo(@PathVariable Integer idTrabajo) {
        return ResponseEntity.ok(dropdownService.getTareasByTrabajo(idTrabajo));
    }

    @GetMapping("/ssoma/peligros")
    public ResponseEntity<List<DropdownDTO>> getPeligros() {
        return ResponseEntity.ok(dropdownService.getPeligros());
    }

    @GetMapping("/ssoma/riesgos/peligro/{idPeligro}")
    public ResponseEntity<List<DropdownDTO>> getRiesgosByPeligro(@PathVariable Integer idPeligro) {
        return ResponseEntity.ok(dropdownService.getRiesgosByPeligro(idPeligro));
    }

    @GetMapping("/ssoma/medidas/riesgo/{idRiesgo}")
    public ResponseEntity<List<DropdownDTO>> getMedidasByRiesgo(@PathVariable Integer idRiesgo) {
        return ResponseEntity.ok(dropdownService.getMedidasByRiesgo(idRiesgo));
    }

    @GetMapping("/ssoma/epps")
    public ResponseEntity<List<DropdownDTO>> getEpps() {
        return ResponseEntity.ok(dropdownService.getEpps());
    }

    @GetMapping("/ssoma/tipos-riesgo")
    public ResponseEntity<List<DropdownDTO>> getTiposRiesgoTrabajo() {
        return ResponseEntity.ok(dropdownService.getTiposRiesgoTrabajo());
    }

    @GetMapping("/ssoma/herramientas")
    public ResponseEntity<List<DropdownDTO>> getHerramientas() {
        return ResponseEntity.ok(dropdownService.getHerramientas());
    }

    @GetMapping("/ssoma/preguntas-petar")
    public ResponseEntity<List<DropdownDTO>> getPreguntasPetar() {
        return ResponseEntity.ok(dropdownService.getPreguntasPetar());
    }

    @GetMapping("/ssoma/supervisores-sst")
    public ResponseEntity<List<DropdownDTO>> getSupervisoresSST() {
        return ResponseEntity.ok(dropdownService.getSupervisoresSST());
    }

    @GetMapping("/ssoma/capacitadores")
    public ResponseEntity<List<DropdownDTO>> getCapacitadores() {
        return ResponseEntity.ok(dropdownService.getCapacitadores());
    }

    @GetMapping("/ssoma/inspectores")
    public ResponseEntity<List<DropdownDTO>> getInspectores() {
        return ResponseEntity.ok(dropdownService.getInspectores());
    }

    @GetMapping("/ssoma/supervisores-operativos")
    public ResponseEntity<List<DropdownDTO>> getSupervisoresOperativos() {
        return ResponseEntity.ok(dropdownService.getSupervisoresOperativos());
    }

    @GetMapping("/ssoma/trabajadores/cargo/{cargo}")
    public ResponseEntity<List<DropdownDTO>> getTrabajadoresByCargo(@PathVariable String cargo) {
        return ResponseEntity.ok(dropdownService.getTrabajadoresByCargo(cargo));
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