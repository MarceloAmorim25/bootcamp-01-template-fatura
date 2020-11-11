package br.com.fatura.resource;

import br.com.fatura.dtos.RenegociacaoRequest;
import br.com.fatura.entidades.Parcela;
import br.com.fatura.entidades.Renegociacao;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.RenegociacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(Renegociacao.class);


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


        /* @complexidade + @complexidade */
        var fatura = faturaRepository.findById(identificadorFatura);
        if(fatura.isEmpty()){
            logger.info("[INFO] Fatura não encontrada pelo identificador");
            return ResponseEntity.notFound().build();
        }


        /* @complexidade + @complexidade */
        var renegociacao = renegociacaoRequest.toModel(fatura.get());
        renegociacaoRepository.save(renegociacao);


        /* @complexidade */
        renegociacao.avisaLegadoAtualizaStatus(integracaoApiCartoes, numeroCartao, renegociacaoRequest, entityManager);
        logger.info("[INFO] Solicitação de renegociação registrada");


        return ResponseEntity.created(uriComponentsBuilder
                .buildAndExpand("/api/faturas/renegociacoes/{numeroCartao}/{identificadorFatura}", numeroCartao, identificadorFatura)
                .toUri()).body(renegociacao);

    }
}
