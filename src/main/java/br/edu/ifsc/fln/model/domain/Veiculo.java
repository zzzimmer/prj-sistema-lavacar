package br.edu.ifsc.fln.model.domain;

public class Veiculo {
    private int id;
    private String placa;
    private String observacoes;

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Veiculo() {
    }

    public Veiculo(String placa) {
        this.placa = placa;
    }

    public Veiculo(String placa, String observacoes) {
        this.placa = placa;
        this.observacoes = observacoes;
    }
}
