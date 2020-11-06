package br.com.fatura.entidades;
import com.sun.istack.NotNull;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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

    @NotNull
    @ManyToOne
    private Cartao cartao;

    @ManyToOne
    private Fatura fatura;

    @NotNull
    @Embedded
    private  Estabelecimento estabelecimento;


    @Deprecated
    public Transacao(){}

    public Transacao(String id, BigDecimal valor, String efetivadaEm,
                     Cartao cartao,Estabelecimento estabelecimento) {
        this.id = id;
        this.valor = valor;
        this.efetivadaEm = converteParaLocalDateTime(efetivadaEm);
        this.cartao = cartao;
        this.estabelecimento = estabelecimento;
    }

    public LocalDateTime converteParaLocalDateTime(String efetivadaEm){

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        LocalDateTime date = LocalDateTime.parse(efetivadaEm, formatter);
        return date;

    }

}
