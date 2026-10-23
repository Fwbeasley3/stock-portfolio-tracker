package com.portfolio.service.persistence.model;

import com.portfolio.service.audit.AuditableEntity;
import com.portfolio.service.domain.model.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "stock_transaction")
@Getter
@NoArgsConstructor
public class StockTransactionEntity extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "holding_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private HoldingEntity holdingEntity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 10)
    private TransactionType transactionType;

    @Column(name = "quantity", nullable = false, precision = 20, scale = 8)
    private BigDecimal quantity;

    @Column(name = "price_per_share", nullable = false, precision = 19, scale = 4)
    private BigDecimal pricePerShare;

    @Column(name = "executed_at", nullable = false)
    private Instant executedAt;

    public StockTransactionEntity(HoldingEntity holdingEntity, TransactionType transactionType, BigDecimal quantity, BigDecimal pricePerShare) {
        this.holdingEntity = Objects.requireNonNull(holdingEntity, "Holding must not be null");
        this.transactionType = Objects.requireNonNull(transactionType, "Transaction type must not be null");
        this.quantity = Objects.requireNonNull(quantity, "Quantity must not be null");
        this.pricePerShare = Objects.requireNonNull(pricePerShare, "Price per share must not be null");
        this.executedAt = Instant.now();
    }
}
