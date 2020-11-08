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


    private final CartaoRepository cartaoRepository;

    private final FaturaRepository faturaRepository;

    private final IntegracaoApiCartoes integracaoApiCartoes;


    public CartaoResource(CartaoRepository cartaoRepository,
                          IntegracaoApiCartoes integracaoApiCartoes, FaturaRepository faturaRepository) {
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
        this.faturaRepository = faturaRepository;
    }


    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> consultaSaldo(@PathVariable String numeroCartao){

        var cartao = cartaoRepository.findByNumero(numeroCartao);

        var limite =
                Objects.requireNonNull(integracaoApiCartoes.buscarLimiteCartao(numeroCartao).getBody()).getLimite();

        if(cartao.isPresent()){

            var fatura =
                    faturaRepository.findByCartao(cartao.get()).orElseThrow();

            var saldo = limite.subtract(fatura.calculaEbuscaTotal());

            return ResponseEntity.ok(saldo);

        }

        return ResponseEntity.notFound().build();

    }
}
