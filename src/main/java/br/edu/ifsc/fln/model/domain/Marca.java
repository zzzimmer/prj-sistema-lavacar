package br.edu.ifsc.fln.model.domain;

public class Marca {
    private int id;
    private String nome;

    public Marca(){

    };

    public Marca(String nome) {
        this.nome = nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
