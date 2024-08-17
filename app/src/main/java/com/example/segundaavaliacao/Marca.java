package com.example.segundaavaliacao;

import java.io.Serializable;
import java.util.Objects;

public class Marca implements Serializable {

    private Integer id;
    private String nome;


    public Marca(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Marca() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Marca)) return false;
        Marca marca = (Marca) o;
        return id.equals(marca.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
