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


    private final Logger logger = LoggerFactory.getLogger(Fatura.class);


    public Cartao buscaCartao(RecebeTransacao transacaoRecebida, CartaoRepository cartaoRepository){

        String numeroCartao = transacaoRecebida.getCartao().getId();
        String emailCartao = transacaoRecebida.getCartao().getEmail();

        var cartao = cartaoRepository.findByNumero(numeroCartao);

        if(cartao.isPresent()){
            return cartao.orElseThrow();
        }

        var novoCartao = new Cartao(numeroCartao, emailCartao);

        cartaoRepository.save(novoCartao);

        return novoCartao;

    }


    public Fatura buscaFatura(RecebeTransacao transacaoRecebida, Cartao cartao,
                            FaturaRepository faturaRepository){

        var fatura = faturaRepository.findByCartao(cartao);

        if(fatura.isPresent()){

            logger.info("Fatura foi buscada com sucesso. Mês referente = {}", fatura.get().getMes());
            return fatura.get();
        }

        var novaFatura = new Fatura(cartao, transacaoRecebida);

        faturaRepository.save(novaFatura);

        logger.info("Fatura foi buscada com sucesso. Mês referente = {}", fatura.get().getMes());

        return novaFatura;

    }

    public void registraTransacao(RecebeTransacao transacaoRecebida, CartaoRepository cartaoRepository,
                                  TransacaoRepository transacaoRepository, Fatura fatura){

        var novaTransacao = transacaoRecebida.toModel(cartaoRepository, fatura);
        transacaoRepository.save(novaTransacao);

        fatura.adicionarTransacao(novaTransacao);

        logger.info("transação no valor de {} foi registrada", novaTransacao.getValor());

    }

}
