package com.cis.payment_service.repo;

import com.cis.payment_service.entity.PaymentDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, String> {
    List<PaymentDetails> findByAccountNoOrderByPaymentDateDesc( Long accountNo);
    boolean existsByBillNoAndStatus(String billNo,String status);
}
