package com.cis.metering_service.repository;

import com.cis.metering_service.entity.MeterStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MeterStockRepository extends JpaRepository<MeterStock,String> {
    Optional<MeterStock> findByMeterNo(String meterNo);
}
