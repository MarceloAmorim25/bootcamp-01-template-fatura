package br.com.fatura.resource;

import br.com.fatura.dtos.FaturaDto;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/faturas")
public class ConsultaFaturaResource {

    /* pontos de dificuldade de entendimento -> 8 pontos */

    /* @complexidade - acoplamento contextual */
    private final FaturaRepository faturaRepository;

    /* @complexidade - acoplamento contextual */
    private final CartaoRepository cartaoRepository;

    private final Logger logger = LoggerFactory.getLogger(Fatura.class);


    public ConsultaFaturaResource(FaturaRepository faturaRepository, CartaoRepository cartaoRepository) {

        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;

    }


    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> buscaAtual(@PathVariable String numeroCartao){


        /* @complexidade + @complexidade */
        var cartao = cartaoRepository.findByNumero(numeroCartao);
        if(cartao.isEmpty()){
            logger.info("[INFO] Cartão relativo à fatura não foi encontrado");
            return ResponseEntity.notFound().build();
        }


        /* @complexidade */
        var fatura = faturaRepository.findByCartao(cartao.get()).orElseThrow();
        logger.info("[INFO] Fatura foi devidamente retornada ao cliente");

                                    /* @complexidade */
        return ResponseEntity.ok(new FaturaDto(fatura));

    }

    @GetMapping("/{numeroCartao}/{mes}/{ano}")
    public ResponseEntity<?> consultaAnteriores(@PathVariable String numeroCartao,
                                                @PathVariable int mes, @PathVariable int ano){

        /* @complexidade + @complexidade */
        var cartao = cartaoRepository.findByNumero(numeroCartao);
        if(cartao.isEmpty()){
            return ResponseEntity.notFound().build();
        }


        var fatura = faturaRepository.findByCartao(cartao.get());

        return ResponseEntity.ok(new FaturaDto(fatura.get(), mes, ano));

    }
}
