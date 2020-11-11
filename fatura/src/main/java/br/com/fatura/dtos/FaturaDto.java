package br.com.fatura.dtos;

import br.com.fatura.entidades.Fatura;
import br.com.fatura.entidades.Transacao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class FaturaDto {


    private BigDecimal total;

    private String email;

    private List<TransacaoDto> transacoes;


    public FaturaDto(Fatura fatura, int mes, int ano) {
        this.email = fatura.getCartao().getEmail();
        this.transacoes = fatura.toDtoListPorPeriodo(mes, ano);
        this.total = fatura.calculaEbuscaTotal();
    }


    public FaturaDto(Fatura fatura) {
        this.email = fatura.getCartao().getEmail();
        this.transacoes = fatura.toDtoList();
        this.total = fatura.calculaEbuscaTotal();
    }

    public List<TransacaoDto> toDto(List<Transacao> transacoes){

        List<TransacaoDto> transacaoDtos = new ArrayList<>();

        transacoes.forEach( transacao -> transacaoDtos.add(new TransacaoDto(transacao)) );

        return transacaoDtos;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<TransacaoDto> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<TransacaoDto> transacoes) {
        this.transacoes = transacoes;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
