package com.cis.metering_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "meter_stock_m")
@Data @NoArgsConstructor @AllArgsConstructor
public class MeterStock extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String meterStockId;
    private String meterNo;
    private String meterMake;
    private String meterPhase;
    private String meterDigit;
    private String meterType;
    private LocalDate meterCreatedDate;
    private double initialKwh;
    private double initialKvah;
    private int recordStatus=1;
    @Column(unique = true)
    private String checkCondition;
}
