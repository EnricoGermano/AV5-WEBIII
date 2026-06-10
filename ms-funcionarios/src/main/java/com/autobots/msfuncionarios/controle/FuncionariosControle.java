package com.autobots.msfuncionarios.controle;

import com.autobots.msfuncionarios.servico.FuncionariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionariosControle {

    @Autowired
    private FuncionariosService funcionariosService;

    @GetMapping("/{empresaId}")
    public ResponseEntity<List<Map<String, Object>>> listarFuncionarios(
            @PathVariable Long empresaId,
            @RequestHeader("Authorization") String authorizationHeader) {

        List<Map<String, Object>> funcionarios = funcionariosService.buscarFuncionarios(empresaId, authorizationHeader);
        return ResponseEntity.ok(funcionarios);
    }
}
