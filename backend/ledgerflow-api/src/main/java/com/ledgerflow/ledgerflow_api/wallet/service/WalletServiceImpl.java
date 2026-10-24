package com.ledgerflow.ledgerflow_api.wallet.service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ledgerflow.ledgerflow_api.common.exception.WalletNotFoundException;
import com.ledgerflow.ledgerflow_api.wallet.constants.WalletStatus;
import com.ledgerflow.ledgerflow_api.wallet.entity.CreateWalletRequest;
import com.ledgerflow.ledgerflow_api.wallet.entity.Wallet;
import com.ledgerflow.ledgerflow_api.wallet.entity.WalletResponse;
import com.ledgerflow.ledgerflow_api.wallet.repository.WalletRepository;

@Service 
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;

    public WalletServiceImpl(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional 
    public WalletResponse createWallet(CreateWalletRequest walletRequest) {
        // Implement the logic to create a wallet
        OffsetDateTime createdAt = OffsetDateTime.now(ZoneOffset.UTC); 
        Wallet wallet = new Wallet(
                UUID.randomUUID(),
                walletRequest.customerId().trim(),
                walletRequest.currency(),
                BigDecimal.ZERO,
                WalletStatus.ACTIVE,
                createdAt,
                createdAt
        );

        Wallet savedWallet = walletRepository.save(wallet);
        // Get the current UTC time
        return mapToWalletResponse(savedWallet);
    }

    @Override
    @Transactional(readOnly = true)
    public WalletResponse getWallet(UUID walletId) {
        // Implement the logic to retrieve a wallet by its ID
        Wallet wallet = walletRepository.findById(walletId).orElseThrow(() -> new WalletNotFoundException(walletId));
        return mapToWalletResponse(wallet);
    }

    private WalletResponse mapToWalletResponse(Wallet wallet) {
        return new WalletResponse(
                wallet.getId(),
                wallet.getCustomerId(),
                wallet.getCurrency(),
                wallet.getBalance(),
                wallet.getStatus(),
                wallet.getCreatedAt()
        );
    }
    
}
