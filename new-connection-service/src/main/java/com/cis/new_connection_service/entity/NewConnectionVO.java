package com.cis.new_connection_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(name = "new_connection_app")
public class NewConnectionVO extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long regId;

    @Column(unique = true)
    private String applicationNumber;
    private String customerId;
    private String applicantName;
    private String mobileNumber;
    private String email;
    private String connectionType;
    private String purpose;
    private double requestedLoad;
    private String phase;
    private int houseNumber;
    private String street;
    private String city;
    private String district;
    private String state;
    private int pincode;
    private String ownershipType;
    private String applicationStatus;
    private int recordStatus=1;
}
