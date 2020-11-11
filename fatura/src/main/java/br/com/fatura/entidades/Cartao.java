package br.com.fatura.entidades;

import br.com.fatura.dtos.FaturaDto;
import br.com.fatura.dtos.TransacaoDto;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.*;


@Entity
public class Cartao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotBlank
    @Column(unique = true)
    private String numero;

    @NotBlank
    @Column(unique = true)
    private String email;

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Fatura> faturas = new ArrayList<>();

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Transacao> transacoes = new ArrayList<>();

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<CartaoVirtual> cartoesVirtuais = new ArrayList<>();

    @Deprecated
    public Cartao(){}

    public Cartao(String numero, String email) {
        this.numero = numero;
        this.email = email;
    }


    public BigDecimal calculaSaldo(IntegracaoApiCartoes integracaoApiCartoes){

        var limite =
                Objects.requireNonNull(integracaoApiCartoes.buscarLimiteCartao(this.numero).getBody()).getLimite();

        return limite.subtract(this.faturas.get(0).calculaEbuscaTotal());

    }

    public List<TransacaoDto> retornarTransacoes(){

        var transacoesDtos = new ArrayList<TransacaoDto>();

        this.transacoes.forEach(transacao -> transacoesDtos.add(new TransacaoDto(transacao)));

        transacoesDtos.sort(Comparator.comparing(TransacaoDto::getEfetivadaEm));

        Collections.reverse(transacoesDtos);

        return transacoesDtos.subList(0,10);

    }


    public String getNumero() {
        return numero;
    }

    public String getEmail() {
        return email;
    }

}
