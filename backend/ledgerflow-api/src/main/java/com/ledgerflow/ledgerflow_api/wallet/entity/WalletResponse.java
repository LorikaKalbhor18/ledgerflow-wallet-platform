package com.ledgerflow.ledgerflow_api.wallet.entity;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.ledgerflow.ledgerflow_api.wallet.constants.WalletStatus;

public record WalletResponse(UUID id,
        String customerId,
        String currency,
        BigDecimal balance,
        WalletStatus status,
        OffsetDateTime createdAt) {

}
