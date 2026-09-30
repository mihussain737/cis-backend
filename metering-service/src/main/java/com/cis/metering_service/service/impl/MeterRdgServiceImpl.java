package com.cis.metering_service.service.impl;

import com.cis.metering_service.client.ConsumerClient;
import com.cis.metering_service.dto.ConsumerDto;
import com.cis.metering_service.dto.MeterReadingDto;
import com.cis.metering_service.entity.ConsMtrRel;
import com.cis.metering_service.entity.MeterReading;
import com.cis.metering_service.exception.ConsumerNotFoundException;
import com.cis.metering_service.exception.ReadingAlreadyDoneForMonth;
import com.cis.metering_service.exception.ResourceNotFoundException;
import com.cis.metering_service.repository.MeterRdgRepository;
import com.cis.metering_service.service.MeterRdgService;
import com.netflix.spectator.api.Meter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

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
    public MeterReadingDto getPreviousRdgFromAccountNo(Long accountNo) {
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if (consumer == null || consumer.equals("")) {
            throw new ConsumerNotFoundException("Consumer not found with accountNo: " + accountNo);
        }
        if (consumer.getBillingStatus().equals("N")) {
            ConsMtrRel consumerMeterData = consMtrRelService.getConsumerMeterData(consumer.getConsumerId());
            MeterReadingDto meterReadingDto = new MeterReadingDto();
            meterReadingDto.setConsumerId(consumer.getConsumerId());
            meterReadingDto.setPrstStatus(1L);
            meterReadingDto.setPrstRdgDate(LocalDate.from(consumerMeterData.getMtrAssignedDate()));
            meterReadingDto.setPrstKwh(consumerMeterData.getInitialKwh());
            meterReadingDto.setPrstKva(consumerMeterData.getInitialKva());
            meterReadingDto.setPrstkw(consumerMeterData.getInitialKva());
            meterReadingDto.setPrstKvah(consumerMeterData.getInitialKvah());
            return meterReadingDto;
        } else {
            MeterReading meterReading = meterRdgRepository.findByLatestRdg(consumer.getConsumerId())
                    .orElseThrow(() -> new ConsumerNotFoundException("Consumer not found with consumerId: " + consumer.getConsumerId()));
            return modelMapper.map(meterReading, MeterReadingDto.class);
        }
    }

    @Override
    @Transactional
    public MeterReadingDto saveReading(Long accountNo, MeterReadingDto meterReadingDto) {
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if (consumer == null || consumer.equals("")) {
            throw new ConsumerNotFoundException("Consumer not found with accountNo: " + accountNo);
        }

        Optional<MeterReading> meteralreadyAvailable = meterRdgRepository.findMeterReadingByRdgMonthAndRdgYearAndConsumerId(meterReadingDto.getRdgMonth(), meterReadingDto.getRdgYear(), consumer.getConsumerId());
        if(meteralreadyAvailable.isPresent()){
            throw  new ReadingAlreadyDoneForMonth("Reading already done for month: "+meterReadingDto.getRdgMonth()+" and year: "+meterReadingDto.getRdgYear());
        }
        MeterReading meterReading = modelMapper.map(meterReadingDto, MeterReading.class);
        MeterReadingDto previousRdg = getPreviousRdgFromAccountNo(accountNo);
        meterReading.setCheckCondition(consumer.getConsumerId() + "," + meterReading.getRdgMonth() + "," + meterReading.getRdgYear());
        meterReading.setConsumerId(consumer.getConsumerId());
        meterReading.setPrstStatus(1L);
        meterReading.setBilledKwh(meterReading.getPrstKwh()-previousRdg.getPrstKwh());
        meterReading.setBilledKvah(meterReading.getPrstKvah()-previousRdg.getPrstKvah());
        MeterReading savedRdg = meterRdgRepository.save(meterReading);
        if(consumer.getBillingStatus().equals("N")) {
            consumerClient.updateConsumerBillingStatus(accountNo,"L");
        }
        return modelMapper.map(savedRdg, MeterReadingDto.class);
    }

    @Override
    public MeterReadingDto getReadingFromMonthAndYear(Long accountNo, int rdgMonth, int rdgYear) {
        MeterReading meterReading;
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if (consumer == null || consumer.equals("")) {
            throw new ConsumerNotFoundException("Consumer not found with accountNo: " + accountNo);
        } else {
            meterReading = meterRdgRepository.findMeterReadingByRdgMonthAndRdgYearAndConsumerId(rdgMonth, rdgYear, consumer.getConsumerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Reading", "MonthAndYear", String.valueOf((rdgMonth) + "," + rdgYear)));
        }
        return modelMapper.map(meterReading, MeterReadingDto.class);
    }

    @Override
    @Transactional
    public MeterReadingDto updateReading(
            Long accountNo,
            MeterReadingDto meterReadingDto) {

        // 1. Get consumer using account number
        ConsumerDto consumer =
                consumerClient.getConsumerByAccountNo(accountNo);

        if (consumer == null || consumer.getConsumerId() == null) {
            throw new ConsumerNotFoundException(
                    "Consumer not found with accountNo: " + accountNo
            );
        }

        // 2. Find the existing meter reading
        MeterReading meterReading =
                meterRdgRepository
                        .findMeterReadingByPrstRdgDateAndConsumerId(
                                meterReadingDto.getPrstRdgDate(),
                                consumer.getConsumerId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reading",
                                        "prstRdgDateAndConsumerId",
                                        meterReadingDto.getPrstRdgDate()
                                                + "," + consumer.getConsumerId()
                                )
                        );

        // 3. Find the previous reading
        MeterReadingDto previousRdg;

        Optional<MeterReading> previousRdgOpt =
                meterRdgRepository.findByBeforeLatestRdg(
                        consumer.getConsumerId()
                );

        if (previousRdgOpt.isPresent()) {

            // Convert MeterReading entity into MeterReadingDto
            MeterReading previousReadingEntity = previousRdgOpt.get();

            previousRdg = modelMapper.map(
                    previousReadingEntity,
                    MeterReadingDto.class
            );

        } else {

            // If no previous database reading exists,
            // get the initial reading from the consumer's meter assignment
            previousRdg = getPreviousRdgFromAccountNo(accountNo);
        }

        // 4. Update the current reading values
        meterReading.setPrstKwh(meterReadingDto.getPrstKwh());
        meterReading.setPrstKvah(meterReadingDto.getPrstKvah());
        meterReading.setPrstKva(meterReadingDto.getPrstKva());
        meterReading.setPrstkw(meterReadingDto.getPrstkw());

        // 5. Update status
        meterReading.setPrstStatus(1L);

        // 6. Set consumer and check condition
        meterReading.setConsumerId(consumer.getConsumerId());

        meterReading.setCheckCondition(
                consumer.getConsumerId()
                        + "," + meterReading.getRdgMonth()
                        + "," + meterReading.getRdgYear()
        );

        // 7. Calculate billed units
        meterReading.setBilledKwh(
                meterReading.getPrstKwh()
                        - previousRdg.getPrstKwh()
        );

        meterReading.setBilledKvah(
                meterReading.getPrstKvah()
                        - previousRdg.getPrstKvah()
        );

        // 8. Save updated reading
        MeterReading savedRdg =
                meterRdgRepository.save(meterReading);

        // 9. Convert saved entity into DTO and return
        return modelMapper.map(
                savedRdg,
                MeterReadingDto.class
        );
    }

}
