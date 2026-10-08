package com.cis.billing_service.service.impl;

import com.cis.billing_service.client.ConsumerClient;
import com.cis.billing_service.client.MeteringClient;
import com.cis.billing_service.dto.*;
import com.cis.billing_service.entity.BillingDetailsT;
import com.cis.billing_service.exception.BillAlreadyExistsException;
import com.cis.billing_service.repo.BillingRepository;
import com.cis.billing_service.service.BillingService;
import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public BillingDetailsDto billingProcess(
            Long accountNo,
            int rdgMonth,
            int rdgYear) {

        // ==========================================
        // 1. GET CONSUMER
        // ==========================================

        ConsumerDto consumer =
                consumerClient.getConsumerByAccountNo(accountNo);

        if (consumer == null) {
            throw new RuntimeException(
                    "Consumer not found for account number: "
                            + accountNo
            );
        }

        // ==========================================
        // 2. CHECK DUPLICATE BILL
        // ==========================================

        billingRepository
                .findByConsumerIdAndBillingMonthAndBillingYear(
                        consumer.getConsumerId(),
                        rdgMonth,
                        rdgYear
                )
                .ifPresent(existingBill -> {

                    throw new BillAlreadyExistsException(
                            "Bill already exists for account "
                                    + accountNo
                                    + " for month "
                                    + rdgMonth
                                    + " and year "
                                    + rdgYear
                    );
                });

        // ==========================================
        // 3. GET METER READING
        // ==========================================

        MeterReadingDto meterRdg;

        try {

            meterRdg =
                    meteringClient
                            .getMeterReadingFromMonthAndYear(
                                    accountNo,
                                    rdgMonth,
                                    rdgYear
                            );

        } catch (FeignException.NotFound e) {

            throw new RuntimeException(
                    "Meter reading not found for account number: "
                            + accountNo
                            + " for month: "
                            + rdgMonth
                            + " and year: "
                            + rdgYear
            );
        }

        if (meterRdg == null) {

            throw new RuntimeException(
                    "Meter reading not found for account number: "
                            + accountNo
            );
        }

        // ==========================================
        // 4. READINGS
        // ==========================================

        BigDecimal previousReading =
                BigDecimal.valueOf(
                        meterRdg.getPrevKwh()
                );

        BigDecimal currentReading =
                BigDecimal.valueOf(
                        meterRdg.getPrstKwh()
                );

        // ==========================================
        // 5. VALIDATE READINGS
        // ==========================================

        if (previousReading.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Previous reading cannot be negative"
            );
        }

        if (currentReading.compareTo(BigDecimal.ZERO) < 0) {

            throw new RuntimeException(
                    "Current reading cannot be negative"
            );
        }

        if (currentReading.compareTo(previousReading) < 0) {

            throw new RuntimeException(
                    "Current reading cannot be less than previous reading"
            );
        }

        // ==========================================
        // 6. CALCULATE CONSUMPTION
        // ==========================================

        BigDecimal consumption =
                currentReading.subtract(previousReading);

        // ==========================================
        // 7. UNIT RATE
        // ==========================================

        BigDecimal unitRate =
                new BigDecimal("5.00");

        // ==========================================
        // 8. ENERGY CHARGE
        // ==========================================

        BigDecimal energyCharge =
                consumption.multiply(unitRate);

        // ==========================================
        // 9. GET ARREAR
        // ==========================================

        BigDecimal arrear =
                getPreviousArrear(
                        consumer,
                        accountNo
                );

        // ==========================================
        // 10. TOTAL BILL
        // ==========================================

        BigDecimal totalAmount =
                energyCharge.add(arrear);

        // ==========================================
        // 11. INITIAL PAYMENT VALUES
        // ==========================================

        BigDecimal paidAmount =
                BigDecimal.ZERO;

        BigDecimal outstandingAmount =
                totalAmount;

        // ==========================================
        // 12. CREATE BILL
        // ==========================================

        BillingDetailsT billingDetails =
                new BillingDetailsT();

        billingDetails.setAccountNo(accountNo);

        billingDetails.setConsumerId(
                consumer.getConsumerId()
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

        billingDetails.setPreviousReading(
                previousReading
        );

        billingDetails.setCurrentReading(
                currentReading
        );

        billingDetails.setConsumption(
                consumption
        );

        billingDetails.setUnitRate(
                unitRate
        );

        billingDetails.setEnergyCharge(
                energyCharge
        );

        billingDetails.setArrear(
                arrear
        );

        billingDetails.setTotalAmount(
                totalAmount
        );

        billingDetails.setPaidAmount(
                paidAmount
        );

        billingDetails.setOutstandingAmount(
                outstandingAmount
        );

        billingDetails.setPaymentStatus(
                "UNPAID"
        );

        billingDetails.setStatus(
                BillStatus.GENERATED
        );

        billingDetails.setBillNo(
                generateBillNo(
                        accountNo,
                        rdgMonth,
                        rdgYear
                )
        );

        // ==========================================
        // 13. SAVE
        // ==========================================

        BillingDetailsT savedBill =
                billingRepository.save(
                        billingDetails
                );

        // ==========================================
        // 14. RETURN DTO
        // ==========================================

        return modelMapper.map(
                savedBill,
                BillingDetailsDto.class
        );
    }

    @Override
    public BillPaymentDto getOutstandingAmount(Long accountNo) {
        ConsumerDto consumer = consumerClient.getConsumerByAccountNo(accountNo);
        if (consumer == null) {
            throw new RuntimeException("Consumer not found for account number: " + accountNo);
        }

        BillingDetailsT billingDetails = billingRepository
                .findTopByConsumerIdOrderByBillDateDesc(consumer.getConsumerId())
                .orElseThrow(() -> new RuntimeException("Billing details not found for account number: " + accountNo));

        BillPaymentDto billPaymentDto = new BillPaymentDto();
        billPaymentDto.setAccountNo(accountNo);
        billPaymentDto.setBillNo(billingDetails.getBillNo());
        billPaymentDto.setBillMonth(billingDetails.getBillingMonth());
        billPaymentDto.setBillYear(billingDetails.getBillingYear());
        billPaymentDto.setBillAmount(billingDetails.getEnergyCharge());
        billPaymentDto.setArrear(billingDetails.getArrear());
        billPaymentDto.setTotalAmount(billingDetails.getTotalAmount());
        billPaymentDto.setPaidAmount(billingDetails.getPaidAmount());
        billPaymentDto.setOutStandingAmount(billingDetails.getTotalAmount().subtract(billingDetails.getPaidAmount()));
        billPaymentDto.setStatus("UNPAID");
        return billPaymentDto;
    }

    private BigDecimal getPreviousArrear(
            ConsumerDto consumer,
            Long accountNo) {

        // Consumer says there is no arrear
        if ("N".equalsIgnoreCase(
                consumer.getBillingStatus())) {

            return BigDecimal.ZERO;
        }

        BillingDetailsT previousBill =
                billingRepository
                        .findTopByAccountNoOrderByBillDateDesc(
                                accountNo
                        )
                        .orElse(null);

        if (previousBill == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal outstanding =
                previousBill.getOutstandingAmount();

        if (outstanding == null) {
            return BigDecimal.ZERO;
        }

        return outstanding.max(
                BigDecimal.ZERO
        );
    }

    private String generateBillNo(
            Long accountNo,
            int month,
            int year) {

        return "Bill-"
                + accountNo
                + "-"
                + month
                + "-"
                + year;
    }
}
