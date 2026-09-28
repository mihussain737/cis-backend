package com.cis.metering_service.client;

import com.cis.metering_service.config.FeignClientConfig;
import com.cis.metering_service.dto.ConsumerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "new-connection-service",
        configuration = FeignClientConfig.class)
public interface ConsumerClient {

    @GetMapping("/api/consumers/{consumerId}")
    ConsumerDto getConsumerById(@PathVariable("consumerId") String consumerId);

    @GetMapping("/api/consumers/{accountNo}/accountNo")
    ConsumerDto getConsumerByAccountNo( @PathVariable Long accountNo);

    @PutMapping("/api/consumers/{accountNo}/accountNo")
    public ResponseEntity<ConsumerDto> updateConsumerBillingStatus(@PathVariable Long accountNo,@RequestParam String newBillingStatus);

    }