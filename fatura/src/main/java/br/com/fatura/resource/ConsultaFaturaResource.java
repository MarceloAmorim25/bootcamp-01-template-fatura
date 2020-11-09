package br.com.fatura.resource;

import br.com.fatura.dtos.AlteraVencimentoRequest;
import br.com.fatura.dtos.FaturaDto;
import br.com.fatura.dtos.ParcelaRequest;
import br.com.fatura.dtos.RenegociacaoRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.ParcelaRepository;
import br.com.fatura.repository.RenegociacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;



@RestController
@RequestMapping("/api/faturas")
public class ConsultaFaturaResource {


    /* @complexidade */
    private final FaturaRepository faturaRepository;

    /* @complexidade */
    private final CartaoRepository cartaoRepository;


    public ConsultaFaturaResource(FaturaRepository faturaRepository, CartaoRepository cartaoRepository) {

        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;

    }


    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> buscaAtual(@PathVariable String numeroCartao){

        /* @complexidade */
        var cartao =
                cartaoRepository.findByNumero(numeroCartao).orElseThrow();

        /* @complexidade */
        var fatura =
                faturaRepository.findByCartao(cartao).orElseThrow();

                                    /* @complexidade */
        return ResponseEntity.ok(new FaturaDto(fatura));

    }

    @GetMapping("/{numeroCartao}/{mes}/{ano}")
    public ResponseEntity<?> consultaAnteriores(@PathVariable String numeroCartao,
                                                @PathVariable int mes, @PathVariable int ano){

        /* @complexidade */
        var cartao =
                cartaoRepository.findByNumero(numeroCartao).orElseThrow();

        /* @complexidade */
        var fatura = cartao.buscaFaturasPorPeriodo(mes, ano);

        return ResponseEntity.ok(fatura);

    }

}
