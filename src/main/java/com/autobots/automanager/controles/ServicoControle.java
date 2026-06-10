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

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.modelo.AdicionadorLinkServico;
import com.autobots.automanager.repositorios.ServicoRepositorio;

@RestController
@RequestMapping("/servico")
public class ServicoControle {

    @Autowired
    private ServicoRepositorio repositorio;

    @Autowired
    private AdicionadorLinkServico adicionadorLink;

    @GetMapping
    public ResponseEntity<List<Servico>> obterServicos() {
        List<Servico> servicos = repositorio.findAll();
        if (servicos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        adicionadorLink.adicionarLink(servicos);
        return ResponseEntity.ok(servicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Servico> obterServico(@PathVariable Long id) {
        Optional<Servico> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Servico servico = opt.get();
        adicionadorLink.adicionarLink(servico);
        return ResponseEntity.ok(servico);
    }

    @PostMapping("/cadastro")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Servico> cadastrarServico(@RequestBody Servico servico) {
        if (servico.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Servico salvo = repositorio.save(servico);
        adicionadorLink.adicionarLink(salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Servico> atualizarServico(@PathVariable Long id, @RequestBody Servico servico) {
        Optional<Servico> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Servico existente = opt.get();
        if (servico.getNome() != null) existente.setNome(servico.getNome());
        if (servico.getValor() > 0) existente.setValor(servico.getValor());
        if (servico.getDescricao() != null) existente.setDescricao(servico.getDescricao());
        Servico atualizado = repositorio.save(existente);
        adicionadorLink.adicionarLink(atualizado);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/excluir/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Void> excluirServico(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
