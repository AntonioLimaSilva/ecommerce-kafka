package br.com.luciano.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaService.class);

    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction<T> consumerFunction;

    public KafkaService(String topic, ConsumerFunction<T> consumerFunction, String groupId, Class<T> type, Map<String, String> properties) {
        this(consumerFunction, groupId, type, properties);
        consumer.subscribe(Collections.singletonList(topic));
    }

    public KafkaService(Pattern pattern, ConsumerFunction<T> consumerFunction, String groupId, Class<T> type, Map<String, String> properties) {
        this(consumerFunction, groupId, type, properties);
        consumer.subscribe(pattern);
    }

    private KafkaService(ConsumerFunction<T> consumerFunction, String groupId, Class<T> type, Map<String, String> properties) {
        consumer = new KafkaConsumer<>(getProperties(groupId, type, properties));
        this.consumerFunction = consumerFunction;
    }

    public void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));

            if (!records.isEmpty()) {
                LOGGER.info("Found {} records", records.count());
                records.forEach(consumerFunction::consumer);
            }
        }
    }

    @Override
    public void close() {
        this.consumer.close();
    }

    private Properties getProperties(String groupId, Class<T> type, Map<String, String> properties) {
        var props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ObjectDeserializer.class.getName());
        props.setProperty(ObjectDeserializer.TYPE_CONFIG, type.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Usado para identificar o nome de cada grupo e ser possivel de todas as mensagens ser entregue para os consumidores daquele grupo
        props.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString()); // ID generico
        props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); // Muito usado para ter controle no recebimento de cada mensagem e ser feito o commit da mesma, evitando problema de rebalanceamento
        props.putAll(properties);
        return props;
    }
}
