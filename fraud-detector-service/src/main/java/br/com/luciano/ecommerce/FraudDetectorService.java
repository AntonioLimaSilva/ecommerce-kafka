package br.com.luciano.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FraudDetectorService.class);

    private static final String TOPIC_ORDER = "ECOMMERCE_NEW_ORDER";
    private static final String TOPIC_REJECTED = "ECOMMERCE_REJECTED_ORDER";
    private static final String TOPIC_APPROVED = "ECOMMERCE_APPROVED_ORDER";

    private final KafkaDispatcher<Order> kafkaDispatcher = new KafkaDispatcher<>();

    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try(var kafkaService = new KafkaService<>(
                TOPIC_ORDER,
                fraudService::parse,
                FraudDetectorService.class.getSimpleName(),
                Order.class,
                Map.of())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        LOGGER.info("########====================##########");
        LOGGER.info("Processing new order, checking for fraud");
        LOGGER.info("Key: {}", record.key());
        LOGGER.info("Partition: {}", record.partition());

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {}

        if (record.value().getAmount().compareTo(new BigDecimal("4000")) >= 0) {
            LOGGER.info("Order is a fraud!!! {}", record.value());

            kafkaDispatcher.send(TOPIC_REJECTED, record.value().getUserId(), record.value());
        } else {
            kafkaDispatcher.send(TOPIC_APPROVED, record.value().getUserId(), record.value());
            LOGGER.info("Approved: {} ", record.value());
        }
    }
}
