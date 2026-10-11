package com.cis.payment_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String paymentId;

    @Column(nullable = false)
    private Long accountNo;

    @Column(nullable = false)
    private String billNo;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, unique = true)
    private String paymentReference;

    private String transactionId;

    @Column(nullable = false)
    private String status;

    private LocalDateTime paymentDate;
}
