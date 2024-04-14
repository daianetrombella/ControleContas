package com.example.controlecontas;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Conta implements Serializable {
    private String descricao;
    private Date vencimento;
    private double valor;
    private String categoria;


    public Conta(String descricao, Date vencimento, double valor, String categoria) {
        this.descricao = descricao;
        this.vencimento = vencimento;
        this.valor = valor;
        this.categoria = categoria;
    }



    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }

    public  String getCategoria() {
        return categoria;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Double.compare(conta.valor, valor) == 0 && Objects.equals(descricao, conta.descricao) && Objects.equals(vencimento, conta.vencimento) && Objects.equals(categoria, conta.categoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, vencimento, valor, categoria);
    }

    @Override
    public String toString() {
        return "Conta{" +
                "descricao='" + descricao + '\'' +
                ", vencimento=" + vencimento +
                ", valor=" + valor +
                ", categoria=" + categoria +
                '}';
    }
}
