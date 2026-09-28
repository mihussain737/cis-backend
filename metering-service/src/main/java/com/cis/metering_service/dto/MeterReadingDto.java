package com.cis.metering_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor
public class MeterReadingDto {

    private String consumerId;
    private Long prstStatus;
    private LocalDate prstRdgDate;
    private Double prstKwh;
    private Double prstkw;
    private Double prstKva;
    private Double prstKvah;
    private Double billedKwh;
    private Double billedKvah;
    private Integer rdgMonth;
    private Integer rdgYear;
}
