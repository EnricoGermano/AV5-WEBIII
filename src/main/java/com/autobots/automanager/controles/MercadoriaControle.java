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

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.modelo.AdicionadorLinkMercadoria;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;

@RestController
@RequestMapping("/mercadoria")
public class MercadoriaControle {

    @Autowired
    private MercadoriaRepositorio repositorio;

    @Autowired
    private AdicionadorLinkMercadoria adicionadorLink;

    @GetMapping
    public ResponseEntity<List<Mercadoria>> obterMercadorias() {
        List<Mercadoria> mercadorias = repositorio.findAll();
        if (mercadorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        adicionadorLink.adicionarLink(mercadorias);
        return ResponseEntity.ok(mercadorias);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable Long id) {
        Optional<Mercadoria> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Mercadoria mercadoria = opt.get();
        adicionadorLink.adicionarLink(mercadoria);
        return ResponseEntity.ok(mercadoria);
    }

    @PostMapping
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Mercadoria> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
        if (mercadoria.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Mercadoria salva = repositorio.save(mercadoria);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Mercadoria> atualizarMercadoria(@PathVariable Long id, @RequestBody Mercadoria mercadoria) {
        Optional<Mercadoria> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Mercadoria existente = opt.get();
        if (mercadoria.getNome() != null) existente.setNome(mercadoria.getNome());
        if (mercadoria.getFabricante() != null) existente.setFabricante(mercadoria.getFabricante());
        if (mercadoria.getQuantidade() > 0) existente.setQuantidade(mercadoria.getQuantidade());
        if (mercadoria.getValor() > 0) existente.setValor(mercadoria.getValor());
        if (mercadoria.getValidade() != null) existente.setValidade(mercadoria.getValidade());
        if (mercadoria.getDescricao() != null) existente.setDescricao(mercadoria.getDescricao());
        Mercadoria atualizada = repositorio.save(existente);
        adicionadorLink.adicionarLink(atualizada);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Void> excluirMercadoria(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
