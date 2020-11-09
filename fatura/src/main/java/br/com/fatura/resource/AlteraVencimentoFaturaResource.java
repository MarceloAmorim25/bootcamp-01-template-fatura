package br.com.fatura.resource;

import br.com.fatura.dtos.AlteraVencimentoRequest;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.FaturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public class AlteraVencimentoFaturaResource {


    private final FaturaRepository faturaRepository;

    private final IntegracaoApiCartoes integracaoApiCartoes;

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
                faturaRepository.findById(identificadorFatura).orElseThrow();

        /* @complexidade */
        fatura.avisaLegadoAtualizaVencimento(alteraVencimentoRequest, integracaoApiCartoes, numeroCartao, entityManager);

        return ResponseEntity.ok().build();

    }


}
