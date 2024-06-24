package com.transferwise.streamprocessing;

import com.transferwise.streamprocessing.model.AmountAggregate;
import com.transferwise.streamprocessing.model.dto.TotalAmountDto;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Properties;

public class PipelineMain {
    public static void main(String[] args) throws InterruptedException {
        String inputTopic = BasicConfig.topicName;
        String group = "stream-processing";
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, group);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BasicConfig.bootstrapServers);
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        StreamsBuilder builder = new StreamsBuilder();
        Materialized<String, AmountAggregate, KeyValueStore<Bytes, byte[]>> materialized =
                Materialized.<String, AmountAggregate, KeyValueStore<Bytes, byte[]>>with(Serdes.String(), CustomSerdes.AmountAggregateSerde()).withCachingDisabled();
        builder.stream(inputTopic, Consumed.with(Serdes.String(), CustomSerdes.TransferDtoSerde()))
                .groupByKey()
                .aggregate(AmountAggregate::new,
                        (key, value, aggregate) -> AmountAggregate.builder().totalAmount(aggregate.getTotalAmount().add(value.getAmount())).build(), materialized)
                .toStream()
                .mapValues((readOnlyKey, value) -> TotalAmountDto.builder().senderId(readOnlyKey).totalAmount(value.getTotalAmount()).build())
                .to("stream.processing.output.topic", Produced.with(Serdes.String(), CustomSerdes.TotalAmountDtoSerde()));

        Topology topology = builder.build();
        KafkaStreams streams = new KafkaStreams(topology, props);
        streams.start();

        Thread.sleep(60000);
        streams.close();
    }
}