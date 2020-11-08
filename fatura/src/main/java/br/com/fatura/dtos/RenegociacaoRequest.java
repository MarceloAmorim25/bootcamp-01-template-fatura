package br.com.fatura.dtos;

import br.com.fatura.entidades.Renegociacao;

import java.math.BigDecimal;

public class RenegociacaoRequest {


    private String identificadorDaFatura;

    private Integer quantidade;

    private BigDecimal valor;


    @Deprecated
    public RenegociacaoRequest(){}

    public RenegociacaoRequest(String identificadorDaFatura, Integer quantidade, BigDecimal valor) {
        this.identificadorDaFatura = identificadorDaFatura;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Renegociacao toModel(){
        return new Renegociacao(identificadorDaFatura, quantidade, valor);
    }

    public String getIdentificadorDaFatura() {
        return identificadorDaFatura;
    }

    public void setIdentificadorDaFatura(String identificadorDaFatura) {
        this.identificadorDaFatura = identificadorDaFatura;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
