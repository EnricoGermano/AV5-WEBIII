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

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private EnderecoRepositorio repositorio;

    @GetMapping
    public ResponseEntity<List<Endereco>> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();
        if (enderecos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(enderecos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Endereco> obterEndereco(@PathVariable Long id) {
        Optional<Endereco> opt = repositorio.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastro")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Endereco> cadastrarEndereco(@RequestBody Endereco endereco) {
        if (endereco.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Endereco salvo = repositorio.save(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Endereco> atualizarEndereco(@PathVariable Long id, @RequestBody Endereco endereco) {
        Optional<Endereco> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Endereco existente = opt.get();
        if (endereco.getEstado() != null) existente.setEstado(endereco.getEstado());
        if (endereco.getCidade() != null) existente.setCidade(endereco.getCidade());
        if (endereco.getBairro() != null) existente.setBairro(endereco.getBairro());
        if (endereco.getRua() != null) existente.setRua(endereco.getRua());
        if (endereco.getNumero() != null) existente.setNumero(endereco.getNumero());
        if (endereco.getCodigoPostal() != null) existente.setCodigoPostal(endereco.getCodigoPostal());
        if (endereco.getInformacoesAdicionais() != null) existente.setInformacoesAdicionais(endereco.getInformacoesAdicionais());
        Endereco atualizado = repositorio.save(existente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/excluir/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Void> excluirEndereco(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
