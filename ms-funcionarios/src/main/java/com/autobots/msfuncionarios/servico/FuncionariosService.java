package com.autobots.msfuncionarios.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class FuncionariosService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${automanager.api.url}")
    private String automanagerApiUrl;

    // Perfis que caracterizam um funcionário
    private static final Set<String> PERFIS_FUNCIONARIO = new HashSet<>(
            Arrays.asList("ADMINISTRADOR", "GERENTE", "VENDEDOR")
    );

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> buscarFuncionarios(Long empresaId, String authorizationHeader) {
        String url = automanagerApiUrl + "/empresa/" + empresaId + "/funcionarios";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
        );

        List<Map<String, Object>> funcionarios = response.getBody();
        if (funcionarios == null) {
            return new ArrayList<>();
        }

        return funcionarios;
    }
}
