package com.cis.billing_service.entity;

import com.cis.billing_service.dto.BillStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(
        name = "billing_details_t",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "consumer_id",
                                "billing_month",
                                "billing_year"
                        }
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingDetailsT extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String billingDetailsId;

    @Column(name = "account_no", nullable = false)
    private Long accountNo;

    @Column(name = "consumer_id", nullable = false)
    private String consumerId;

    @Column(name = "bill_no", nullable = false, unique = true)
    private String billNo;

    @Column(name = "billing_month", nullable = false)
    private Integer billingMonth;

    @Column(name = "billing_year", nullable = false)
    private Integer billingYear;

    @Column(name = "previous_reading")
    private BigDecimal previousReading;

    @Column(name = "current_reading")
    private BigDecimal currentReading;

    @Column(name = "consumption")
    private BigDecimal consumption;

    @Column(name = "unit_rate", nullable = false)
    private BigDecimal unitRate;

    @Column(name = "energy_charge", nullable = false)
    private BigDecimal energyCharge;

    @Column(name = "arrear")
    private BigDecimal arrear;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;

    @Column(name = "outstanding_amount")
    private BigDecimal outstandingAmount;

    @Column(name = "payment_status")
    private String paymentStatus;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status;
}