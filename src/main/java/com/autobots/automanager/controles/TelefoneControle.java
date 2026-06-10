package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.TelefoneRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneRepositorio repositorio;

    @GetMapping
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        if (telefones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(telefones);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Telefone> obterTelefone(@PathVariable Long id) {
        Optional<Telefone> opt = repositorio.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Telefone> cadastrarTelefone(@RequestBody Telefone telefone) {
        if (telefone.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Telefone salvo = repositorio.save(telefone);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Telefone> atualizarTelefone(@PathVariable Long id, @RequestBody Telefone telefone) {
        Optional<Telefone> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Telefone existente = opt.get();
        if (telefone.getDdd() != null) existente.setDdd(telefone.getDdd());
        if (telefone.getNumero() != null) existente.setNumero(telefone.getNumero());
        Telefone atualizado = repositorio.save(existente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirTelefone(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
