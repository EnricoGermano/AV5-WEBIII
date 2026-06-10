package com.autobots.msveiculos.controle;

import com.autobots.msveiculos.servico.VeiculosAtendidosService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VeiculosAtendidosControle {

    private final VeiculosAtendidosService veiculosAtendidosService;

    public VeiculosAtendidosControle(VeiculosAtendidosService veiculosAtendidosService) {
        this.veiculosAtendidosService = veiculosAtendidosService;
    }

    @GetMapping("/veiculos-atendidos/{empresaId}")
    public ResponseEntity<List<Map<String, Object>>> listarVeiculosAtendidos(
            @PathVariable Long empresaId,
            @RequestHeader("Authorization") String authorizationHeader) {

        List<Map<String, Object>> veiculos = veiculosAtendidosService.buscarVeiculosAtendidos(empresaId, authorizationHeader);
        return ResponseEntity.ok(veiculos);
    }
}
