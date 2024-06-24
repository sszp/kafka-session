package com.transferwise.streamprocessing;

public interface BasicConfig {
    String bootstrapServers = "localhost:9092";
    String topicName = "streaming.input.topic.v2";
}
