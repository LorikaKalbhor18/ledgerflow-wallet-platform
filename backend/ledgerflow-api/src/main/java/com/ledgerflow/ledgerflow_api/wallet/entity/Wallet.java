package com.ledgerflow.ledgerflow_api.wallet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.ledgerflow.ledgerflow_api.wallet.constants.WalletStatus;

@Entity
@Table(name = "wallets")
public class Wallet {

    @Id
    private UUID id;

    @Column(name = "customer_id", nullable = false, length = 100)
    private String customerId;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING) 
    @Column(nullable = false, length = 20)
    private WalletStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected Wallet() {
        // Required by JPA.
    }

    public Wallet(
            UUID id,
            String customerId,
            String currency,
            BigDecimal balance,
            WalletStatus status,
            OffsetDateTime createdAt,
            OffsetDateTime updatedAt) {
        this.id = id;
        this.customerId = customerId;
        this.currency = currency;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void credit(BigDecimal amount) {
    validateAmount(amount);
    ensureWalletIsActive();

    this.balance = this.balance.add(amount);
    this.updatedAt = OffsetDateTime.now(java.time.ZoneOffset.UTC);
}

public void debit(BigDecimal amount) {
    validateAmount(amount);
    ensureWalletIsActive();

    if (this.balance.compareTo(amount) < 0) {
        throw new IllegalStateException("Insufficient wallet balance");
    }

    this.balance = this.balance.subtract(amount);
    this.updatedAt = OffsetDateTime.now(java.time.ZoneOffset.UTC);
}

private void validateAmount(BigDecimal amount) {
    if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
        throw new IllegalArgumentException("Amount must be greater than zero");
    }

    if (amount.scale() > 2) {
        throw new IllegalArgumentException("Amount cannot have more than 2 decimal places");
    }
}

private void ensureWalletIsActive() {
    if (this.status != WalletStatus.ACTIVE) {
        throw new IllegalStateException("Wallet is not active");
    }
}
}
