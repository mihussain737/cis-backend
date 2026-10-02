package com.cis.billing_service.service.impl;

import com.cis.billing_service.client.ConsumerClient;
import com.cis.billing_service.client.MeteringClient;
import com.cis.billing_service.dto.BillingDetailsDto;
import com.cis.billing_service.dto.ConsumerDto;
import com.cis.billing_service.dto.MeterReadingDto;
import com.cis.billing_service.entity.BillingDetailsT;
import com.cis.billing_service.repo.BillingRepository;
import com.cis.billing_service.service.BillingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BillingServiceImpl implements BillingService {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private ConsumerClient consumerClient;

    @Autowired
    private MeteringClient meteringClient;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public BillingDetailsDto billingProcess(Long accountNo, int rdgMonth, int rdgYear) {
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if(consumer == null){
            throw new RuntimeException("Consumer not found for account number: " + accountNo);
        }
        MeterReadingDto meterRdg = meteringClient.getMeterReadingFromMonthAndYear(accountNo, rdgMonth, rdgYear);
        BillingDetailsT billingDetails =
                new BillingDetailsT();

        // --------------------------------------------------
        // 4. ARREAR CALCULATION
        // --------------------------------------------------

        BigDecimal arrear = BigDecimal.ZERO;

        if ("N".equals(consumer.getBillingStatus())) {

            // No previous arrear
            arrear = BigDecimal.ZERO;

        } else {

            // Consumer has previous arrear
            BillingDetailsT previousBill =
                    billingRepository.findTopByConsumerIdOrderByBillDateDesc(consumer.getConsumerId())
                            .orElse(null);

            if (previousBill != null) {

                arrear = previousBill.getTotalAmount();

                if (arrear == null) {
                    arrear = BigDecimal.ZERO;
                }
            }
        }

        billingDetails.setArrear(arrear);

        billingDetails.setConsumerId(
                meterRdg.getConsumerId()
        );

        billingDetails.setBillDate(
                meterRdg.getPrstRdgDate()
        );

        billingDetails.setBillingMonth(
                meterRdg.getRdgMonth()
        );

        billingDetails.setBillingYear(
                meterRdg.getRdgYear()
        );

        billingDetails.setBillNo(
                "Bill-" +
                        accountNo +
                        "-" +
                        meterRdg.getRdgMonth() +
                        "-" +
                        meterRdg.getRdgYear()
        );

        // --------------------------------------------------
        // 6. UNIT RATE
        // --------------------------------------------------

        BigDecimal unitRate =
                new BigDecimal("5");

        billingDetails.setUnitRate(unitRate);

        // --------------------------------------------------
        // 7. CONSUMPTION
        // --------------------------------------------------

        BigDecimal previousReading =
                BigDecimal.valueOf(meterRdg.getPrevKwh());

        BigDecimal presentReading =
                BigDecimal.valueOf(meterRdg.getPrstKwh());

        BigDecimal consumption =
                presentReading.subtract(previousReading);

        if (consumption.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Present reading cannot be less than previous reading"
            );
        }

        // --------------------------------------------------
        // 8. ENERGY CHARGE
        // --------------------------------------------------

        BigDecimal energyCharge =
                consumption.multiply(unitRate);

        billingDetails.setEnergyCharge(
                energyCharge
        );

        // --------------------------------------------------
        // 9. TOTAL AMOUNT
        // --------------------------------------------------

        BigDecimal totalAmount =
                energyCharge.add(arrear);

        billingDetails.setTotalAmount(
                totalAmount
        );

        // --------------------------------------------------
        // 10. SAVE BILL
        // --------------------------------------------------

        BillingDetailsT savedBill =
                billingRepository.save(billingDetails);

        // --------------------------------------------------
        // 11. RETURN DTO
        // --------------------------------------------------

        return modelMapper.map(
                savedBill,
                BillingDetailsDto.class
        );
    }
}
