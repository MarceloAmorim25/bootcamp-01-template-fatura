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

import java.util.Objects;


@RestController
@RequestMapping("/api/faturas")
public class FaturaResource {


    private final FaturaRepository faturaRepository;

    private final CartaoRepository cartaoRepository;

    private final ParcelaRepository parcelaRepository;

    private final RenegociacaoRepository renegociacaoRepository;

    private final IntegracaoApiCartoes integracaoApiCartoes;


    public FaturaResource(FaturaRepository faturaRepository, CartaoRepository cartaoRepository,
                          IntegracaoApiCartoes integracaoApiCartoes, ParcelaRepository parcelaRepository,
                          RenegociacaoRepository renegociacaoRepository) {
        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
        this.integracaoApiCartoes = integracaoApiCartoes;
        this.parcelaRepository = parcelaRepository;
        this.renegociacaoRepository = renegociacaoRepository;
    }


    @GetMapping
    public ResponseEntity<?> buscaAtual(@PathVariable String numeroCartao){

        var cartao = cartaoRepository.findByNumero(numeroCartao);

        var fatura = faturaRepository.findByCartao(cartao.get());

        return ResponseEntity.ok(new FaturaDto(fatura.get()));

    }

    @PostMapping("/{numeroCartao}/parcelas/{identificadorFatura}")
    public ResponseEntity<?> parcela(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                     @RequestBody ParcelaRequest parcelaRequest){

        var parcela = parcelaRequest.toModel();

        parcelaRepository.save(parcela);

        var respostaApiCartoes =
                Objects.requireNonNull(integracaoApiCartoes.solicitarParcelamento(numeroCartao, parcelaRequest).getBody()).getResultado();

        parcela.setStatus(StatusAprovacao.valueOf(respostaApiCartoes));

        return ResponseEntity.ok().build();

    }

    @PostMapping("/{numeroCartao}/renegociacoes/{identificadorFatura}")
    public ResponseEntity<?> renegocia(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                       @RequestBody RenegociacaoRequest renegociacaoRequest){

        var renegociacao = renegociacaoRequest.toModel();

        renegociacaoRepository.save(renegociacao);

        var respostaApiCartoes =
                Objects.requireNonNull(integracaoApiCartoes.renegociacaoFatura(numeroCartao, renegociacaoRequest).getBody()).getResultado();

        renegociacao.setStatus(StatusAprovacao.valueOf(respostaApiCartoes));

        return ResponseEntity.ok().build();

    }

    @PutMapping("/{numeroCartao}/vencimentos/{identificadorFatura}")
    public ResponseEntity<?> alteraVencimento(@PathVariable String numeroCartao, @PathVariable String identificadorFatura,
                                              @RequestBody AlteraVencimentoRequest alteraVencimentoRequest){

        var fatura =
                faturaRepository.findById(identificadorFatura).orElseThrow();

        fatura.alteraVencimento(alteraVencimentoRequest.getDia());

        var resultadoAviso = Objects.requireNonNull(integracaoApiCartoes.avisaAlteracaoVencimento
                (numeroCartao, alteraVencimentoRequest).getBody()).getResultado();

        fatura.setStatusAlteracaoVencimento(StatusAprovacao.valueOf(resultadoAviso));

        faturaRepository.save(fatura);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/{numeroCartao}/{identificadorFatura}/{mes}/{ano}")
    public ResponseEntity<?> consultaAnteriores(@PathVariable String numeroCartao,
                                                @PathVariable Integer mes, @PathVariable Integer ano){

        var cartao =
                cartaoRepository.findByNumero(numeroCartao).orElseThrow();

        var faturas = cartao.getFaturas();

        var fatura = faturas
                .stream()
                .filter(f -> f.getMes().getValue() == mes && f.getGeradaEm().getYear() == ano)
                .findFirst().orElseThrow();

        return ResponseEntity.ok(new FaturaDto(fatura));

    }
}
