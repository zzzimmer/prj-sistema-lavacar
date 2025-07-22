package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrdemServico {
    private long numero;
    private double total;
    private LocalDate agenda;
    private double desconto;
    private EStatus status;

    private List <ItemOS> listServicos = new ArrayList<>();
    private Veiculo veiculo;

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public double getTotal() {
        double soma = listServicos.stream()
                .mapToDouble(item -> item.getServico().getValor())
                .sum();
        return soma - desconto;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getAgenda() {
        return agenda;
    }

    public void setAgenda(LocalDate agenda) {
        this.agenda = agenda;
    }

    public double getDesconto() {
        return desconto;
    }

    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus(EStatus status) {
        this.status = status;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public List<ItemOS> getListServicos() {
        return listServicos;
    }

    public void setListServicos(List<ItemOS> listServicos) {
        this.listServicos = listServicos;
    }

    public void add(ItemOS itemOS){//faz a amarração. O método adiciona um ItemOS (Servico que sera consumido)
        // a lista da Ordem de Servico. E já adiciona, no ItemOS, set desta Ordem de servico.
        listServicos.add(itemOS);
        itemOS.setOrdemServico(this);
    }

    public void remove(ItemOS itemOS){
        listServicos.remove(itemOS);
        itemOS.setOrdemServico(null);
    }

    public double calcularServico(){
        double valor = 0.0;
        for (ItemOS itemOS: listServicos){
            valor += itemOS.getValorServico();
        }
        return valor;
    }

}
