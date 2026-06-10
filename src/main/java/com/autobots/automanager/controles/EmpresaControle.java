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

    @PostMapping("/cadastro")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa) {
        if (empresa.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Empresa salva = repositorio.save(empresa);
        adicionadorLink.adicionarLink(salva);
        return ResponseEntity.status(HttpStatus.CREATED).body(salva);
    }

    @PutMapping("/atualizar/{id}")
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

    @DeleteMapping("/excluir/{id}")
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
}
