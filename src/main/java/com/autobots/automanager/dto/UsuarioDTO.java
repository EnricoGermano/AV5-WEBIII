package com.autobots.automanager.dto;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.enumeracoes.TipoUsuario;
import java.util.List;
import java.util.Date;
import java.util.stream.Collectors;

public record UsuarioDTO(
    Long id,
    String nome,
    String email,
    List<TipoUsuario> perfil,
    Endereco endereco,
    List<Telefone> telefones,
    List<Documento> documentos
) {
    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) return null;
        return new UsuarioDTO(
            usuario.getId(),
            usuario.getNome(),
            usuario.getEmail(),
            usuario.getPerfil(),
            usuario.getEndereco(),
            usuario.getTelefones(),
            usuario.getDocumentos()
        );
    }

    public static List<UsuarioDTO> fromEntityList(List<Usuario> usuarios) {
        if (usuarios == null) return List.of();
        return usuarios.stream().map(UsuarioDTO::fromEntity).collect(Collectors.toList());
    }
}
