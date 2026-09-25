package com.cis.metering_service.dto;

import lombok.Data;

@Data
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