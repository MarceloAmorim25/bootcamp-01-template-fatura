package br.com.fatura.resource;

import br.com.fatura.dtos.AlteraVencimentoRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;


@RestController
@RequestMapping("/api/faturas")
public class AlteraVencimentoFaturaResource {


    /* pontos de dificuldade de entendimento -> 6 pontos */


    /* @complexidade - acoplamento contextual */
    private final FaturaRepository faturaRepository;

    /* @complexidade - acoplamento contextual */
    private final IntegracaoApiCartoes integracaoApiCartoes;

    /* @complexidade - acoplamento contextual */
    private final EntityManager entityManager;


    public AlteraVencimentoFaturaResource(FaturaRepository faturaRepository, IntegracaoApiCartoes integracaoApiCartoes,
                                          EntityManager entityManager) {
        this.faturaRepository = faturaRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
        this.entityManager = entityManager;
    }


    @Transactional
    @PutMapping("/{numeroCartao}/vencimentos/{identificadorFatura}")
    public ResponseEntity<?> alteraVencimento(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                              @RequestBody AlteraVencimentoRequest alteraVencimentoRequest){

        /* @complexidade */
        var fatura =
                faturaRepository.findById(identificadorFatura);

        /* @complexidade */
        if(fatura.isEmpty()){
            return ResponseEntity.notFound().build();
        }

        /* @complexidade */
        fatura.get().avisaLegadoAtualizaVencimento(alteraVencimentoRequest, integracaoApiCartoes, numeroCartao, entityManager);

        return ResponseEntity.ok().build();

    }
}
