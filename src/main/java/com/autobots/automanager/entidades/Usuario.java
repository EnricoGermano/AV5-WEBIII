package com.autobots.automanager.entidades;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import com.autobots.automanager.enumeracoes.TipoUsuario;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "usuarios")
public class Usuario extends RepresentationModel<Usuario> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "nome_social")
    private String nomeSocial;

    @Column(unique = true, nullable = false)
    private String email;

    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCadastro;

    @ElementCollection(targetClass = TipoUsuario.class)
    @CollectionTable(name = "usuario_tipos", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    private List<TipoUsuario> perfil = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "credencial_id")
    private CredencialAcesso credencial;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToMany
    @JoinTable(
        name = "usuario_telefones",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "telefone_id")
    )
    private List<Telefone> telefones = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "usuario_documentos",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "documento_id")
    )
    private List<Documento> documentos = new ArrayList<>();

    @ManyToMany
    @JoinTable(
        name = "usuario_veiculos",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "veiculo_id")
    )
    private List<Veiculo> veiculos = new ArrayList<>();

    public Usuario() {
    }

    public Usuario(String nome, String nomeSocial, String email, Date dataNascimento, Date dataCadastro, CredencialAcesso credencial, Endereco endereco) {
        this.nome = nome;
        this.nomeSocial = nomeSocial;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.credencial = credencial;
        this.endereco = endereco;
    }

    public Usuario(Long id, String nome, String nomeSocial, String email, Date dataNascimento, Date dataCadastro, List<TipoUsuario> perfil, CredencialAcesso credencial, Endereco endereco, List<Telefone> telefones, List<Documento> documentos, List<Veiculo> veiculos) {
        this.id = id;
        this.nome = nome;
        this.nomeSocial = nomeSocial;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.dataCadastro = dataCadastro;
        this.perfil = perfil;
        this.credencial = credencial;
        this.endereco = endereco;
        this.telefones = telefones;
        this.documentos = documentos;
        this.veiculos = veiculos;
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

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<TipoUsuario> getPerfil() {
        return perfil;
    }

    public void setPerfil(List<TipoUsuario> perfil) {
        this.perfil = perfil;
    }

    public CredencialAcesso getCredencial() {
        return credencial;
    }

    public void setCredencial(CredencialAcesso credencial) {
        this.credencial = credencial;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public List<Telefone> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<Telefone> telefones) {
        this.telefones = telefones;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public List<Veiculo> getVeiculos() {
        return veiculos;
    }

    public void setVeiculos(List<Veiculo> veiculos) {
        this.veiculos = veiculos;
    }
}
