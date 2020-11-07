package br.com.fatura.resource;

import br.com.fatura.dtos.FaturaDto;
import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/faturas/{numeroCartao}")
public class FaturaResource {


    private final FaturaRepository faturaRepository;

    private final CartaoRepository cartaoRepository;


    public FaturaResource(FaturaRepository faturaRepository, CartaoRepository cartaoRepository) {
        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
    }


    @GetMapping
    public ResponseEntity<?> busca(@PathVariable String numeroCartao){

        Optional<Cartao> cartao = cartaoRepository.findByNumero(numeroCartao);

        Optional<Fatura> fatura = faturaRepository.findByCartao(cartao.get());

        return ResponseEntity.ok(new FaturaDto(fatura.get()));

    }
}
