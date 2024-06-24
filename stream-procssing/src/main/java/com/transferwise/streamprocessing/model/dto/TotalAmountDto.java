package com.transferwise.streamprocessing.model.dto;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class TotalAmountDto {
    private String senderId;
    private BigDecimal totalAmount;
}
