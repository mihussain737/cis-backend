package com.cis.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {

    private Long accountNo;
    private String billNo;
    private BigDecimal amount;
    private String paymentReference;
    private String transactionId;
    private String status;
    private LocalDateTime paymentDate;
}
