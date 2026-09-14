package com.cis.metering_service.controller;

import com.cis.metering_service.entity.MeterStock;
import com.cis.metering_service.service.MeterStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metering/meterStock")
public class MeterStockController {

    @Autowired
    private MeterStockService meterStockService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<MeterStock> saveMeter(@RequestBody MeterStock meterStock){
        MeterStock savedMeterStock = meterStockService.saveMeter(meterStock);
      return new ResponseEntity<>(savedMeterStock, HttpStatus.CREATED);
    }
}
