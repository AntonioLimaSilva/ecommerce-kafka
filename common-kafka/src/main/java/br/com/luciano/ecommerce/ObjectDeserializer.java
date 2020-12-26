package br.com.luciano.ecommerce;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class ObjectDeserializer<T> implements Deserializer<T> {

    public static final String TYPE_CONFIG = "br.com.luciano.ecommerce.type_config";
    private Class<T> type;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        String typeName = String.valueOf(configs.get(TYPE_CONFIG));
        try {
            this.type = (Class<T>) Class.forName(typeName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Type for serialization does not exist in the classpath: ", e);
        }
    }

    @Override
    public T deserialize(String value, byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
           return objectMapper.readValue(new String(bytes), type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("It was not possible to transform the object: ", e);
        }
    }
}
