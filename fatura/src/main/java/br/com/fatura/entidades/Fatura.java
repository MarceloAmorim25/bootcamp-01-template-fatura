package br.com.fatura.entidades;

import br.com.fatura.dtos.RecebeTransacao;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Fatura {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;


    @OneToMany(mappedBy = "fatura", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Transacao> transacoes = new HashSet<>();

    @ManyToOne
    private Cartao cartao;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Month mes;

    @Deprecated
    public Fatura(){}

    public Fatura(Cartao cartao, RecebeTransacao recebeTransacao) {
        this.cartao = cartao;
        this.mes = defineMesDaFatura(recebeTransacao.getEfetivadaEm());
    }

    public void adicionarTransacao(Transacao transacao){
        this.transacoes.add(transacao);
    }

    public Month defineMesDaFatura(String efetivadaEm){

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        LocalDateTime date = LocalDateTime.parse(efetivadaEm, formatter);

        Month mes = date.getMonth();

        return mes;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Set<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(Set<Transacao> transacoes) {
        this.transacoes = transacoes;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public Month getMes() {
        return mes;
    }

    public void setMes(Month mes) {
        this.mes = mes;
    }
}
