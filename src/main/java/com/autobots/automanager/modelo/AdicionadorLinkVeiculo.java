package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.VeiculoControle;
import com.autobots.automanager.entidades.Veiculo;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkVeiculo implements AdicionadorLink<Veiculo> {

    @Override
    public void adicionarLink(List<Veiculo> lista) {
        for (Veiculo veiculo : lista) {
            adicionarLink(veiculo);
        }
    }

    @Override
    public void adicionarLink(Veiculo veiculo) {
        veiculo.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class)
                        .obterVeiculo(veiculo.getId()))
                .withSelfRel());
        veiculo.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class)
                        .obterVeiculos())
                .withRel("veiculos"));
    }
}
