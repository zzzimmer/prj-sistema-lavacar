package br.edu.ifsc.fln.model.domain;

public class Modelo {
    private int id;
    private String descricao;
    private Motor motor;
    private Marca marca; // o que garante essa associação? marca ter um Modelo?
    private Ecategoria ecategoria;

    public Modelo (){
        criaMotor();
    }

    public Modelo(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        criaMotor();
    }

    public Motor getMotor() {
        return motor;
    }

    private void criaMotor(){
        this.motor = new Motor();
    }
}
