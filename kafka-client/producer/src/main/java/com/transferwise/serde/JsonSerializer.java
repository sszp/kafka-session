package com.transferwise.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferwise.dto.TransferDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class JsonSerializer implements Serializer<TransferDto> {
    private static final Logger log = LoggerFactory.getLogger(JsonSerializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, TransferDto data) {
        try {
            if (data == null) {
                log.error("Null received at serializing");
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing TransferDto to byte[]");
        }
    }

    @Override
    public void close() {
    }
}