package br.com.fatura.consumer;

import br.com.fatura.dtos.RecebeTransacao;
import br.com.fatura.entidades.Cartao;
import br.com.fatura.entidades.Fatura;
import br.com.fatura.entidades.Transacao;
import br.com.fatura.repository.CartaoRepository;
import br.com.fatura.repository.FaturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.Optional;


@Component
public class TransacaoConsumer {


    private final FaturaRepository faturaRepository;

    private final CartaoRepository cartaoRepository;

    private final EntityManager entityManager;

    private final Logger logger = LoggerFactory.getLogger(Fatura.class);


    public TransacaoConsumer(FaturaRepository faturaRepository, CartaoRepository cartaoRepository, EntityManager entityManager) {
        this.faturaRepository = faturaRepository;
        this.cartaoRepository = cartaoRepository;
        this.entityManager = entityManager;
    }

    @KafkaListener(topics="${spring.kafka.topic.transactions}")
    public void consume(RecebeTransacao transacaoRecebida) {


        Cartao cartao = transacaoRecebida.retornaModeloCartao();

        Transacao transacao = transacaoRecebida.toModel(cartaoRepository);

        Optional<Fatura> fatura = faturaRepository.findByCartao(cartao);


        if(!fatura.isPresent()){

            Fatura novaFatura = new Fatura(transacaoRecebida);
            faturaRepository.save(novaFatura);

        }else{

            fatura.get().adicionarTransacao(transacao);
            entityManager.merge(fatura);

        }

        logger.info("[TRANSACAO] transação de valor = {} realizada por = {} no estabelecimento = {} ",
                transacaoRecebida.getValor(),transacaoRecebida.getCartao().getEmail(), transacaoRecebida.getEstabelecimento().getNome());

    }
}
