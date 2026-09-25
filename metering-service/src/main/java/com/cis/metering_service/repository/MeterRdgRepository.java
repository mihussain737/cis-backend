package com.cis.metering_service.repository;

import com.cis.metering_service.entity.MeterReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MeterRdgRepository extends JpaRepository<MeterReading,String> {

    @Query(value = """
        SELECT *
        FROM meter_rdg_t
        WHERE consumer_id = :consumerId
        ORDER BY prst_rdg_date DESC
        LIMIT 1
        """, nativeQuery = true)
    Optional<MeterReading> findByLatestRdg(@Param("consumerId") String consumerId);
}
