package com.cis.new_connection_service.entity;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "consumer_m")
@Data @NoArgsConstructor @AllArgsConstructor
public class ConsumerMasterVO {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String consumerId;
    @Column(length = 10,nullable = false,updatable = false)
    private Long accountNo;
    private Long customerId;
    private String consName;
    private String consAddress;
    private String mobileNumber;
    private String loadType;
    private String typeOfSupply;
    private String billingStatus;

}
