package com.backend.comfutura.service;

public interface EmailService {
    void enviarCorreo(String[] to, String subject, String html);
}

