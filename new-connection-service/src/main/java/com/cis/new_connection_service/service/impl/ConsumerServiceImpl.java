package com.cis.new_connection_service.service.impl;

import com.cis.new_connection_service.dto.ConsumerDto;
import com.cis.new_connection_service.entity.ConsumerMasterVO;
import com.cis.new_connection_service.repository.ConsumerRepository;
import com.cis.new_connection_service.service.ConsumerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConsumerServiceImpl implements ConsumerService {

    @Autowired
    private final ConsumerRepository consumerRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ConsumerServiceImpl(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    @Override
    public ConsumerDto getConsumerById(String consumerId) {
        ConsumerMasterVO consumer = consumerRepository
                .findByConsumerId(consumerId)
                .orElseThrow(() ->
                        new RuntimeException("Consumer not found"));
        ConsumerDto consumerDto = modelMapper.map(consumer, ConsumerDto.class);
        return consumerDto;
    }
}
