package br.com.fatura.entidades;

import br.com.fatura.entidades.enums.StatusAprovacao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Parcela {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String identificadorDaFatura;

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
                   @Positive BigDecimal valor) {
        this.identificadorDaFatura = identificadorDaFatura;
        this.quantidade = quantidade;
        this.valor = valor;
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
