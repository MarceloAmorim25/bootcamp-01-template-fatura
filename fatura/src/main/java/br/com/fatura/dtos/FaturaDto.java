package br.com.fatura.dtos;

import br.com.fatura.entidades.Fatura;
import br.com.fatura.entidades.Transacao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FaturaDto {


    private String email;

    private List<TransacaoDto> transacoes = new ArrayList<>();


    public FaturaDto(Fatura fatura) {
        this.email = fatura.getCartao().getEmail();
        this.transacoes = toDtoSet(fatura.getTransacoes());
    }

    public List<TransacaoDto> toDtoSet(Set<Transacao> transacoes){

        List<TransacaoDto> transacaoDtos = new ArrayList<>();

        transacoes.forEach(transacao -> {
            transacaoDtos.add(new TransacaoDto(transacao));
        });

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
}
