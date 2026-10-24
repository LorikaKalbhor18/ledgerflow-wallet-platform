package com.ledgerflow.ledgerflow_api.wallet.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ledgerflow.ledgerflow_api.wallet.entity.Wallet;

@Repository 
public interface WalletRepository extends JpaRepository<Wallet,UUID> {
    // Additional custom query methods can be defined here if needed.

}
