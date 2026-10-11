package com.cis.payment_service.service;

import com.cis.payment_service.dto.PaymentDto;

public interface PaymentService {
    PaymentDto paymentInitiate(Long accountNo);
}
