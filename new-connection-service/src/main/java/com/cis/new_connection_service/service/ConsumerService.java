package com.cis.new_connection_service.service;

import com.cis.new_connection_service.dto.ConsumerDto;

public interface ConsumerService {
    ConsumerDto getConsumerById(String consumerId);

    ConsumerDto getConsumerByAccountNo(Long accountNo);
}
