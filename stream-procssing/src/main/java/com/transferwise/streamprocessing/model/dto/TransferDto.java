package com.transferwise.streamprocessing.model.dto;


import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ToString
public class TransferDto {
    private String id;
    private String senderId;
    private String receiverId;
    private BigDecimal amount;
    private String currency;
    private long transferredAt;
}
