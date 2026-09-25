package com.cis.metering_service.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data @Table(name = "meter_status_m")
public class MeterStatus extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long meterStatusId;
    private String meterStatus;
    private String code;
    private String mapCode;
}
