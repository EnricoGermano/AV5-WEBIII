package com.autobots.mscatalogo.controle;

import com.autobots.mscatalogo.servico.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoControle {

    private final CatalogoService catalogoService;

    public CatalogoControle(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/{empresaId}")
    public ResponseEntity<Map<String, Object>> obterCatalogo(
            @PathVariable Long empresaId,
            @RequestHeader("Authorization") String authorizationHeader) {
        Map<String, Object> catalogo = catalogoService.obterCatalogoPorEmpresa(empresaId, authorizationHeader);
        return ResponseEntity.ok(catalogo);
    }
}
