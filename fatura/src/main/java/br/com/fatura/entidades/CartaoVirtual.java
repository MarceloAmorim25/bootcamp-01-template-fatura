package br.com.fatura.entidades;

import br.com.fatura.entidades.enums.StatusCartaoVirtual;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
public class CartaoVirtual {

    /* regras ->
        - validade = 48 horas
        - deve realizar uma unica compra
    * */

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    private BigDecimal limite;

    @NotBlank
    private String numero;

    @ManyToOne
    private Cartao cartao;

    @Future
    @NotNull
    private final LocalDateTime validade  = LocalDateTime.now().plusDays(2);

    /* ao realizar uma compra, o status ficara como invalido */
    private StatusCartaoVirtual realizouCompra;

    @Deprecated
    public CartaoVirtual(){}

    public CartaoVirtual(Cartao cartao) {
        this.numero = UUID.randomUUID().toString();
        this.cartao = cartao;
    }

    public void defineLimite(IntegracaoApiCartoes integracaoApiCartoes){

        this.limite =
                Objects.requireNonNull(integracaoApiCartoes.buscarLimiteCartao(this.cartao.getNumero()).getBody()).getLimite();

    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

}
