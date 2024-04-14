package com.example.controlecontas;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Objects;

public class Categoria implements Serializable {
    private String descricao;
    private LinkedList<Conta> contas;

    public Categoria(String descricao) {
        this.descricao = descricao;
        contas = new LinkedList<>();
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LinkedList<Conta> getContas() {
        return contas;
    }


    public void setContas(Conta conta) {
        contas.add(conta);
    }

    public void modificarTodasContas(LinkedList<Conta> conta){
        contas = conta;
    }
    public int getTotalContas(){
        if(contas == null){
            return 0;
        }else{
            return contas.size();
        }
    }

    public double getValorTotal(){
        double valor = 0;
        if(contas != null){
            for (Conta c : contas) {
                valor = valor + c.getValor();
            }
            return valor;
        }else{
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(descricao, categoria.descricao) && Objects.equals(contas, categoria.contas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descricao, contas);
    }

    @Override
    public String toString() {
        return "Categoria{" +
                "descricao='" + descricao + '\'' +
                ", contas=" + contas +
                '}';
    }
}
