package br.com.fatura.resource;

import br.com.fatura.dtos.RenegociacaoRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.RenegociacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@RestController
@RequestMapping("/api/faturas/")
public class RenegociaFaturaResource {


    /* pontos de dificuldade de entendimento -> 8 pontos */


    /* @complexidade */
    private final FaturaRepository faturaRepository;

    /* @complexidade */
    private final RenegociacaoRepository renegociacaoRepository;

    /* @complexidade */
    private final IntegracaoApiCartoes integracaoApiCartoes;

    private final EntityManager entityManager;


    public RenegociaFaturaResource(FaturaRepository faturaRepository, RenegociacaoRepository renegociacaoRepository,
                                   IntegracaoApiCartoes integracaoApiCartoes, EntityManager entityManager) {

        this.faturaRepository = faturaRepository;
        this.renegociacaoRepository = renegociacaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
        this.entityManager = entityManager;

    }

    @Transactional
    @PostMapping("renegociacoes/{numeroCartao}/{identificadorFatura}")
    public ResponseEntity<?> renegocia(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                       @RequestBody RenegociacaoRequest renegociacaoRequest, UriComponentsBuilder uriComponentsBuilder){

        /* @complexidade */
        var fatura = faturaRepository.findById(identificadorFatura);

        /* @complexidade */
        if(fatura.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        /* @complexidade */
        var renegociacao =
                renegociacaoRequest.toModel(fatura.get());

        /* @complexidade */
        renegociacaoRepository.save(renegociacao);

        /* @complexidade */
        renegociacao.avisaLegadoAtualizaStatus(integracaoApiCartoes,numeroCartao, renegociacaoRequest, entityManager);

        return ResponseEntity.created(uriComponentsBuilder
                .buildAndExpand("/api/faturas/renegociacoes/{numeroCartao}/{identificadorFatura}", numeroCartao, identificadorFatura)
                .toUri()).body(renegociacao);

    }

}
