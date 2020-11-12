package br.com.fatura.entidades;

import br.com.fatura.dtos.AlteraVencimentoRequest;
import br.com.fatura.dtos.RecebeTransacao;
import br.com.fatura.dtos.TransacaoDto;
import br.com.fatura.entidades.enums.StatusAprovacao;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Entity
public class Fatura {

    /* pontos de dificuldade de entendimento -> 12 */

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    /* @complexidade (1) - classe específica do projeto */
    @OneToMany(mappedBy = "fatura", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private List<Transacao> transacoes = new ArrayList<>();

    /* @complexidade (1) - classe específica do projeto */
    @OneToMany(mappedBy = "fatura", cascade = CascadeType.MERGE)
    private List<Parcela> parcelas = new ArrayList<>();

    /* @complexidade (1) - classe específica do projeto */
    @OneToMany(mappedBy = "fatura", cascade = CascadeType.MERGE)
    private List<Renegociacao> renegociacoes = new ArrayList<>();

    /* @complexidade (1) - classe específica do projeto */
    @ManyToOne
    private Cartao cartao;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private Month mes;

    @NotNull
    private LocalDateTime geradaEm = LocalDateTime.now();

    @NotNull
    private BigDecimal total = new BigDecimal(0);

    @NotNull
    private LocalDateTime vencimento = LocalDateTime.now().withDayOfMonth(30);

    /* @complexidade (1) - classe específica do projeto */
    private StatusAprovacao statusAlteracaoVencimento;

    @Deprecated
    public Fatura(){}

    public Fatura(Cartao cartao, RecebeTransacao recebeTransacao) {
        this.cartao = cartao;
        this.mes = defineMesDaFatura(recebeTransacao.getEfetivadaEm());
    }

    /* @complexidade (1) - método específico */
    public void alteraVencimento(String novoVencimento){
        this.vencimento = this.vencimento.plusDays(Long.parseLong(novoVencimento));
    }

    /* @complexidade (1) - método específico */
    public void adicionarTransacao(Transacao transacao){
        this.transacoes.add(transacao);
    }

    /* @complexidade (1) - método específico */
    public Month defineMesDaFatura(String efetivadaEm){

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss")
                .withZone(ZoneId.of("UTC"));

        return LocalDateTime.parse(efetivadaEm, formatter).getMonth();

    }

    /* @complexidade (1) - método específico */
    public List<TransacaoDto> toDtoList(){

        List<TransacaoDto> transacaoDtos = new ArrayList<>();

        this.transacoes.forEach(transacao -> { transacaoDtos.add(new TransacaoDto(transacao)); });

        return transacaoDtos
                .stream()
                .filter(transacaoDto -> transacaoDto.getEfetivadaEm().getMonth() == LocalDateTime.now().getMonth())
                .collect(Collectors.toList());

    }

    /* @complexidade (1) - método específico */
    public List<TransacaoDto> toDtoListPorPeriodo(int mes, int ano){

        List<TransacaoDto> transacaoDtos = new ArrayList<>();

        this.transacoes.forEach(transacao -> { transacaoDtos.add(new TransacaoDto(transacao)); });

        return transacaoDtos
                .stream()
                .filter(t -> t.getEfetivadaEm().getMonth().getValue() == mes &&
                        t.getEfetivadaEm().getYear() == ano).collect(Collectors.toList());

    }

    /* @complexidade (1) - método específico */
    public BigDecimal calculaEbuscaTotal(){

        return toDtoList().stream()
                .map(TransacaoDto::getValor)
                .reduce(BigDecimal.valueOf(0), BigDecimal::add);

    }

    /* @complexidade (1) - método específico */
    public void avisaLegadoAtualizaVencimento(AlteraVencimentoRequest alteraVencimentoRequest,
                                              IntegracaoApiCartoes integracaoApiCartoes, String numeroCartao,
                                              EntityManager entityManager){

        this.alteraVencimento(alteraVencimentoRequest.getDia());

        var resultadoAviso = Objects.requireNonNull(integracaoApiCartoes.avisaAlteracaoVencimento
                (numeroCartao, alteraVencimentoRequest).getBody()).getResultado();

        this.setStatusAlteracaoVencimento(StatusAprovacao.valueOf(resultadoAviso));

        entityManager.merge(this);

    }

    public void setStatusAlteracaoVencimento(StatusAprovacao statusAlteracaoVencimento) {
        this.statusAlteracaoVencimento = statusAlteracaoVencimento;
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

}
