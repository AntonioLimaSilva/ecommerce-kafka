package br.com.luciano.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class EmailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private static final String TOPIC = "ECOMMERCE_SEND_EMAIL";

    public static void main(String[] args) {
        var emailService = new EmailService();
        try(var kafkaService = new KafkaService<>(
                TOPIC,
                emailService::parse,
                EmailService.class.getSimpleName(),
                Email.class,
                Map.of())) {
            kafkaService.run();
        }
    }

    public void parse(ConsumerRecord<String, Email> record) {
        LOGGER.info("########====================##########");
        LOGGER.info("Send mail");
        LOGGER.info("Key: {}", record.key());
        LOGGER.info("Value: {}", record.value().toString());
        LOGGER.info("Partition: {}", record.partition());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        LOGGER.info("Email successfully send");
    }
}
