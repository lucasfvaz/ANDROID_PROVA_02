package com.example.segundaavaliacao;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Locacao implements Serializable {
   private String cpf;

   private Date inicio;

   private Date fim;

   private Integer id;

   private String placa;
   private Double valor;


    public Locacao(String cpf, Date inicio, Date fim, Integer id, String placa, Double valor) {
        this.cpf = cpf;
        this.inicio = inicio;
        this.fim = fim;
        this.id = id;
        this.placa = placa;
        this.valor = valor;
    }

    public Locacao() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Locacao)) return false;
        Locacao locacao = (Locacao) o;
        return Objects.equals(id, locacao.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
