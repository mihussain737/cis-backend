
package com.cis.payment_service.service.impl;

import com.cis.payment_service.client.BillingClient;
import com.cis.payment_service.dto.BillPaymentDto;
import com.cis.payment_service.dto.PaymentDto;
import com.cis.payment_service.entity.PaymentDetails;
import com.cis.payment_service.repo.PaymentDetailsRepository;
import com.cis.payment_service.service.PaymentService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final BillingClient billingClient;
    private final ModelMapper modelMapper;
    private final PaymentDetailsRepository paymentDetailsRepository;

    @Override
    @Transactional
    public PaymentDto paymentInitiate(Long accountNo) {

        // 1. Fetch outstanding bill from Billing Service
        BillPaymentDto bill;

        try {
            bill = billingClient.getOutstandingAmount(accountNo);
        } catch (FeignException.NotFound e) {
            throw new RuntimeException(
                    "No outstanding bill found for account: " + accountNo
            );
        }

        // 2. Validate response
        if (bill == null) {
            throw new RuntimeException(
                    "Billing Service returned no bill information"
            );
        }

        BigDecimal outstanding = bill.getOutStandingAmount();

        if (outstanding == null ||
                outstanding.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException(
                    "Payment cannot be initiated because no amount is outstanding"
            );
        }

        if (bill.getBillNo() == null || bill.getBillNo().isBlank()) {
            throw new RuntimeException(
                    "Bill number is missing from Billing Service response"
            );
        }

        // 3. Prevent duplicate active attempts for this bill
        boolean paymentAlreadyInitiated =
                paymentDetailsRepository.existsByBillNoAndStatus(
                        bill.getBillNo(), "INITIATED"
                );

        if (paymentAlreadyInitiated) {
            throw new RuntimeException(
                    "A payment is already initiated for bill: "
                            + bill.getBillNo()
            );
        }

        // 4. Create a payment record
        PaymentDetails payment = new PaymentDetails();

        payment.setAccountNo(accountNo);
        payment.setBillNo(bill.getBillNo());
        payment.setAmount(outstanding);

        // Unique reference for each payment attempt
        payment.setPaymentReference(
                "PAY-" + UUID.randomUUID()
        );

        payment.setStatus("INITIATED");
        payment.setPaymentDate(LocalDateTime.now());

        // 5. Save to database
        PaymentDetails savedPayment =
                paymentDetailsRepository.save(payment);

        // 6. Return saved payment details
        return modelMapper.map(savedPayment, PaymentDto.class);
    }
}