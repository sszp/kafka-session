package com.transferwise.service;

import com.transferwise.dto.TransferDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import static com.transferwise.configuration.BasicConfig.bootstrapServers;
import static com.transferwise.configuration.BasicConfig.topicName;

public class ConsumerMain {
    private static final Logger log = LoggerFactory.getLogger(ConsumerMain.class);

    public static void main(String[] args) {
        try (KafkaConsumer<String, TransferDto> consumer = createConsumer()) {

            consumer.subscribe(List.of(topicName));

            while (true) {
                ConsumerRecords<String, TransferDto> records =
                        consumer.poll(Duration.ofMillis(100));

                for (ConsumerRecord<String, TransferDto> record : records) {
                    log.info("Key: " + record.key() + ", Value: " + record.value());
                    log.info("Partition: " + record.partition() + ", Offset:" + record.offset());
                }
            }
        }
    }

    private static KafkaConsumer<String, TransferDto> createConsumer() {
        String groupId = "basic-consumer-v1";

        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "com.transferwise.serde.JsonDeserializer");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new KafkaConsumer<>(properties);
    }
}