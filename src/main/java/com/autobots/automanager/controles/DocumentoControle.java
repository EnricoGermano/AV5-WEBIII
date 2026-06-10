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

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private DocumentoRepositorio repositorio;

    @GetMapping
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = repositorio.findAll();
        if (documentos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> obterDocumento(@PathVariable Long id) {
        Optional<Documento> opt = repositorio.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/cadastro")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Documento> cadastrarDocumento(@RequestBody Documento documento) {
        if (documento.getId() != null) {
            return ResponseEntity.badRequest().build();
        }
        Documento salvo = repositorio.save(documento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @PutMapping("/atualizar/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Documento> atualizarDocumento(@PathVariable Long id, @RequestBody Documento documento) {
        Optional<Documento> opt = repositorio.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Documento existente = opt.get();
        if (documento.getTipo() != null) existente.setTipo(documento.getTipo());
        if (documento.getNumero() != null) existente.setNumero(documento.getNumero());
        Documento atualizado = repositorio.save(existente);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/excluir/{id}")
    @PreAuthorize("@autorizacaoService.isInterno(authentication)")
    public ResponseEntity<Void> excluirDocumento(@PathVariable Long id) {
        if (!repositorio.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repositorio.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
