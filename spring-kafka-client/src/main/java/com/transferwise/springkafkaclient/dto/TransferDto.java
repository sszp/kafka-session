package com.transferwise.springkafkaclient.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransferDto {
    private String id;
    private String senderId;
    private String receiverId;
    private Long amount;
    private String currency;
    private Long sentAt;
}
