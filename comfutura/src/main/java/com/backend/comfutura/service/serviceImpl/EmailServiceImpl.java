package com.backend.comfutura.service.serviceImpl;

import com.backend.comfutura.service.EmailService;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

//    @Value("${resend.api-key}")
//    private String apiKey;
//
//    @Value("${resend.from-email}")
//    private String fromEmail;

//    private static final String RESEND_URL = "https://api.resend.com/emails";
//
//    private final OkHttpClient client = new OkHttpClient();
//
//    @Override
//    public void enviarCorreo(
//            String[] destinatarios,
//            String asunto,
//            String contenidoHtml) {
//
//        String toJson = String.join("\",\"", destinatarios);
//
//        // 🔐 Escapar HTML para JSON
//        String htmlEscaped = contenidoHtml
//                .replace("\"", "\\\"")
//                .replace("\n", "")
//                .replace("\r", "");
//
//        String json = """
//    {
//      "from": "Compras <onboarding@resend.dev>",
//      "to": ["%s"],
//      "subject": "%s",
//      "html": "%s"
//    }
//    """.formatted(
//                toJson,
//                asunto.replace("\"", "\\\""),
//                htmlEscaped
//        );
//
//        RequestBody body = RequestBody.create(
//                json,
//                MediaType.parse("application/json")
//        );
//
////        Request request = new Request.Builder()
////                .url("https://api.resend.com/emails")
////                .addHeader("Authorization", "Bearer " + apiKey)
////                .addHeader("Content-Type", "application/json")
////                .post(body)
////                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            if (!response.isSuccessful()) {
//                throw new RuntimeException(
//                        "Error enviando correo Resend: " + response.body().string()
//                );
//            }
//        } catch (IOException e) {
//            throw new RuntimeException("Error conexión Resend", e);
//        }
//    }

}
