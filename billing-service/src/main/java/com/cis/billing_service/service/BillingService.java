package com.cis.billing_service.service;

import com.cis.billing_service.dto.BillingDetailsDto;

public interface BillingService {
    BillingDetailsDto billingProcess(Long accountNo, int rdgMonth, int rdgYear);
}
