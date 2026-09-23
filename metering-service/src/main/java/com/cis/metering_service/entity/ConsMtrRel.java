package com.cis.metering_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cons_meter_rel_m")
public class ConsMtrRel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String consMtrRelId;

    // ID of the meter from meter_stock_m
    private String meterStockId;

    // ID of the consumer from new-connection-service
    private String consumerId;

    private double mf;

    private LocalDateTime mtrAssignedDate;

    private double initialKwh;
    private double initialKvah;
    private double initialKva;
}