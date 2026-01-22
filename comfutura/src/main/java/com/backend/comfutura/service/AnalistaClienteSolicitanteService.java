package com.backend.comfutura.service;

import com.backend.comfutura.model.AnalistaClienteSolicitante;

import java.util.List;


public interface AnalistaClienteSolicitanteService {


    AnalistaClienteSolicitante guardar(AnalistaClienteSolicitante analista);

    List<AnalistaClienteSolicitante> listar();

    void toggle(Integer id);
}