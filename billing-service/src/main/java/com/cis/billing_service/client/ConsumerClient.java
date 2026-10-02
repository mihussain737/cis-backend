package com.cis.billing_service.client;

import com.cis.billing_service.dto.ConsumerDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "new-connection-service",
        configuration = FeignClientConfig.class)
public interface ConsumerClient {

    @GetMapping("/api/consumers/{consumerId}")
    ConsumerDto getConsumerById(@PathVariable("consumerId") String consumerId);

    @GetMapping("/api/consumers/{accountNo}/accountNo")
    ConsumerDto getConsumerByAccountNo(@PathVariable Long accountNo);

    @PutMapping("/api/consumers/{accountNo}/accountNo")
    public ResponseEntity<ConsumerDto> updateConsumerBillingStatus(@PathVariable Long accountNo, @RequestParam String newBillingStatus);
}
