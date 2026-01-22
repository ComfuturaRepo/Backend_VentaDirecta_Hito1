package com.backend.comfutura.service;

import com.backend.comfutura.model.JefaturaClienteSolicitante;
import java.util.List;

public interface JefaturaClienteSolicitanteService {

    JefaturaClienteSolicitante guardar(JefaturaClienteSolicitante jefatura);

    List<JefaturaClienteSolicitante> listar();

    void toggle(Integer id);
}