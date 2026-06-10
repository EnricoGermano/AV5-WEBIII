package com.autobots.msvendas.controle;

import com.autobots.msvendas.servico.VendasPeriodoService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class VendasPeriodoControle {

    private final VendasPeriodoService vendasPeriodoService;

    public VendasPeriodoControle(VendasPeriodoService vendasPeriodoService) {
        this.vendasPeriodoService = vendasPeriodoService;
    }

    @GetMapping("/vendas-periodo/{empresaId}")
    public ResponseEntity<Map<String, Object>> buscarVendasPorPeriodo(
            @PathVariable Long empresaId,
            @RequestParam("inicio") @DateTimeFormat(pattern = "yyyy-MM-dd") Date inicio,
            @RequestParam("fim") @DateTimeFormat(pattern = "yyyy-MM-dd") Date fim,
            @RequestHeader("Authorization") String authorizationHeader) {

        Map<String, Object> resultado = vendasPeriodoService.buscarVendasPorPeriodo(empresaId, inicio, fim, authorizationHeader);
        return ResponseEntity.ok(resultado);
    }
}
