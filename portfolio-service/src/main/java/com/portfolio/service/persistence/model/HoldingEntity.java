package com.portfolio.service.persistence.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "holding")
@Getter
@NoArgsConstructor
public class HoldingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "portfolio_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private PortfolioEntity portfolioEntity;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "quantity", nullable = false, precision = 20, scale = 8)
    private BigDecimal quantity;

    @Column(name = "average_cost", nullable = false, precision = 19, scale = 4)
    private BigDecimal averageCost;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public HoldingEntity(PortfolioEntity portfolioEntity, String symbol, BigDecimal quantity, BigDecimal averageCost) {
        this.portfolioEntity = Objects.requireNonNull(portfolioEntity, "Portfolio must not be null");
        this.symbol = Objects.requireNonNull(symbol, "Symbol must not be null");
        this.quantity = Objects.requireNonNull(quantity, "Quantity must not be null");
        this.averageCost = Objects.requireNonNull(averageCost, "Average Cost must not be null");
    }
}
