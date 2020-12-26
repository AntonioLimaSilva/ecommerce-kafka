package br.com.luciano.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

@FunctionalInterface
public interface ConsumerFunction<T> {

    void consumer(ConsumerRecord<String, T> record);

}
