package com.example.segundaavaliacao;

import java.io.Serializable;
import java.util.Objects;

public class Veiculo implements Serializable  {
    private Integer ano;
    private String cor;

    private Integer idModelo;

    private Modelo modelo;

    private String placa;


    public Veiculo(Integer ano, String cor, Integer idModelo, Modelo modelo, String placa) {
        this.ano = ano;
        this.cor = cor;
        this.idModelo = idModelo;
        this.modelo = modelo;
        this.placa = placa;
    }

    public Veiculo(Integer ano, String cor, Integer idModelo, String placa) {
        this.ano = ano;
        this.cor = cor;
        this.idModelo = idModelo;
        this.placa = placa;
    }
    public Veiculo() {
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public Integer getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(Integer idModelo) {
        this.idModelo = idModelo;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public void setModelo(Modelo modelo) {
        this.modelo = modelo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Veiculo)) return false;
        Veiculo veiculo = (Veiculo) o;
        return idModelo.equals(veiculo.idModelo) && placa.equals(veiculo.placa);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idModelo, placa);
    }

    @Override
    public String toString() {
        return  modelo.getDescricao() ;

    }
}
