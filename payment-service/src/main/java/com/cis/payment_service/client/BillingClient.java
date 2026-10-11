package com.cis.payment_service.client;

import com.cis.payment_service.config.FeignClientConfig;
import com.cis.payment_service.dto.BillPaymentDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "billing-service",  configuration = FeignClientConfig.class)
public interface BillingClient {

    @GetMapping("api/billing/{accountNo}/outstanding")
    BillPaymentDto getOutstandingAmount(@PathVariable Long accountNo) ;
}
