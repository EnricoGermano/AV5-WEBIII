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
        if (empresa == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> usuarios = (List<Map<String, Object>>) empresa.get("usuarios");
        if (usuarios == null) {
            return new ArrayList<>();
        }

        List<Map<String, Object>> funcionarios = new ArrayList<>();
        for (Map<String, Object> usuario : usuarios) {
            List<String> perfil = (List<String>) usuario.get("perfil");
            if (perfil != null && possuiPerfilFuncionario(perfil)) {
                funcionarios.add(usuario);
            }
        }

        return funcionarios;
    }

    private boolean possuiPerfilFuncionario(List<String> perfis) {
        for (String perfil : perfis) {
            if (PERFIS_FUNCIONARIO.contains(perfil)) {
                return true;
            }
        }
        return false;
    }
}
