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

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelo.AdicionadorLinkEmpresa;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;

@RestController
@RequestMapping("/empresa")
public class EmpresaControle {

    @Autowired
    private EmpresaRepositorio repositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private AdicionadorLinkEmpresa adicionadorLink;

    @GetMapping
    public ResponseEntity<List<Empresa>> obterEmpresas() {
        List<Empresa> empresas = repositorio.findAll();
        if (empresas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        adicionadorLink.adicionarLink(empresas);
        return ResponseEntity.ok(empresas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Empresa> obterEmpresa(@PathVariable Long id) {
        Optional<Empresa> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = opt.get();
        adicionadorLink.adicionarLink(empresa);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa) {
        if (empresa.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Empresa salva = repositorio.save(empresa);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Empresa> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresa) {
        Optional<Empresa> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa existente = opt.get();
        if (empresa.getRazaoSocial() != null) existente.setRazaoSocial(empresa.getRazaoSocial());
        if (empresa.getNomeFantasia() != null) existente.setNomeFantasia(empresa.getNomeFantasia());
        if (empresa.getCnpj() != null) existente.setCnpj(empresa.getCnpj());
        if (empresa.getEndereco() != null) existente.setEndereco(empresa.getEndereco());
        Empresa atualizada = repositorio.save(existente);
        adicionadorLink.adicionarLink(atualizada);
        return ResponseEntity.ok(atualizada);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Void> excluirEmpresa(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{empresaId}/usuario/{usuarioId}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Empresa> vincularUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId) {
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        Optional<Usuario> optUsuario = usuarioRepositorio.findById(usuarioId);
        if (optEmpresa.isEmpty() || optUsuario.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = optEmpresa.get();
        empresa.getUsuarios().add(optUsuario.get());
        Empresa salva = repositorio.save(empresa);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    @DeleteMapping("/{empresaId}/usuario/{usuarioId}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Empresa> desvincularUsuario(@PathVariable Long empresaId, @PathVariable Long usuarioId) {
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Empresa empresa = optEmpresa.get();
        empresa.getUsuarios().removeIf(u -> u.getId().equals(usuarioId));
        Empresa salva = repositorio.save(empresa);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.ok(salva);
    }

    // NOVA ROTA OTIMIZADA: Resolve o problema de OVER-FETCHING do MS-Clientes
    @GetMapping("/{empresaId}/clientes")
    public ResponseEntity<List<Usuario>> obterClientesDaEmpresa(@PathVariable Long empresaId) {
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Usuario> clientes = optEmpresa.get().getUsuarios().stream()
                .filter(u -> u.getPerfil().contains(com.autobots.automanager.enumeracoes.TipoUsuario.CLIENTE))
                .toList();
        return ResponseEntity.ok(clientes);
    }

    // NOVA ROTA OTIMIZADA: MS-Funcionarios
    @GetMapping("/{empresaId}/funcionarios")
    public ResponseEntity<List<Usuario>> obterFuncionariosDaEmpresa(@PathVariable Long empresaId) {
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Usuario> funcionarios = optEmpresa.get().getUsuarios().stream()
                .filter(u -> u.getPerfil().contains(com.autobots.automanager.enumeracoes.TipoUsuario.FUNCIONARIO) || 
                             u.getPerfil().contains(com.autobots.automanager.enumeracoes.TipoUsuario.GERENTE) || 
                             u.getPerfil().contains(com.autobots.automanager.enumeracoes.TipoUsuario.VENDEDOR))
                .toList();
        return ResponseEntity.ok(funcionarios);
    }

    // NOVA ROTA OTIMIZADA: MS-Catalogo
    @GetMapping("/{empresaId}/catalogo")
    public ResponseEntity<java.util.Map<String, Object>> obterCatalogoDaEmpresa(@PathVariable Long empresaId) {
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        java.util.Map<String, Object> catalogo = new java.util.HashMap<>();
        catalogo.put("mercadorias", optEmpresa.get().getMercadorias());
        catalogo.put("servicos", optEmpresa.get().getServicos());
        return ResponseEntity.ok(catalogo);
    }

    // NOVA ROTA OTIMIZADA: MS-Vendas (Vendas por periodo)
    @GetMapping("/{empresaId}/vendas-periodo")
    public ResponseEntity<List<com.autobots.automanager.entidades.Venda>> obterVendasPorPeriodo(
            @PathVariable Long empresaId, 
            @org.springframework.web.bind.annotation.RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.util.Date inicio, 
            @org.springframework.web.bind.annotation.RequestParam @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE) java.util.Date fim) {
        
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<com.autobots.automanager.entidades.Venda> vendasPeriodo = optEmpresa.get().getVendas().stream()
                .filter(v -> v.getCadastro() != null && !v.getCadastro().before(inicio) && !v.getCadastro().after(fim))
                .toList();
        return ResponseEntity.ok(vendasPeriodo);
    }

    // NOVA ROTA OTIMIZADA: MS-Veiculos (Veículos atendidos)
    @GetMapping("/{empresaId}/veiculos-atendidos")
    public ResponseEntity<java.util.Set<com.autobots.automanager.entidades.Veiculo>> obterVeiculosAtendidos(@PathVariable Long empresaId) {
        Optional<Empresa> optEmpresa = repositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        java.util.Set<com.autobots.automanager.entidades.Veiculo> veiculos = new java.util.HashSet<>();
        optEmpresa.get().getVendas().forEach(venda -> {
            if (venda.getVeiculo() != null) {
                veiculos.add(venda.getVeiculo());
            }
        });
        return ResponseEntity.ok(veiculos);
    }
}
