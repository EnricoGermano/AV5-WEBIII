package com.autobots.automanager.entidades;

import java.util.Date;

import org.springframework.hateoas.RepresentationModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "mercadorias")
public class Mercadoria extends RepresentationModel<Mercadoria> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String fabricante;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private int quantidade;

    @Column(nullable = false)
    private double valor;

    @Temporal(TemporalType.DATE)
    private Date validade;

    @Temporal(TemporalType.TIMESTAMP)
    private Date cadastro;

    public Mercadoria() {
    }

    public Mercadoria(String nome, String fabricante, String descricao, int quantidade, double valor, Date validade) {
        this.nome = nome;
        this.fabricante = fabricante;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valor = valor;
        this.validade = validade;
    }

    public Mercadoria(Long id, String nome, String fabricante, String descricao, int quantidade, double valor, Date validade) {
        this.id = id;
        this.nome = nome;
        this.fabricante = fabricante;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.valor = valor;
        this.validade = validade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }
}
