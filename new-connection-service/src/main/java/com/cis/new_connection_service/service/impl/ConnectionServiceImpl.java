package com.cis.new_connection_service.service.impl;

import com.cis.new_connection_service.common.ApplicationStatus;
import com.cis.new_connection_service.dto.ConnectionRequestDto;
import com.cis.new_connection_service.dto.ConnectionResponseDto;
import com.cis.new_connection_service.entity.NewConnectionVO;
import com.cis.new_connection_service.repository.NewConnectionRepository;
import com.cis.new_connection_service.service.ConnectionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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
        connection.setCustomerId(generateCustomerId());
        if(Integer.parseInt(request.getPhase())==1){
            connection.setConnectionType("LT");
        }else if(Integer.parseInt(request.getPhase())==1){
            connection.setConnectionType("HT");
        }
        NewConnectionVO savedConnection = newConnectionRepository.save(connection);
        return modelMapper.map(savedConnection, ConnectionRequestDto.class);
    }

    @Override
    public List<ConnectionResponseDto> getAllPendingConnectionDetails() {
        List<NewConnectionVO> allPendingConnection = newConnectionRepository.findAllByApplicationStatus(ApplicationStatus.SUBMITTED.toString());
        List<ConnectionResponseDto> connectionResponseDtoList = allPendingConnection.stream().map(connection -> modelMapper.map(connection, ConnectionResponseDto.class))
                .toList();
        return connectionResponseDtoList;
    }

    private String generateApplicationNumber() {
        return "CONN-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private Long generateCustomerId() {
        Long customerId;
        do {
            customerId = ThreadLocalRandom.current()
                    .nextLong(1000000000L, 9999999999L);
        } while (newConnectionRepository.existsByCustomerId(customerId));
        return customerId;
    }
}
