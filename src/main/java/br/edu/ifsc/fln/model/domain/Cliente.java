package br.edu.ifsc.fln.model.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Cliente extends Object{
    protected int id;
    protected String nome;
    protected String celular;
    protected String email;
    protected LocalDate dataCadastro = LocalDate.now();
    protected Pontuacao pontuacao = new Pontuacao();

    public Pontuacao getPontuacao() {
        return pontuacao;
    }

    public void setPontuacao(Pontuacao pontuacao) {
        this.pontuacao = pontuacao;
    }

    private List <Veiculo> veiculoList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public List<Veiculo> getVeiculoList() {
        return veiculoList;
    }

    public void add(Veiculo veiculo) {
        if(veiculoList == null){
            veiculoList = new ArrayList<>();
        }
        veiculoList.add(veiculo);
        veiculo.setCliente(this);
    }

    public void remove(Veiculo veiculo){
        if (veiculoList != null){
            veiculoList.remove(veiculo);
            veiculo.setCliente(null);
        }
    }

    @Override
    public String toString(){
        return nome;
    }

    //todo
//    public String getDados() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Dados do fornecedor ").append(this.getClass().getSimpleName()).append("\n");
//        sb.append("Id........: ").append(id).append("\n");
//        sb.append("Nome......: ").append(nome).append("\n");
//        sb.append("Fone......: ").append(fone).append("\n");
//        sb.append("Email.....: ").append(email).append("\n");
//        return sb.toString();
//    }

}
