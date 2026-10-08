package com.cis.billing_service.service;

import com.cis.billing_service.dto.BillPaymentDto;
import com.cis.billing_service.dto.BillingDetailsDto;

public interface BillingService {
    BillingDetailsDto billingProcess(Long accountNo, int rdgMonth, int rdgYear);
     BillPaymentDto getOutstandingAmount(Long accountNo);
}
