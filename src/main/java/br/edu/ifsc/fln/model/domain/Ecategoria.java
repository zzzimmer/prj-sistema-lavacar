package br.edu.ifsc.fln.model.domain;

public enum Ecategoria {

    PEQUENO ("Pequeno"),
    MEDIO("Médio"),
    GRANDE("Grande"),
    PADRAO("Padrão"),
    MOTO("Moto");

    private final String descricao;

    Ecategoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
