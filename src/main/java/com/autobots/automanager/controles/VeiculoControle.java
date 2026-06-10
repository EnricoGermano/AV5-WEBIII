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

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.modelo.AdicionadorLinkVeiculo;
import com.autobots.automanager.repositorios.VeiculoRepositorio;

@RestController
@RequestMapping("/veiculo")
public class VeiculoControle {

    @Autowired
    private VeiculoRepositorio repositorio;

    @Autowired
    private AdicionadorLinkVeiculo adicionadorLink;

    @GetMapping
    public ResponseEntity<List<Veiculo>> obterVeiculos() {
        List<Veiculo> veiculos = repositorio.findAll();
        if (veiculos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        adicionadorLink.adicionarLink(veiculos);
        return ResponseEntity.ok(veiculos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veiculo> obterVeiculo(@PathVariable Long id) {
        Optional<Veiculo> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Veiculo veiculo = opt.get();
        adicionadorLink.adicionarLink(veiculo);
        return ResponseEntity.ok(veiculo);
    }

    @PostMapping("/cadastro")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Veiculo> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
        if (veiculo.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Veiculo salvo = repositorio.save(veiculo);
        adicionadorLink.adicionarLink(salvo);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Veiculo> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculo) {
        Optional<Veiculo> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Veiculo existente = opt.get();
        if (veiculo.getModelo() != null) existente.setModelo(veiculo.getModelo());
        if (veiculo.getFabricante() != null) existente.setFabricante(veiculo.getFabricante());
        if (veiculo.getAnoFabricacao() != null) existente.setAnoFabricacao(veiculo.getAnoFabricacao());
        if (veiculo.getPlaca() != null) existente.setPlaca(veiculo.getPlaca());
        Veiculo atualizado = repositorio.save(existente);
        adicionadorLink.adicionarLink(atualizado);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/excluir/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Void> excluirVeiculo(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
