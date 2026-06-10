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
        // Monta os headers com o token JWT recebido
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Chama a API principal para buscar a empresa
        String url = automanagerApiUrl + "/empresa/" + empresaId;
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        Map<String, Object> empresa = response.getBody();
        if (empresa == null) {
            return new ArrayList<>();
        }

        // Extrai a lista de vendas da empresa
        List<Map<String, Object>> vendas = (List<Map<String, Object>>) empresa.get("vendas");
        if (vendas == null || vendas.isEmpty()) {
            return new ArrayList<>();
        }

        // Coleta veículos únicos (por id), removendo nulos e duplicados
        Map<Object, Map<String, Object>> veiculosUnicos = new LinkedHashMap<>();

        for (Map<String, Object> venda : vendas) {
            Map<String, Object> veiculo = (Map<String, Object>) venda.get("veiculo");

            if (veiculo != null && veiculo.get("id") != null) {
                Object veiculoId = veiculo.get("id");

                if (!veiculosUnicos.containsKey(veiculoId)) {
                    Map<String, Object> veiculoData = new LinkedHashMap<>();
                    veiculoData.put("id", veiculo.get("id"));
                    veiculoData.put("modelo", veiculo.get("modelo"));
                    veiculoData.put("fabricante", veiculo.get("fabricante"));
                    veiculoData.put("anoFabricacao", veiculo.get("anoFabricacao"));
                    veiculoData.put("placa", veiculo.get("placa"));
                    veiculosUnicos.put(veiculoId, veiculoData);
                }
            }
        }

        return new ArrayList<>(veiculosUnicos.values());
    }
}
