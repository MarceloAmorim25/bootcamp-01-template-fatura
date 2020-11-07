package br.com.fatura.consumer;

import br.com.fatura.dtos.RecebeTransacao;
import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.entidades.Transacao;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import br.com.fatura.repository.TransacaoRepository;
import br.com.fatura.service.ProcessaTransacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;


@Component
public class TransacaoConsumer {


    private final FaturaRepository faturaRepository;

    private final CartaoRepository cartaoRepository;

    private final TransacaoRepository transacaoRepository;

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
    public void consume(RecebeTransacao transacaoRecebida) {

        var cartao = processaTransacaoService.buscaCartao(transacaoRecebida, cartaoRepository);

        var fatura = processaTransacaoService
                .buscaFatura(transacaoRecebida, cartao, faturaRepository);

        processaTransacaoService
                .registraTransacao(transacaoRecebida, cartaoRepository, transacaoRepository, fatura);

        logger.info("[TRANSACAO] transação de valor = {} realizada por = {} no estabelecimento = {} ",
                transacaoRecebida.getValor(),transacaoRecebida.getCartao().getEmail(), transacaoRecebida.getEstabelecimento().getNome());

    }
}
