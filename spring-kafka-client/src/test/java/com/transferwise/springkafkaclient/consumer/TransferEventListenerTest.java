package com.transferwise.springkafkaclient.consumer;

import com.transferwise.springkafkaclient.dto.TransferDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.DoubleDeserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class TransferEventListenerTest {
    @Test
    void shouldHandleTransferEvent() {
        try (KafkaProducer<String, TransferDto> producer = createProducer()) {
            try {
                producer.send(new ProducerRecord<>("basic.topic.1", "1",
                        TransferDto.builder().id("1").senderId("1").receiverId("987654321").amount(5L).build())).get();
                producer.send(new ProducerRecord<>("basic.topic.1", "1",
                        TransferDto.builder().id("2").senderId("1").receiverId("987654321").amount(10L).build())).get();
                producer.send(new ProducerRecord<>("basic.topic.1", "1",
                        TransferDto.builder().id("3").senderId("1").receiverId("987654321").amount(15L).build())).get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        await()
                .pollInterval(Duration.ofSeconds(3))
                .atMost(10, SECONDS)
                .untilAsserted(() -> {
                    ConsumerRecords<String, Long> records = null;
                    CountDownLatch latch = new CountDownLatch(10);
                    try (KafkaConsumer<String, Long> consumer = createConsumer()) {
                        consumer.subscribe(List.of("test-topic"));
                        while (latch.getCount() > 0 && records == null) {
                            records = consumer.poll(Duration.ofSeconds(1));
                            latch.countDown();
                        }
                    }
                    assert records != null;
                    List<Long> values = records.records(new TopicPartition("test-topic", 0))
                            .stream()
                            .filter(stringLongConsumerRecord -> stringLongConsumerRecord.key().equals("1"))
                            .map(ConsumerRecord::value)
                            .toList();
                    assertThat(values, is(List.of(5L, 10L, 15L)));
                });
    }

    private static KafkaConsumer<String, Long> createConsumer() {
        String groupId = "test-consumer";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
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