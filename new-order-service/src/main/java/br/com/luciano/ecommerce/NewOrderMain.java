package br.com.luciano.ecommerce;

import br.com.luciano.ecommerce.KafkaDispatcher;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    private static final String TOPIC_ORDER = "ECOMMERCE_NEW_ORDER";
    private static final String TOPIC_EMAIL = "ECOMMERCE_NEW_EMAIL";

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderKafkaDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailKafkaDispatcher = new KafkaDispatcher<Email>()) {
                var email = new Email("Order sent by email");
                for (var i = 0; i < 10; i++) {
                    var id = UUID.randomUUID().toString();
                    var order = new Order(id, UUID.randomUUID().toString(), BigDecimal.valueOf(Math.random() * 5000 + 1));
                    orderKafkaDispatcher.send(TOPIC_ORDER, order);

                    emailKafkaDispatcher.send(TOPIC_EMAIL, email);
                }
            }
        }
    }
}
