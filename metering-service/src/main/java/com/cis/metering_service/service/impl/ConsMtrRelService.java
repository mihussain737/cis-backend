package com.cis.metering_service.service.impl;

import com.cis.metering_service.client.ConsumerClient;
import com.cis.metering_service.dto.ConsMtrRelRequest;
import com.cis.metering_service.dto.ConsumerDto;
import com.cis.metering_service.entity.ConsMtrRel;
import com.cis.metering_service.entity.MeterStock;
import com.cis.metering_service.repository.ConsMtrRelRepository;
import com.cis.metering_service.repository.MeterStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ConsMtrRelService {

    private final ConsMtrRelRepository consMtrRelRepository;
    private final MeterStockRepository meterStockRepository;
    private final ConsumerClient consumerClient;

    @Transactional
    public ConsMtrRel assignMeter(ConsMtrRelRequest request) {

        // 1. Check meter exists in metering-service
        MeterStock meterStock = meterStockRepository
                .findById(request.getMeterStockId())
                .orElseThrow(() ->
                        new RuntimeException("Meter not found"));

        // 2. Check consumer exists in new-connection-service
        ConsumerDto consumer = consumerClient.getConsumerById(
                request.getConsumerId()
        );

        if (consumer == null) {
            throw new RuntimeException("Consumer not found");
        }

        // 3. Create relationship
        ConsMtrRel relation = new ConsMtrRel();

        relation.setMeterStockId(meterStock.getMeterStockId());
        relation.setConsumerId(consumer.getConsumerId());
        relation.setMf(request.getMf());
        relation.setMtrAssignedDate(LocalDateTime.now());

        // 4. Save relationship
        return consMtrRelRepository.save(relation);
    }
}