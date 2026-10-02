package com.cis.billing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class BillingDetailsDto {

    private String consumerId;
    private String billNo;
    private Integer billingMonth;
    private Integer billingYear;
    private BigDecimal unitRate;
    private BigDecimal energyCharge;
    private BigDecimal totalAmount;
    private LocalDate billDate;
    private BillStatus status;
    private BigDecimal arrear;
}
