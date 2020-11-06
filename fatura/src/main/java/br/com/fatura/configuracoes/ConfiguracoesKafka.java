package br.com.fatura.configuracoes;


import br.com.fatura.dtos.RecebeTransacao;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ConfiguracoesKafka {


    private final KafkaProperties kafkaProperties;

    public ConfiguracoesKafka(KafkaProperties kafkaProperties) {
        this.kafkaProperties = kafkaProperties;
    }


    public Map<String, Object> consumerConfigurations() {

        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapServers());
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getConsumer().getGroupId());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getConsumer().getAutoOffsetReset());

        return properties;

    }


    @Bean
    public ConsumerFactory<String, RecebeTransacao> transactionConsumerFactory() {

        StringDeserializer stringDeserializer = new StringDeserializer();

        JsonDeserializer<RecebeTransacao> jsonDeserializer =
                new JsonDeserializer<>(RecebeTransacao.class, false);

        return new DefaultKafkaConsumerFactory<>(consumerConfigurations(), stringDeserializer, jsonDeserializer);
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RecebeTransacao> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, RecebeTransacao> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(transactionConsumerFactory());

        return factory;
    }


}
