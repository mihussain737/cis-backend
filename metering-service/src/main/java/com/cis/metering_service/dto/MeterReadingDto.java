package com.cis.metering_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class MeterReadingDto {

    private String consumerId;
    private Long prstStatus;
    private LocalDateTime prstRdgDate;
    private Double prstKwh;
    private Double prstkw;
    private Double prstKva;
    private Double prstKvah;
    private Double billedKwh;
    private Double billedKvah;
    private int rdgMonth;
    private int rdgYear;
}
