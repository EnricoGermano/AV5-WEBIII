package com.autobots.automanager.controles;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.CredencialAcesso;
import com.autobots.automanager.repositorios.CredencialAcessoRepositorio;

@RestController
@RequestMapping("/credencial")
@PreAuthorize("@autorizacaoService.isInterno(authentication)")
public class CredencialAcessoControle {

    @Autowired
    private CredencialAcessoRepositorio repositorio;

    @GetMapping
    public ResponseEntity<List<CredencialAcesso>> obterCredenciais() {
        List<CredencialAcesso> credenciais = repositorio.findAll();
        if (credenciais.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(credenciais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CredencialAcesso> obterCredencial(@PathVariable Long id) {
        Optional<CredencialAcesso> opt = repositorio.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<CredencialAcesso> cadastrarCredencial(@RequestBody CredencialAcesso credencial) {
        if (credencial.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        CredencialAcesso salva = repositorio.save(credencial);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<CredencialAcesso> atualizarCredencial(@PathVariable Long id, @RequestBody CredencialAcesso credencial) {
        Optional<CredencialAcesso> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CredencialAcesso existente = opt.get();
        if (credencial.getNomeUsuario() != null) existente.setNomeUsuario(credencial.getNomeUsuario());
        if (credencial.getSenha() != null) existente.setSenha(credencial.getSenha());
        CredencialAcesso atualizada = repositorio.save(existente);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirCredencial(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
