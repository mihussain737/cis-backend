package com.cis.new_connection_service.controller;

import com.cis.new_connection_service.dto.ConsumerDto;
import com.cis.new_connection_service.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consumers")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    @GetMapping("/{consumerId}")
    public ResponseEntity<ConsumerDto> getConsumerById(
            @PathVariable String consumerId
    ) {
        ConsumerDto consumer = consumerService.getConsumerById(consumerId);
        return ResponseEntity.ok(consumer);
    }

    @GetMapping("/{accountNo}/accountNo")
    public ResponseEntity<ConsumerDto> getConsumerByAccountNo(
            @PathVariable Long accountNo
    ) {
        ConsumerDto consumer = consumerService.getConsumerByAccountNo(accountNo);
        return ResponseEntity.ok(consumer);
    }

    @PutMapping("/{accountNo}/accountNo")
    public ResponseEntity<ConsumerDto> updateConsumerBillingStatus(@PathVariable Long accountNo,@RequestParam String newBillingStatus){
        ConsumerDto consumerDto=consumerService.updateConsumerBillingStatus(accountNo,newBillingStatus);
        return ResponseEntity.ok(consumerDto);
    }
}
