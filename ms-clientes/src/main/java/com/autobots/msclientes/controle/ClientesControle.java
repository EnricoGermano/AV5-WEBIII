package com.autobots.msclientes.controle;

import com.autobots.msclientes.servico.ClientesService;
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
@RequestMapping("/api/clientes")
public class ClientesControle {

    @Autowired
    private ClientesService clientesService;

    @GetMapping("/{empresaId}")
    public ResponseEntity<List<Map<String, Object>>> listarClientes(
            @PathVariable Long empresaId,
            @RequestHeader("Authorization") String authorizationHeader) {

        List<Map<String, Object>> clientes = clientesService.buscarClientesDaEmpresa(empresaId, authorizationHeader);
        return ResponseEntity.ok(clientes);
    }
}
