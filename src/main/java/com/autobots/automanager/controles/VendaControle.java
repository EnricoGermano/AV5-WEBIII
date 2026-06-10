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
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelo.AdicionadorLinkVenda;
import com.autobots.automanager.repositorios.MercadoriaRepositorio;
import com.autobots.automanager.repositorios.ServicoRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import com.autobots.automanager.repositorios.VeiculoRepositorio;
import com.autobots.automanager.repositorios.VendaRepositorio;

@RestController
@RequestMapping("/venda")
@PreAuthorize("@autorizacaoService.isInterno(authentication)")
public class VendaControle {

    @Autowired
    private VendaRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private VeiculoRepositorio veiculoRepositorio;

    @Autowired
    private MercadoriaRepositorio mercadoriaRepositorio;

    @Autowired
    private ServicoRepositorio servicoRepositorio;

    @Autowired
    private AdicionadorLinkVenda adicionadorLink;

    @GetMapping
    public ResponseEntity<List<Venda>> obterVendas() {
        List<Venda> vendas = repositorio.findAll();
        if (vendas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        adicionadorLink.adicionarLink(vendas);
        return ResponseEntity.ok(vendas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Venda> obterVenda(@PathVariable Long id) {
        Optional<Venda> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = opt.get();
        adicionadorLink.adicionarLink(venda);
        return ResponseEntity.ok(venda);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Venda> cadastrarVenda(@RequestBody Venda venda) {
        if (venda.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        if (venda.getCadastro() == null) {
            venda.setCadastro(new java.util.Date());
        }
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Venda> atualizarVenda(@PathVariable Long id, @RequestBody Venda venda) {
        Optional<Venda> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda existente = opt.get();
        if (venda.getFuncionario() != null) existente.setFuncionario(venda.getFuncionario());
        if (venda.getCliente() != null) existente.setCliente(venda.getCliente());
        if (venda.getVeiculo() != null) existente.setVeiculo(venda.getVeiculo());
        if (venda.getMercadorias() != null && !venda.getMercadorias().isEmpty()) {
            existente.setMercadorias(venda.getMercadorias());
        }
        if (venda.getServicos() != null && !venda.getServicos().isEmpty()) {
            existente.setServicos(venda.getServicos());
        }
        Venda atualizada = repositorio.save(existente);
        adicionadorLink.adicionarLink(atualizada);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> excluirVenda(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{vendaId}/funcionario/{funcionarioId}")
    public ResponseEntity<Venda> vincularFuncionario(@PathVariable Long vendaId, @PathVariable Long funcionarioId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        Optional<Usuario> optUsuario = usuarioRepositorio.findById(funcionarioId);
        if (optVenda.isEmpty() || optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        venda.setFuncionario(optUsuario.get());
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @PostMapping("/{vendaId}/cliente/{clienteId}")
    public ResponseEntity<Venda> vincularCliente(@PathVariable Long vendaId, @PathVariable Long clienteId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        Optional<Usuario> optUsuario = usuarioRepositorio.findById(clienteId);
        if (optVenda.isEmpty() || optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        venda.setCliente(optUsuario.get());
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @PostMapping("/{vendaId}/veiculo/{veiculoId}")
    public ResponseEntity<Venda> vincularVeiculo(@PathVariable Long vendaId, @PathVariable Long veiculoId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        Optional<Veiculo> optVeiculo = veiculoRepositorio.findById(veiculoId);
        if (optVenda.isEmpty() || optVeiculo.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        venda.setVeiculo(optVeiculo.get());
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @PostMapping("/{vendaId}/mercadoria/{mercadoriaId}")
    public ResponseEntity<Venda> adicionarMercadoria(@PathVariable Long vendaId, @PathVariable Long mercadoriaId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        Optional<Mercadoria> optMercadoria = mercadoriaRepositorio.findById(mercadoriaId);
        if (optVenda.isEmpty() || optMercadoria.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        if (!venda.getMercadorias().contains(optMercadoria.get())) {
            venda.getMercadorias().add(optMercadoria.get());
        }
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @DeleteMapping("/{vendaId}/mercadoria/{mercadoriaId}")
    public ResponseEntity<Venda> removerMercadoria(@PathVariable Long vendaId, @PathVariable Long mercadoriaId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        if (optVenda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        venda.getMercadorias().removeIf(m -> m.getId().equals(mercadoriaId));
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @PostMapping("/{vendaId}/servico/{servicoId}")
    public ResponseEntity<Venda> adicionarServico(@PathVariable Long vendaId, @PathVariable Long servicoId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        Optional<Servico> optServico = servicoRepositorio.findById(servicoId);
        if (optVenda.isEmpty() || optServico.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        if (!venda.getServicos().contains(optServico.get())) {
            venda.getServicos().add(optServico.get());
        }
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @DeleteMapping("/{vendaId}/servico/{servicoId}")
    public ResponseEntity<Venda> removerServico(@PathVariable Long vendaId, @PathVariable Long servicoId) {
        Optional<Venda> optVenda = repositorio.findById(vendaId);
        if (optVenda.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Venda venda = optVenda.get();
        venda.getServicos().removeIf(s -> s.getId().equals(servicoId));
        Venda salva = repositorio.save(venda);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }
}
