package com.cis.new_connection_service.service;

import com.cis.new_connection_service.dto.ConnectionRequestDto;

public interface ConnectionService {
    ConnectionRequestDto createConnection(ConnectionRequestDto request);
}
