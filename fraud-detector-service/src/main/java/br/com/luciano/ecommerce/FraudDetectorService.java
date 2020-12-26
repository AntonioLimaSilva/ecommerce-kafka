package br.com.luciano.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class FraudDetectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FraudDetectorService.class);

    private static final String TOPIC = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try(var kafkaService = new KafkaService<>(
                TOPIC,
                fraudService::parse,
                FraudDetectorService.class.getSimpleName(),
                Order.class,
                Map.of())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        LOGGER.info("########====================##########");
        LOGGER.info("Processing new order, checking for fraud");
        LOGGER.info("Key: {}", record.key());
        LOGGER.info("Value: {}", record.value());
        LOGGER.info("Partition: {}", record.partition());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        LOGGER.info("Successfully processed");
    }
}
