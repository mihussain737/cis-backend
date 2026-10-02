package com.cis.billing_service.repo;

import com.cis.billing_service.entity.BillingDetailsT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingRepository extends JpaRepository<BillingDetailsT,Long> {
     Optional<BillingDetailsT> findTopByConsumerIdOrderByBillDateDesc(String consumerId);
}
