package br.com.luciano.ecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Serializer;

public class ObjectSerializer<T> implements Serializer<T> {

    @Override
    public byte[] serialize(String s, T object) {
        try {
            return new ObjectMapper().writeValueAsString(object).getBytes();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("It was not possible to transform the object: ", e);
        }
    }
}
