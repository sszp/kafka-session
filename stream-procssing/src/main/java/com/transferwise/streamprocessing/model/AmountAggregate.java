package com.transferwise.streamprocessing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmountAggregate {
    @Builder.Default
    private BigDecimal totalAmount = BigDecimal.ZERO;
}
