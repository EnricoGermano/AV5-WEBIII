package com.autobots.mscatalogo.servico;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CatalogoService {

    @Value("${automanager.api.url}")
    private String automanagerApiUrl;

    private final RestTemplate restTemplate;

    public CatalogoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> obterCatalogoPorEmpresa(Long empresaId, String authorizationHeader) {
        String url = automanagerApiUrl + "/empresa/" + empresaId + "/catalogo";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> catalogo = response.getBody();

        if (catalogo == null) {
            catalogo = new HashMap<>();
            catalogo.put("servicos", new ArrayList<>());
            catalogo.put("mercadorias", new ArrayList<>());
        }

        return catalogo;
    }
}
