package com.backend.comfutura.controller;

import com.backend.comfutura.dto.response.ProyectoResponse;
import com.backend.comfutura.model.Proyecto;
import com.backend.comfutura.service.serviceImpl.ProyectoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoServiceImpl proyectoService;

    // Listar proyectos con paginación
    @GetMapping
    public Page<ProyectoResponse> listarProyectos(
            @RequestParam(defaultValue = "0") int page
    ) {
        return proyectoService.listarProyectos(page);
    }

    // Crear proyecto
    @PostMapping
    public ProyectoResponse crearProyecto(@RequestBody Proyecto proyecto) {
        return proyectoService.crearProyecto(proyecto);
    }

    // Editar proyecto
    @PutMapping("/{id}")
    public ProyectoResponse editarProyecto(
            @PathVariable Integer id,
            @RequestBody Proyecto proyectoActualizado
    ) {
        return proyectoService.editarProyecto(id, proyectoActualizado);
    }

    // Obtener proyecto por ID
    @GetMapping("/{id}")
    public ProyectoResponse obtenerProyectoPorId(@PathVariable Integer id) {
        return proyectoService.obtenerProyectoPorId(id);
    }

    // Toggle activar/desactivar proyecto
    @PatchMapping("/{id}/toggle")
    public ProyectoResponse toggleProyecto(@PathVariable Integer id) {
        return proyectoService.toggleProyecto(id);
    }
}