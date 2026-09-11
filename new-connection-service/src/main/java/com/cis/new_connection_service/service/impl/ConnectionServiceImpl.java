package com.cis.new_connection_service.service.impl;

import com.cis.new_connection_service.common.ApplicationStatus;
import com.cis.new_connection_service.dto.ConnectionRequestDto;
import com.cis.new_connection_service.entity.NewConnectionVO;
import com.cis.new_connection_service.repository.NewConnectionRepository;
import com.cis.new_connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    @Autowired
    private NewConnectionRepository newConnectionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ConnectionRequestDto createConnection(ConnectionRequestDto request) {
        NewConnectionVO connection = modelMapper.map(request, NewConnectionVO.class);
        connection.setApplicationNumber(generateApplicationNumber());
        connection.setApplicationStatus(ApplicationStatus.SUBMITTED.toString());
        NewConnectionVO savedConnection = newConnectionRepository.save(connection);
        return modelMapper.map(savedConnection, ConnectionRequestDto.class);
    }

    private String generateApplicationNumber() {
        return "CONN-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
