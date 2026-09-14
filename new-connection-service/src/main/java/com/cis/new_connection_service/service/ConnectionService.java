package com.cis.new_connection_service.service;

import com.cis.new_connection_service.dto.ConnectionRequestDto;
import com.cis.new_connection_service.dto.ConnectionResponseDto;
import com.cis.new_connection_service.entity.ConsumerMasterVO;

import java.util.List;

public interface ConnectionService {
    ConnectionRequestDto createConnection(ConnectionRequestDto request);

    List<ConnectionResponseDto> getAllPendingConnectionDetails();

    ConsumerMasterVO
    approvedByCustomerId(Long customerId);

    String rejectedByCustomerId(Long customerId);

    ConnectionRequestDto searchWithApplicationNumber(String applicationNumber);
}
