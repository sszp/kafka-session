package com.transferwise.serde;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transferwise.dto.TransferDto;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonDeserializer implements Deserializer<TransferDto> {
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public TransferDto deserialize(String topic, byte[] data) {
        try {
            if (data == null) {
                log.error("Null received at deserializing");
                return null;
            }
            return objectMapper.readValue(new String(data, StandardCharsets.UTF_8), TransferDto.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to TransferDto");
        }
    }

    @Override
    public void close() {
    }
}