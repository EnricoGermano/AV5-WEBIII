package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Telefone;
import java.util.List;
import java.util.stream.Collectors;

public record EmpresaDTO(
    Long id,
    String razaoSocial,
    String nomeFantasia,
    String cnpj,
    Endereco endereco,
    List<Telefone> telefones
) {
    public static EmpresaDTO fromEntity(Empresa empresa) {
        if (empresa == null) return null;
        return new EmpresaDTO(
            empresa.getId(),
            empresa.getRazaoSocial(),
            empresa.getNomeFantasia(),
            empresa.getCnpj(),
            empresa.getEndereco(),
            empresa.getTelefones()
        );
    }

    public static List<EmpresaDTO> fromEntityList(List<Empresa> empresas) {
        if (empresas == null) return List.of();
        return empresas.stream().map(EmpresaDTO::fromEntity).collect(Collectors.toList());
    }
}
