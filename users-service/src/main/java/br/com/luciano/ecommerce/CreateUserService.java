package br.com.luciano.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

public class CreateUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateUserService.class);

    private static final String TOPIC_ORDER = "ECOMMERCE_NEW_ORDER";

    public static void main(String[] args) {
        var createUserService = new CreateUserService();
        try (var kafkaService = new KafkaService<>(
                TOPIC_ORDER,
                createUserService::parse,
                CreateUserService.class.getSimpleName(),
                Order.class,
                Map.of())) {
            kafkaService.run();
        }
    }

    private void parse(ConsumerRecord<String, Order> record) {
        LOGGER.info("------------------------------------------");
        LOGGER.info("Processing new order, checking for new user");
        LOGGER.info("{}",record.value());

        this.verifyIfExistToCreate(record.value());
    }

    private void verifyIfExistToCreate(Order order) {
        try (var userRepository = new UserRepository()) {
            var userEntityOptional = userRepository.findByUuid(order.getEmail());

            if (userEntityOptional.isEmpty()) {
                UserEntity newUserEntity = new UserEntity();
                newUserEntity.setEmail(order.getEmail());
                newUserEntity.setUuid(UUID.randomUUID().toString());
                userRepository.create(newUserEntity);

                LOGGER.info("Insert new {} ", newUserEntity);
            }
        }
    }
}
