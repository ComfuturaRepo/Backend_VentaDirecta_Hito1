package com.backend.comfutura.controller;
import com.backend.comfutura.dto.Page.PageResponseDTO;
import com.backend.comfutura.dto.response.permisos.PermisoDTO;
import com.backend.comfutura.dto.response.permisos.PermisoResponseDTO;
import com.backend.comfutura.dto.response.permisos.VerificarPermisoDTO;
import com.backend.comfutura.service.PermisoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permisos")
public class PermisoController {

    @Autowired
    private PermisoService permisoService;

    @PostMapping
    public ResponseEntity<PermisoResponseDTO> crearPermiso(@Valid @RequestBody PermisoDTO permisoDTO) {
        PermisoResponseDTO nuevoPermiso = permisoService.crearPermiso(permisoDTO);
        return new ResponseEntity<>(nuevoPermiso, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PermisoResponseDTO> actualizarPermiso(
            @PathVariable Integer id,
            @Valid @RequestBody PermisoDTO permisoDTO) {
        PermisoResponseDTO permisoActualizado = permisoService.actualizarPermiso(id, permisoDTO);
        return ResponseEntity.ok(permisoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPermiso(@PathVariable Integer id) {
        permisoService.eliminarPermiso(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PermisoResponseDTO> obtenerPermisoPorId(@PathVariable Integer id) {
        PermisoResponseDTO permiso = permisoService.obtenerPermisoPorId(id);
        return ResponseEntity.ok(permiso);
    }
    // ✅ NUEVO ENDPOINT PAGINADO
    @GetMapping("/paginados")
    public ResponseEntity<PageResponseDTO<PermisoResponseDTO>> listarPermisosPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "codigo") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        PageResponseDTO<PermisoResponseDTO> permisosPaginados =
                permisoService.listarTodosPermisosPaginados(page, size, sortBy, sortDirection);

        return ResponseEntity.ok(permisosPaginados);
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<PermisoResponseDTO> obtenerPermisoPorCodigo(@PathVariable String codigo) {
        PermisoResponseDTO permiso = permisoService.obtenerPermisoPorCodigo(codigo);
        return ResponseEntity.ok(permiso);
    }

    @GetMapping
    public ResponseEntity<List<PermisoResponseDTO>> listarTodosPermisos() {
        List<PermisoResponseDTO> permisos = permisoService.listarTodosPermisos();
        return ResponseEntity.ok(permisos);
    }

    @PostMapping("/verificar")
    public ResponseEntity<Boolean> verificarPermisoUsuario(@Valid @RequestBody VerificarPermisoDTO verificarPermisoDTO) {
        boolean tienePermiso = permisoService.verificarPermisoUsuario(verificarPermisoDTO);
        return ResponseEntity.ok(tienePermiso);
    }

    @GetMapping("/nivel/{idNivel}")
    public ResponseEntity<List<PermisoResponseDTO>> obtenerPermisosPorNivel(@PathVariable Integer idNivel) {
        List<PermisoResponseDTO> permisos = permisoService.obtenerPermisosPorNivel(idNivel);
        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/area/{idArea}")
    public ResponseEntity<List<PermisoResponseDTO>> obtenerPermisosPorArea(@PathVariable Integer idArea) {
        List<PermisoResponseDTO> permisos = permisoService.obtenerPermisosPorArea(idArea);
        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/cargo/{idCargo}")
    public ResponseEntity<List<PermisoResponseDTO>> obtenerPermisosPorCargo(@PathVariable Integer idCargo) {
        List<PermisoResponseDTO> permisos = permisoService.obtenerPermisosPorCargo(idCargo);
        return ResponseEntity.ok(permisos);
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<String>> obtenerPermisosUsuario(@PathVariable Integer idUsuario) {
        List<String> permisos = permisoService.obtenerPermisosUsuario(idUsuario);
        return ResponseEntity.ok(permisos);
    }
}
