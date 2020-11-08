package br.com.fatura.dtos;

import br.com.fatura.entidades.Fatura;
import br.com.fatura.entidades.Parcela;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class ParcelaRequest {


    @NotBlank
    private String identificadorDaFatura;

    @Positive
    @NotNull
    private Integer quantidade;

    @Positive
    private BigDecimal valor;


    public ParcelaRequest(@NotBlank String identificadorDaFatura,
                          @Positive @NotNull Integer quantidade, @Positive BigDecimal valor) {
        this.identificadorDaFatura = identificadorDaFatura;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Parcela toModel(Fatura fatura){
        return new Parcela(identificadorDaFatura, quantidade, valor, fatura);
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
