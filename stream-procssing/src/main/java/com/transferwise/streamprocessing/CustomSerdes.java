package com.transferwise.streamprocessing;

import com.transferwise.streamprocessing.model.AmountAggregate;
import com.transferwise.streamprocessing.model.dto.TotalAmountDto;
import com.transferwise.streamprocessing.model.dto.TransferDto;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

public final class CustomSerdes {
    private CustomSerdes() {
    }

    public static Serde<TransferDto> TransferDtoSerde() {
        JsonSerializer<TransferDto> serializer = new JsonSerializer<>();
        JsonDeserializer<TransferDto> deserializer = new JsonDeserializer<>(TransferDto.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<TotalAmountDto> TotalAmountDtoSerde() {
        JsonSerializer<TotalAmountDto> serializer = new JsonSerializer<>();
        JsonDeserializer<TotalAmountDto> deserializer = new JsonDeserializer<>(TotalAmountDto.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
    public static Serde<AmountAggregate> AmountAggregateSerde() {
        JsonSerializer<AmountAggregate> serializer = new JsonSerializer<>();
        JsonDeserializer<AmountAggregate> deserializer = new JsonDeserializer<>(AmountAggregate.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}