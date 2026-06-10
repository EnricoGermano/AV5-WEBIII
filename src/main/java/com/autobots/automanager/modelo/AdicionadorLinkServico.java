package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entidades.Servico;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {

    @Override
    public void adicionarLink(List<Servico> lista) {
        for (Servico servico : lista) {
            adicionarLink(servico);
        }
    }

    @Override
    public void adicionarLink(Servico servico) {
        servico.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class)
                        .obterServico(servico.getId()))
                .withSelfRel());
        servico.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class)
                        .obterServicos())
                .withRel("servicos"));
    }
}
