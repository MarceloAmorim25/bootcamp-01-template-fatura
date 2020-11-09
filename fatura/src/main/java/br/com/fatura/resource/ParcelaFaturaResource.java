package br.com.fatura.resource;

import br.com.fatura.dtos.ParcelaRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.ParcelaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/faturas/")
public class ParcelaFaturaResource {


    /* pontos de dificuldade de entendimento -> 6 pontos */


    /* @complexidade */
    private final FaturaRepository faturaRepository;

    /* @complexidade */
    private final IntegracaoApiCartoes integracaoApiCartoes;

    /* @complexidade */
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
                                     @RequestBody ParcelaRequest parcelaRequest){

        /* @complexidade */
        var fatura =
                faturaRepository.findById(identificadorFatura).orElseThrow();

        System.out.println("programa passou por aqui");

        /* @complexidade */
        var parcela = parcelaRequest.toModel(fatura);

        parcelaRepository.save(parcela);

        /* @complexidade */
        parcela.avisaLegadoEAtualizaStatus(integracaoApiCartoes, numeroCartao, parcelaRequest, entityManager);

        return ResponseEntity.ok().build();

    }

}
