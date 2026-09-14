package com.cis.metering_service.dto;

import lombok.Data;

@Data
public class ConsMtrRelRequest {
    private String meterStockId;
    private String consumerId;
    private double mf;
}
