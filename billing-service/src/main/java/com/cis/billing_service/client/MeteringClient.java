package com.cis.billing_service.client;

import com.cis.billing_service.dto.MeterReadingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "metering-service",
        configuration = FeignClientConfig.class
)
public interface MeteringClient {

    @GetMapping("/api/metering/meterRdg/rdgApi/{accountNo}")
    MeterReadingDto getMeterReadingFromMonthAndYear(
            @PathVariable Long accountNo,
            @RequestParam int rdgMonth,
            @RequestParam int rdgYear
    );
}