package br.com.fatura.entidades;

import br.com.fatura.dtos.FaturaDto;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
public class Cartao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    private String numero;

    @NotBlank
    private String email;

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Fatura> faturas = new ArrayList<>();

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Transacao> transacoes = new ArrayList<>();

    @Deprecated
    public Cartao(){}

    public Cartao(String numero, String email) {
        this.numero = numero;
        this.email = email;
    }

    public FaturaDto buscaFaturasPorPeriodo(int mes, int ano){

        var fatura = this.faturas
                    .stream()
                    .filter(f -> f.getMes().getValue() == mes && f.getGeradaEm().getYear() == ano)
                    .findAny()
                    .orElseThrow();

        return new FaturaDto(fatura);

    }

    public BigDecimal calculaSaldo(IntegracaoApiCartoes integracaoApiCartoes){

        var limite =
                Objects.requireNonNull(integracaoApiCartoes.buscarLimiteCartao(this.numero).getBody()).getLimite();

        var fatura =
                this.faturas
                        .stream()
                        .filter(f -> f.getMes() == LocalDateTime.now().getMonth())
                        .findAny()
                        .orElseThrow();

        return limite.subtract(fatura.calculaEbuscaTotal());

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Fatura> getFaturas() {
        return faturas;
    }

    public void setFaturas(List<Fatura> faturas) {
        this.faturas = faturas;
    }

    public List<Transacao> getTransacoes() {
        return transacoes;
    }

    public void setTransacoes(List<Transacao> transacoes) {
        this.transacoes = transacoes;
    }
}
