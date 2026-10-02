package com.cis.billing_service.controller;

import com.cis.billing_service.client.MeteringClient;
import com.cis.billing_service.dto.BillingDetailsDto;
import com.cis.billing_service.dto.MeterReadingDto;
import com.cis.billing_service.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    @Autowired
    private MeteringClient meteringClient;

    @Autowired
    private BillingService billingService;

    @GetMapping("/{accountNo}")
    public ResponseEntity<MeterReadingDto> getMeterReadingFromMonthAndYear(@RequestParam int rdgMonth, @RequestParam int rdgYear, @PathVariable Long accountNo) {
        MeterReadingDto meterRdg = meteringClient.getMeterReadingFromMonthAndYear(accountNo, rdgMonth, rdgYear);
        return new ResponseEntity<>(meterRdg, HttpStatus.OK);
    }

    @PostMapping("/{accountNo}")
    public ResponseEntity<BillingDetailsDto> billingProcess(@PathVariable Long accountNo, @RequestParam int rdgMonth, @RequestParam int rdgYear) {
        BillingDetailsDto billingDetailsDto = billingService.billingProcess(accountNo, rdgMonth, rdgYear);
        return new ResponseEntity<>(billingDetailsDto, HttpStatus.OK);
    }
}
