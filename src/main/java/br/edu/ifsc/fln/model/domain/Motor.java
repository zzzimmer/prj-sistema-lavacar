package br.edu.ifsc.fln.model.domain;

public class Motor {
    private int potencia;
    private EtipoCombustivel EtipoCombustivel;
    private Modelo modelo;

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public Modelo getModelo() {
        return modelo;
    }

    public EtipoCombustivel getEtipoCombustivel() {
        return EtipoCombustivel;
    }

    public void setEtipoCombustivel(EtipoCombustivel etipoCombustivel) {
        EtipoCombustivel = etipoCombustivel;
    }

    public void setModelo() {
        this.modelo = modelo;
    }
}
