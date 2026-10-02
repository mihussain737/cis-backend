package com.cis.billing_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor
public class MeterReadingDto {

    private String consumerId;
    private Long prevStatus;
    private LocalDate prevRdgDate;
    private Double prevKwh;
    private Double prevkw;
    private Double prevKva;
    private Double prevKvah;
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
