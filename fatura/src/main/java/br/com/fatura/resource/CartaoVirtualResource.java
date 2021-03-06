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

import javax.validation.Valid;

@RestController
public class CartaoVirtualResource {

    /* pontos de dificuldade de entendimento -> 9 pontos */

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
    public ResponseEntity<?> gera(@RequestBody @Valid CartaoVirtualRequest cartaoVirtualRequest, @PathVariable
                                  String numeroCartao, UriComponentsBuilder uriComponentsBuilder){

        /* @complexidade + @complexidade */
        var cartao = cartaoRepository.findByNumero(numeroCartao);
        if(cartao.isEmpty()){
            logger.info("[CARTÃO VIRTUAL] Número do cartão não encontrado.");
            return ResponseEntity.notFound().build();
        }

        /* @complexidade + @complexidade + @complexidade*/
        var cartaoVirtual = cartaoVirtualRequest.toModel(cartao.get());
        cartaoVirtual.defineLimite(integracaoApiCartoes);
        cartaoVirtualRepository.save(cartaoVirtual);
        logger.info("[CARTÃO VIRTUAL] Cartão Virtual criado com sucesso.");


        return ResponseEntity.created(uriComponentsBuilder
                .buildAndExpand("/api/cartoes-virtuais/{numeroCartao}", numeroCartao)
                .toUri()).build();

    }
}
