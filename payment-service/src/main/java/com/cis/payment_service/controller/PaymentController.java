package com.cis.payment_service.controller;

import com.cis.payment_service.client.BillingClient;
import com.cis.payment_service.dto.PaymentDto;
import com.cis.payment_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;


    @PostMapping("/initiate/{accountNo}")
    public ResponseEntity<PaymentDto> paymentInitiate(@PathVariable Long accountNo) {
        PaymentDto paymentDto = paymentService.paymentInitiate(accountNo);
        return new ResponseEntity<>(paymentDto, HttpStatus.OK);
    }
}
