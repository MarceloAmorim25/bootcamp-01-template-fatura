package br.com.fatura.dtos;

import br.com.fatura.entidades.Fatura;
import br.com.fatura.entidades.Renegociacao;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;


public class RenegociacaoRequest {

    @NotBlank
    private String identificadorDaFatura;

    @Positive
    @NotNull
    private Integer quantidade;

    @Positive
    @NotNull
    private BigDecimal valor;


    @Deprecated
    public RenegociacaoRequest(){}

    public RenegociacaoRequest(String identificadorDaFatura, Integer quantidade, BigDecimal valor) {
        this.identificadorDaFatura = identificadorDaFatura;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Renegociacao toModel(Fatura fatura){
        return new Renegociacao(identificadorDaFatura, quantidade, valor, fatura);
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
