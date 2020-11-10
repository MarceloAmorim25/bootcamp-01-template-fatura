package br.com.fatura.resource;

import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/cartoes")
public class CartaoResource {

    /* pontos de dificuldade de entendimento -> 5 pontos */

    /* @complexidade - acoplamento contextual */
    private final CartaoRepository cartaoRepository;

    /* @complexidade - acoplamento contextual */
    private final IntegracaoApiCartoes integracaoApiCartoes;

    private final Logger logger = LoggerFactory.getLogger(Cartao.class);


    public CartaoResource(CartaoRepository cartaoRepository,
                          IntegracaoApiCartoes integracaoApiCartoes) {
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
    }


    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> consultaSaldo(@PathVariable String numeroCartao){

        /* @complexidade */
        var cartao = cartaoRepository.findByNumero(numeroCartao);

        /* @complexidade */
        if(cartao.isEmpty()){

            logger.info("[INFO] Cartão buscado não foi encontrado");

            return ResponseEntity.notFound().build();
        }

        /* @complexidade */
        var saldo = cartao.get().calculaSaldo(integracaoApiCartoes);

        logger.info("[INFO] Cartão foi encontrado e saldo retornado");

        return ResponseEntity.ok(saldo);

    }
}
