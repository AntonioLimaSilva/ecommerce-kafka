package br.com.luciano.ecommerce;

import br.com.luciano.ecommerce.KafkaService;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.regex.Pattern;

public class LogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogService.class);

    public static void main(String[] args) {
        var logService = new LogService();
        try(var consumer = new KafkaService<>(
                Pattern.compile("ECOMMERCE.*"),
                logService::parse,
                LogService.class.getSimpleName(),
                String.class,
                Map.of(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName()))) {
            consumer.run();
        }
    }

    public void parse(ConsumerRecord<String, String> record) {
        LOGGER.info("########====================##########");
        LOGGER.info("LOG {} ", record.topic());
        LOGGER.info("Key: {}", record.key());
        LOGGER.info("Value: {}", record.value());
        LOGGER.info("Partition: {}", record.partition());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        LOGGER.info("LOGGING");
    }
}
