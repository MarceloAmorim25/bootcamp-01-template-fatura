package br.com.fatura.consumer;

import br.com.fatura.dtos.RecebeTransacao;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.TransacaoRepository;
import br.com.fatura.service.ProcessaTransacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class TransacaoConsumer {

    /* pontos de dificuldade de entendimento -> 7 pontos */

    /* @complexidade */
    private final FaturaRepository faturaRepository;

    /* @complexidade */
    private final CartaoRepository cartaoRepository;

    /* @complexidade */
    private final TransacaoRepository transacaoRepository;

    /* @complexidade */
    private final ProcessaTransacaoService processaTransacaoService;

    private final Logger logger = LoggerFactory.getLogger(Fatura.class);



    public TransacaoConsumer(FaturaRepository faturaRepository, CartaoRepository cartaoRepository,
                             TransacaoRepository transacaoRepository, ProcessaTransacaoService processaTransacaoService) {

        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
        this.transacaoRepository = transacaoRepository;
        this.processaTransacaoService = processaTransacaoService;

    }


    @KafkaListener(topics="${spring.kafka.topic.transactions}")
    public void consume(RecebeTransacao transacaoRecebida)  {

        /* @complexidade */
        var cartao = processaTransacaoService
                .buscaCartao(transacaoRecebida, cartaoRepository);

        /* @complexidade */
        var fatura = processaTransacaoService
                .buscaFatura(transacaoRecebida, cartao, faturaRepository);

        /* @complexidade */
        processaTransacaoService
                .registraTransacao(transacaoRecebida, cartao, transacaoRepository, fatura);

        logger.info("[TRANSACAO] transação de valor = {} realizada por = {} no estabelecimento = {} ",
                transacaoRecebida.getValor(),transacaoRecebida.getCartao().getEmail(), transacaoRecebida.getEstabelecimento().getNome());

    }
}
