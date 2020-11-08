package br.com.fatura.integracoes;

import br.com.fatura.dtos.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(url = "${cartao.host}/", name = "integracaoCartao")
public interface IntegracaoApiCartoes {

    @GetMapping("{numeroCartao}")
    ResponseEntity<LimiteResponse> buscarLimiteCartao(@PathVariable String numeroCartao);

    @PostMapping("{numeroCartao}/parcelas")
    ResponseEntity<SolicitacaoParcelamentoResponse> solicitarParcelamento
            (@PathVariable String numeroCartao, @RequestBody ParcelaRequest parcelaRequest);

    @PostMapping("{numeroCartao}/renegociacoes")
    ResponseEntity<SolicitacaoParcelamentoResponse> renegociacaoFatura
            (@PathVariable String numeroCartao, @RequestBody RenegociacaoRequest renegociacaoRequest);

    @PostMapping("{numeroCartao}/vencimentos")
    ResponseEntity<SolicitacaoParcelamentoResponse> avisaAlteracaoVencimento
            (@PathVariable String numeroCartao, @RequestBody AlteraVencimentoRequest alteraVencimentoRequest);

}
