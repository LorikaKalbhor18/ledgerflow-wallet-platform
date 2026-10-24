package com.ledgerflow.ledgerflow_api.wallet.service;

import java.util.UUID;

import com.ledgerflow.ledgerflow_api.wallet.entity.CreateWalletRequest;
import com.ledgerflow.ledgerflow_api.wallet.entity.WalletResponse;

public interface WalletService {

    WalletResponse createWallet(CreateWalletRequest walletRequest);
    WalletResponse getWallet(UUID walletId);
}
