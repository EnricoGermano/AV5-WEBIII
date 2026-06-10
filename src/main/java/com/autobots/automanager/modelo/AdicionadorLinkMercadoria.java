package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.MercadoriaControle;
import com.autobots.automanager.entidades.Mercadoria;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkMercadoria implements AdicionadorLink<Mercadoria> {

    @Override
    public void adicionarLink(List<Mercadoria> lista) {
        for (Mercadoria mercadoria : lista) {
            adicionarLink(mercadoria);
        }
    }

    @Override
    public void adicionarLink(Mercadoria mercadoria) {
        mercadoria.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class)
                        .obterMercadoria(mercadoria.getId()))
                .withSelfRel());
        mercadoria.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class)
                        .obterMercadorias())
                .withRel("mercadorias"));
    }
}
