package br.com.fatura.entidades;

import br.com.fatura.dtos.RenegociacaoRequest;
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
public class Renegociacao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String identificadorDaFatura;

    @ManyToOne
    private Fatura fatura;

    @NotNull
    @Positive
    private Integer quantidade;

    @NotNull
    @Positive
    private BigDecimal valor;

    @Enumerated(value = EnumType.STRING)
    private StatusAprovacao status;

    @Deprecated
    public Renegociacao(){}

    public Renegociacao(String identificadorDaFatura, Integer quantidade, BigDecimal valor, Fatura fatura) {
        this.identificadorDaFatura = identificadorDaFatura;
        this.quantidade = quantidade;
        this.valor = valor;
        this.fatura = fatura;
    }

    public void avisaLegadoAtualizaStatus(IntegracaoApiCartoes integracaoApiCartoes, String numeroCartao,
                                          RenegociacaoRequest renegociacaoRequest, EntityManager entityManager){

        var respostaApiCartoes =
                Objects.requireNonNull(integracaoApiCartoes.renegociacaoFatura(numeroCartao, renegociacaoRequest).getBody()).getResultado();

        this.setStatus(StatusAprovacao.valueOf(respostaApiCartoes));

        entityManager.merge(this);

    }

    public Fatura getFatura() {
        return fatura;
    }

    public void setFatura(Fatura fatura) {
        this.fatura = fatura;
    }

    public void setStatus(StatusAprovacao status) {
        this.status = status;
    }

}
