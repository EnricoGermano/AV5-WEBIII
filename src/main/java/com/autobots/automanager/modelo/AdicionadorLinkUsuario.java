package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entidades.Usuario;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

    @Override
    public void adicionarLink(List<Usuario> lista) {
        for (Usuario usuario : lista) {
            adicionarLink(usuario);
        }
    }

    @Override
    public void adicionarLink(Usuario usuario) {
        usuario.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class)
                        .obterUsuario(usuario.getId()))
                .withSelfRel());
        usuario.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class)
                        .obterUsuarios())
                .withRel("usuarios"));
    }
}
