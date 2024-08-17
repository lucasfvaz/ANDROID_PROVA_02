package com.example.segundaavaliacao;

import java.io.Serializable;
import java.util.Objects;

public class Locador implements Serializable {

    private  String cep;
    private String nome;
    private String cpf;
    private String complemento;

    private String email;

    private String localidade;

    private String logradouro;

    private String numero;

    private String uf;

    private String telefone;


    public Locador(String cep, String nome, String cpf, String complemento, String email, String localidade, String logradouro, String numero, String uf, String telefone) {
        this.cep = cep;
        this.nome = nome;
        this.cpf = cpf;
        this.complemento = complemento;
        this.email = email;
        this.localidade = localidade;
        this.logradouro = logradouro;
        this.numero = numero;
        this.uf = uf;
        this.telefone = telefone;
    }

    public Locador() {
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }


    @Override
    public String toString() {
        return  nome ;
    }
}
