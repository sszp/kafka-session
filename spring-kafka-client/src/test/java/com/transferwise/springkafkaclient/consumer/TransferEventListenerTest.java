package com.transferwise.springkafkaclient.consumer;

import com.transferwise.springkafkaclient.dto.TransferDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TransferEventListenerTest {
    @Test
    void shouldHandleProductPriceChangedEvent() {
        try (KafkaProducer<String, TransferDto> producer = createProducer()) {
            try {
                producer.send(new ProducerRecord<>("basic.topic.1",
                        TransferDto.builder().id("1").receiverId("987654321").build())).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    ConsumerRecords<String, String> records = null;
                    CountDownLatch latch = new CountDownLatch(10);
                    try (KafkaConsumer<String, String> consumer = createConsumer()) {
                        consumer.subscribe(List.of("test-topic"));
                        while (latch.getCount() > 0 && records == null) {
                            records = consumer.poll(Duration.ofSeconds(1));
                            latch.countDown();
                        }
                    }
                    assert records != null;
                    assertEquals("987654321", records.iterator().next().value());
                });
    }

    private static KafkaConsumer<String, String> createConsumer() {
        String groupId = "test-consumer";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(properties);
    }

    private static KafkaProducer<String, TransferDto> createProducer() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "com.transferwise.springkafkaclient.JsonSerializer");

        return new KafkaProducer<>(properties);
    }
}