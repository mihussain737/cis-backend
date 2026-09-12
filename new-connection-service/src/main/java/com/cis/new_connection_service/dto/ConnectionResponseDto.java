package com.cis.new_connection_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ConnectionResponseDto {
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
}
