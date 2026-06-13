package com.autobots.msvendas.servico;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class VendasPeriodoService {

    private final RestTemplate restTemplate;
    private final String apiUrl;

    public VendasPeriodoService(RestTemplate restTemplate,
                                @Value("${automanager.api.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> buscarVendasPorPeriodo(Long empresaId, Date inicio, Date fim, String authorizationHeader) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationHeader);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        SimpleDateFormat sdfUrl = new SimpleDateFormat("yyyy-MM-dd");
        String inicioStr = sdfUrl.format(inicio);
        String fimStr = sdfUrl.format(fim);

        String url = apiUrl + "/empresa/" + empresaId + "/vendas-periodo?inicio=" + inicioStr + "&fim=" + fimStr;
        ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.GET, entity, List.class);

        List<Map<String, Object>> vendasFiltradas = response.getBody();
        if (vendasFiltradas == null) {
            vendasFiltradas = new ArrayList<>();
        }

        // Coletar servicos e mercadorias únicos
        List<Map<String, Object>> servicosUnicos = new ArrayList<>();
        List<Map<String, Object>> mercadoriasUnicas = new ArrayList<>();
        Set<String> servicosIds = new HashSet<>();
        Set<String> mercadoriasIds = new HashSet<>();

        for (Map<String, Object> venda : vendasFiltradas) {
            // Coletar servicos
            if (venda.containsKey("servicos")) {
                List<Map<String, Object>> servicos = (List<Map<String, Object>>) venda.get("servicos");
                if (servicos != null) {
                    for (Map<String, Object> servico : servicos) {
                        String id = extrairId(servico);
                        if (id != null && servicosIds.add(id)) {
                            servicosUnicos.add(servico);
                        }
                    }
                }
            }

            // Coletar mercadorias
            if (venda.containsKey("mercadorias")) {
                List<Map<String, Object>> mercadorias = (List<Map<String, Object>>) venda.get("mercadorias");
                if (mercadorias != null) {
                    for (Map<String, Object> mercadoria : mercadorias) {
                        String id = extrairId(mercadoria);
                        if (id != null && mercadoriasIds.add(id)) {
                            mercadoriasUnicas.add(mercadoria);
                        }
                    }
                }
            }
        }

        // Montar resposta
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("inicio", sdf.format(inicio));
        resultado.put("fim", sdf.format(fim));
        resultado.put("servicos", servicosUnicos);
        resultado.put("mercadorias", mercadoriasUnicas);

        return resultado;
    }

    private Date extrairDataCadastro(Object cadastroObj) {
        if (cadastroObj == null) {
            return null;
        }

        try {
            // Se o cadastro vier como um número (timestamp em milissegundos)
            if (cadastroObj instanceof Number) {
                long timestamp = ((Number) cadastroObj).longValue();
                return new Date(timestamp);
            }

            // Se o cadastro vier como String numérica
            if (cadastroObj instanceof String) {
                String cadastroStr = (String) cadastroObj;
                try {
                    long timestamp = Long.parseLong(cadastroStr);
                    return new Date(timestamp);
                } catch (NumberFormatException e) {
                    // Tentar como data ISO
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    return sdf.parse(cadastroStr);
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    private String extrairId(Map<String, Object> item) {
        if (item.containsKey("id")) {
            return String.valueOf(item.get("id"));
        }
        return null;
    }
}
