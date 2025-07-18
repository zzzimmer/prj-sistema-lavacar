package br.edu.ifsc.fln.model.domain;

public class ItemOS {
    private double valorServico;
    private String observacoes;

    private Servico servico;
    private OrdemServico ordemServico;

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public double getValorServico() {
        return valorServico;
    }

    public void setValorServico() {
        valorServico = servico.getValor(); // para que exista valor, é necessário existir serviço. Diferente aqui
        //this.valorServico = valorServico;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
