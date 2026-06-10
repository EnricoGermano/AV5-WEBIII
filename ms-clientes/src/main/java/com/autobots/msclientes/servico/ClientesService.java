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
        String url = automanagerApiUrl + "/empresa/" + empresaId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> empresa = response.getBody();

        if (empresa == null || !empresa.containsKey("usuarios")) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> usuarios = (List<Map<String, Object>>) empresa.get("usuarios");
        List<Map<String, Object>> clientes = new ArrayList<>();

        for (Map<String, Object> usuario : usuarios) {
            List<String> perfis = (List<String>) usuario.get("perfil");
            if (perfis != null && perfis.contains("CLIENTE")) {
                clientes.add(usuario);
            }
        }

        return clientes;
    }
}
