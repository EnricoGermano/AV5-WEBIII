package com.autobots.automanager.modelo;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entidades.Empresa;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {

    @Override
    public void adicionarLink(List<Empresa> lista) {
        for (Empresa empresa : lista) {
            adicionarLink(empresa);
        }
    }

    @Override
    public void adicionarLink(Empresa empresa) {
        empresa.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class)
                        .obterEmpresa(empresa.getId()))
                .withSelfRel());
        empresa.add(WebMvcLinkBuilder
                .linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class)
                        .obterEmpresas())
                .withRel("empresas"));
    }
}
