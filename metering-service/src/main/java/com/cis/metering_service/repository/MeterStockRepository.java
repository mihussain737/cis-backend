package com.cis.metering_service.repository;

import com.cis.metering_service.entity.MeterStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeterStockRepository extends JpaRepository<MeterStock,String> {
}
