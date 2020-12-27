package br.com.luciano.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    private static final String TOPIC_ORDER = "ECOMMERCE_NEW_ORDER";
    private static final String TOPIC_EMAIL = "ECOMMERCE_NEW_EMAIL";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderKafkaDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailKafkaDispatcher = new KafkaDispatcher<Email>()) {
                var email = new Email("Thank you for your order! We are proposing your order.");
                for (var i = 0; i < 10; i++) {
                    var userId = UUID.randomUUID().toString();
                    var order = new Order(UUID.randomUUID().toString(), userId, BigDecimal.valueOf(Math.random() * 5000 + 1));
                    orderKafkaDispatcher.send(TOPIC_ORDER, userId, order);

                    emailKafkaDispatcher.send(TOPIC_EMAIL, userId, email);
                }
            }
        }
    }
}
