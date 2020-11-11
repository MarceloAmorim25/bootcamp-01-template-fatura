package br.com.fatura.resource;

import br.com.fatura.dtos.CartaoVirtualRequest;
import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.CartaoVirtual;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.CartaoVirtualRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CartaoVirtualResource {

    /* pontos de dificuldade de entendimento -> 7 pontos */

    /* @complexidade - acoplamento contextual */
    private final CartaoVirtualRepository cartaoVirtualRepository;

    /* @complexidade - acoplamento contextual */
    private final CartaoRepository cartaoRepository;

    /* @complexidade - acoplamento contextual */
    private final IntegracaoApiCartoes integracaoApiCartoes;

    private final Logger logger = LoggerFactory.getLogger(CartaoVirtual.class);


    public CartaoVirtualResource(CartaoVirtualRepository cartaoVirtualRepository, CartaoRepository cartaoRepository,
                                 IntegracaoApiCartoes integracaoApiCartoes) {
        this.cartaoVirtualRepository = cartaoVirtualRepository;
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
    }

    @PostMapping("/api/cartoes-virtuais/{numeroCartao}")     /* @complexidade */
    public ResponseEntity<?> gera(@RequestBody CartaoVirtualRequest cartaoVirtualRequest, @PathVariable
                                  String numeroCartao, UriComponentsBuilder uriComponentsBuilder){

        /* @complexidade + @complexidade + @complexidade*/
        var cartao = cartaoVirtualRequest.toModel(cartaoRepository);
        cartao.defineLimite(integracaoApiCartoes);
        cartaoVirtualRepository.save(cartao);

        logger.info("[INFO] Cartão Virtual criado com sucesso.");

        return ResponseEntity.created(uriComponentsBuilder
                .buildAndExpand("/api/cartoes-virtuais/{numeroCartao}", numeroCartao)
                .toUri()).build();

    }
}
