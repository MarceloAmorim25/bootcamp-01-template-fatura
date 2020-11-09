package br.com.fatura.service;

import br.com.fatura.dtos.RecebeTransacao;
import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.TransacaoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class ProcessaTransacaoService {

    /* pontos de dificuldade de entendimento -> 8 pontos */

    private final Logger logger = LoggerFactory.getLogger(Fatura.class);


    public Cartao buscaCartao(RecebeTransacao transacaoRecebida, CartaoRepository cartaoRepository){

        String numeroCartao = transacaoRecebida.getCartao().getId();
        String emailCartao = transacaoRecebida.getCartao().getEmail();

        /* @complexidade */
        var cartao = cartaoRepository.findByNumero(numeroCartao);

        /* @complexidade */
        if(cartao.isPresent()){
            return cartao.orElseThrow();
        }

        /* @complexidade */
        var novoCartao = new Cartao(numeroCartao, emailCartao);

        cartaoRepository.save(novoCartao);

        return novoCartao;

    }


    public Fatura buscaFatura(RecebeTransacao transacaoRecebida, Cartao cartao,
                            FaturaRepository faturaRepository){

        /* @complexidade */
        var fatura = faturaRepository.findByCartao(cartao);

        /* @complexidade */
        if(fatura.isPresent()){
            return fatura.get();
        }

        /* @complexidade */
        var novaFatura = new Fatura(cartao, transacaoRecebida);

        faturaRepository.save(novaFatura);

        logger.info("Fatura foi buscada com sucesso. Mês referente = {}", fatura.get().getMes());

        return novaFatura;

    }

    public void registraTransacao(RecebeTransacao transacaoRecebida, Cartao cartao,
                                  TransacaoRepository transacaoRepository, Fatura fatura){

        /* @complexidade */
        var novaTransacao = transacaoRecebida.toModel(cartao, fatura);
        transacaoRepository.save(novaTransacao);

        /* @complexidade */
        fatura.adicionarTransacao(novaTransacao);

        logger.info("transação no valor de {} foi registrada", novaTransacao.getValor());

    }

}
