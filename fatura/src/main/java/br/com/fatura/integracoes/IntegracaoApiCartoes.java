package br.com.fatura.integracoes;

import br.com.fatura.dtos.LimiteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(url = "${cartao.host}/", name = "integracaoCartao")
public interface IntegracaoApiCartoes {

    @GetMapping("{numeroCartao}")
    ResponseEntity<LimiteResponse> buscarLimiteCartao(@PathVariable String numeroCartao);

}
