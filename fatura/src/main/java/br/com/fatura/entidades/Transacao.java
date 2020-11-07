package br.com.fatura.entidades;
import br.com.fatura.dtos.CartaoDto;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import com.sun.istack.NotNull;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
public class Transacao {

    @Id
    private String id;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private LocalDateTime efetivadaEm;

    @ManyToOne
    private Cartao cartao;

    @ManyToOne
    private Fatura fatura;

    @NotNull
    @Embedded
    private  Estabelecimento estabelecimento;


    @Deprecated
    public Transacao(){}

    public Transacao(String id, BigDecimal valor, String efetivadaEm, CartaoDto cartao,
                     Estabelecimento estabelecimento, CartaoRepository cartaoRepository, Fatura fatura) {
        this.id = id;
        this.valor = valor;
        this.efetivadaEm = converteParaLocalDateTime(efetivadaEm);
        this.cartao = salvarCartao(cartao, cartaoRepository);
        this.estabelecimento = estabelecimento;
        this.fatura = fatura;
    }

    public LocalDateTime converteParaLocalDateTime(String efetivadaEm){

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        LocalDateTime date = LocalDateTime.parse(efetivadaEm, formatter);
        return date;

    }

    public Cartao salvarCartao(CartaoDto cartao, CartaoRepository cartaoRepository){

        if (cartaoRepository.findByNumero(cartao.getId()).isEmpty()) {
            cartaoRepository.save(cartao.toModel());
        }

        return cartaoRepository.findByNumero(cartao.getId()).orElseThrow();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public Estabelecimento getEstabelecimento() {
        return estabelecimento;
    }

    public void setEstabelecimento(Estabelecimento estabelecimento) {
        this.estabelecimento = estabelecimento;
    }
}
