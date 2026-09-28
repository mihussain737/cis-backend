package com.cis.metering_service.controller;

import com.cis.metering_service.dto.MeterReadingDto;
import com.cis.metering_service.entity.MeterReading;
import com.cis.metering_service.service.MeterRdgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metering/meterRdg")
public class MeterRdgController {
    @Autowired
    private MeterRdgService meterRdgService;

    @GetMapping("/{accountNo}")
    public ResponseEntity<MeterReadingDto> getMeterRdgData(@PathVariable Long accountNo){
        MeterReadingDto meterRdgDto = meterRdgService.getPreviousRdgFromAccountNo(accountNo);
        return new ResponseEntity<>(meterRdgDto, HttpStatus.OK);
    }

    @PostMapping("/{accountNo}")
    public ResponseEntity<MeterReadingDto> addReadingWithAccountNo(
            @PathVariable Long accountNo,@RequestBody MeterReadingDto meterReadingDto){
        MeterReadingDto meterReadingDtoAdded=meterRdgService.saveReading(accountNo,meterReadingDto);
        return new ResponseEntity<>(meterReadingDtoAdded, HttpStatus.OK);
    }

    @GetMapping("/rdgApi/{accountNo}")
    public ResponseEntity<MeterReadingDto> getMeterReadingFromMonthAndYear(@RequestParam int rdgMonth,@RequestParam int rdgYear,@PathVariable Long accountNo){
        MeterReadingDto meterReadingDto=meterRdgService.getReadingFromMonthAndYear(accountNo,rdgMonth,rdgYear);
        return new ResponseEntity<>(meterReadingDto,HttpStatus.OK);
    }

    @PutMapping("/{accountNo}")
    public ResponseEntity<MeterReadingDto> updateReadingWithAccountNo(
            @PathVariable Long accountNo,@RequestBody MeterReadingDto meterReadingDto){
        MeterReadingDto meterReadingDtoAdded=meterRdgService.updateReading(accountNo,meterReadingDto);
        return new ResponseEntity<>(meterReadingDtoAdded, HttpStatus.OK);
    }
}
