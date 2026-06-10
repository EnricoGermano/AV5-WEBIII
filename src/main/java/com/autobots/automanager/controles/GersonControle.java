package com.autobots.automanager.controles;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GersonControle {

    @GetMapping(value = "/gerson", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] imagem() throws IOException {
        return carregarImagem("static/gerson.png");
    }

    @GetMapping(value = "/gerson2", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] imagem2() throws IOException {
        return carregarImagem("static/gerson2.png");
    }

    private byte[] carregarImagem(String caminho) throws IOException {
        try (InputStream inputStream = new ClassPathResource(caminho).getInputStream()) {
            return inputStream.readAllBytes();
        }
    }
}