package br.com.fatura.dtos;

import br.com.fatura.entidades.Transacao;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransacaoDto {


    private BigDecimal valor;

    private LocalDateTime efetivadaEm;

    private String local;


    public TransacaoDto(Transacao transacao) {
        this.valor = transacao.getValor();
        this.efetivadaEm = transacao.getEfetivadaEm();
        this.local = transacao.getEstabelecimento().getNome();
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getEfetivadaEm() {
        return efetivadaEm;
    }

    public void setEfetivadaEm(LocalDateTime efetivadaEm) {
        this.efetivadaEm = efetivadaEm;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }
}
