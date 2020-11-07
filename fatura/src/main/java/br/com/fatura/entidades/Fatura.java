package br.com.fatura.entidades;

import br.com.fatura.dtos.RecebeTransacao;
import br.com.fatura.dtos.TransacaoDto;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


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

    private BigDecimal total = new BigDecimal(0);

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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        LocalDateTime date = LocalDateTime.parse(efetivadaEm, formatter);

        return date.getMonth();

    }

    public List<TransacaoDto> toDtoSet(){

        List<TransacaoDto> transacaoDtos = new ArrayList<>();

        this.transacoes.forEach(transacao -> { transacaoDtos.add(new TransacaoDto(transacao)); });

        return transacaoDtos
                .stream()
                .filter(transacaoDto -> transacaoDto.getEfetivadaEm().getMonth() == LocalDateTime.now().getMonth())
                .collect(Collectors.toList());

    }

    public BigDecimal calculaEbuscaTotal(){

        return toDtoSet().stream()
                .map(TransacaoDto::getValor)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {

        this.total = total;

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
