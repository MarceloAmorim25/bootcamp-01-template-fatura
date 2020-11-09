package br.com.fatura.resource;

import br.com.fatura.dtos.RenegociacaoRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.RenegociacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class RenegociaFaturaResource {

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
    @PostMapping("/{numeroCartao}/renegociacoes/{identificadorFatura}")
    public ResponseEntity<?> renegocia(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                       @RequestBody RenegociacaoRequest renegociacaoRequest){

        /* @complexidade */
        var fatura = faturaRepository.findById(identificadorFatura).orElseThrow();

        /* @complexidade */
        var renegociacao =
                renegociacaoRequest.toModel(fatura);

        renegociacaoRepository.save(renegociacao);

        /* @complexidade */
        renegociacao.avisaLegadoAtualizaStatus(integracaoApiCartoes,numeroCartao, renegociacaoRequest, entityManager);

        return ResponseEntity.ok().build();

    }

}
