package com.cis.metering_service.dto;

import lombok.Data;

@Data
public class ConsMtrRelRequest {
    private String meterNo;
    private Long accountNo;
    private double mf;
    private double initialKwh;
    private double initialKvah;
    private double initialKva;
}
