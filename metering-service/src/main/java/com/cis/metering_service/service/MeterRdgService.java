package com.cis.metering_service.service;

import com.cis.metering_service.dto.MeterReadingDto;

public interface MeterRdgService {
    public MeterReadingDto getPreviousRdgFromAccountNo(Long accountNo);

    MeterReadingDto saveReading(Long accountNo,MeterReadingDto meterReadingDto);
}
