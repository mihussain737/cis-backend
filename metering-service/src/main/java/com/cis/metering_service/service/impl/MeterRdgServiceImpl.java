package com.cis.metering_service.service.impl;

import com.cis.metering_service.client.ConsumerClient;
import com.cis.metering_service.dto.ConsumerDto;
import com.cis.metering_service.dto.MeterReadingDto;
import com.cis.metering_service.entity.ConsMtrRel;
import com.cis.metering_service.entity.MeterReading;
import com.cis.metering_service.exception.ConsumerNotFoundException;
import com.cis.metering_service.repository.MeterRdgRepository;
import com.cis.metering_service.service.MeterRdgService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MeterRdgServiceImpl implements MeterRdgService {

    @Autowired
    private MeterRdgRepository meterRdgRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ConsumerClient consumerClient;

    @Autowired
    private ConsMtrRelService consMtrRelService;

    @Override
    public MeterReadingDto  getPreviousRdgFromAccountNo(Long accountNo) {
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if(consumer == null || consumer.equals("")){
            throw new ConsumerNotFoundException("Consumer not found with accountNo: "+accountNo);
        }
        if(consumer.getBillingStatus().equals("N")){
            ConsMtrRel consumerMeterData = consMtrRelService.getConsumerMeterData(consumer.getConsumerId());
            MeterReadingDto meterReadingDto = new MeterReadingDto();
            meterReadingDto.setConsumerId(consumer.getConsumerId());
            meterReadingDto.setPrstStatus(1L);
            meterReadingDto.setPrstRdgDate(consumerMeterData.getMtrAssignedDate());
            meterReadingDto.setPrstKwh(consumerMeterData.getInitialKwh());
            meterReadingDto.setPrstKva(consumerMeterData.getInitialKva());
            meterReadingDto.setPrstkw(consumerMeterData.getInitialKva());
            meterReadingDto.setPrstKvah(consumerMeterData.getInitialKvah());
            return meterReadingDto;
        }else {
            MeterReading meterReading = meterRdgRepository.findByLatestRdg(consumer.getConsumerId())
                    .orElseThrow(() -> new ConsumerNotFoundException("Consumer not found with consumerId: " + consumer.getConsumerId()));
            return modelMapper.map(meterReading, MeterReadingDto.class);
        }
    }

    @Override
    public MeterReadingDto saveReading(Long accountNo,MeterReadingDto meterReadingDto) {
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if(consumer == null || consumer.equals("")){
            throw new ConsumerNotFoundException("Consumer not found with accountNo: "+accountNo);
        }
        MeterReading meterReading = modelMapper.map(meterReadingDto, MeterReading.class);
        meterReading.setCheckCondition(meterReading.getConsumerId()+","+meterReading.getRdgMonth()+","+meterReading.getRdgYear());
        meterReading.setConsumerId(consumer.getConsumerId());
        MeterReading savedRdg = meterRdgRepository.save(meterReading);
        return modelMapper.map(savedRdg, MeterReadingDto.class);
    }
}
