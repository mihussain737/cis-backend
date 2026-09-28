package com.cis.metering_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "meter_rdg_t")
@Entity @Data @AllArgsConstructor @NoArgsConstructor
public class MeterReading extends  BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String meterRdgId;
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
    private String checkCondition;
    private String meterChangeId;
    private LocalDateTime meterChangeDate;
}
