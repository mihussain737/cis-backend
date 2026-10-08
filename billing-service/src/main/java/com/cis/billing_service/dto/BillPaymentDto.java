package com.cis.billing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor @AllArgsConstructor
public class BillPaymentDto {

    private Long accountNo;
    private String billNo;
    private Integer billMonth;
    private Integer billYear;
    private BigDecimal billAmount;
    private BigDecimal arrear;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private BigDecimal outStandingAmount;
    private String status;
}
