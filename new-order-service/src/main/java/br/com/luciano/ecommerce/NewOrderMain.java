package br.com.luciano.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    private static final String TOPIC_ORDER = "ECOMMERCE_NEW_ORDER";
    private static final String TOPIC_EMAIL = "ECOMMERCE_SEND_EMAIL";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderKafkaDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailKafkaDispatcher = new KafkaDispatcher<Email>()) {
                var subject = new Email("Thank you for your order! We are proposing your order.");
                for (var i = 0; i < 10; i++) {
                    var email = Math.random() + "@gmail.com";
                    var order = new Order(UUID.randomUUID().toString(), BigDecimal.valueOf(Math.random() * 5000 + 1), email);
                    orderKafkaDispatcher.send(TOPIC_ORDER, email, order);

                    emailKafkaDispatcher.send(TOPIC_EMAIL, email, subject);
                }
            }
        }
    }
}
