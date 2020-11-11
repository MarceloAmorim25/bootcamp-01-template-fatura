package br.com.fatura.dtos;

import br.com.fatura.entidades.Cartao;

import java.math.BigDecimal;
import java.util.List;

public class SaldoDto {


    private BigDecimal saldo;

    private List<TransacaoDto> transacoes;


    public SaldoDto(BigDecimal saldo, Cartao cartao) {
        this.saldo = saldo;
        this.transacoes = cartao.retornarTransacoes();
    }


    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public List<TransacaoDto> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransacaoDto> transacoes) {
        this.transacoes = transacoes;
    }
}
