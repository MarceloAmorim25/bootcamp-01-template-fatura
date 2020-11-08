package br.com.fatura.entidades;

import br.com.fatura.dtos.ParcelaRequest;
import br.com.fatura.entidades.enums.StatusAprovacao;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Parcela {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String identificadorDaFatura;

    @ManyToOne
    private Fatura fatura;

    @Positive
    @NotNull
    private Integer quantidade;

    @Positive
    private BigDecimal valor;

    @Enumerated(value = EnumType.STRING)
    private StatusAprovacao status;

    @Deprecated
    public Parcela(){}

    public Parcela(@NotBlank String identificadorDaFatura, @Positive @NotNull Integer quantidade,
                   @Positive BigDecimal valor, Fatura fatura) {
        this.identificadorDaFatura = identificadorDaFatura;
        this.quantidade = quantidade;
        this.valor = valor;
        this.fatura = fatura;
    }

    public void avisaLegadoEAtualizaStatus(IntegracaoApiCartoes integracaoApiCartoes, String numeroCartao,
                                           ParcelaRequest parcelaRequest, EntityManager entityManager){

        var respostaApiCartoes =
                Objects.requireNonNull(integracaoApiCartoes.solicitarParcelamento(numeroCartao, parcelaRequest).getBody()).getResultado();

        this.setStatus(StatusAprovacao.valueOf(respostaApiCartoes));

        entityManager.merge(this);

    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public StatusAprovacao getStatus() {
        return status;
    }

    public void setStatus(StatusAprovacao status) {
        this.status = status;
    }

    public String getIdentificadorDaFatura() {
        return identificadorDaFatura;
    }

    public void setIdentificadorDaFatura(String identificadorDaFatura) {
        this.identificadorDaFatura = identificadorDaFatura;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }
}
