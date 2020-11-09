package br.com.fatura.resource;

import br.com.fatura.dtos.CartaoVirtualRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.CartaoVirtualRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
public class CartaoVirtualResource {

    private final CartaoVirtualRepository cartaoVirtualRepository;

    private final CartaoRepository cartaoRepository;

    private final IntegracaoApiCartoes integracaoApiCartoes;

    public CartaoVirtualResource(CartaoVirtualRepository cartaoVirtualRepository, CartaoRepository cartaoRepository,
                                 IntegracaoApiCartoes integracaoApiCartoes) {
        this.cartaoVirtualRepository = cartaoVirtualRepository;
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
    }

    @PostMapping("/api/cartoes-virtuais/{numeroCartao}")
    public ResponseEntity<?> gera(@RequestBody CartaoVirtualRequest cartaoVirtualRequest, @PathVariable
                                  String numeroCartao, UriComponentsBuilder uriComponentsBuilder){

        var cartao = cartaoVirtualRequest.toModel(cartaoRepository);

        cartao.defineLimite(integracaoApiCartoes);

        cartaoVirtualRepository.save(cartao);

        return ResponseEntity.created(uriComponentsBuilder
                .buildAndExpand("/api/cartoes-virtuais/{numeroCartao}", numeroCartao)
                .toUri()).build();

    }
}
