package com.cis.metering_service.service.impl;

import com.cis.metering_service.entity.MeterStock;
import com.cis.metering_service.repository.MeterStockRepository;
import com.cis.metering_service.service.MeterStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeterStockImpl implements MeterStockService {

    @Autowired
    private MeterStockRepository meterStockRepository;
    @Override
    public MeterStock saveMeter(MeterStock meterStock) {
        meterStock.setCheckCondition(meterStock.getMeterNo()+","+meterStock.getMeterMake());
        meterStock.setMeterAvailable('N');
        MeterStock savedMeter = meterStockRepository.save(meterStock);
        return savedMeter;
    }
}
