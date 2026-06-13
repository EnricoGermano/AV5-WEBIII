package com.autobots.msveiculos.servico;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class VeiculosAtendidosService {

    @Value("${automanager.api.url}")
    private String automanagerApiUrl;

    private final RestTemplate restTemplate;

    public VeiculosAtendidosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> buscarVeiculosAtendidos(Long empresaId, String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        String url = automanagerApiUrl + "/empresa/" + empresaId + "/veiculos-atendidos";
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        List<Map<String, Object>> veiculos = response.getBody();
        if (veiculos == null) {
            return new ArrayList<>();
        }

        return veiculos;
    }
}
