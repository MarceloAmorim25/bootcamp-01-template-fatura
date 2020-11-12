package br.com.fatura.entidades;
import com.sun.istack.NotNull;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Entity
public class Transacao {

    /* pontos de dificuldade de entendimento -> 4 */

    @Id
    private String id;

    @NotNull
    private BigDecimal valor;

    @NotNull
    private LocalDateTime efetivadaEm;

    /* @complexidade (1) - classe específica do projeto */
    @ManyToOne
    private Cartao cartao;

    /* @complexidade (1) - classe específica do projeto */
    @ManyToOne
    private Fatura fatura;

    /* @complexidade (1) - classe específica do projeto */
    @NotNull
    @Embedded
    private  Estabelecimento estabelecimento;


    @Deprecated
    public Transacao(){}

    public Transacao(String id, BigDecimal valor, String efetivadaEm, Cartao cartao,
                     Estabelecimento estabelecimento, Fatura fatura) {
        this.id = id;
        this.valor = valor;
        this.efetivadaEm = converteParaLocalDateTime(efetivadaEm);
        this.cartao = cartao;
        this.estabelecimento = estabelecimento;
        this.fatura = fatura;
    }

    /* @complexidade (1) - método específico */
    public LocalDateTime converteParaLocalDateTime(String efetivadaEm){

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        return LocalDateTime.parse(efetivadaEm, formatter);

    }

    public BigDecimal getValor() {
        return valor;
    }

    public LocalDateTime getEfetivadaEm() {
        return efetivadaEm;
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


}
