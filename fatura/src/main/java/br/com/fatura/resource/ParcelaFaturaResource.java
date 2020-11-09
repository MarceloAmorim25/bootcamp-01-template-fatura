package br.com.fatura.resource;

import br.com.fatura.dtos.ParcelaRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.ParcelaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/faturas/")
public class ParcelaFaturaResource {


    /* pontos de dificuldade de entendimento -> 8 pontos */


    /* @complexidade - acoplamento contextual */
    private final FaturaRepository faturaRepository;

    /* @complexidade - acoplamento contextual */
    private final IntegracaoApiCartoes integracaoApiCartoes;

    /* @complexidade - acoplamento contextual */
    private final ParcelaRepository parcelaRepository;

    private final EntityManager entityManager;


    public ParcelaFaturaResource(FaturaRepository faturaRepository, IntegracaoApiCartoes integracaoApiCartoes,
                                 ParcelaRepository parcelaRepository, EntityManager entityManager) {

        this.faturaRepository = faturaRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
        this.parcelaRepository = parcelaRepository;
        this.entityManager = entityManager;

    }

    @Transactional
    @PostMapping("parcelas/{numeroCartao}/{identificadorFatura}")
    public ResponseEntity<?> parcela(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                     @RequestBody ParcelaRequest parcelaRequest, UriComponentsBuilder uriComponentsBuilder){

        /* @complexidade */
        var fatura = faturaRepository.findById(identificadorFatura);

        /* @complexidade */
        if(fatura.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        /* @complexidade */
        var parcela = parcelaRequest.toModel(fatura.get());

        /* @complexidade */
        parcelaRepository.save(parcela);

        /* @complexidade */
        parcela.avisaLegadoEAtualizaStatus(integracaoApiCartoes, numeroCartao, parcelaRequest, entityManager);

        return ResponseEntity.created(uriComponentsBuilder
                        .buildAndExpand("/api/faturas/parcelas/{numeroCartao}/{identificadorFatura}", numeroCartao, identificadorFatura)
                        .toUri()).body(parcela);

    }
}
