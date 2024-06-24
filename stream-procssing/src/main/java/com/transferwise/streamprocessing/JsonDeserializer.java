package com.transferwise.streamprocessing;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Class<T> destinationClass;

    public JsonDeserializer(Class<T> destinationClass) {
        this.destinationClass = destinationClass;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.error("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), destinationClass);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to TransferDto");
        }
    }

    @Override
    public void close() {
    }
}