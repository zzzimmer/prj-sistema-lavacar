package br.edu.ifsc.fln.model.domain;

public class Servico {
    private int id;
    private String descricao;
    private double valor;
    private int pontos;
    private Ecategoria categoria;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public void setValor(double valor) { //Pode firar um Definir posteriormente
        this.valor = valor;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) { //Pode firar um Definir posteriormente
        this.pontos = pontos;
    }

    public Ecategoria getEcategoria() {
        return categoria;
    }

    public void setEcategoria(Ecategoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
