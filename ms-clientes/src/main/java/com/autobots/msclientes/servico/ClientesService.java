package com.autobots.msclientes.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ClientesService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${automanager.api.url}")
    private String automanagerApiUrl;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> buscarClientesDaEmpresa(Long empresaId, String authorizationHeader) {
        String url = automanagerApiUrl + "/empresa/" + empresaId + "/clientes";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> clientes = response.getBody();

        if (clientes == null) {
            return new ArrayList<>();
        }

        return clientes;
    }
}
