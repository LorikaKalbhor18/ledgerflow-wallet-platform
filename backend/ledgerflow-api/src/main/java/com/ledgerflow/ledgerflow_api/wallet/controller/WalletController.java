package com.ledgerflow.ledgerflow_api.wallet.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ledgerflow.ledgerflow_api.wallet.entity.CreateWalletRequest;
import com.ledgerflow.ledgerflow_api.wallet.entity.WalletResponse;
import com.ledgerflow.ledgerflow_api.wallet.service.WalletService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping ("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        // Constructor implementation
        this.walletService = walletService;
    }

    @PostMapping
    @ResponseStatus (HttpStatus.CREATED)
    public WalletResponse createWallet(@Valid @RequestBody CreateWalletRequest walletRequest) {
        // Call the service to create a wallet
        return walletService.createWallet(walletRequest);  
     }
    
    @GetMapping("/{walletId}")
    public WalletResponse getWallet(@PathVariable UUID walletId) {
        // Implement the logic to retrieve a wallet by its ID
        return walletService.getWallet(walletId);
    }
}
