package br.edu.ifsc.fln.model.domain;


//todo dao de veiculo e adiante, tela e controller.
public class Veiculo {
    private int id;
    private String placa;
    private String observacoes;
    private Cor cor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public Cor getCor() {
        return cor;
    }

    public void setCor(Cor cor) {
        this.cor = cor;
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
