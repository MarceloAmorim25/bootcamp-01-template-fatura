package br.com.fatura.entidades;

import br.com.fatura.dtos.RecebeTransacao;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;


@Entity
public class Fatura {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @OneToMany(mappedBy = "fatura")
    private Set<Transacao> transacoes;

    @ManyToOne
    private Cartao cartao;

    @NotNull
    private Month mes;

    @Deprecated
    public Fatura(){}

    public Fatura(Cartao cartao) {
        this.cartao = cartao;
    }

    public Fatura(RecebeTransacao transacaoRecebida) {
        this.cartao = transacaoRecebida.getCartao().toModel();
        this.mes = defineMesDaFatura(transacaoRecebida.getEfetivadaEm());
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

}
