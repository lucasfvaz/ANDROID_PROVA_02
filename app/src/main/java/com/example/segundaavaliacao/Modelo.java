package com.example.segundaavaliacao;

import java.io.Serializable;
import java.util.Objects;

public class Modelo implements Serializable {


    private String categoria;
    private String descricao;

    private Integer id;


    private Marca marca;
    private Double valorDiaria;


    public Modelo(String categoria, String descricao, Integer id, Marca marca, Double valorDiaria) {
        this.categoria = categoria;
        this.descricao = descricao;
        this.id = id;
        this.marca = marca;
        this.valorDiaria = valorDiaria;
    }


    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Marca getMarca() {
        return marca;
    }

    public void setMarca(Marca marca) {
        this.marca = marca;
    }

    public Double getValorDiaria() {
        return valorDiaria;
    }

    public void setValorDiaria(Double valorDiaria) {
        this.valorDiaria = valorDiaria;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Modelo)) return false;
        Modelo modelo = (Modelo) o;
        return id.equals(modelo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    @Override
    public String toString() {
        return descricao;
    }
}
