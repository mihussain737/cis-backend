package com.cis.billing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ConsumerDto {

    private String consumerId;
    private Long accountNo;
    private Long customerId;
    private String consName;
    private String consAddress;
    private String mobileNumber;
    private String loadType;
    private String typeOfSupply;
    private String billingStatus;
}
