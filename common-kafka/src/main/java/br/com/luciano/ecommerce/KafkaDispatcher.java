package br.com.luciano.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaDispatcher.class);

    private final KafkaProducer<String, T> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    public void send(String topic, T value) throws ExecutionException, InterruptedException {
        var producerRecordOrder = new ProducerRecord<>(topic, UUID.randomUUID().toString(), value);
        producer.send(producerRecordOrder, getCallback()).get();
    }

    private static Callback getCallback() {
        return (data, ex) -> {
            if (ex != null) {
                ex.printStackTrace();
                return;
            }
            LOGGER.info("Success message to Topic: {} Partition: {} Offset: {}", data.topic(), data.partition(), data.offset());
        };
    }

    @Override
    public void close() {
        this.producer.close();
    }

    private Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectSerializer.class.getName());
        return properties;
    }
}
