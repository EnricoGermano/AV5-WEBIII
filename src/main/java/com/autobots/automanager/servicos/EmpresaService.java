package com.autobots.automanager.servicos;

import com.autobots.automanager.dto.EmpresaDTO;
import com.autobots.automanager.dto.UsuarioDTO;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.enumeracoes.TipoUsuario;
import com.autobots.automanager.repositorios.EmpresaRepositorio;
import com.autobots.automanager.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepositorio empresaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public List<EmpresaDTO> obterTodas() {
        return EmpresaDTO.fromEntityList(empresaRepositorio.findAll());
    }

    public Optional<EmpresaDTO> obterPorId(Long id) {
        return empresaRepositorio.findById(id).map(EmpresaDTO::fromEntity);
    }

    public EmpresaDTO cadastrar(Empresa empresa) {
        empresa.setId(null);
        return EmpresaDTO.fromEntity(empresaRepositorio.save(empresa));
    }

    public Optional<EmpresaDTO> atualizar(Long id, Empresa dadosAtualizacao) {
        return empresaRepositorio.findById(id).map(existente -> {
            if (dadosAtualizacao.getRazaoSocial() != null) existente.setRazaoSocial(dadosAtualizacao.getRazaoSocial());
            if (dadosAtualizacao.getNomeFantasia() != null) existente.setNomeFantasia(dadosAtualizacao.getNomeFantasia());
            if (dadosAtualizacao.getCnpj() != null) existente.setCnpj(dadosAtualizacao.getCnpj());
            if (dadosAtualizacao.getEndereco() != null) existente.setEndereco(dadosAtualizacao.getEndereco());
            return EmpresaDTO.fromEntity(empresaRepositorio.save(existente));
        });
    }

    public boolean excluir(Long id) {
        if (!empresaRepositorio.existsById(id)) return false;
        empresaRepositorio.deleteById(id);
        return true;
    }

    public List<UsuarioDTO> obterClientesDaEmpresa(Long empresaId) {
        Optional<Empresa> optEmpresa = empresaRepositorio.findById(empresaId);
        if (optEmpresa.isEmpty()) return List.of();
        
        List<Usuario> clientes = optEmpresa.get().getUsuarios().stream()
            .filter(u -> u.getPerfil().contains(TipoUsuario.CLIENTE))
            .collect(Collectors.toList());
            
        return UsuarioDTO.fromEntityList(clientes);
    }
}
