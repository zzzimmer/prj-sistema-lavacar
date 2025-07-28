package br.edu.ifsc.fln.model.domain;

public class Pontuacao {
    private int quantidade;


    public void add(int qtd){
        this.quantidade+=qtd;
    }

    public void subtrair(int qtd){
        this.quantidade-=qtd;
    }

    public int saldo() {
        return quantidade;
    }
}
