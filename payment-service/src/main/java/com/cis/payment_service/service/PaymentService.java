package com.cis.payment_service.service;

import com.cis.payment_service.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto paymentInitiate(Long accountNo);

    PaymentDto getPaymentDetails(String paymentId);

     List<PaymentDto> getPaymentHistory(Long accountNo);
}
