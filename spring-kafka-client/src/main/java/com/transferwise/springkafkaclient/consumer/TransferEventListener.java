package com.transferwise.springkafkaclient.consumer;

import com.transferwise.springkafkaclient.dto.TransferDto;
import lombok.AllArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TransferEventListener {
    private static final Logger log = LoggerFactory.getLogger(TransferEventListener.class);

    final KafkaTemplate<String, String> kafkaTemplate;

    @KafkaListener(topics = "basic.topic.1", properties = {"spring.json.value.default.type=com.transferwise.springkafkaclient.dto.TransferDto"})
    public void process(TransferDto event) {
        log.info("Received a TransferDto:{}: ", event.getId());
        kafkaTemplate.send(new ProducerRecord<>("test-topic", event.getSenderId(), event.getReceiverId()));
    }
}
