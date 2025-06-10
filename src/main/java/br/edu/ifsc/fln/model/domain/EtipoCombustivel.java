package br.edu.ifsc.fln.model.domain;

public enum EtipoCombustivel {

    GASOLINA ("Gasolina"),
    ETANOL("Etanol"),
    FLEX("Flex"),
    DIESEL("Diesel"),
    GNV("GNV"),
    OUTRO("Outro");

    private final String descricao;

    EtipoCombustivel(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
