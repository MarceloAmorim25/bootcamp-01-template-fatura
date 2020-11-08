package br.com.fatura.resource;

import br.com.fatura.dtos.AlteraVencimentoRequest;
import br.com.fatura.dtos.FaturaDto;
import br.com.fatura.dtos.ParcelaRequest;
import br.com.fatura.dtos.RenegociacaoRequest;
import br.com.fatura.entidades.enums.StatusAprovacao;
import br.com.fatura.integracoes.IntegracaoApiCartoes;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.ParcelaRepository;
import br.com.fatura.repository.RenegociacaoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Objects;


@RestController
@RequestMapping("/api/faturas")
public class FaturaResource {


    private final FaturaRepository faturaRepository;

    private final CartaoRepository cartaoRepository;

    private final ParcelaRepository parcelaRepository;

    private final RenegociacaoRepository renegociacaoRepository;

    private final IntegracaoApiCartoes integracaoApiCartoes;

    private final EntityManager entityManager;


    public FaturaResource(FaturaRepository faturaRepository, CartaoRepository cartaoRepository,
                          IntegracaoApiCartoes integracaoApiCartoes, ParcelaRepository parcelaRepository,
                          RenegociacaoRepository renegociacaoRepository, EntityManager entityManager) {
        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
        this.parcelaRepository = parcelaRepository;
        this.renegociacaoRepository = renegociacaoRepository;
        this.entityManager = entityManager;
    }


    @GetMapping("/{numeroCartao}")
    public ResponseEntity<?> buscaAtual(@PathVariable String numeroCartao){

        var cartao =
                cartaoRepository.findByNumero(numeroCartao);

        if(cartao.isPresent()){

            var fatura =
                    faturaRepository.findByCartao(cartao.get());

            return ResponseEntity.ok(new FaturaDto(fatura.get()));

        }

        return ResponseEntity.notFound().build();

    }

    @GetMapping("/{numeroCartao}/{mes}/{ano}")
    public ResponseEntity<?> consultaAnteriores(@PathVariable String numeroCartao,
                                                @PathVariable int mes, @PathVariable int ano){

        var cartao = cartaoRepository.findByNumero(numeroCartao);

        if(cartao.isPresent()){

            var fatura = cartao.get().buscaFaturasPorPeriodo(mes, ano);

            return ResponseEntity.ok(fatura);

        }

        return ResponseEntity.notFound().build();

    }

    @Transactional
    @PostMapping("/{numeroCartao}/parcelas/{identificadorFatura}")
    public ResponseEntity<?> parcela(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                     @RequestBody ParcelaRequest parcelaRequest){

        var fatura = faturaRepository.findById(identificadorFatura);

        if(fatura.isPresent()){

            var parcela = parcelaRequest.toModel(fatura.get());

            parcelaRepository.save(parcela);

            parcela.avisaLegadoEAtualizaStatus(integracaoApiCartoes, numeroCartao, parcelaRequest, entityManager);

            return ResponseEntity.ok().build();

        }

        return ResponseEntity.notFound().build();

    }


    @Transactional
    @PostMapping("/{numeroCartao}/renegociacoes/{identificadorFatura}")
    public ResponseEntity<?> renegocia(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                       @RequestBody RenegociacaoRequest renegociacaoRequest){

        var fatura = faturaRepository.findById(identificadorFatura);

        if(fatura.isPresent()){

            var renegociacao = renegociacaoRequest.toModel(fatura.get());

            renegociacaoRepository.save(renegociacao);

            renegociacao.avisaLegadoAtualizaStatus(integracaoApiCartoes,numeroCartao, renegociacaoRequest, entityManager);

            return ResponseEntity.ok().build();

        }

        return ResponseEntity.notFound().build();

    }

    @Transactional
    @PutMapping("/{numeroCartao}/vencimentos/{identificadorFatura}")
    public ResponseEntity<?> alteraVencimento(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                              @RequestBody AlteraVencimentoRequest alteraVencimentoRequest){

        var fatura =
                faturaRepository.findById(identificadorFatura).orElseThrow();

        fatura.avisaLegadoAtualizaVencimento(alteraVencimentoRequest, integracaoApiCartoes, numeroCartao, entityManager);

        return ResponseEntity.ok().build();

    }

}
