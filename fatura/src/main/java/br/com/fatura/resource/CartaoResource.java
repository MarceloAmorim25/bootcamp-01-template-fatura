package br.com.fatura.resource;

import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/cartoes")
public class CartaoResource {

    /* pontos de dificuldade de entendimento -> 4 pontos */

    /* @complexidade */
    private final CartaoRepository cartaoRepository;

    /* @complexidade */
    private final IntegracaoApiCartoes integracaoApiCartoes;


    public CartaoResource(CartaoRepository cartaoRepository,
                          IntegracaoApiCartoes integracaoApiCartoes) {
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
    }


    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> consultaSaldo(@PathVariable String numeroCartao){

        /* @complexidade */
        var cartao = cartaoRepository.findByNumero(numeroCartao).orElseThrow();

        /* @complexidade */
        var saldo =
                cartao.calculaSaldo(integracaoApiCartoes);

        return ResponseEntity.ok(saldo);

    }
}
