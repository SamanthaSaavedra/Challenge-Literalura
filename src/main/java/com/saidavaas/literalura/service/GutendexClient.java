package com.saidavaas.literalura.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class GutendexClient {

    @Value("${gutendex.api.url}")
    private String apiUrl;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public String buscarPorTitulo(String titulo) throws Exception {
        String tituloEncoded = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
        String url = apiUrl + "?search=" + tituloEncoded;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Error al consultar la API. Código HTTP: " + response.statusCode());
        }

        return response.body();
    }
}
