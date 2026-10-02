package com.cis.billing_service.entity;

import com.cis.billing_service.dto.BillStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="billing_details_t")
@Data @NoArgsConstructor @AllArgsConstructor
public class BillingDetailsT extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String billingDetailsId;

    private String consumerId;
    @Column(name = "bill_no", nullable = false, unique = true)
    private String billNo;


    @Column(name = "billing_month", nullable = false)
    private Integer billingMonth;

    @Column(name = "billing_year", nullable = false)
    private Integer billingYear;

    @Column(name = "unit_rate", nullable = false)
    private BigDecimal unitRate;

    @Column(name = "energy_charge", nullable = false)
    private BigDecimal energyCharge;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "bill_date")
    private LocalDate billDate;

    private BigDecimal arrear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status;
}
