package com.cis.metering_service.client;

import com.cis.metering_service.config.FeignClientConfig;
import com.cis.metering_service.dto.ConsumerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "new-connection-service",
        configuration = FeignClientConfig.class)
public interface ConsumerClient {

    @GetMapping("/api/consumers/{consumerId}")
    ConsumerDto getConsumerById(@PathVariable("consumerId") String consumerId);

    @GetMapping("/api/consumers/{accountNo}/accountNo")
    ConsumerDto getConsumerByAccountNo( @PathVariable Long accountNo);
}