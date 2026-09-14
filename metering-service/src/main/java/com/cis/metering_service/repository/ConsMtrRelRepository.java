package com.cis.metering_service.repository;

import com.cis.metering_service.entity.ConsMtrRel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConsMtrRelRepository
        extends JpaRepository<ConsMtrRel, String> {

    Optional<ConsMtrRel> findByMeterStockId(String meterStockId);

    Optional<ConsMtrRel> findByConsumerId(String consumerId);
}
