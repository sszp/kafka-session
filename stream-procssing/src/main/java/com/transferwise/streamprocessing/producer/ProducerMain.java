package com.transferwise.streamprocessing.producer;

import com.transferwise.streamprocessing.model.dto.TransferDto;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static com.transferwise.streamprocessing.BasicConfig.bootstrapServers;
import static com.transferwise.streamprocessing.BasicConfig.topicName;

public class ProducerMain {
    private static final Logger log = LoggerFactory.getLogger(ProducerMain.class);

    public static void main(String[] args) throws InterruptedException {
        try (KafkaProducer<String, TransferDto> producer = createProducer()) {
            for (int i = 0; i < 3; i++) {
                final int key = 5;
                producer.send(new ProducerRecord<>(topicName, String.valueOf(key), createTransferDto(key)),
                        (metadata, exception) -> log.info("event with key {} is sent", key));
                TimeUnit.SECONDS.sleep(1);
            }
        }
    }

    private static KafkaProducer<String, TransferDto> createProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.transferwise.streamprocessing.producer.JsonSerializer");

        return new KafkaProducer<>(properties);
    }

    private static TransferDto createTransferDto(final int key) {
        return TransferDto.builder()
                .id(String.valueOf(key))
                .senderId(String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000)))
                .receiverId(String.valueOf(ThreadLocalRandom.current().nextInt(10000, 100000)))
                .amount(BigDecimal.valueOf(5))
                .currency("GBP")
                .transferredAt(Calendar.getInstance().getTimeInMillis()).build();
    }
}